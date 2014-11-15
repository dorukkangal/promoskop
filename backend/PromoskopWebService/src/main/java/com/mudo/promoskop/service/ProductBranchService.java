package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.ProductBranch;

public interface ProductBranchService {

	public List<ProductBranch> findProductBranchWithMinPrice(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
}
