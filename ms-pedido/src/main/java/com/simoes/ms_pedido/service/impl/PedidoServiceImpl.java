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

    @Override
    public Pedido criarPedido(Long usuarioId, Pedido pedido) {
        // Busca o usuário no banco
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isPresent()) {
            // Atribuindo Id do cliente (Classe pedido) ao ID do usuario
            pedido.setClienteId(usuario.get().getId());
            pedido.setStatus("Pendente");
            //Salvar Pedido no Bando de dados
            pedidoRepository.save(pedido);
            //Enviando pedido a fila 'pedido-pentende.ms-notificacao e pedido-pendente.ms-vendedor'
            notificacaoRabbitService.notificar(pedido, exchange);
            return pedido;
        } else {
            throw new RuntimeException("USUÁRIO NÃO ENCONTRADO");
        }
    }

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
                        pedido.getItem(),
                        pedido.getValorTotal()))
                .collect(Collectors.toList());
    }


}
