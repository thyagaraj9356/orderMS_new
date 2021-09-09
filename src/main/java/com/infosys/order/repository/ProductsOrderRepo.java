package com.infosys.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.order.entity.Productsorder;

public interface  ProductsOrderRepo extends JpaRepository<Productsorder, String>{
	
	List<Productsorder> findByProdid(String prodid);
	}

