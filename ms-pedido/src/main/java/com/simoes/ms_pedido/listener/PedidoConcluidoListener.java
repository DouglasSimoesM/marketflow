package com.simoes.ms_pedido.listener;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import com.simoes.ms_pedido.service.PedidoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoConcluidoListener {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

@RabbitListener(queues = "${rabbitmq.queue.situacao.pedido}")
public void pedidoConcluido(Pedido pedido) {
    if (pedido.isAprovado()) {
            // Lógica para pedidos aprovados
        Usuario usuario = usuarioRepository.findById(pedido.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Converte para lista ao enviar
        pedidoService.adicionarPedidoProcessado(usuario, List.of(pedido));
    } else {
        Usuario usuario = usuarioRepository.findById(pedido.getIdUsuario())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        pedido.setValorTotal(BigDecimal.valueOf(0));
        // Converte para lista ao enviar
        pedidoService.adicionarPedidoProcessado(usuario, List.of(pedido));
        }
    }
}