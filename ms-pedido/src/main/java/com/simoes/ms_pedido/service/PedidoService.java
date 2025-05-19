package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.PedidoProcessado;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.entity.dto.PedidoDto;
import com.simoes.ms_pedido.exception.StrategyException;
import com.simoes.ms_pedido.repository.CarrinhoRepository;
import com.simoes.ms_pedido.repository.PedidoProcessadoRepository;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoRabbitService notificacaoRabbitService;
    private final PedidoProcessadoRepository pedidoProcessadoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final String exchange;
    private final String fila;
    private final String exchangeConsulta;
    private final String filaConsulta;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         NotificacaoRabbitService notificacaoRabbitService, PedidoProcessadoRepository pedidoProcessadoRepository,
                         CarrinhoRepository carrinhoRepository,
                         @Value("${rabbitmq.pedidopendente.exchange}") String exchange,
                         @Value("${rabbitmq.pedidospendente.msvendedor}")String fila,
                         @Value("${rabbitmq.pedidoconsulta.exchange}")String exchangeConsulta,
                         @Value("${rabbitmq.pedidoconsulta.msvendedor}")String filaConsulta) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.pedidoProcessadoRepository = pedidoProcessadoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.exchange = exchange;
        this.fila = fila;
        this.exchangeConsulta = exchangeConsulta;
        this.filaConsulta = filaConsulta;
    }

    // Consulta feita pelo cliente
    public Pedido consultarValorAdicionarCarrinho(Long usuarioId, Pedido pedido){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(()-> new StrategyException("Usuario não encontrado !"));
        pedido.setIdUsuario(usuarioId);
        pedido.setStatus("Pendente");
        pedido.setAprovado(false);
        pedido.setUsuario(usuario);
        pedido.setCarrinho(usuario.getCarrinho());

        pedidoRepository.save(pedido);
        // Enviando para ms-vendedor se existe em estoque e consulta valor
        notificacaoRabbitService.notificar(pedido, exchangeConsulta, filaConsulta);
        return pedido;
    }

    // Adicionando pedido em PedidoProcessado
    public void adicionarPedidoProcessado(Usuario usuario, List<Pedido> pedidos){
        // Verificando se carrinho de usuario é Null
        Carrinho carrinho = usuario.getCarrinho();
        if (carrinho == null) {
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            usuario.setCarrinho(carrinho);
        }

        // Buscando Lista processadas caso seja nula inicia-la
        List<PedidoProcessado> pedidosprocessados = carrinho.getPedidoProcessado();
        if (pedidosprocessados == null) {
            pedidosprocessados = new ArrayList<>();
        }

        // inserindo lista de pedido em na lista de pedidos processado
        for (Pedido pedido : pedidos) {
            PedidoProcessado pedidoProcessado = new PedidoProcessado(
                    pedido.getId(),
                    pedido.getIdUsuario(),
                    pedido.getNomeItem(),
                    pedido.getQuantidade(),
                    pedido.getValor(),
                    pedido.getValorTotal(),
                    pedido.getStatus(),
                    pedido.isAprovado(),
                    pedido.getObservacao(),
                    usuario,
                    carrinho
            );
            pedidosprocessados.add(pedidoProcessado);
        }

        // Colocando pededido processado no carrinho em pedidos processados
        carrinho.setPedidoProcessado(pedidosprocessados);

        // salvando usuario and carrinho do usuario
        carrinhoRepository.save(carrinho);
        usuarioRepository.save(usuario);
    }

    public void finalizarPedido(Long usuarioId, Long pedidoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new StrategyException("Usuário não encontrado!"));

        // Busca o pedido específico pelo ID
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new StrategyException("Pedido não encontrado!"));

        // Verifica se o pedido pertence ao usuário
        if (!pedido.getIdUsuario().equals(usuarioId)) {
            throw new StrategyException("O pedido não pertence a este usuário!");
        }

        // Calcula o valor total e notifica via RabbitMQ
        pedido.setValorTotal(pedido.getQuantidade() * pedido.getValor());
        notificacaoRabbitService.notificar(pedido, exchange, fila);

        // Remove apenas o pedido específico do banco
        pedidoRepository.delete(pedido);

        // Remove o pedido da lista do carrinho e salva a atualização
        usuario.getCarrinho().getPedidos().removeIf(p -> p.getId().equals(pedidoId));
        usuarioRepository.save(usuario);
    }


    //Busca pedidos processados de cada Usuario
    public List<PedidoDto> buscarPedidoProcessadoPorUsuario(Long idUsuario){
        // Valida se usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new StrategyException("Usuario não encontrado"));

        // Convertendo com stream para Lista PedidoDto
        return usuario.getCarrinho().getPedidoProcessado().stream()
                .map(pedido -> new PedidoDto(
                        pedido.getIdUsuario(),
                        pedido.getQuantidade(),
                        pedido.getNomeItem(),
                        pedido.getValorTotalFmt(),
                        pedido.getObservacao()
                )).toList();
    }

    //Buscar todos pedidos processados
    public List<PedidoDto> buscarPedidosProcessados(){

        // Convertendo com stream para Lista PedidoDto
        return pedidoProcessadoRepository.findAll().stream().map(
                pedidoProcessado -> new PedidoDto(
                        pedidoProcessado.getIdUsuario(),
                        pedidoProcessado.getQuantidade(),
                        pedidoProcessado.getNomeItem(),
                        pedidoProcessado.getValorTotalFmt(),
                        pedidoProcessado.getObservacao()
                )).toList();
    }

    // Busca pedidos no carrinho do Usuario
    public List<PedidoDto> buscarPedidosCarrinho(Long idUsuario){
        // Valida se usuario existe
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new StrategyException("Usuario não encontrado"));
        // Convertendo com stream para Lista PedidoDto
        return usuario.getCarrinho().getPedidos().stream().map(pedido -> new PedidoDto(
                pedido.getIdUsuario(),
                pedido.getQuantidade(),
                pedido.getNomeItem(),
                pedido.getValorTotalFmt(),
                pedido.getObservacao()
        )).toList();
    }

    // Deleta pedido do carrinho do cliente
    @Transactional
    public String delPedido(Long id){
        pedidoRepository.deleteById(id);
        return "Pedido excluido !!!";
    }

}
