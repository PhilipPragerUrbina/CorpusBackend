package com.corpus.corpusbackend.Data;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void testMap() {
        Map map = new Map(10);
        map.sendReport(new ReportedDisease(new Location(40.05,20.05), "corona","bad", new Date(20)));
        map.sendReport(new ReportedDisease(new Location(40.002,19.99), "corona","bad2", new Date(10)));
        ArrayList<ReportedDisease> diseases = map.getRegion(new Location(40,20), 4000);
        assertEquals(2,diseases.size());
        map.flush(5,new Date(20));
        diseases = map.getRegion(new Location(40,20), 4000);
        assertEquals(1,diseases.size());
    }

}