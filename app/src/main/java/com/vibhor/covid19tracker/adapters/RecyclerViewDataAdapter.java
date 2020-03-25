package com.vibhor.covid19tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vibhor.covid19tracker.R;
import com.vibhor.covid19tracker.models.StateWise;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ViewHolder> {

    private Context mContext;
    private List<StateWise> mStateWiseList;

    public RecyclerViewDataAdapter(Context context, List<StateWise> stateWiseList) {
        this.mContext = context;
        this.mStateWiseList = stateWiseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StateWise stateWise = mStateWiseList.get(position);
        holder.stateTv.setText(stateWise.getState());
        holder.confirmedTv.setText(stateWise.getConfirmed());
        holder.activeTv.setText(stateWise.getActive());
        holder.recoveredTv.setText(stateWise.getRecovered());
        holder.deathsTv.setText(stateWise.getDeaths());
    }


    @Override
    public int getItemCount() {
        return mStateWiseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.state_tv)
        TextView stateTv;

        @BindView(R.id.confirmed_tv)
        TextView confirmedTv;

        @BindView(R.id.active_tv)
        TextView activeTv;

        @BindView(R.id.recovered_tv)
        TextView recoveredTv;

        @BindView(R.id.deaths_tv)
        TextView deathsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}