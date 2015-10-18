package com.mudo.promoskop.request;

import java.io.Serializable;

public class CheapestPriceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String[] barcodes;

	private double currentLatitude;

	private double currentLongitude;

	private double maxDistance;

	public String[] getBarcodes() {
		return barcodes;
	}

	public void setBarcodes(String[] barcodes) {
		this.barcodes = barcodes;
	}

	public double getCurrentLatitude() {
		return currentLatitude;
	}

	public void setCurrentLatitude(double currentLatitude) {
		this.currentLatitude = currentLatitude;
	}

	public double getCurrentLongitude() {
		return currentLongitude;
	}

	public void setCurrentLongitude(double currentLongitude) {
		this.currentLongitude = currentLongitude;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}
}
