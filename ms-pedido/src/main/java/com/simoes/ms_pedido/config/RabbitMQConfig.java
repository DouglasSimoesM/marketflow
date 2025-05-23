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

    // Definição de Exchanges
    @Value("${rabbitmq.pedidopendente.exchange}")
    private String exchangePedidoPendente;

    @Value("${rabbitmq.situacaopedido.exchange}")
    private String exchangeSituacaoPedido;

    @Value("${rabbitmq.pedidoconsulta.exchange}")
    private String exchangeConsultarValor;

    @Bean
    public FanoutExchange criarExchangeSituacaoPedido(){
        return ExchangeBuilder.fanoutExchange(exchangeSituacaoPedido).build();
    }

    @Bean
    public DirectExchange criarDirectExchangePedidoPendente(){
        return ExchangeBuilder.directExchange(exchangePedidoPendente).build();
    }

    @Bean
    public DirectExchange criarDirectExchangeConsultarValor(){
        return ExchangeBuilder.directExchange(exchangeConsultarValor).build();
    }

    // Definição de Filas
    @Bean
    public Queue criarFilaSituacaoPedidoMsPedido(){
        return QueueBuilder.durable("situacao-pedido.ms-pedido").build();
    }

    @Bean
    public Queue criarFilaSituacaoPedidoMsNotificacao(){
        return QueueBuilder.durable("situacao-pedido.ms-notificacao").build();
    }

    @Bean
    public Queue criarFilaConsultaConcluidaMsNotificacao(){
        return QueueBuilder.durable("consulta-concluida.ms-notificacao").build();
    }

    @Bean
    public Queue criarFilaConsultarValorMsVendedor(){
        return QueueBuilder.durable("consultar-valor.ms-vendedor").build();
    }

    @Bean
    public Queue criarFilaConsultarValorMsPedido(){
        return QueueBuilder.durable("consultar-valor.ms-pedido").build();
    }

    @Bean
    public Queue criarFilaPedidoPendenteMsVendedor(){
        return QueueBuilder.durable("pedido-pendente.ms-vendedor").build();
    }

    // Definição de Bindings
    @Bean
    public Binding criarBindingConsultaConcluidaMsPedido(){
        return BindingBuilder.bind(criarFilaConsultarValorMsPedido())
                .to(criarDirectExchangeConsultarValor())
                .with("consultar-valor.ms-pedido");
    }

    @Bean
    public Binding criarBindingConsultaConcluidaMsNotificacao(){
        return BindingBuilder.bind(criarFilaConsultaConcluidaMsNotificacao())
                .to(criarDirectExchangeConsultarValor())
                .with("consulta-concluida.ms-notificacao");
    }

    @Bean
    public Binding criarBindingConsultarValorMsVendedor(){
        return BindingBuilder.bind(criarFilaConsultarValorMsVendedor())
                .to(criarDirectExchangeConsultarValor())
                .with("consultar-valor.ms-vendedor");
    }

    @Bean
    public Binding criarBindingConsultarValorMsPedido(){
        return BindingBuilder.bind(criarFilaConsultarValorMsPedido())
                .to(criarDirectExchangeConsultarValor())
                .with("consultar-valor.ms-pedido");
    }

    @Bean
    public Binding criarBindingSituacaoPedidoMsPedido(){
        return BindingBuilder.bind(criarFilaSituacaoPedidoMsPedido())
                .to(criarExchangeSituacaoPedido());
    }

    @Bean
    public Binding criarBindingSituacaoPedidoMsNotificacao(){
        return BindingBuilder.bind(criarFilaSituacaoPedidoMsNotificacao())
                .to(criarExchangeSituacaoPedido());
    }

    @Bean
    public Binding criarBindingPedidoPendenteMsVendedor(){
        return BindingBuilder.bind(criarFilaPedidoPendenteMsVendedor())
                .to(criarDirectExchangePedidoPendente())
                .with("pedido-pendente.ms-vendedor");
    }

    // Configurações do RabbitMQ
    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> {
            rabbitAdmin.initialize(); // Garante a criação das filas e exchanges no RabbitMQ
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
