package com.mudo.promoskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.service.JsonService;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	private JsonService jsonService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getProductByIdInJSON(@PathVariable(value = "id") int id) {
		return jsonService.generateJsonForProduct(JsonService.PRODUCT_BY_ID_FILTER, id);
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getProductByNameInJSON(@RequestParam(value = "text") String containText) {
		return jsonService.generateJsonForProducts(JsonService.PRODUCT_BY_NAME_FILTER, containText);
	}
}