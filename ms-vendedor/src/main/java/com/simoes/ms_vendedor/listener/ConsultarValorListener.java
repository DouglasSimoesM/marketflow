package com.simoes.ms_vendedor.listener;

import com.simoes.ms_vendedor.constante.MensagemConstante;
import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.entity.Produto;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.ProdutoRepository;
import com.simoes.ms_vendedor.service.NotificacaoRabbitService;
import com.simoes.ms_vendedor.service.VendedorService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ConsultarValorListener {

    private final VendedorService vendedorService;
    private final NotificacaoRabbitService notificacaoRabbitService;
    private final ProdutoRepository produtoRepository;
    private final String exchange;

    public ConsultarValorListener(VendedorService vendedorService,
                                  NotificacaoRabbitService notificacaoRabbitService, ProdutoRepository produtoRepository,
                                  @Value("${rabbitmq.pedidoconsulta.exchange}") String exchange) {
        this.vendedorService = vendedorService;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.produtoRepository = produtoRepository;
        this.exchange = exchange;
    }

    @RabbitListener(queues = "${rabbitmq.queue.consultar.valor}")
    public void consultarValor(Pedido pedido){
        vendedorService.consultarValor(pedido);

        if (pedido.isAprovado()){
            notificacaoRabbitService.notificarDirect(pedido,"consultar-valor.ms-pedido" ,  exchange);
        } else if (pedido.getObservacao().equalsIgnoreCase(MensagemConstante.PRODUTO_NAO_ENCONTRADO)){
            notificacaoRabbitService.notificarDirect(pedido, "consultar-valor.ms-pedido", exchange);
        } else {
            List<Produto> produto = produtoRepository.findByNomeItemContainingIgnoreCase(pedido.getNomeItem());
            Produto produto1 = produtoRepository.findById(produto.getFirst().getProdutoId())
                    .orElseThrow(()-> new StrategyException("ID VENDEDOR NAO ENCONTRADO"));

            notificacaoRabbitService.notificarVendedor(produto1,"consulta-concluida.ms-notificacao", exchange);

            notificacaoRabbitService.notificarDirect(pedido,"consultar-valor.ms-pedido", exchange);
        }

    }
}
