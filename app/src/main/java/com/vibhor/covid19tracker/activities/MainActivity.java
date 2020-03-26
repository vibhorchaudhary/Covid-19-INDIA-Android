package com.vibhor.covid19tracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vibhor.covid19tracker.R;
import com.vibhor.covid19tracker.adapters.RecyclerViewDataAdapter;
import com.vibhor.covid19tracker.interfaces.DataInterface;
import com.vibhor.covid19tracker.models.CovidDataModel;
import com.vibhor.covid19tracker.models.KeyValuesModel;
import com.vibhor.covid19tracker.models.StateWiseModel;
import com.vibhor.covid19tracker.utils.AppUtils;
import com.vibhor.covid19tracker.utils.GridSpacingItemDecoration;
import com.vibhor.covid19tracker.viewmodels.ActivityViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.data_rv)
    RecyclerView dataRv;

    @BindView(R.id.refresh_iv)
    ImageView refreshIv;

    @BindView(R.id.last_reported_case_tv)
    TextView lastReportedCaseTv;

    @BindView(R.id.confirmed_head_tv)
    TextView confirmedHeadTv;

    @BindView(R.id.confirmed_count_tv)
    TextView confirmedCountTv;

    @BindView(R.id.active_head_tv)
    TextView activeHeadTv;

    @BindView(R.id.active_count_tv)
    TextView activeCountTv;

    @BindView(R.id.recovered_head_tv)
    TextView recoveredHeadTv;

    @BindView(R.id.recovered_count_tv)
    TextView recoveredCountTv;

    @BindView(R.id.deceased_head_tv)
    TextView deceasedHeadTv;

    @BindView(R.id.deceased_count_tv)
    TextView deceasedCountTv;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_tv)
    TextView toolbarTv;


    private ActivityViewModel activityViewModel;
    private RecyclerViewDataAdapter recyclerViewDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("");
        toolbarTv.setText(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }


    private void init() {
        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        activityViewModel.init(this);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        dataRv.setLayoutManager(mLayoutManager);
        dataRv.addItemDecoration(new GridSpacingItemDecoration(2, AppUtils.dpToPx(this, 10), true));
        dataRv.setItemAnimator(new DefaultItemAnimator());


        refreshIv.setOnClickListener(v -> getDataFromServer());

        getDataFromServer();
    }


    private void setData(CovidDataModel data) {
        String lastReportedCaseString = lastReportedCaseText(data.getKey_values());
        lastReportedCaseTv.setText(lastReportedCaseString);
        StateWiseModel stateWiseModel = getTotalData(data.getStatewise());
        activeCountTv.setText(stateWiseModel.getActive());
        confirmedCountTv.setText(stateWiseModel.getConfirmed());
        deceasedCountTv.setText(stateWiseModel.getDeaths());
        recoveredCountTv.setText(stateWiseModel.getRecovered());

        confirmedHeadTv.setText("CONFIRMED");
        activeHeadTv.setText("ACTIVE");
        deceasedHeadTv.setText("DEATHS");
        recoveredHeadTv.setText("RECOVERED");
    }

    private String lastReportedCaseText(List<KeyValuesModel> keyValuesModel) {
        StringBuilder data = new StringBuilder("LAST REPORTED CASE - ");
        for (KeyValuesModel model : keyValuesModel) {
            data.append(model.getLastupdatedtime());
            break;
        }
        return data.toString();
    }

    private StateWiseModel getTotalData(List<StateWiseModel> stateWiseModel) {
        StateWiseModel state = null;
        for (StateWiseModel total : stateWiseModel) {
            if (total.getState().equalsIgnoreCase("TOTAL")) {
                state = total;
                break;
            }
        }
        return state;
    }


    private void getDataFromServer() {
        activityViewModel.getDataFromServer(new DataInterface() {
            @Override
            public void onSuccess(CovidDataModel covidDataModel) {
                setData(covidDataModel);

//                List<StateWiseModel> stateWiseModelArrayList = new ArrayList<>();

//                StateWiseModel stateWiseModel = new StateWiseModel();
//                stateWiseModel.setActive("Active");
//                stateWiseModel.setConfirmed("Confirmed");
//                stateWiseModel.setDeaths("Deaths");
//                stateWiseModel.setRecovered("Recovered");
//                stateWiseModel.setState("State/UT");

//                stateWiseModelArrayList.add(stateWiseModel);

                List<StateWiseModel> mStateWiseModelList = covidDataModel.getStatewise();
                if (mStateWiseModelList.get(0).getState().equalsIgnoreCase("TOTAL")) {
                    mStateWiseModelList.remove(0);
                }

                Collections.sort(mStateWiseModelList, (obj1, obj2) -> Integer.valueOf(obj2.getConfirmed().trim()).compareTo(Integer.valueOf(obj1.getConfirmed().trim())));

//                stateWiseModelArrayList.addAll(mStateWiseModelList);

                recyclerViewDataAdapter = new RecyclerViewDataAdapter(MainActivity.this, mStateWiseModelList);
                dataRv.setAdapter(recyclerViewDataAdapter);
            }

            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong. Please try again!", Toast.LENGTH_LONG).show();
            }

        });
    }


}
