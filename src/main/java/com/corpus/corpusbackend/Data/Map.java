package com.corpus.corpusbackend.Data;


import java.awt.*;
import java.util.ArrayList;

/**
 * Stores info
 */
public class Map {

    private final ArrayList<ReportedDisease>[][] cells;
    ArrayList<Point> active_cells = new ArrayList<>();
    private final int cell_size;

    /**
     * Latitude size of cells in degrees
     * Must evenly divide into 360
     */
    public Map(int cell_size){
        assert cell_size <= 360;
        this.cell_size = cell_size;
        final int max = 360;
        int cell_count = max/cell_size;
        cells = new ArrayList[cell_count][cell_count];
    }

    //todo serialize and store backup

    /**
     * Add a report to the database
     */
    public void sendReport(ReportedDisease report){
        int cell_x = (int)((report.location.latitude+180) / cell_size);
        int cell_y = (int)((report.location.latitude+180) / cell_size);

        if(cells[cell_x][cell_y] == null) {
            cells[cell_x][cell_y] = new ArrayList<>();
            active_cells.add(new Point(cell_x,cell_y));
        }

        cells[cell_x][cell_y].add(report);
    }

    /**
     * Get all reports in region
     * @param distance Approximate search radius in KM
     */
    public ArrayList<ReportedDisease> getRegion(Location location, int distance){
        ArrayList<ReportedDisease> collected_reports = new ArrayList<>();
            for (Point active_cell:active_cells) {
                Location approx_cell_location = new Location(active_cell.x * cell_size - 180, active_cell.y * cell_size - 180);
                if(approx_cell_location.distance(location) < distance){
                    collected_reports.addAll(cells[active_cell.x][active_cell.y]);
                }
            }
            return collected_reports;
    }

    /**
     * Remove all reports older than n days
     */
    public void flush(int days, Date current_date){
        for (int j = 0; j < active_cells.size(); j++) {
            Point active_cell = active_cells.get(j);
            ArrayList<ReportedDisease> list = cells[active_cell.x][active_cell.y];
            for (int i = 0; i < list.size(); i++) {
                ReportedDisease report = list.get(i);
                if(current_date.days_since_last_epoch - report.date.days_since_last_epoch > days){
                    list.remove(i);
                    i--;
                }
            }
            if(list.isEmpty()){
                cells[active_cell.x][active_cell.y] = null;
                active_cells.remove(j);
                j--;
            }
        }
    }
}
