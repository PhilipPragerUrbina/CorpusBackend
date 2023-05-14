package com.corpus.corpusbackend.Data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void distance() {
        Location a = new Location(50,50);
        Location b = new Location(-50,-50);
        assertEquals(14587,(int)a.distance(b));
    }
}