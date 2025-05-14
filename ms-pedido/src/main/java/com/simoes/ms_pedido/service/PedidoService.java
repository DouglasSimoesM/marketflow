package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.PedidoProcessado;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.entity.dto.PedidoDto;
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
                            .orElseThrow(()-> new RuntimeException("Usuario não encontrado !"));
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

    // Adicionando peido em PedidoProcessado
    public void adicionarPedidoProcessado(Usuario usuario, List<Pedido> pedidos){
        Carrinho carrinho = usuario.getCarrinho();
        if (carrinho == null) {
            carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            usuario.setCarrinho(carrinho);
        }

        List<PedidoProcessado> pedidosprocessados = carrinho.getPedidoProcessado();
        if (pedidosprocessados == null) {
            pedidosprocessados = new ArrayList<>();
        }

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

        carrinho.setPedidoProcessado(pedidosprocessados);

        carrinhoRepository.save(carrinho);
        usuarioRepository.save(usuario);
    }

    // Finalizar compra enviando pedido ao ms-vendedor
    public void finalizarCompra(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        //Lista correta a ser inserida para efetuar pedidos.clear
        List<Pedido> pedidos = pedidoRepository.findByIdUsuario(usuarioId);

        if (pedidos.isEmpty()) {
            throw new RuntimeException("O carrinho está vazio! Não há pedidos para finalizar.");
        }

        // Envia os pedidos ao RabbitMQ
        for (Pedido pedido : pedidos) {
            pedido.setValorTotal(pedido.getQuantidade() * pedido.getValor());
            notificacaoRabbitService.notificar(pedido, exchange, fila);
        }

        // Remove os pedidos do banco antes de limpar a lista
        pedidoRepository.deleteAll(pedidos);

        // Limpa a lista
        pedidos.clear();

        usuario.getCarrinho().setPedidos(pedidos);
        //Salvando no Banco de dados
        usuarioRepository.save(usuario);
    }

    //Busca pedidos processados de cada Usuario
    public List<PedidoDto> buscarPedidoProcessadoPorUsuario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));

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
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
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
