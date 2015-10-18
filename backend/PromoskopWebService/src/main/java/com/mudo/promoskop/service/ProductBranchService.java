package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.ProductBranch;

public interface ProductBranchService {

	public List<ProductBranch> findCheapestAndClosest(String barcode, double currentLatitude, double currentLongitude);

	public List<ProductBranch> findCheapestInRadius(String[] barcodes, double currentLatitude, double currentLongitude, double radius);
}
