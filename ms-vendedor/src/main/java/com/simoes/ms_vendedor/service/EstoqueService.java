package com.simoes.ms_vendedor.service;

import com.simoes.ms_vendedor.constante.MensagemConstante;
import com.simoes.ms_vendedor.entity.Estoque;
import com.simoes.ms_vendedor.entity.Pedido;
import com.simoes.ms_vendedor.entity.dto.EstoqueDto;
import com.simoes.ms_vendedor.exception.StrategyException;
import com.simoes.ms_vendedor.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;

    private final String exchangeSituacaoPedido;

    private final NotificacaoRabbitService notificacaoRabbitService;

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository,
                          NotificacaoRabbitService notificacaoRabbitService,
                          @Value("${rabbitmq.situacaopedido.exchange}") String exchangeSituacaoPedido) {
        this.estoqueRepository = estoqueRepository;
        this.exchangeSituacaoPedido = exchangeSituacaoPedido;
        this.notificacaoRabbitService = notificacaoRabbitService;
    }

    public Estoque adicionarProduto(Estoque estoque) {
        produtoCadastrado(estoque);
        nomeExistente(estoque);
        return estoqueRepository.save(estoque);
    }

    public void nomeExistente(Estoque estoque) {
        var nomeProd = estoqueRepository.findByNomeItemContainingIgnoreCase(estoque.getNomeItem());
        // Buscando se existe nome na lista de produtos cadastrados
        boolean nomeExiste = nomeProd.stream()
                .anyMatch(prod -> prod.getNomeItem().equalsIgnoreCase(estoque.getNomeItem()));

        if (nomeExiste) {
            throw new StrategyException("Este nome já existe no estoque");
        }
    }

    public void produtoCadastrado(Estoque estoque) {
        Estoque produtoEmEstoque = estoqueRepository.findByProdutoId(estoque.getProdutoId());

        if (produtoEmEstoque != null && produtoEmEstoque.getProdutoId().equals(estoque.getProdutoId())) {
            throw new StrategyException("Produto ou ID já Cadastrado !!!");
        }
    }


    public List<Estoque> buscarAllEstoque() {
        List<Estoque> estoques = estoqueRepository.findAll();

        if (estoques.isEmpty()) {
            throw new StrategyException("Nenhum produto encontrado no estoque!");
        }

        return estoques;
    }

    public List<EstoqueDto> buscarProdutoPorNome(String nomeProduto){
        List<Estoque> estoques = estoqueRepository.findByNomeItemContainingIgnoreCase(nomeProduto);
       return estoques.stream()
                .map(estoque -> new EstoqueDto(
                        estoque.getProdutoId(),
                        estoque.getNomeItem(),
                        estoque.getQuantidade()))
                        .collect(Collectors.toList());
    }

    public void analisarPedido(Pedido pedido) throws StrategyException {

        List<EstoqueDto> estoque = buscarProdutoPorNome(pedido.getItem());

        if (estoque.isEmpty()) {
            throw new StrategyException(String.format(MensagemConstante.PRODUTO_NAO_ENCONTRADO));
        }

        int quantidadeEstoque = estoque.get(0).quantidade(); // Obtendo a quantidade do primeiro item encontrado

        if (quantidadeEstoque < 1) {
            throw new StrategyException(String.format(MensagemConstante.PEDIDO_RECUSADO));
        }

        pedido.setProdutoId(estoque.get(0).produtoId());
        Estoque estoque1 = estoqueRepository.findByProdutoId(pedido.getProdutoId());
        // Atualizando estoque
        estoque1.setQuantidade(estoque1.getQuantidade() - pedido.getQuantidade());
        estoqueRepository.save(estoque1);

        System.out.println("Quantidade disponível no estoque: " + estoque1.getQuantidade());
    }

    public void retornoAnalise(Pedido pedido) {
        try {
            analisarPedido(pedido);
            // Pedido aprovado
            pedido.setAprovado(true);
            pedido.setStatus("Enviado");
            pedido.setObservacao(MensagemConstante.PEDIDO_APROVADO);
        } catch (StrategyException ex) {
            pedido.setStatus("Recusado");
            pedido.setObservacao(ex.getMessage());
        }

        notificacaoRabbitService.notificar(pedido, exchangeSituacaoPedido);
    }
}
