package com.mudo.promoskop.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter("filterResponseBean")
public class ProductResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private int barcodeId;

	private String productName;

	private String productUrl;

	private List<BranchResponse> branches = new ArrayList<BranchResponse>();

	public int getBarcodeId() {
		return barcodeId;
	}

	public void setBarcodeId(int barcodeId) {
		this.barcodeId = barcodeId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductUrl() {
		return productUrl;
	}

	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}

	public List<BranchResponse> getBranches() {
		return branches;
	}

	public void setBranches(List<BranchResponse> branches) {
		this.branches = branches;
	}
}
