package com.mudo.promoskop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductBranchDao;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.service.ProductBranchService;
import com.mudo.promoskop.util.DistanceUtil;
import com.mudo.promoskop.util.DistanceUtil.FarthestPoints;

@Service
@Transactional
public class ProductBranchServiceImpl implements ProductBranchService {

	@Autowired
	private ProductBranchDao productBranchDao;

	@Override
	public List<ProductBranch> findCheapestAndClosest(String barcode, double currentLatitude, double currentLongitude) {
		FarthestPoints points = DistanceUtil.getFarthestPoints(currentLatitude, currentLongitude, DistanceUtil.MAX_DISTANCE);
		return productBranchDao.findCheapestInCoordinates(barcode, points.minLatitude(), points.minLongitude(), points.maxLatitude(), points.maxLongitude());
	}

	@Override
	public List<ProductBranch> findCheapestInRadius(String[] barcodes, double currentLatitude, double currentLongitude, double radius) {
		FarthestPoints points = DistanceUtil.getFarthestPoints(currentLatitude, currentLongitude, radius);

		List<ProductBranch> branches = new ArrayList<ProductBranch>();
		for (String barcode : barcodes)
			branches.addAll(productBranchDao.findCheapestInCoordinates(barcode, points.minLatitude(), points.minLongitude(), points.maxLatitude(), points.maxLongitude()));
		return branches;
	}
}
