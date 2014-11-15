package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.response.BranchResponse;

public interface BranchResponseService {

	List<BranchResponse> findProductBranchWithMinPrice(String[] barcodeIds, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude);
}
