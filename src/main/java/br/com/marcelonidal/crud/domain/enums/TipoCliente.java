package br.com.marcelonidal.crud.domain.enums;

public enum TipoCliente {

	PESSOAFISICA(1, "Pessoa Fisica"),
	PESSOAJURIDICA(2, "Pessoa Juridica");
	
	// CRIACAO DO CONTROLE DA NUMERACAO
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static TipoCliente toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for (TipoCliente element : TipoCliente.values()) {
			if(cod.equals(element.getCod())){
				return element;
			}
		}
		
		throw new  IllegalArgumentException("Id invalido: " + cod);
	}
}
