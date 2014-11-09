package com.mudo.promoskop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.request.CheapestPriceRequest;
import com.mudo.promoskop.service.JsonService;
import com.mudo.promoskop.util.JsonFilter;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	private JsonService jsonService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getProductByIdInJSON(@PathVariable(value = "id") int id) {
		ResponseEntity<String> response = jsonService.generateJsonForProduct(JsonFilter.PRODUCT_BY_ID_FILTER, id);
		return response;
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getProductByNameInJSON(@RequestParam(value = "text") String containText) {
		return jsonService.generateJsonForProducts(JsonFilter.PRODUCT_BY_NAME_FILTER, containText);
	}

	@RequestMapping(value = "/getPopularProducts", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getPopularProductsJSON(@RequestParam(value = "count") int count) {
		return jsonService.generateJsonForPopularProducts(JsonFilter.POPULAR_PRODUCTS_FILTER, count);
	}

	@RequestMapping(value = "/basket/calculate", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" }, consumes = { "application/json; charset=UTF-8" })
	public @ResponseBody
	ResponseEntity<String> getCheapestPrice(@RequestBody CheapestPriceRequest holder) {
		return jsonService.generateJsonForBasket(JsonFilter.BASKET_FILTER, holder.getCurrentLatitude(), holder.getCurrentLongitude(), holder.getMaxDistance(),
				holder.getBarcodeIds());
	}
}