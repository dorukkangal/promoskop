package com.mudo.promoskop.dao;

import java.util.List;

import com.mudo.promoskop.model.ProductBranch;

public interface ProductBranchDao {

	public List<ProductBranch> findProductBranchWithMinPrice(int barcode, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}
