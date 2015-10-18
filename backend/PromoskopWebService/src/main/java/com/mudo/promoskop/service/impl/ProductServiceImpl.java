package com.mudo.promoskop.service.impl;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Branch;
import com.mudo.promoskop.model.Product;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.service.ProductService;
import com.mudo.promoskop.util.DistanceUtil;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;

	@Override
	public Product findByBarcode(String barcode) {
		return productDao.findByBarcode(barcode);
	}

	@Override
	public List<ProductBranch> findByBarcodeInRadius(String barcode, double currentLatitude, double currentLongitude, double maxDistance) {
		Product product = productDao.findByBarcode(barcode);
		for (Iterator<ProductBranch> itr = product.getProductBranches().iterator(); itr.hasNext();) {
			ProductBranch pb = itr.next();
			if (pb != null) {
				Branch b = pb.getBranch();
				double distance = DistanceUtil.getDistanceAsKm(currentLatitude, currentLongitude, b.getLatitude(), b.getLongitude());
				if (distance > maxDistance)
					itr.remove();
			}
		}
		return null;
	}

	@Override
	public List<Product> findBySubString(String containText) {
		return productDao.findBySubString(containText);
	}

	@Override
	public List<Product> findMaxGapped(int count) {
		return productDao.findMaxGapped(count);
	}
}
