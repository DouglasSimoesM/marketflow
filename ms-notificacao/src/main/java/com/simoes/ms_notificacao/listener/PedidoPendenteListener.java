package com.simoes.ms_notificacao.listener;

import com.simoes.ms_notificacao.constante.MensagemConstante;
import com.simoes.ms_notificacao.entity.Pedido;
import com.simoes.ms_notificacao.service.NotificacaoSnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoPendenteListener {
    @Autowired
    private NotificacaoSnsService notificacaoSnsService;
    //RabbitListener é um "ouvinte"
    @RabbitListener(queues = "${rabbitmq.queue.pedido.pendente}")
    public void PedidoPendente(Pedido pedido){
        String mensage = String.format(MensagemConstante.ANALISANDO_PEDIDO, pedido.getUsuario().getNome());
        notificacaoSnsService.notificar(pedido.getUsuario().getTelefone(), mensage);

    }
}
