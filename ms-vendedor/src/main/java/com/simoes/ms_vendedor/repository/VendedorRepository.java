package com.simoes.ms_vendedor.repository;

import com.simoes.ms_vendedor.entity.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    boolean existsByCpf(String cpf);  //
}

