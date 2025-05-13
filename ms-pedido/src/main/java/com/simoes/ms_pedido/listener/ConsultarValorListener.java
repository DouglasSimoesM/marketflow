package com.simoes.ms_pedido.listener;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.repository.PedidoRepository;
import jakarta.persistence.Access;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultarValorListener {

    @Autowired
    private PedidoRepository pedidoRepository;

    // Receber pedido de ms-vendedor para retornar valor ao cliente
    @RabbitListener(queues = "${rabbitmq.queue.consultar.valor}")
    public void consultarValor(Pedido pedido){

        pedido.setValorTotal(pedido.getQuantidade() * pedido.getValor());

        pedidoRepository.save(pedido);
    }
}
