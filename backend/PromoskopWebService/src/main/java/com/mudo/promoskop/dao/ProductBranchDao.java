package com.mudo.promoskop.dao;

import java.util.List;

import com.mudo.promoskop.model.ProductBranch;

public interface ProductBranchDao {

	public List<ProductBranch> findCheapestInCoordinates(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
}
