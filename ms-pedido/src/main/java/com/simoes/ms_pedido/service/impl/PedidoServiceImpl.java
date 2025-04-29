package com.simoes.ms_pedido.service.impl;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.entity.dto.PedidoDTO;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import com.simoes.ms_pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacaoRabbitServiceImpl notificacaoRabbitService;
    private final String exchange;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             UsuarioRepository usuarioRepository,
                             NotificacaoRabbitServiceImpl notificacaoRabbitService,
                             @Value("${rabbitmq.pedidopendente.exchange}") String exchange) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.exchange = exchange;
    }

    // Metodo para ***Criar um pedido***
    @Override
    public Pedido criarPedido(Long usuarioId, Pedido pedido) {

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isPresent()) {
            pedido.setClienteId(usuario.get().getId());
            pedido.setUsuario(usuario.get());
            pedido.setStatus("Pendente");
            pedido.setAprovado(false);

            pedidoRepository.save(pedido);

            // Enviando pedido para a fila RabbitMQ
            notificacaoRabbitService.notificar(pedido, exchange);

            return pedido;
        } else {
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
    }

    // Metodo para ***Buscar pedido***
    @Override
    public List<PedidoDTO> obterPedido(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }

        // Obtendo pedidos do cliente
        List<Pedido> pedidos = pedidoRepository.findByClienteId(id);

        // Convertendo para DTO
        return pedidos.stream()
                .map(pedido -> new PedidoDTO(
                        pedido.getClienteId(),
                        usuario.get().getNome(),
                        pedido.getQuantidade(),
                        pedido.getItem(),
                        pedido.getValorTotal(),
                        pedido.getObservacao()))
                .collect(Collectors.toList());
    }


}
