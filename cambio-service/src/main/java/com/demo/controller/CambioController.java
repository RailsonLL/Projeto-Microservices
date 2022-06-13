package com.demo.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.model.Cambio;
import com.demo.repository.CambioRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cambio Service API")
@RestController
@RequestMapping("cambio-service")
public class CambioController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private CambioRepository repository;
	
	/**
	 * Converte o valor(amount) de uma moeda(from) para outra moeda(to)
	 */
	@Operation(description = "Get cambio from currency!")
	@GetMapping(value = "/{amount}/{from}/{to}")
	public Cambio getCambio(@PathVariable("amount") BigDecimal amount,
			                @PathVariable("from") String from,
	                        @PathVariable("to") String to) {
		
		var cambio = repository.findByFromAndTo(from, to);
		
		if (cambio == null) throw new RuntimeException("Currency Unsupported");
		
		var port = environment.getProperty("local.server.port");
		cambio.setEnvironment(port);
		BigDecimal conversionFactor = cambio.getConversionFactor();
		BigDecimal convertedValue = conversionFactor.multiply(amount);
		cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
		
		return cambio;
	}
	
}
