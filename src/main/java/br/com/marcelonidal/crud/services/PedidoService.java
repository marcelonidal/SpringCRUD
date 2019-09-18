package br.com.marcelonidal.crud.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.marcelonidal.crud.domain.Cliente;
import br.com.marcelonidal.crud.domain.ItemPedido;
import br.com.marcelonidal.crud.domain.PagamentoComBoleto;
import br.com.marcelonidal.crud.domain.Pedido;
import br.com.marcelonidal.crud.domain.enums.EstadoPagamento;
import br.com.marcelonidal.crud.repositories.ItemPedidoRepository;
import br.com.marcelonidal.crud.repositories.PagamentoRepository;
import br.com.marcelonidal.crud.repositories.PedidoRepository;
import br.com.marcelonidal.crud.security.UserSpringSecurity;
import br.com.marcelonidal.crud.services.exceptions.AuthorizationException;
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
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private EmailService emailService;
	
	public Pedido find(Integer id) throws ObjectNotFoundException {
		Optional<Pedido> cat = repo.findById(id);
		
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
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
			//pegar produto e definir no pedido
			element.setProduto(produtoServ.find(element.getProduto().getId()));
			//pegar preco do produto e definir no pedido
			element.setPreco(element.getProduto().getPreco());
			element.setPedido(obj);
		}
		itemPedidoRepo.saveAll(obj.getItens());
		//emailService.sendOrderConfirmationEmail(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
	// RESTRINGIR CONTEUDO POR USUARIO
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSpringSecurity user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageReq = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		Cliente cli = clienteService.find(user.getId());
		return repo.findByCliente(cli, pageReq);
	}
	
}
