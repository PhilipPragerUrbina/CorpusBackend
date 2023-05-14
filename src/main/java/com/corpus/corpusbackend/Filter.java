package com.corpus.corpusbackend;

import com.corpus.corpusbackend.Data.Date;
import com.corpus.corpusbackend.Data.Location;
import com.corpus.corpusbackend.Data.ReportedDisease;

import java.util.ArrayList;
import java.util.HashMap;

public class Filter {

    /**
     * Filter diseases for ones relevant to the user
     * @param nearby All diseases in region
     * @param date Current date
     * @param user_location Location of user
     * @return Names of diseases
     */
    public static ArrayList<String> filter(ArrayList<ReportedDisease> nearby, Date date, Location user_location ){
        //params
        double max_distance = 15; //todo change based on disease
        int max_days = 14;
        int min_reports = 5; //todo change

        HashMap<String, ArrayList<ReportedDisease>> map = new HashMap<>();

        //get counts of reports
        for (ReportedDisease report: nearby ) {
            map.computeIfAbsent(report.name, k -> new ArrayList<>());
            map.get(report.name).add(report);
        }


        ArrayList<String> out = new ArrayList<>();
        for (ArrayList<ReportedDisease> report_list: map.values()) {
            if(report_list.size() >= min_reports) { //Valid disease
                for (ReportedDisease report: report_list) {
                    //Concerns user
                    if(report.location.distance(user_location) < max_distance && date.days_since_last_epoch - report.date.days_since_last_epoch < max_days){
                        out.add(report.name);
                        break;
                    }
                }
            }
        }
        return out;
    }
}
