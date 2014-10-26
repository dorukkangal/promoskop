package com.mudo.promoskop.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.util.AppContext;
import com.mudo.promoskop.util.JsonGenerator;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String getProductByIdInJSON(@PathVariable(value = "id") int id) {

		Product product = AppContext.getProductService().find(id);
		try {
			return JsonGenerator.generateJson(new String[] {}, product);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String getProductByNameInJSON(@RequestParam(value = "text") String text) {

		List<Product> matchingProductList = AppContext.getProductService().findBySubString(text);
		try {
			return JsonGenerator.generateJson(new String[] { "branches" }, matchingProductList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}