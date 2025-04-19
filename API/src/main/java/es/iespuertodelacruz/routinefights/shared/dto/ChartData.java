package es.iespuertodelacruz.routinefights.shared.dto;

import java.util.List;

public class ChartData {
    private List<String> labels;
    private List<Long> data;

    public ChartData() {
    }
    
    public ChartData(List<String> labels, List<Long> data) {
        this.labels = labels;
        this.data = data;
    }

    public List<String> getLabels() {
        return this.labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Long> getData() {
        return this.data;
    }

    public void setData(List<Long> data) {
        this.data = data;
    }

}