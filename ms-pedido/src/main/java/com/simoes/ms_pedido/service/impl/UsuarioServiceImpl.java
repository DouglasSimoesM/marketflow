package com.simoes.ms_pedido.service.impl;

import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import com.simoes.ms_pedido.service.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario criarUsuario(Usuario usuario) {
        Usuario save = usuarioRepository.save(usuario);
        return save;
    }

    @Override
    public List<Usuario> obterUsuarios() {
        return usuarioRepository.findAll();
    }
}
