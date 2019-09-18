package br.com.marcelonidal.crud.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.marcelonidal.crud.security.UserSpringSecurity;

public class UserService {

	// PEGA USUARIO LOGADO
	public static UserSpringSecurity authenticated() {
		try {
			return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
