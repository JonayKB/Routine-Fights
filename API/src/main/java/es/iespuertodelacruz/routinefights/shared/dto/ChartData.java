package es.iespuertodelacruz.routinefights.shared.dto;

import java.util.ArrayList;
import java.util.List;

public class ChartData {
    private List<String> labels;
    private List<Long> data;

    public ChartData() {
        this.labels = new ArrayList<>();
        this.data = new ArrayList<>();
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

    public void addLabel(String label) {
        this.labels.add(label);
    }
    public void addData(Long data) {
        this.data.add(data);
    }

    @Override
    public String toString() {
        return "ChartData [labels=" + labels + ", data=" + data + "]";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChartData other = (ChartData) obj;
        return labels.equals(other.labels) && data.equals(other.data);
    }

}