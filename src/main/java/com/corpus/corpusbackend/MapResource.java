package com.corpus.corpusbackend;

import com.corpus.corpusbackend.Data.Date;
import com.corpus.corpusbackend.Data.Location;
import com.corpus.corpusbackend.Data.Map;
import com.corpus.corpusbackend.Data.ReportedDisease;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;
import java.util.ArrayList;

@Path("/map")
public class MapResource {

    private static final Map map = new Map(5);
    static int count_since_last_flush = 0;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDiseases(@QueryParam("latitude") String latitude, @QueryParam("longitude") String longitude,@QueryParam("date") int date) {
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
        return Response
                .status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Credentials", "true")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                .header("Access-Control-Max-Age", "1209600")
                .entity(out.build())
                .build();
    }


    //expects json with "date":days since last epoch(int), "name" : name of disease, "longitude", "latitude" : location in degrees(string)
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response submitReport(String json_str) {
        JsonReader builder = Json.createReader(new StringReader(json_str));
        JsonObject json = builder.read().asJsonObject();

        try {
            count_since_last_flush++;
            if(count_since_last_flush > 50){ //todo not hardcode these values
                map.flush(20, new Date(json.getInt("date"))); //todo make this more secure
            }
            ReportedDisease disease = new ReportedDisease(new Location(Double.parseDouble(json.getString("longitude")),Double.parseDouble(json.getString("latitude"))),json.getString("name") ,"", new Date(json.getInt("date")));
            map.sendReport( disease);
            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity("success, added " + disease + count_since_last_flush)
                    .build();
        } catch (Exception e){
            return Response
                    .status(200)
                    .header("Access-Control-Allow-Origin", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
                    .header("Access-Control-Max-Age", "1209600")
                    .entity(e.getMessage())
                    .build();
        }

    }
}