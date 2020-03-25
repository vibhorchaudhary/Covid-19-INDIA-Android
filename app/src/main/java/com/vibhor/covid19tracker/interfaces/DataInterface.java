package com.vibhor.covid19tracker.interfaces;

import com.vibhor.covid19tracker.models.CovidDataModel;

public interface DataInterface {

    void onSuccess(CovidDataModel covidDataModel);

    void onFailure();

    void onError(Throwable e);

}
