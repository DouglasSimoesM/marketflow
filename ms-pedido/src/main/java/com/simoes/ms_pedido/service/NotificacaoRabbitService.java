package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Pedido;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificacaoRabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //Mandar msgm para fila 'pedido-pendente.ms-vendedor'
    public void notificar(Pedido pedido, String exchange, String routingKey){
        rabbitTemplate.convertAndSend(exchange,routingKey, pedido);
    }
}