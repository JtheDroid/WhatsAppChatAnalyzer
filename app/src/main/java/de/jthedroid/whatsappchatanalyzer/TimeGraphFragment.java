package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TimeGraphFragment extends Fragment {
    private static final String GRAPH_DATA = "graphData";
    private GraphData graphData;

    public TimeGraphFragment() {
    }

    public static TimeGraphFragment newInstance(GraphData graphData) {
        TimeGraphFragment fragment = new TimeGraphFragment();
        Bundle args = new Bundle();
        args.putParcelable(GRAPH_DATA, graphData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            graphData = getArguments().getParcelable(GRAPH_DATA);
        }
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
