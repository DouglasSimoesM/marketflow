package com.simoes.ms_vendedor.service;

import com.simoes.ms_vendedor.constante.MensagemConstante;
import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.entity.Produto;
import com.simoes.ms_vendedor.entity.Vendedor;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.ProdutoRepository;
import com.simoes.ms_vendedor.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VendedorService {

    private final VendedorRepository vendedorRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public VendedorService(VendedorRepository vendedorRepository,
                           ProdutoRepository produtoRepository) {
        this.vendedorRepository = vendedorRepository;
        this.produtoRepository = produtoRepository;
    }

    public Vendedor cadastrarVendedor(Vendedor vendedor) {

        // Validando número de telefone com regex (DDD + 9 dígitos)
        if (vendedor.getTelefone() == null || !vendedor.getTelefone().matches("^\\d{2}\\d{9}$")) {
            throw new StrategyException("Numero invalido "+ vendedor.getTelefone());  // Impossibilita numero de telefone invalido ou null
        }
        boolean existeCpf = vendedorRepository.existsByCpf(vendedor.getCpf());

        if (existeCpf) {
            throw new StrategyException("Vendedor já cadastrado!");
        }
        return vendedorRepository.save(vendedor);
    }

    public List<Vendedor> buscarTodosVendedores() {
        return vendedorRepository.findAll();
    }

    public void consultarValor(Pedido pedido) {
        try {
            // Busca produto pelo nome
            List<Produto> produto = produtoRepository.findByNomeItemContainingIgnoreCase(pedido.getNomeItem());
            // Validar se produto existe
            if (produto.isEmpty()) {
                String msg = MensagemConstante.PRODUTO_NAO_ENCONTRADO;
                pedido.setObservacao(msg);
                pedido.setAprovado(false);
                throw new StrategyException(msg);
            }

            Produto produto1 = produtoRepository.findById(produto.getFirst().getId())
                    .orElseThrow(() -> new StrategyException("ID produto não encontrado"));
            // Validar se existe a quatidade em estoque
            if (produto1.getQuantidade() < pedido.getQuantidade()) {
                if (produto1.getQuantidade() > 0) {
                    String msg = String.format(MensagemConstante.PEDIDO_MAIOR_ESTOQUE, produto1.getQuantidade(), pedido.getNomeItem(), pedido.getQuantidade());
                    pedido.setObservacao(msg);
                    pedido.setAprovado(false);
                    throw new StrategyException(msg);
                } else {
                    String msg = String.format(MensagemConstante.PRODUTO_SEM_ESTOQUE);
                    pedido.setObservacao(msg);
                    pedido.setAprovado(false);
                    throw new StrategyException(msg);
                }

            }

            // Define os atributos do pedido APROVADO
            pedido.setProdutoId(produto1.getProdutoId());
            pedido.setValor(produto1.getValor());
            pedido.setAprovado(true);
            if (produto1.getLoja().equalsIgnoreCase("MAGAZINE LUIZA")) {
                String msg = String.format(MensagemConstante.CONVERSA_COM_VENDEDOR, produto1.getVendedor().getNome(), produto1.getVendedor().getTelefone());
                pedido.setObservacao(msg);
            } else {
                pedido.setObservacao("Finalize seu pedido");
            }

        } catch (StrategyException ex) {
            pedido.setAprovado(false);
            pedido.setValor(0);
        }
    }

    @Transactional
    public void deletarVendedor(Long id) {
        Vendedor vendedor = vendedorRepository.findById(id).orElseThrow(() -> new StrategyException("ID não cadastrado"));
        vendedorRepository.delete(vendedor);
    }
}
