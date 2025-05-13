package com.simoes.ms_vendedor.controller;

import com.simoes.ms_vendedor.entity.Produto;
import com.simoes.ms_vendedor.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque", description = "Endpoint Para gerenciar Estoque")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @Operation(summary = "Adicionar produto no estoque", description = "Recebe id do produto, nome do produto e quantidade")
    public ResponseEntity<Produto> adicionarProduto(@RequestParam Long vendedorId, @RequestBody Produto produto) {
        Produto novoProduto = produtoService.adicionarProduto(vendedorId, produto);
        return ResponseEntity.ok(novoProduto);
    }

    @GetMapping
    @Operation(summary = "Consultar estoque", description = "Retorna todos produtos cadastrados")
    public ResponseEntity<List<Produto>> buscarAll() {
        List<Produto> produtos = produtoService.buscarAllEstoque();
        return ResponseEntity.ok(produtos);
    }

}
