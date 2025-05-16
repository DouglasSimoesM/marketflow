package com.simoes.ms_vendedor.service;

import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.entity.Produto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificacaoRabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //Enviar msgm para exchange 'situacao-pedido' que encimnha para filas 'ms-notificacao' and 'ms-pedido'
    public void notificar(Pedido pedido, String exchange){
        rabbitTemplate.convertAndSend(exchange,"", pedido);
    }
    public void notificarDirect(Pedido pedido, String routingKey, String exchange){
        rabbitTemplate.convertAndSend(exchange,routingKey,pedido);
    }
    public void notificarVendedor(Produto produto, String routingKey, String exchange){
        rabbitTemplate.convertAndSend(exchange, routingKey,produto);
    }
}
