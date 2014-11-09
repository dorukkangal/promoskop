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
	public List<ProductBranch> findProductBranchWithMinPrice(int barcode, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
		List<ProductBranch> l = productBranchDao.findProductBranchWithMinPrice(barcode, minLatitude, maxLatitude, minLongitude, maxLongitude);
//		if (l.isEmpty())
//			throw new ResourceNotFoundException();
		return l;
	}
}
