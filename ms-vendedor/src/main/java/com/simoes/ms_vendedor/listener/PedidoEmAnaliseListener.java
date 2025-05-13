package com.simoes.ms_vendedor.listener;

import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.service.ProdutoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoEmAnaliseListener {

    private final ProdutoService produtoService;

    public PedidoEmAnaliseListener(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.pedido.pendente}")
    public void pedidoEmAnalise(Pedido pedido) {
        produtoService.retornoAnalise(pedido);
    }
}
