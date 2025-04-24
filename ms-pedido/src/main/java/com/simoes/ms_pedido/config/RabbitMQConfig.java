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

    @Bean
    public Queue criarFilaPedidoPendenteMsVendedor(){
        return QueueBuilder.durable("pedido-pendente.ms-vendedor").build();
    }

    @Bean
    public Queue criarFilaPedidoPendenteMsNotificacao(){
            return QueueBuilder.durable("pedido-pendente.ms-notificacao").build();
    }

    @Bean
    public FanoutExchange criarFanoutExchangePedidoPendente() {
        return ExchangeBuilder.fanoutExchange(exchangePedidoPendente).build();
    }

    @Bean
    public Binding criarBindingPropostaPendentenMsVendendor(){
        return BindingBuilder.bind(criarFilaPedidoPendenteMsVendedor())
                .to(criarFanoutExchangePedidoPendente());
    }

    @Bean
    public Binding criarBindingPropostaPendentenMsNotificacao(){
        return BindingBuilder.bind(criarFilaPedidoPendenteMsNotificacao())
                .to(criarFanoutExchangePedidoPendente());
    }

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
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
