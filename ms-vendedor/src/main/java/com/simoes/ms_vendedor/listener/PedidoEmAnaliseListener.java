package com.simoes.ms_vendedor.listener;

import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.service.EstoqueService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoEmAnaliseListener {

        private final EstoqueService estoqueService;

    public PedidoEmAnaliseListener(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.pedido.pendente}")
    public void pedidoEmAnalise(Pedido pedido) {
        estoqueService.retornoAnalise(pedido);
    }
}
