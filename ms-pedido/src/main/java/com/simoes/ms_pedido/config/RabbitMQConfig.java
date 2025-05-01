package com.simoes.ms_pedido.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //Criando exchange de roteamente para pedidos pentendes
    @Value("${rabbitmq.pedidopendente.exchange}")
    private String exchangePedidoPendente;

    @Value("${rabbitmq.situacaopedido.exchange}")
    private String exchangeSituacaoPedido;

    @Bean
    public FanoutExchange criarExchangeSituacaoPedido(){
        return ExchangeBuilder.fanoutExchange(exchangeSituacaoPedido).build();
    }
    
    @Bean
    public DirectExchange criarDirectExchangePedidoPendente(){
        return ExchangeBuilder.directExchange(exchangePedidoPendente).build();
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
    public Queue criarFilaPedidoPendenteMsVendedor(){
        return QueueBuilder.durable("pedido-pendente.ms-vendedor").build();
    }


    @Bean
    public Binding criarBindingPropostaPendentenMsVendendor(){
        return BindingBuilder.bind(criarFilaPedidoPendenteMsVendedor())
                .to(criarDirectExchangePedidoPendente())
                .with("pedido-pendente.ms-vendedor");
    }

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> {
            rabbitAdmin.initialize(); // Garanir a criação das filas e exchanges
        };
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}
