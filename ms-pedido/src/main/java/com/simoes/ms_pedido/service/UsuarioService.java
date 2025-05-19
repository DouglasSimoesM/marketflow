package com.simoes.ms_pedido.service;

import com.simoes.ms_pedido.entity.Carrinho;
import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.exception.StrategyException;
import com.simoes.ms_pedido.repository.CarrinhoRepository;
import com.simoes.ms_pedido.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CarrinhoRepository carrinhoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, CarrinhoRepository carrinhoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    public Usuario criarUsuario(Usuario usuario){
        // Validando número de telefone com regex (DDD + 9 dígitos)
        if (usuario.getTelefone() == null || !usuario.getTelefone().matches("^\\d{2}\\d{9}$")) {
            throw new StrategyException("Numero de telefone invalido " + usuario.getTelefone());  // Impossibilita numero de telefone invalido ou null
        }

        var usuarioexiste = usuarioRepository.findByCpf(usuario.getCpf());

        if (usuarioexiste.isEmpty()){
            Carrinho carrinho = new Carrinho();
            carrinho.setUsuario(usuario);
            usuario.setCarrinho(carrinho);

            usuarioRepository.save(usuario);
            carrinhoRepository.save(carrinho);

            return usuario;
        } else {
            throw new StrategyException("Usuario já existe");
        }
    }

    public List<Usuario> obterUsuarios() {
        return usuarioRepository.findAll();
    }
}
