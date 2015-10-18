package com.mudo.promoskop.dao;

import java.util.List;

import com.mudo.promoskop.model.Branch;

public interface BranchDao {

	public List<Branch> findInCoordinates(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);

	public List<Branch> findInCoordinates(String barcode, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
}
