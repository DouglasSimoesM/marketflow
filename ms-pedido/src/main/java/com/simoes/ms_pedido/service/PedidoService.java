package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.PedidoProcessado;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.CarrinhoRepository;
import com.simoes.ms_pedido.repository.PedidoProcessadoRepository;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         NotificacaoRabbitService notificacaoRabbitService, PedidoProcessadoRepository pedidoProcessadoRepository,
                         CarrinhoRepository carrinhoRepository,
                         @Value("${rabbitmq.pedidopendente.exchange}") String exchange,
                         @Value("${rabbitmq.pedidospendente.msvendedor}")String fila) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.pedidoProcessadoRepository = pedidoProcessadoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.exchange = exchange;
        this.fila = fila;
    }

    public Pedido adicionarPedido(Long usuarioId, Pedido pedido){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(()-> new RuntimeException("Usuario não encontrado !"));
        pedido.setIdUsuario(usuarioId);
        pedido.setStatus("Pendente");
        pedido.setAprovado(false);
        pedido.setUsuario(usuario);
        pedido.setCarrinho(usuario.getCarrinho());
        pedido.setValorTotal(
                BigDecimal.valueOf(pedido.getQuantidade())
                        .multiply(pedido.getValor()));
        return pedidoRepository.save(pedido);
    }

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
                    pedido.getItem(),
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




    public void finalizarCompra(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        Carrinho carrinho = usuario.getCarrinho();
        //Lista correta a ser inserida para efetuar pedidos.clear
        List<Pedido> pedidos = pedidoRepository.findByIdUsuario(usuarioId);

        if (pedidos.isEmpty()) {
            throw new RuntimeException("O carrinho está vazio! Não há pedidos para finalizar.");
        }

        // Envia os pedidos ao RabbitMQ
        for (Pedido pedido : pedidos) {
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


    public List<PedidoProcessado> buscarPedidoProcessadoPorUsuario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
        return usuario.getCarrinho().getPedidoProcessado();
    }
    public List<PedidoProcessado> buscarPedidosProcessados(){
        return pedidoProcessadoRepository.findAll();
    }

    public List<Pedido> buscarPedidosCarrinho(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
        return usuario.getCarrinho().getPedidos();
    }

}
