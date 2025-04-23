package com.simoes.ms_pedido.controller;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.service.impl.PedidoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciar pedidos")
public class PedidoController {

    @Autowired
    private PedidoServiceImpl pedidoService;

    @PostMapping("/{usuarioId}")
    @Operation(summary = "Criar um novo pedido", description = "Recebe o ID do usuário e pedido, adiciona na fila do RabbitMQ para processamento.")    public ResponseEntity<Pedido> criarPedido(@PathVariable Long usuarioId,
                                              @RequestBody Pedido pedido) {
        var pedidoCreate = pedidoService.criarPedido(usuarioId, pedido);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(pedidoCreate.getId())
                        .toUri())
                        .body(pedidoCreate);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido", description = "Retorna informações sobre um pedido existente.")
    public ResponseEntity<Pedido> obterPedido(@PathVariable Long id) {
        var pedido = pedidoService.obterPedido(id);
        return ResponseEntity.ok(pedido);
    }
}