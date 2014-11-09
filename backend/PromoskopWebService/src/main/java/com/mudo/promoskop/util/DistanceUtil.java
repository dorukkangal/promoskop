package com.mudo.promoskop.util;

public class DistanceUtil {

	public static double[] minMaxLatitudes(double latitude, double longitude, double raiusAsKm) {
//		return new double[] { latitudeOfTarget(latitude, longitude,270, raiusAsKm), latitudeOfTarget(latitude, longitude, 90, raiusAsKm) };
		return new double[] { 40.000000, 45.000000};
	}

	public static double[] minMaxLongitudes(double latitude, double longitude, double raiusAsKm) {
//		return new double[] { longitudeOfTarget(latitude, longitude, 180, raiusAsKm), longitudeOfTarget(latitude, longitude, 0, raiusAsKm) };
		return new double[] { 25.000000, 30.000000};
	}

	public static double latitudeOfTarget(double latitude, double longitude, double bearing, double distance) {
		double distToRadians = distanceToRadians(distance);
		return Math.asin(Math.sin(deg2rad(latitude)) * Math.cos(distToRadians) + Math.cos(deg2rad(latitude)) * Math.sin(distToRadians) * Math.cos(deg2rad(bearing)));
	}

	public static double longitudeOfTarget(double latitude, double longitude, double bearing, double distance) {
		double distToRadians = distanceToRadians(distance);
		double lat = latitudeOfTarget(latitude, longitude, bearing, distance);
		double dlon = Math.atan2(Math.sin(deg2rad(bearing)) * Math.sin(distToRadians) * Math.cos(deg2rad(latitude)),
				Math.cos(distToRadians) - Math.sin(deg2rad(latitude)) * Math.sin(lat));
		return ((deg2rad(longitude) - dlon + Math.PI) % (2 * Math.PI)) - Math.PI;
	}

	private static double distanceToRadians(double distance) {
		return deg2rad(distance / (60 * 1.1515 * 1.609344));
	}

	public static double distanceAsKmBetweenCoordinates(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude) {
		return distance(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude, 'K');
	}

	private static double distance(double sourceLatitude, double sourceLongitude, double targetLatitude, double targetLongitude, char unit) {
		double theta = targetLongitude - sourceLongitude;
		double dist = Math.sin(deg2rad(targetLatitude)) * Math.sin(deg2rad(sourceLatitude)) + Math.cos(deg2rad(targetLatitude)) * Math.cos(deg2rad(sourceLatitude))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == 'K') {
			dist = dist * 1.609344;
		} else if (unit == 'N') {
			dist = dist * 0.8684;
		}
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
}