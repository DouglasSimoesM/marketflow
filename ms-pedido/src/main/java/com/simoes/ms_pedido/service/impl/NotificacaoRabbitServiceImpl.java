package com.simoes.ms_pedido.service.impl;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.service.NotificacaoRabbitService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificacaoRabbitServiceImpl implements NotificacaoRabbitService {

    private RabbitTemplate rabbitTemplate;

    //Mandar msgm para fila 'pedido-pendente.ms-notificacao'
    @Override
    public void notificar(Pedido pedido, String exchange){
        rabbitTemplate.convertAndSend(exchange,"", pedido);
    }
}