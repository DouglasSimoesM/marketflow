package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    Usuario criarUsuario(Usuario usuario);
    List<Usuario> obterUsuarios();
}
