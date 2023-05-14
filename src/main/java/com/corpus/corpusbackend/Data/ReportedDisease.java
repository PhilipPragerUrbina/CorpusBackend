package com.corpus.corpusbackend.Data;

/**
 * A disease reported at a location
 */
public class ReportedDisease {
    public Location location;
    public String name;
    public String description;

    public Date date;

    public ReportedDisease(Location location, String name, String description, Date date) {
        this.location = location;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    @Override
    public String toString() {
        return "ReportedDisease{" +
                "location=" + location +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
