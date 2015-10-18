package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.model.ProductBranch;

public interface ProductService {

	public Product findByBarcode(String barcode);

	public List<ProductBranch> findByBarcodeInRadius(String barcode, double currentLatitude, double currentLongitude, double maxDistance);

	public List<Product> findBySubString(String containText);

	public List<Product> findMaxGapped(int count);
}
