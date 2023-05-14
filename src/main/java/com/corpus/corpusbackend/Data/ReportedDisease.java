package com.corpus.corpusbackend.Data;

/**
 * A disease reported at a location
 */
public class ReportedDisease {
    public Location location;
    public String name;
    public String description;

    public ReportedDisease(Location location, String name, String description) {
        this.location = location;
        this.name = name;
        this.description = description;
    }
}
