package com.simoes.ms_vendedor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.situacaopedido.exchange}")
    private String exchangeSituacaoPedido;

    @Bean
    public FanoutExchange criarExchangeSituacaoPedido(){
        return ExchangeBuilder.fanoutExchange(exchangeSituacaoPedido).build();
    }

    @Bean
    public Queue criarFilaSituacaoPedidoMsPedido(){
        return QueueBuilder.durable("situacao-pedido.ms-pedido").build();
    }
    @Bean
    public Queue criarFilaSituacaoPeiddoMsNotificacao(){
        return QueueBuilder.durable("situacao-pedido.ms-notificacao").build();
    }

    @Bean
    public Binding criarBindingSituacaoPedidoMsPedido(){
        return BindingBuilder.bind(criarFilaSituacaoPedidoMsPedido())
                .to(criarExchangeSituacaoPedido());
    }

    @Bean
    public Binding criarBindingSituacaoPedidoMsNotificacao(){
        return BindingBuilder.bind(criarFilaSituacaoPeiddoMsNotificacao())
                .to(criarExchangeSituacaoPedido());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
