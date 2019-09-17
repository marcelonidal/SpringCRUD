package br.com.marcelonidal.crud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.marcelonidal.crud.domain.PagamentoComBoleto;
import br.com.marcelonidal.crud.domain.PagamentoComCartao;

@Configuration
public class JacksonConfig {
	//adiciona os subtipos de pagamento para pagamento no JSON
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
			};
		};
		return builder;
	}
}
