package br.com.marcelonidal.crud.domain.enums;

public enum Perfil {

	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	// CRIACAO DO CONTROLE DA NUMERACAO
	private int cod;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static Perfil toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for (Perfil element : Perfil.values()) {
			if(cod.equals(element.getCod())){
				return element;
			}
		}
		
		throw new  IllegalArgumentException("Id invalido: " + cod);
	}
}
