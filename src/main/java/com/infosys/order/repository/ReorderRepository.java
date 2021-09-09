package com.infosys.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infosys.order.entity.Order;

public interface ReorderRepository extends JpaRepository<Order, String>{
	Order findByOrderid(String orderid);
}
