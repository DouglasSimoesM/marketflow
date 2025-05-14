package com.simoes.ms_vendedor.listener;

import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.service.NotificacaoRabbitService;
import com.simoes.ms_vendedor.service.VendedorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ConsultarValorListener {

    private final VendedorService vendedorService;
    private final NotificacaoRabbitService notificacaoRabbitService;
    private final String exchange;
    private final String exchangeFanout;

    public ConsultarValorListener(VendedorService vendedorService,
                                  NotificacaoRabbitService notificacaoRabbitService,
                                  @Value("${rabbitmq.pedidoconsulta.exchange}") String exchange,
                                  @Value("${rabbitmq.consultaconcluida.exchange}") String exchangeFanout) {
        this.vendedorService = vendedorService;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.exchange = exchange;
        this.exchangeFanout = exchangeFanout;
    }

    @RabbitListener(queues = "${rabbitmq.queue.consultar.valor}")
    public void consultarValor(Pedido pedido){
        vendedorService.consultarValor(pedido);

        if (pedido.isAprovado()){
            notificacaoRabbitService.notificarDirect(pedido,"consultar-valor.ms-pedido" ,  exchange);
        } else {
            notificacaoRabbitService.notificar(pedido , exchangeFanout);
        }

    }
}
