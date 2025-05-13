package com.simoes.ms_vendedor.service;

import com.simoes.ms_vendedor.constante.MensagemConstante;
import com.simoes.ms_vendedor.entity.Produto;
import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.entity.Vendedor;
import com.simoes.ms_vendedor.entity.dto.ProdutoDto;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.ProdutoRepository;
import com.simoes.ms_vendedor.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    private final String exchangeSituacaoPedido;

    private final NotificacaoRabbitService notificacaoRabbitService;

    private final VendedorRepository vendedorRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository,
                          NotificacaoRabbitService notificacaoRabbitService,
                          @Value("${rabbitmq.situacaopedido.exchange}") String exchangeSituacaoPedido, VendedorRepository vendedorRepository) {
        this.produtoRepository = produtoRepository;
        this.exchangeSituacaoPedido = exchangeSituacaoPedido;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.vendedorRepository = vendedorRepository;
    }

    public Produto adicionarProduto(Long vendedorId, Produto produto) {
        // Busca o vendedor pelo ID
        Vendedor vendedor = vendedorRepository.findById(vendedorId)
                .orElseThrow(() -> new StrategyException("Vendedor não encontrado!"));

        // Configura o vendedor no produto
        produto.setVendedor(vendedor);
        produto.setNomeVendedor(vendedor.getNome());
        produto.setLoja(vendedor.getLoja());

        produtoCadastrado(produto);
        nomeExistente(produto);

        Produto produtoSalvo = produtoRepository.save(produto);

        produtoSalvo.setProdutoId(produtoSalvo.getId());

        return produtoRepository.save(produtoSalvo);
    }


    public void nomeExistente(Produto produto) {
        var nomeProd = produtoRepository.findByNomeItemContainingIgnoreCase(produto.getNomeItem());
        // Buscando se existe nome na lista de produtos cadastrados
        boolean nomeExiste = nomeProd.stream()
                .anyMatch(prod -> prod.getNomeItem().equalsIgnoreCase(produto.getNomeItem()));

        if (nomeExiste) {
            throw new StrategyException("Este nome já existe no estoque");
        }
    }

    public void produtoCadastrado(Produto produto) {
        Produto produtoEmProduto = produtoRepository.findByProdutoId(produto.getProdutoId());

        if (produtoEmProduto != null && produtoEmProduto.getProdutoId().equals(produto.getProdutoId())) {
            throw new StrategyException("Produto ou ID já Cadastrado !!!");
        }
    }


    public List<Produto> buscarAllEstoque() {
        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) {
            throw new StrategyException("Nenhum produto encontrado no estoque!");
        }

        return produtos;
    }

    public List<ProdutoDto> buscarProdutoPorNome(String nomeProduto){
        List<Produto> produtos = produtoRepository.findByNomeItemContainingIgnoreCase(nomeProduto);
       return produtos.stream()
                .map(produto -> new ProdutoDto(
                        produto.getProdutoId(),
                        produto.getNomeItem(),
                        produto.getQuantidade()))
                        .collect(Collectors.toList());
    }

    public void analisarPedido(Pedido pedido) throws StrategyException {

        List<ProdutoDto> estoque = buscarProdutoPorNome(pedido.getItem());

        if (estoque.isEmpty()) {
            throw new StrategyException(String.format(MensagemConstante.PRODUTO_NAO_ENCONTRADO));
        }

        // Obtendo a quantidade do primeiro item encontrado
        int quantidadeEstoque = estoque.get(0).quantidade();

        if (quantidadeEstoque < pedido.getQuantidade()) {
            throw new StrategyException(String.format(MensagemConstante.PRODUTO_SEM_ESTOQUE, quantidadeEstoque, pedido.getItem(), pedido.getQuantidade()));
        }

        pedido.setProdutoId(estoque.get(0).produtoId());
        Produto produto1 = produtoRepository.findByProdutoId(pedido.getProdutoId());
        // Atualizando estoque
        produto1.setQuantidade(produto1.getQuantidade() - pedido.getQuantidade());
        produtoRepository.save(produto1);

        System.out.println("Quantidade disponível no estoque: " + produto1.getQuantidade());
    }

    public void retornoAnalise(Pedido pedido) {
        try {
            analisarPedido(pedido);
            // Pedido aprovado
            pedido.setAprovado(true);
            pedido.setStatus("Enviado");
            pedido.setObservacao(MensagemConstante.PEDIDO_APROVADO);
        } catch (StrategyException ex) {
            pedido.setValorTotal(0);
            pedido.setStatus("Recusado");
            pedido.setObservacao(ex.getMessage());
        }

        notificacaoRabbitService.notificar(pedido, exchangeSituacaoPedido);
    }
}
