package com.mudo.promoskop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.exception.InternalServerErrorException;
import com.mudo.promoskop.exception.ResourceNotFoundException;
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
	ResponseEntity<String> getProductByIdInJSON(@PathVariable(value = "id") int id) {

		try {
			Product product = productService.findById(id);
			String json = JsonGenerator.generateJson(new String[] {}, product);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			String json = JsonGenerator.generateErrorJson(e);
			return new ResponseEntity<String>(json, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			String json = JsonGenerator.generateErrorJson(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getProductByNameInJSON(@RequestParam(value = "text") String text) {
		try {
			List<Product> matchingProductList = productService.findBySubString(text);
			String json = JsonGenerator.generateJson(new String[] { "branches" }, matchingProductList);
			return new ResponseEntity<String>(json, HttpStatus.OK);
			// } catch (ResourceNotFoundException e) {
			// String json = JsonGenerator.generateErrorJson(e);
			// return new ResponseEntity<String>(json, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			String json = JsonGenerator.generateErrorJson(new InternalServerErrorException());
			return new ResponseEntity<String>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}