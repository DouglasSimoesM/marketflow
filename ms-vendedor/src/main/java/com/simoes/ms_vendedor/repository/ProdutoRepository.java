package com.simoes.ms_vendedor.repository;

import com.simoes.ms_vendedor.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByNomeItemContainingIgnoreCase(String nomeItem);
    Produto findByProdutoId(Long produtoId);
}