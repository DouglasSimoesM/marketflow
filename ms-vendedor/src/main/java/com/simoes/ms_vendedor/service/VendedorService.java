package com.simoes.ms_vendedor.service;

import com.simoes.ms_vendedor.entity.Vendedor;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.VendedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;

    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    public Vendedor cadastrarVendedor(Vendedor vendedor){
        boolean existeCpf = vendedorRepository.existsByCpf(vendedor.getCpf());

        if (existeCpf) {
            throw new StrategyException("Vendedor já cadastrado!");
        }
        return vendedorRepository.save(vendedor);
    }

    public List<Vendedor> buscarTodosVendedores(){
        return vendedorRepository.findAll();
    }

    @Transactional
    public void deletarVendedor(Long id){
        Vendedor vendedor = vendedorRepository.findById(id).orElseThrow(()-> new StrategyException("ID não cadastrado"));
        vendedorRepository.delete(vendedor);
    }
}
