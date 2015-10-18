package com.mudo.promoskop.util;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class DistanceUtil {
	public static final double MAX_DISTANCE = new Double(100);

	public static double getDistanceAsKm(double sourceLat, double sourceLng, double targetLat, double targetLng) {
		return LatLngTool.distance(new LatLng(sourceLat, sourceLng), new LatLng(targetLat, targetLng), LengthUnit.KILOMETER);
	}

	public static FarthestPoints getFarthestPoints(double currentLatitude, double currentLongitude, double radius) {
		return new FarthestPoints(currentLatitude, currentLongitude, radius);
	}

	public static class FarthestPoints {
		private double minLatitude;
		private double minLongitude;
		private double maxLatitude;
		private double maxLongitude;

		private FarthestPoints(double currentLatitude, double currentLongitude, double radius) {
			LatLng current = new LatLng(currentLatitude, currentLongitude);

			this.minLatitude = LatLngTool.travel(current, LatLngTool.Bearing.SOUTH, radius, LengthUnit.KILOMETER).getLatitude();
			this.minLongitude = LatLngTool.travel(current, LatLngTool.Bearing.WEST, radius, LengthUnit.KILOMETER).getLongitude();
			this.maxLatitude = LatLngTool.travel(current, LatLngTool.Bearing.NORTH, radius, LengthUnit.KILOMETER).getLatitude();
			this.maxLongitude = LatLngTool.travel(current, LatLngTool.Bearing.EAST, radius, LengthUnit.KILOMETER).getLongitude();
		}

		public double minLatitude() {
			return minLatitude;
		}

		public double minLongitude() {
			return minLongitude;
		}

		public double maxLatitude() {
			return maxLatitude;
		}

		public double maxLongitude() {
			return maxLongitude;
		}
	}
}