package com.infosys.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infosys.order.dto.OrderDTO;
import com.infosys.order.entity.Order;
import com.infosys.order.exception.InfyException;
import com.infosys.order.repository.OrderRepository;
import com.infosys.order.repository.ReorderRepository;

@Service
@Transactional
public class OrderService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	OrderRepository orderrepo;

	@Autowired
	ReorderRepository reorderRepo;

	public List<OrderDTO> getSpecificOrderById(String orderid) throws InfyException {

		logger.info("Order details of Id {}", orderid);

		Iterable<Order> order = orderrepo.findByOrderid(orderid);
		List<OrderDTO> orderDTO = new ArrayList<OrderDTO>();

		order.forEach(ord -> {
			orderDTO.add(OrderDTO.valueOf(ord));
		});
		if (orderDTO.isEmpty())
			throw new InfyException("ORDERS_NOT_FOUND");
		logger.info("{}", orderDTO);
		return orderDTO;
	}

	public List<OrderDTO> getAllOrders() throws InfyException {

		Iterable<Order> orders = orderrepo.findAll();
		List<OrderDTO> orderDTOs = new ArrayList<>();

		orders.forEach(order -> {
			OrderDTO orderDTO = OrderDTO.valueOf(order);
			orderDTOs.add(orderDTO);
		});
		if (orderDTOs.isEmpty())
			throw new InfyException("ORDERS_NOT_FOUND");
		logger.info("Order Details : {}", orderDTOs);
		return orderDTOs;
	}

	public String OrderSearch(OrderDTO orderDTO) throws InfyException {

		Order order = orderrepo.getOrderByBuyerIdAndAddress(orderDTO.getBuyerid(), orderDTO.getAddress());
		if (order != null) {
			return order.getOrderid();
		} else {
			throw new InfyException("ORDER_NOT_PLACED");
		}
	}
	
	public boolean ReOrder(OrderDTO orderDTO) throws InfyException {
		logger.info("Reordering the order{}", orderDTO.getOrderid());
		Order ord = reorderRepo.findByOrderid(orderDTO.getOrderid());
		if (ord != null && ord.getOrderid().equals(orderDTO.getOrderid())) {
			ord.setAddress(orderDTO.getAddress());
			return true;
		} else {
			throw new InfyException("ORDER_NOT_PLACED");
		}
	}

	public void deleteOrder(String orderid) throws InfyException {
		Optional<Order> ord = orderrepo.findById(orderid);
		ord.orElseThrow(() -> new InfyException("ORDERS_NOT_FOUND"));
		orderrepo.deleteById(orderid);
	}
}