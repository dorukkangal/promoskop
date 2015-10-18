package com.mudo.promoskop.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.BranchDao;
import com.mudo.promoskop.model.Branch;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.service.BranchService;
import com.mudo.promoskop.util.DistanceUtil;
import com.mudo.promoskop.util.DistanceUtil.FarthestPoints;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchDao branchDao;

	@Override
	public List<Branch> findCheapestInRadius(String[] barcodes, double currentLatitude, double currentLongitude, double radius) {
		FarthestPoints points = DistanceUtil.getFarthestPoints(currentLatitude, currentLongitude, radius);
		List<Branch> brancheList = branchDao.findInCoordinates(points.minLatitude(), points.minLongitude(), points.maxLatitude(), points.maxLongitude());
		brancheList = eliminateBrancheProductsByBarcodes(brancheList, Arrays.asList(barcodes));
		brancheList = eliminateBrancheProductsByPrices(brancheList, currentLatitude, currentLongitude);

		for (Iterator<Branch> itrBranch = brancheList.iterator(); itrBranch.hasNext();) {
			Branch b = itrBranch.next();
			Set<ProductBranch> pbList = b.getProductBranches();
			if (pbList.size() <= 0)
				itrBranch.remove();
		}

		// calculate total price per branch
		for (Branch b : brancheList)
			for (ProductBranch pb : b.getProductBranches())
				b.setTotalPrice(b.getTotalPrice() + pb.getPrice());

		return brancheList;
	}

	@Override
	public List<Branch> find(String[] barcodes, double currentLatitude, double currentLongitude, double radius) {
		FarthestPoints points = DistanceUtil.getFarthestPoints(currentLatitude, currentLongitude, radius);
		List<Branch> brancheList = new ArrayList<Branch>();
		for (String barcode : barcodes) {
			List<Branch> subList = branchDao.findInCoordinates(barcode, points.minLatitude(), points.minLongitude(), points.maxLatitude(), points.maxLongitude());
			brancheList = addSublist(brancheList, subList);
		}
		brancheList = eliminateBrancheProductsByBarcodes(brancheList, Arrays.asList(barcodes));
		brancheList = eliminateBrancheProductsByPrices(brancheList, currentLatitude, currentLongitude);

		for (Iterator<Branch> itrBranch = brancheList.iterator(); itrBranch.hasNext();) {
			Branch b = itrBranch.next();
			Set<ProductBranch> pbList = b.getProductBranches();
			if (pbList.size() <= 0)
				itrBranch.remove();
		}

		// calculate total price per branch
		for (Branch b : brancheList)
			for (ProductBranch pb : b.getProductBranches())
				b.setTotalPrice(b.getTotalPrice() + pb.getPrice());

		return brancheList;
	}

	private List<Branch> eliminateBrancheProductsByBarcodes(List<Branch> brancheList, List<String> barcodes) {
		for (int i = 0; i < brancheList.size(); i++) {
			Set<ProductBranch> pbList = brancheList.get(i).getProductBranches();
			for (Iterator<ProductBranch> itr = pbList.iterator(); itr.hasNext();) {
				ProductBranch pb = itr.next();
				if (!barcodes.contains(pb.getProduct().getBarcode()))
					itr.remove();
			}
		}
		return brancheList;
	}

	private List<Branch> eliminateBrancheProductsByPrices(List<Branch> brancheList, double currentLatitude, double currentLongitude) {
		for (int i = 0; i < brancheList.size(); i++) {
			Branch b1 = brancheList.get(i);
			for (Iterator<ProductBranch> itr1 = b1.getProductBranches().iterator(); itr1.hasNext();) {
				ProductBranch pb1 = itr1.next();
				for (int j = i + 1; j < brancheList.size(); j++) {
					Branch b2 = brancheList.get(j);
					for (Iterator<ProductBranch> itr2 = b2.getProductBranches().iterator(); itr2.hasNext();) {
						ProductBranch pb2 = itr2.next();
						// eliminate overpriced
						if (pb1.getPrice() > pb2.getPrice()) {
							itr1.remove();
						} else if (pb1.getPrice() < pb2.getPrice()) {
							itr2.remove();
						} else {
							// eliminate farthest
							double d1 = DistanceUtil.getDistanceAsKm(currentLatitude, currentLongitude, b1.getLatitude(), b1.getLongitude());
							double d2 = DistanceUtil.getDistanceAsKm(currentLatitude, currentLongitude, b2.getLatitude(), b2.getLongitude());
							if (d1 > d2)
								itr1.remove();
							else
								itr2.remove();
						}
					}
				}
			}
		}
		return brancheList;
	}

	private List<Branch> addSublist(List<Branch> mainList, List<Branch> subList) {
		for (Branch b : subList) {
			int index = mainList.indexOf(b);
			if (index >= 0)
				mainList.get(index).getProductBranches().addAll(b.getProductBranches());
			else
				mainList.add(b);
		}
		return mainList;
	}

}
