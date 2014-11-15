package com.mudo.promoskop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.mudo.promoskop.service.BranchResponseService;
import com.mudo.promoskop.service.ProductBranchService;

@Service
@Transactional
public class BranchResponseServiceImpl implements BranchResponseService {

	@Autowired
	private ProductBranchService productBranchService;

	@Override
	public List<BranchResponse> findProductBranchWithMinPrice(String[] barcodeIds, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		List<BranchResponse> allResponses = new ArrayList<BranchResponse>();
		for (String barcode : barcodeIds) {
			List<ProductBranch> productBranches = productBranchService.findProductBranchWithMinPrice(barcode, minLatitude, minLongitude, maxLatitude, maxLongitude);
			List<BranchResponse> barcodeResponses = convertResponseBeans(productBranches);
			allResponses = unifyResponseList(allResponses, barcodeResponses);
		}
		return allResponses;
	}

	private List<BranchResponse> convertResponseBeans(List<ProductBranch> productBranches) {
		List<BranchResponse> responses = new ArrayList<BranchResponse>();
		for (ProductBranch productBranch : productBranches)
			responses.add(convertResponseBean(productBranch));
		return responses;
	}

	private BranchResponse convertResponseBean(ProductBranch productBranch) {
		BranchResponse branchResponse = new BranchResponse();

		Product product = productBranch.getProduct();
		ProductResponse productResponse = new ProductResponse(product.getBarcode(), product.getName(), product.getUrl());
		branchResponse.getProducts().addAll(Arrays.asList(productResponse));
		branchResponse.setPrice(productBranch.getPrice());

		Branch branch = productBranch.getBranch();
		if (branch != null) {
			branchResponse.setBranchId(branch.getId());
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
		return branchResponse;
	}

	private List<BranchResponse> unifyResponseList(List<BranchResponse> mainList, List<BranchResponse> subList) {
		for (BranchResponse response : subList) {
			int index = indexOf(mainList, response);
			if (index >= 0) {
				BranchResponse main = mainList.get(index);
				main.getProducts().addAll(response.getProducts());
				main.setPrice(main.getPrice() + response.getPrice());
			} else {
				mainList.add(response);
			}
		}
		return mainList;
	}

	private int indexOf(List<BranchResponse> responseList, BranchResponse response) {
		int index = -1;
		for (int i = 0; i < responseList.size(); i++)
			if (responseList.get(i).getBranchId() == response.getBranchId())
				index = i;
		return index;
	}
}
