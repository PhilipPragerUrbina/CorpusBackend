package com.corpus.corpusbackend.Data;

/**
 * A location on the world
 */
public class Location {
    public double longitude;
    public double latitude;

    /**
     * In degrees
     */
    public Location(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Get distance in km
     *
     */
    public double distance(Location other){
        double theta_1 = Math.toRadians(latitude);
        double theta_2 = Math.toRadians(other.latitude);
        double gamma_1 = Math.toRadians(longitude);
        double gamma_2 = Math.toRadians(other.longitude);

        final double earth_radius = 6371;

        return 2*earth_radius * Math.asin(Math.sqrt(Math.sin((theta_2 - theta_1)/2.0)*Math.sin((theta_2 - theta_1)/2.0) + Math.cos(theta_1) * Math.cos(theta_2) * Math.sin((gamma_2 - gamma_1)/2.0)*Math.sin((gamma_2 - gamma_1)/2.0)));

    }

    @Override
    public String toString() {
        return "Location{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
