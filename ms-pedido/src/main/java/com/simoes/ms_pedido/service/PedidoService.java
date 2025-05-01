package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.CarrinhoRepository;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoRabbitService notificacaoRabbitService;
    private final CarrinhoRepository carrinhoRepository;
    private final String exchange;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         NotificacaoRabbitService notificacaoRabbitService,
                         CarrinhoRepository carrinhoRepository,
                         @Value("${rabbitmq.pedidopendente.exchange}") String exchange) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.carrinhoRepository = carrinhoRepository;
        this.exchange = exchange;
    }

    public Pedido adicionarPedido(Long usuarioId, Pedido pedido){
        Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(()-> new RuntimeException("Usuario não encontrado !"));
        pedido.setStatus("Pendente");
        pedido.setAprovado(false);
        pedido.setUsuario(usuario);
        pedido.setCarrinho(usuario.getCarrinho());
        pedido.setValorTotal(
                BigDecimal.valueOf(pedido.getQuantidade())
                        .multiply(pedido.getValor()));
        return pedidoRepository.save(pedido);
    }



    public void finalizarCompra(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        List<Pedido> pedidos = usuario.getCarrinho().getPedidos();

        if (pedidos.isEmpty()) {  // Se não houver pedidos, interrompe a execução
            throw new RuntimeException("O carrinho está vazio! Não há pedidos para finalizar.");
        }

        // Envia os pedidos ao RabbitMQ
        for (Pedido pedido : pedidos) {
            notificacaoRabbitService.notificar(pedido, exchange, "pedido-pendente.ms-vendedor");
        }

        // Limpa a lista de pedidos do carrinho para evitar envios repetidos
        pedidos.clear();
        usuario.getCarrinho().setPedidos(pedidos);
        usuarioRepository.save(usuario);
    }

}
