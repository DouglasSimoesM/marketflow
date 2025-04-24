package com.simoes.ms_pedido.controller;

import com.simoes.ms_pedido.entity.Usuario;
import com.simoes.ms_pedido.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuarios", description = "Endpoints para gerenciar usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo Usuario", description = "Recebe nome, cpf, telefone e endereço")
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario){
        Usuario criar = usuarioService.criarUsuario(usuario);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(criar.getId())
                        .toUri())
                .body(criar);
    }

    @GetMapping
    @Operation(summary = "Busca todos Usuarios")
    public ResponseEntity<List<Usuario>> obterUsuarios(){
        return ResponseEntity.ok(usuarioService.obterUsuarios());
    }
}

