package com.mudo.promoskop.web.controller;

import java.util.Arrays;

import org.springframework.http.MediaType;
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

	@RequestMapping(value = "/find", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	String getProductInJSON(@RequestParam(value = "id") int id) {

		Product product = AppContext.getProductService().find(id);
		try {
			return JsonPropertyFilter.generateJson(new String[] {}, Arrays.asList(product));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}