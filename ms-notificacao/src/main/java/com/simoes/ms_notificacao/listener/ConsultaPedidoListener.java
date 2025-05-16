package com.simoes.ms_notificacao.listener;

import com.simoes.ms_notificacao.constante.MensagemConstante;
import com.simoes.ms_notificacao.entity.Pedido;
import com.simoes.ms_notificacao.entity.Produto;
import com.simoes.ms_notificacao.exception.StrategyException;
import com.simoes.ms_notificacao.service.LogsService;
import com.simoes.ms_notificacao.service.NotificacaoSnsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPedidoListener {

    private final NotificacaoSnsService notificacaoSnsService;

    private final LogsService logsService;

    @Autowired
    public ConsultaPedidoListener(NotificacaoSnsService notificacaoSnsService, LogsService logsService) {
        this.notificacaoSnsService = notificacaoSnsService;
        this.logsService = logsService;
    }

    // RabbitMQ esta buscando mensagens na fila 'rabbitmq.queue.consultar.valor' <- Nome da fila localizado no application properties
    @RabbitListener(queues = "${rabbitmq.queue.consulta.concluida}")
    public void ProdutoSemEsotque(Produto produto) {
        try {
            String mensage = String.format(MensagemConstante.PRODUTO_SEM_ESTOQUE, produto.getNomeVendedor(), produto.getNomeItem());
            notificacaoSnsService.notificar(produto.getTelefone(), mensage);

        } catch (StrategyException e) {
            System.err.println("ERRO TRATADO");
        }

    }
}
