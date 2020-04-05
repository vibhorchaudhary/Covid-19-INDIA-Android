package com.vibhor.covid19tracker.viewmodels;

import android.app.Activity;
import android.app.ProgressDialog;

import com.vibhor.covid19tracker.https.ServiceFactory;
import com.vibhor.covid19tracker.interfaces.DataInterface;
import com.vibhor.covid19tracker.models.CovidDataModel;

import androidx.lifecycle.ViewModel;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityViewModel extends ViewModel {

    private ServiceFactory serviceFactory;
    private ProgressDialog pDialog;
    private Activity mContext;

    private String BASE_URL = "https://api.covid19india.org/";

    public void init(Activity context) {
        this.mContext = context;
        serviceFactory = new ServiceFactory(BASE_URL);
    }

    public void getDataFromServer(DataInterface dataInterface) {
        setProgressDialog("Fetching details.. Please wait..");
        serviceFactory.getBaseService().getCovidDataFromServer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CovidDataModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dataInterface.onError(e);
                        hideProgressDialog();
                    }

                    @Override
                    public void onNext(Response<CovidDataModel> covidDataModelResponse) {
                        hideProgressDialog();
                        if (covidDataModelResponse.code() == 200) {
                            dataInterface.onSuccess(covidDataModelResponse.body());
                        }
                    }
                });
    }

    public void setProgressDialog(String message) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(mContext);
        }
        if (!pDialog.isShowing() && !mContext.isFinishing()) {
            pDialog.setMessage(message);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }
}