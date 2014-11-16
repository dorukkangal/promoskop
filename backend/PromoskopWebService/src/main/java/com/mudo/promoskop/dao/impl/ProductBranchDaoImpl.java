package com.mudo.promoskop.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.mudo.promoskop.dao.ProductBranchDao;
import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.ProductBranch;
import com.mudo.promoskop.util.CacheUtil;

@Repository
public class ProductBranchDaoImpl implements ProductBranchDao {
	private static Logger LOG = LoggerFactory.getLogger(ProductDao.class);

	@PersistenceContext
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductBranch> findProductBranchWithMinPrice(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		LOG.debug("find with minPrice: {} from ProductBranch", barcode);

		Query query = em.createNativeQuery("select * from product_branch where (product_id, price) = "
				+ "(select product_id, MIN(price) from product_branch as pb where pb.product_id=(select id from product as p where p.barcode=:barcode) and pb.branch_id in"
				+ "(select b.id from branch as b where b.latitude>:minLatitude and b.latitude<:maxLatitude and b.longitude>:minLongitude and b.longitude<:maxLongitude))",
				ProductBranch.class);
		List<ProductBranch> results = query.setParameter("barcode", barcode).setParameter("minLatitude", minLatitude).setParameter("maxLatitude", maxLatitude)
				.setParameter("minLongitude", minLongitude).setParameter("maxLongitude", maxLongitude).setHint(QueryHints.CACHEABLE, true).getResultList();
		
		CacheUtil.displayStatistics(em);
		return results;
	}
}
