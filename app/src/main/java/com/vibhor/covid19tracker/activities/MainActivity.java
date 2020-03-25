package com.vibhor.covid19tracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
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
import com.vibhor.covid19tracker.models.StateWise;
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
    private LinearLayoutManager linearLayoutManager;
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
        linearLayoutManager = new LinearLayoutManager(this);
        dataRv.setLayoutManager(linearLayoutManager);
        dataRv.setHasFixedSize(true);

        refreshIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServer();
            }
        });

        getDataFromServer();
    }


    private void setData(CovidDataModel data) {
        String lastReportedCaseString = lastReportedCaseText(data.getKey_values());
        lastReportedCaseTv.setText(lastReportedCaseString);
        StateWise stateWise = getTotalData(data.getStatewise());
        activeCountTv.setText(stateWise.getActive());
        confirmedCountTv.setText(stateWise.getConfirmed());
        deceasedCountTv.setText(stateWise.getDeaths());
        recoveredCountTv.setText(stateWise.getRecovered());

        confirmedHeadTv.setText("CONFIRMED");
        activeHeadTv.setText("ACTIVE");
        deceasedHeadTv.setText("DECEASED");
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

    private StateWise getTotalData(List<StateWise> stateWise) {
        StateWise state = null;
        for (StateWise total : stateWise) {
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

                List<StateWise> stateWiseArrayList = new ArrayList<>();

                StateWise stateWise = new StateWise();
                stateWise.setActive("Active");
                stateWise.setConfirmed("Confirmed");
                stateWise.setDeaths("Deaths");
                stateWise.setRecovered("Recovered");
                stateWise.setState("State/UT");

                stateWiseArrayList.add(stateWise);

                List<StateWise> mStateWiseList = covidDataModel.getStatewise();
                if (mStateWiseList.get(0).getState().equalsIgnoreCase("TOTAL")) {
                    mStateWiseList.remove(0);
                }

                Collections.sort(mStateWiseList, (obj1, obj2) -> Integer.valueOf(obj2.getConfirmed().trim()).compareTo(Integer.valueOf(obj1.getConfirmed().trim())));

                stateWiseArrayList.addAll(mStateWiseList);

                recyclerViewDataAdapter = new RecyclerViewDataAdapter(MainActivity.this, stateWiseArrayList);
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
