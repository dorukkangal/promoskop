package com.mudo.promoskop.request;

import java.io.Serializable;

public class CheapestPriceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private double currentLatitude;

	private double currentLongitude;

	private double maxDistance;

	private String[] barcodeIds;

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

	public String[] getBarcodeIds() {
		return barcodeIds;
	}

	public void setBarcodeIds(String[] barcodeIds) {
		this.barcodeIds = barcodeIds;
	}
}
