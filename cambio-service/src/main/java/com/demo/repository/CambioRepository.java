package com.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Cambio;

public interface CambioRepository extends JpaRepository<Cambio, Long> {

	Cambio findByFromAndTo(String from, String to);
	
}
