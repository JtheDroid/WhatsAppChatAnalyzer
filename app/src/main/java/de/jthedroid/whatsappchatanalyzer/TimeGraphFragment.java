package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class TimeGraphFragment extends Fragment {
    static final String GRAPH_DATA = "graphData";
    private GraphData graphData;
    private String graphDataKey;

    public TimeGraphFragment() {
    }

    static TimeGraphFragment newInstance(String graphDataKey) {
        TimeGraphFragment fragment = new TimeGraphFragment();
        Bundle args = new Bundle();
        args.putString(GRAPH_DATA, graphDataKey);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getArguments());
    }

    void init(Bundle args) {
        DataStorage dataStorage = DataStorage.getInstance();
        if (args != null && dataStorage != null) {
            String key = args.getString(GRAPH_DATA);
            graphData = dataStorage.getData(key);
            graphDataKey = key;
        }
    }

    void initGraphView() {
        View view = getView();
        if (view == null) return;
        GraphView graphView = view.findViewById(R.id.graphView);
        if (graphView == null) return;
        initGraphView(graphView);
    }

    private void initGraphView(GraphView graphView) {
        graphView.setKey(graphDataKey);
        DataStorage.getInstance().getMutableLiveData(graphDataKey, true).observe(this, graphView.getGraphDataObserver());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_time_graph, container, false);
        GraphView graphView = v.findViewById(R.id.graphView);
        graphView.init(graphData, v.findViewById(R.id.progressBarGraphLoading));
        initGraphView(graphView);
        return v;
    }
}
