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

import com.mudo.promoskop.dao.BranchDao;
import com.mudo.promoskop.dao.ProductDao;
import com.mudo.promoskop.model.Branch;
import com.mudo.promoskop.util.CacheUtil;

@Repository
public class BranchDaoImpl implements BranchDao {
	private static Logger LOG = LoggerFactory.getLogger(ProductDao.class);

	@PersistenceContext(type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<Branch> findInCoordinates(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		LOG.debug("find with find cheapest branches in coordinates");

		Query query = em.createNativeQuery("select * from branch "
										+ "where latitude>:minLatitude and latitude<:maxLatitude "
										+ "and longitude>:minLongitude and longitude<:maxLongitude "
										+ "order by store_id", Branch.class);
		List<Branch> results = query.setParameter("minLatitude", minLatitude).setParameter("maxLatitude", maxLatitude)
				.setParameter("minLongitude", minLongitude).setParameter("maxLongitude", maxLongitude).setHint(QueryHints.CACHEABLE, true).getResultList();

		CacheUtil.displayStatistics(em);
		return results;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Branch> findInCoordinates(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
		LOG.debug("find with find cheapest branches in coordinates");

		Query query = em.createNativeQuery("select b.*, pb.* "
										+ "from branch as b "
										+ "inner join product_branch as pb "
										+ "on pb.branch_id=b.id "
										+ "where b.latitude>:minLatitude and b.latitude<:maxLatitude "
										+ "and b.longitude>:minLongitude and b.longitude<:maxLongitude "
										+ "and pb.product_id = (select id from product p where p.barcode=:barcodes)"
										+ "order by b.store_id", Branch.class);
		List<Branch> results = query.setParameter("minLatitude", minLatitude).setParameter("maxLatitude", maxLatitude).setParameter("minLongitude", minLongitude)
				.setParameter("maxLongitude", maxLongitude).setParameter("barcodes", barcode).setHint(QueryHints.CACHEABLE, true).getResultList();

		CacheUtil.displayStatistics(em);
		return results;
	}
}
