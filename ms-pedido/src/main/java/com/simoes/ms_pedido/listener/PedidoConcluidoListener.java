package com.simoes.ms_pedido.listener;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.repository.PedidoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PedidoConcluidoListener {

    @Autowired
    private PedidoRepository pedidoRepository;

    @RabbitListener(queues = "${rabbitmq.queue.situacao.pedido}")
    public void propostaConcluida(Pedido pedido){

        if (pedido.isAprovado()){
            //Convertendo em BigDecimal e salvando valor Total
            pedido.setValorTotal(
                    BigDecimal.valueOf(pedido.getQuantidade())
                            .multiply(pedido.getValor())
            );
            pedidoRepository.save(pedido);
        } else {
            //Setando valor 0 para não retornar NullPointer
            pedido.setValorTotal(BigDecimal.valueOf(0));
            pedidoRepository.save(pedido);}
    }
}

