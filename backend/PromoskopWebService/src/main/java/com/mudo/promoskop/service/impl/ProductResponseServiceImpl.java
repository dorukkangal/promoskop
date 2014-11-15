package com.mudo.promoskop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.model.Branch;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.model.Store;
import com.mudo.promoskop.response.BranchResponse;
import com.mudo.promoskop.response.ProductResponse;
import com.mudo.promoskop.service.ProductResponseService;
import com.mudo.promoskop.service.ProductService;

@Service
@Transactional
public class ProductResponseServiceImpl implements ProductResponseService {

	@Autowired
	private ProductService productService;

	@Override
	public ProductResponse findByBarcode(String barcode) {
		Product p = productService.findByBarcode(barcode);
		return convertResponseBean(p);
	}

	@Override
	public List<ProductResponse> findBySubString(String containText) {
		List<Product> matchingProducts = productService.findBySubString(containText);
		return convertResponseBeans(matchingProducts);
	}
	
	@Override
	public List<ProductResponse> findMaxGapped(int count) {
		List<Product> maxQueriedProducts = productService.findMaxGapped(count);
		return convertResponseBeans(maxQueriedProducts);
	}

	private List<ProductResponse> convertResponseBeans(List<Product> products) {
		List<ProductResponse> responses = new ArrayList<ProductResponse>();
		for (Product product : products)
			responses.add(convertResponseBean(product));
		return responses;
	}

	private ProductResponse convertResponseBean(Product product) {
		ProductResponse productResponse = new ProductResponse(product.getBarcode(), product.getName(), product.getUrl());
		for (ProductBranch productBranch : product.getProductBranchs()) {
			BranchResponse branchResponse = new BranchResponse();
			branchResponse.setPrice(productBranch.getPrice());

			Branch branch = productBranch.getBranch();
			if (branch != null) {
				branchResponse.setBranchName(branch.getName());
				branchResponse.setBranchAddress(branch.getAddress());
				branchResponse.setLatitude(branch.getLatitude());
				branchResponse.setLongitude(branch.getLongitude());

				Store store = branch.getStore();
				if (store != null) {
					branchResponse.setStoreName(store.getName());
					branchResponse.setStoreLogo(store.getLogo());
				}
			}
			productResponse.getBranches().add(branchResponse);
		}
		return productResponse;
	}
}
