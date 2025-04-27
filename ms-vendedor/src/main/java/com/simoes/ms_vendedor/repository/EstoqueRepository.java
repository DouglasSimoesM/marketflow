package com.simoes.ms_vendedor.repository;

import com.simoes.ms_vendedor.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    List<Estoque> findByNomeItemContainingIgnoreCase(String nomeItem);
    Estoque findByProdutoId(Long produtoId);
}
