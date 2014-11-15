package com.mudo.promoskop.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter("filterResponseBean")
public class ProductResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private String barcodeId;

	private String productName;

	private String productUrl;

	private List<BranchResponse> branches = new ArrayList<BranchResponse>();

	public ProductResponse(String barcodeId, String productName, String productUrl) {
		super();
		this.barcodeId = barcodeId;
		this.productName = productName;
		this.productUrl = productUrl;
	}

	public ProductResponse() {
	}

	public String getBarcodeId() {
		return barcodeId;
	}

	public void setBarcodeId(String barcodeId) {
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
