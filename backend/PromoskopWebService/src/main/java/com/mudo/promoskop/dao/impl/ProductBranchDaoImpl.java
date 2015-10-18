package com.mudo.promoskop.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
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

	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<ProductBranch> findCheapestInCoordinates(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		LOG.debug("find with findCheapestInCoordinates: {} from ProductBranch", barcode);

		Query query = em.createNativeQuery("select * from product_branch "
										+ "where (product_id, price) in "
										+ "(select pb.product_id, min(pb.price) "
										+ "from product_branch as pb "
										+ "inner join branch as b "
										+ "on pb.branch_id=b.id "
										+ "where b.latitude>:minLatitude and b.latitude<:maxLatitude "
										+ "and b.longitude>:minLongitude and b.longitude<:maxLongitude "
										+ "and pb.product_id = (select id from product p where p.barcode=:barcode) "
										+ "group by b.store_id)", ProductBranch.class);
		List<ProductBranch> results = query.setParameter("minLatitude", minLatitude).setParameter("maxLatitude", maxLatitude)
				.setParameter("minLongitude", minLongitude).setParameter("maxLongitude", maxLongitude).setParameter("barcode", barcode).setHint(QueryHints.CACHEABLE, true).getResultList();

		CacheUtil.displayStatistics(em);
		return results;
	}
}
