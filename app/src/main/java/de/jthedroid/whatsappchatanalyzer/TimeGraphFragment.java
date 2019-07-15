package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class TimeGraphFragment extends Fragment {
    private static final String GRAPH_DATA = "graphData";
    private GraphData graphData;
    private String graphDataKey;

    public TimeGraphFragment() {
    }

    public static TimeGraphFragment newInstance(String graphDataKey) {
        TimeGraphFragment fragment = new TimeGraphFragment();
        Bundle args = new Bundle();
        args.putString(GRAPH_DATA, graphDataKey);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataStorage dataStorage = DataStorage.getInstance();
        if (getArguments() != null && dataStorage != null) {
            String key = getArguments().getString(GRAPH_DATA);
            graphData = dataStorage.getData(key);
            graphDataKey = key;
        } else
            Log.e("TimeGraphFragment", "Error creating Fragment: " + (getArguments() == null ? "getArguments()==null" : " ") + (dataStorage == null ? "dataStorage==null" : ""));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_graph, container, false);
        GraphView graphView = v.findViewById(R.id.graphView);
        graphView.init(graphData, v.findViewById(R.id.progressBarGraphLoading));
        graphView.setKey(graphDataKey);
        DataStorage.getInstance().getMutableLiveData(graphDataKey, true).observe(this, graphView.getGraphDataObserver());
        return v;
    }
}
