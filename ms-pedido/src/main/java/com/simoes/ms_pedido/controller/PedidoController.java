package com.simoes.ms_pedido.controller;

import com.simoes.ms_pedido.entity.Pedido;
import com.simoes.ms_pedido.entity.dto.PedidoDto;
import com.simoes.ms_pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pedidos") // Removemos 'produces' e 'consumes' daqui
@Tag(name = "Pedidos", description = "Endpoints para gerenciar pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Pedido> adicionarPedido(@RequestParam Long usuarioId, @RequestBody Pedido pedido){
        Pedido adicionarPedido = pedidoService.consultarValorAdicionarCarrinho(usuarioId, pedido);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(adicionarPedido.getId())
                        .toUri())
                .body(adicionarPedido);
    }

    @PostMapping("/finalizar")
    public ResponseEntity<String> finalizaPedido(@RequestParam Long usuarioId) {
        try {
            pedidoService.finalizarCompra(usuarioId);
            return ResponseEntity.ok("Compra realizada e pedidos enviados ao RabbitMQ.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erro ao finalizar compra: " + e.getMessage());
        }
    }

    @GetMapping("/processados/{idUsuario}")
    @Operation(summary = "Busca pedidos processados por usuário")
    public ResponseEntity<List<PedidoDto>> buscarPedidoProcessadoPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.buscarPedidoProcessadoPorUsuario(idUsuario));
    }

    @GetMapping("/processados")
    @Operation(summary = "Busca todos pedidos finalizados")
    public ResponseEntity<List<PedidoDto>> buscarPedidosFinalizados() {
        return ResponseEntity.ok(pedidoService.buscarPedidosProcessados());
    }

    @GetMapping("/carrinho/{idUsuario}")
    @Operation(summary = "Busca pedidos ativos no carrinho do usuário")
    public ResponseEntity<List<PedidoDto>> buscarPedidosCarrinho(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.buscarPedidosCarrinho(idUsuario));
    }

    @DeleteMapping("/del")
    @Operation(summary = "Del pedido do carrinho")
    public ResponseEntity<String> del(Long id){
        return ResponseEntity.ok(pedidoService.delPedido(id));
    }

}
