package br.com.marcelonidal.crud.domain.enums;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	// CRIACAO DO CONTROLE DA NUMERACAO
	private int cod;
	private String descricao;
	
	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static EstadoPagamento toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for (EstadoPagamento element : EstadoPagamento.values()) {
			if(cod.equals(element.getCod())){
				return element;
			}
		}
		
		throw new  IllegalArgumentException("Id invalido: " + cod);
	}
}
