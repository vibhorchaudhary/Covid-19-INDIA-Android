package com.vibhor.covid19tracker.models;

import java.util.List;

public class CovidDataModel {

    List<CasesTimeSeries> cases_time_series;
    List<KeyValuesModel> key_values;
    List<StateWise> statewise;

    public List<CasesTimeSeries> getCases_time_series() {
        return cases_time_series;
    }

    public void setCases_time_series(List<CasesTimeSeries> cases_time_series) {
        this.cases_time_series = cases_time_series;
    }

    public List<KeyValuesModel> getKey_values() {
        return key_values;
    }

    public void setKey_values(List<KeyValuesModel> key_values) {
        this.key_values = key_values;
    }

    public List<StateWise> getStatewise() {
        return statewise;
    }

    public void setStatewise(List<StateWise> statewise) {
        this.statewise = statewise;
    }
}
