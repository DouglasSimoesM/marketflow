package com.simoes.ms_notificacao.listener;

import com.simoes.ms_notificacao.constante.MensagemConstante;
import com.simoes.ms_notificacao.entity.Pedido;
import com.simoes.ms_notificacao.exception.StrategyException;
import com.simoes.ms_notificacao.service.LogsService;
import com.simoes.ms_notificacao.service.NotificacaoSnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SituacaoPedidoListener {

    @Autowired
    private NotificacaoSnsService notificacaoSnsService;

    @Autowired
    private LogsService logsService;

    // RabbitMQ esta buscando mensagens na fila 'rabbitmq.queue.situacao.pedido' <- Nome da fila localizado no application properties
    @RabbitListener(queues = "${rabbitmq.queue.situacao.pedido}")
    public void SituacaoPedido(Pedido pedido){

        try {
        if (pedido.isAprovado()){
            String mensage = String.format(MensagemConstante.PEDIDO_ENVIADO, pedido.getUsuario().getNome(), pedido.getStatus());
            notificacaoSnsService.notificar(pedido.getUsuario().getTelefone(), mensage);
        } else {
            String mensage = String.format(MensagemConstante.PEDIDO_RECUSADO, pedido.getUsuario().getNome(),pedido.getStatus() ,pedido.getObservacao());
            notificacaoSnsService.notificar(pedido.getUsuario().getTelefone(), mensage);

        }} catch (StrategyException e){
        pedido.setObservacao(pedido.getObservacao() + " --- TELEFONE: "
                + pedido.getUsuario().getTelefone() + " - INVALIDO)");
        logsService.salvarLog(pedido);
    }

    }
}
