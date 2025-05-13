package com.simoes.ms_vendedor.controller;

import com.simoes.ms_vendedor.entity.Vendedor;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.VendedorRepository;
import com.simoes.ms_vendedor.service.VendedorService;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendedor")
@Tag(name = "Vendedor", description = "Endpoint para gerenciar Estoque")
public class VendedorController {

    @Autowired
    public VendedorService vendedorService;

    @Autowired
    public VendedorRepository vendedorRepository;

    @PostMapping
    public ResponseEntity<Vendedor> cadastrarVendedor(@RequestBody Vendedor vendedor){
        return ResponseEntity.ok(vendedorService.cadastrarVendedor(vendedor));
    }

    @GetMapping
    public ResponseEntity<List<Vendedor>> buscarVendedor(){
        return ResponseEntity.ok(vendedorService.buscarTodosVendedores());
    }

    @DeleteMapping
    public ResponseEntity<String> deletar(Long idVendedor){
        Vendedor vendedor = vendedorRepository.findById(idVendedor)
                .orElseThrow(()-> new StrategyException("ID não existe"));
        vendedorService.deletarVendedor(idVendedor);
        return ResponseEntity.ok(String.format("Vendedor %s excluído", vendedor.getNome()));
    }

}
