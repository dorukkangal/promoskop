package com.mudo.promoskop.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductBranchDao;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.service.ProductBranchService;

@Service
@Transactional
public class ProductBranchServiceImpl implements ProductBranchService {

	@Autowired
	private ProductBranchDao productBranchDao;

	@Override
	public List<ProductBranch> findProductBranchWithMinPrice(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		return productBranchDao.findProductBranchWithMinPrice(barcode, minLatitude, minLongitude, maxLatitude, maxLongitude);
	}
}
