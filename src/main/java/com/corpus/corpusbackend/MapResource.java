package com.corpus.corpusbackend;

import com.corpus.corpusbackend.Data.Date;
import com.corpus.corpusbackend.Data.Location;
import com.corpus.corpusbackend.Data.Map;
import com.corpus.corpusbackend.Data.ReportedDisease;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/map")
public class MapResource {

    private static final Map map = new Map(5);
    static int count_since_last_flush = 0;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getDiseases(@QueryParam("latitude") String latitude, @QueryParam("longitude") String longitude,@QueryParam("date") int date) {
        Location location = new Location(Double.parseDouble(longitude),Double.parseDouble(latitude));
        ArrayList<String> results = Filter.filter(map.getRegion(location,1000),new Date(date),location);
        JsonObjectBuilder out = Json.createObjectBuilder();
        JsonArrayBuilder array = Json.createArrayBuilder();
        for (String str: results) {
            array.add(str);
        }
        JsonArrayBuilder array2 = Json.createArrayBuilder();
        ArrayList<ReportedDisease> results2 = map.getRegion(location,1000);
        for (ReportedDisease report: results2) {
            JsonObjectBuilder sub = Json.createObjectBuilder();
            sub.add("longitude", report.location.longitude);
            sub.add("latitude", report.location.latitude);
            sub.add("date", report.date.days_since_last_epoch);
            sub.add("name",report.name);

            array2.add(sub.build());
        }
        out.add("relevant_results", array.build());
        out.add("area", array2.build());
        return out.build();
    }


    //expects json with "date":days since last epoch(int), "name" : name of disease, "longitude", "latitude" : location in degrees(string)
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String submitReport(JsonObject json) {

        try {
            count_since_last_flush++;
            if(count_since_last_flush > 50){ //todo not hardcode these values
                map.flush(20, new Date(json.getInt("date"))); //todo make this more secure
            }
            ReportedDisease disease = new ReportedDisease(new Location(Double.parseDouble(json.getString("longitude")),Double.parseDouble(json.getString("latitude"))),json.getString("name") ,"", new Date(json.getInt("date")));
            map.sendReport( disease);
            return "success, added " + disease + count_since_last_flush;
        } catch (Exception e){
            return e.getMessage();
        }

    }
}