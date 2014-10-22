package com.mudo.promoskop.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.mudo.promoskop.web.model.Product;
import com.mudo.promoskop.web.util.AppContext;
import com.mudo.promoskop.web.util.JsonPropertyFilter;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView getProductByIdInJSON(@PathVariable(value = "id") int id) {

		Product product = AppContext.getProductService().find(id);
		try {
			ModelAndView model = new ModelAndView("json");
			return model.addObject("json", JsonPropertyFilter.generateJson(new String[] { "url" }, Arrays.asList(product)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET)
	public ModelAndView getProductByNameInJSON(@RequestParam(value = "text") String text) {

		List<Product> matchingProductList = AppContext.getProductService().findBySubString(text);
		try {
			ModelAndView model = new ModelAndView("json");
			return model.addObject("json",
					JsonPropertyFilter.generateJson(new String[] { "price", "branch_name", "address", "latitude", "longtitude", "store_name" }, matchingProductList));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}