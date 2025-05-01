package com.simoes.ms_pedido.repository;

import com.simoes.ms_pedido.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long > {

    Optional<Usuario> findByCpf(String cpf);
}
