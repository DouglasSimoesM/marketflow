package com.simoes.ms_vendedor.controller;

import com.simoes.ms_vendedor.entity.Estoque;
import com.simoes.ms_vendedor.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Endpoint Para gerenciar Estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @PostMapping
    @Operation(summary = "Adicionar produto no estoque", description = "Recebe id do produto, nome do produto e quantidade")
    public ResponseEntity<Estoque> adicionarProduto(@RequestBody Estoque estoque) {
        Estoque novoEstoque = estoqueService.adicionarProduto(estoque);
        return ResponseEntity.ok(novoEstoque);
    }

    @GetMapping
    @Operation(summary = "Consultar estoque", description = "Retorna todos produtos cadastrados")
    public ResponseEntity<List<Estoque>> buscarAll() {
        List<Estoque> estoques = estoqueService.buscarAllEstoque();
        return ResponseEntity.ok(estoques);
    }

}
