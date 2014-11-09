package com.mudo.promoskop.service;

import java.util.List;

import com.mudo.promoskop.response.BranchResponse;

public interface BranchResponseService {

	List<BranchResponse> findProductBranchWithMinPrice(int[] barcodeIds, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);
}
