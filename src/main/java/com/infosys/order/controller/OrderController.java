package com.infosys.order.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.infosys.order.dto.OrderDTO;
import com.infosys.order.dto.ProductsorderedDTO;
import com.infosys.order.exception.InfyException;
import com.infosys.order.service.OrderService;
import com.infosys.order.service.ProductOrderService;

@RestController
@CrossOrigin
@RequestMapping
public class OrderController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	Environment environment;
	
	@Autowired
	private OrderService orderservice;
	
	@Autowired
	ProductOrderService productsService;

	@RequestMapping(value = "/api/orders/{orderid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> getSpecificOrderById(@PathVariable String orderid) throws InfyException {
		try {
			logger.info("Order details {}", orderid);
			List<OrderDTO> orders = orderservice.getSpecificOrderById(orderid);
			return new ResponseEntity<>(orders, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}

	@GetMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<OrderDTO>> getAllOrders() throws InfyException {
		try {
			logger.info("Fetching all orders");
			List<OrderDTO> orderdto = orderservice.getAllOrders();
			return new ResponseEntity<>(orderdto, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}
	
	@RequestMapping(value = "/orders/placedorders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> OrderSearch(@RequestBody OrderDTO orderDTO) throws InfyException {
		try {
			String orderid = orderservice.OrderSearch(orderDTO);
			String successMessage = environment.getProperty("ORDER_SUCCESS") + orderid;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}
	}

	@PostMapping(value = "/orders/reorder/{orderid}/{orderid}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> ReOrder(@RequestBody OrderDTO orderDTO) throws InfyException {
		try {
			logger.info("Reordering the order {}", orderDTO.getOrderid());
			boolean order = orderservice.ReOrder(orderDTO);
			String successMessage = environment.getProperty("ORDER_SUCCESS") + order;
			return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}

	}

	
	@RequestMapping(value = "/api/productsorders/{prodid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ProductsorderedDTO>> getProductById(@PathVariable String prodid)
			throws InfyException {
		try {
			logger.info("product details request for ordered product {}", prodid);
			List<ProductsorderedDTO> orders = productsService.getProductById(prodid);
			return new ResponseEntity<>(orders, HttpStatus.CREATED);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);
		}
	}

	@DeleteMapping(value = "/order/{orderid}")
	public ResponseEntity<String> deleteOrder(@PathVariable String orderid) throws InfyException {
		try {
			orderservice.deleteOrder(orderid);
			String successMessage = environment.getProperty("DELETE_SUCCESS");
			return new ResponseEntity<>(successMessage, HttpStatus.OK);
		} catch (Exception exception) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(exception.getMessage()),
					exception);

		}

	}
}
