package com.vibhor.covid19tracker.https;

import com.vibhor.covid19tracker.models.CovidDataModel;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

public interface APIService {

    @GET("data.json")
    Observable<Response<CovidDataModel>> getCovidDataFromServer();

}
