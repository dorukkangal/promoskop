package com.mudo.promoskop.response;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter("filterResponseBean")
public class BranchResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	private double price;

	private String branchName;

	private String branchAddress;

	private double latitude;

	private double longitude;

	private String storeName;

	private String storeLogo;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
}
