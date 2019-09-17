package br.com.marcelonidal.crud.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marcelonidal.crud.domain.ItemPedido;
import br.com.marcelonidal.crud.domain.PagamentoComBoleto;
import br.com.marcelonidal.crud.domain.Pedido;
import br.com.marcelonidal.crud.domain.enums.EstadoPagamento;
import br.com.marcelonidal.crud.repositories.ItemPedidoRepository;
import br.com.marcelonidal.crud.repositories.PagamentoRepository;
import br.com.marcelonidal.crud.repositories.PedidoRepository;
import br.com.marcelonidal.crud.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired //Injecao de dependencia
	private PedidoRepository repo;
	@Autowired
	BoletoService boletoService;
	@Autowired
	private PagamentoRepository pagamentoRepo;
	@Autowired
	private ProdutoService produtoServ;
	@Autowired
	private ItemPedidoRepository itemPedidoRepo;
			
	public Pedido find(Integer id) throws ObjectNotFoundException {
		Optional<Pedido> cat = repo.findById(id);
		
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepo.save(obj.getPagamento());
		for (ItemPedido element : obj.getItens()) {
			element.setDesconto(0.0);
			//pegar preco do produto e definir no pedido
			element.setPreco(produtoServ.find(element.getProduto().getId()).getPreco());
			element.setPedido(obj);
		}
		itemPedidoRepo.saveAll(obj.getItens());
		return obj;
	}
	
}
