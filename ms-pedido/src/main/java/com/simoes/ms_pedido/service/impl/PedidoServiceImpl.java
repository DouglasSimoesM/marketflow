package com.simoes.ms_pedido.service.impl;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.PedidoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import com.simoes.ms_pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Pedido criarPedido(Long usuarioId, Pedido pedido) {
        // Busca o usuário no banco
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        if (usuario.isPresent()) {
            pedido.setClienteId(usuario.get().getId().toString());
            pedido.setStatus("Pendente");
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
    }

    @Override
    public Pedido obterPedido(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }
}
