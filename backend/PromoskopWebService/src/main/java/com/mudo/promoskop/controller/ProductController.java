package com.mudo.promoskop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.exception.InvalidRequestException;
import com.mudo.promoskop.jackson.JacksonFilter;
import com.mudo.promoskop.model.Branch;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.request.CheapestPriceRequest;
import com.mudo.promoskop.service.BranchService;
import com.mudo.promoskop.service.ProductService;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private BranchService branchService;

	@JacksonFilter(excludePaths = { "product.product_branches", "branch.product_branches" })
	@RequestMapping(value = "/barcode", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" }, consumes = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<ProductBranch> getProductByIdInJSON(@RequestBody CheapestPriceRequest holder) throws Exception {
		if (holder.getBarcodes().length != 1)
			throw new InvalidRequestException();
		List<ProductBranch> branches = productService.findByBarcodeInRadius(holder.getBarcodes()[0], holder.getCurrentLatitude(), holder.getCurrentLongitude(),
				holder.getMaxDistance());
		return branches;
	}

	@RequestMapping(value = "/findBySubString", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Product> getProductByNameInJSON(@RequestParam(value = "text") String containText) throws Exception {
		List<Product> matchingProducts = productService.findBySubString(containText);
		return matchingProducts;
	}

	@RequestMapping(value = "/popular", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Product> getPopularProductsJSON(@RequestParam(value = "count") int count) throws Exception {
		List<Product> popularProducts = productService.findMaxGapped(count);
		return popularProducts;
	}

	@JacksonFilter(excludePaths = { "product.product_branches"})
	@RequestMapping(value = "/basket/calculate", method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" }, consumes = { "application/json; charset=UTF-8" })
	public @ResponseBody
	List<Branch> getCheapestPrice(@RequestBody CheapestPriceRequest holder) throws Exception {
		List<Branch> branches = branchService.find(holder.getBarcodes(), holder.getCurrentLatitude(), holder.getCurrentLongitude(),
				holder.getMaxDistance());
		return branches;
	}
}