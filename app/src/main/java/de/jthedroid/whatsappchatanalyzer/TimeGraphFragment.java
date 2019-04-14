package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TimeGraphFragment extends Fragment {
    private static final String GRAPH_DATA = "graphData";
    private GraphData graphData;

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
        DataProvider dataProvider = DataStorage.getInstance();
        if (getArguments() != null && dataProvider != null) {
            String key = getArguments().getString(GRAPH_DATA);
            graphData = (GraphData) dataProvider.getData(key);
        } else
            Log.e("TimeGraphFragment", "Error creating Fragment: " + (getArguments() == null ? "getArguments()==null" : " ") + (dataProvider == null ? "dataProvider==null" : ""));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_graph, container, false);
        GraphView graphView = v.findViewById(R.id.graphView);
        graphView.init(graphData, v.findViewById(R.id.progressBarGraphLoading));
        return v;
    }


}
