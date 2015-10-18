package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.model.Branch;

public interface BranchService {

	public List<Branch> findCheapestInRadius(String[] barcodes, double currentLatitude, double currentLongitude, double radius);

	public List<Branch> find(String[] barcodes, double currentLatitude, double currentLongitude, double radius);
}
