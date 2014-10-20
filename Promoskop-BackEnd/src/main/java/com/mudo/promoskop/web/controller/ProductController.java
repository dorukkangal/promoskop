package com.mudo.promoskop.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.web.model.Product;
import com.mudo.promoskop.web.util.AppContext;
import com.mudo.promoskop.web.util.JsonPropertyFilter;

@RestController
public class ProductController {

	@RequestMapping(value = "/find/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String getProductByIdInJSON(@PathVariable(value = "id") int id) {

		Product product = AppContext.getProductService().find(id);
		try {
			return JsonPropertyFilter.generateJson(new String[] {}, Arrays.asList(product));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String getProductByNameInJSON(@RequestParam(value = "text") String text) {

		List<Product> matchingProductList = AppContext.getProductService().findBySubString(text);
		try {
			return JsonPropertyFilter.generateJson(new String[] { "price", "branchName", "address", "latitude", "longtitude", "storeName" }, matchingProductList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}