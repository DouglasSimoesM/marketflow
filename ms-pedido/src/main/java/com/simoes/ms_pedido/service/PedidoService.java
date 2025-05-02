package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.CarrinhoRepository;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
            notificacaoRabbitService.notificar(pedido, exchange, "pedido-pendente.ms-vendedor");
        }

        // Remove os pedidos do banco antes de limpar a lista
        pedidoRepository.deleteAll(pedidos);

        // Limpa a lista
        pedidos.clear();

        usuario.getCarrinho().setPedidos(pedidos);
        //Salvando no Banco de dados
        usuarioRepository.save(usuario);
    }




}
