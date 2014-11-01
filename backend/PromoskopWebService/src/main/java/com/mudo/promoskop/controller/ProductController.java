package com.mudo.promoskop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.service.ProductService;
import com.mudo.promoskop.util.JsonGenerator;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String getProductByIdInJSON(@PathVariable(value = "id") int id) {

		Product product = productService.findById(id);
		return JsonGenerator.generateJson(new String[] {}, product);
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String getProductByNameInJSON(@RequestParam(value = "text") String text) {

		List<Product> matchingProductList = productService.findBySubString(text);
		return JsonGenerator.generateJson(new String[] { "branches" }, matchingProductList);
	}
}