package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import java.util.HashMap;
import java.util.Objects;

enum LoadingStage {
    OPENING_FILE,
    LOADING_FILE,
    PROCESSING,
    DONE,
    ERROR
}

class DataStorage implements LoadingInfoProvider {

    private static final DataStorage instance = new DataStorage();
    final private MutableLiveData<HashMap<String, MutableLiveData<GraphData>>> graphDataMap;
    final private MutableLiveData<HashMap<String, Runnable>> runnableMap;

    private DataStorage() {
        graphDataMap = new MutableLiveData<>();
        graphDataMap.setValue(new HashMap<String, MutableLiveData<GraphData>>());
        runnableMap = new MutableLiveData<>();
        runnableMap.setValue(new HashMap<String, Runnable>());
    }

    static DataStorage getInstance() {
        return instance;
    }

    GraphData getData(String key) {
        MutableLiveData<GraphData> data = getMutableLiveData(key);
        return data == null ? null : data.getValue();
    }

    MutableLiveData<GraphData> getMutableLiveData(String key) {
        return getMutableLiveData(key, false);
    }

    MutableLiveData<GraphData> getMutableLiveData(String key, boolean createIfNull) {
        HashMap<String, MutableLiveData<GraphData>> map = graphDataMap.getValue();
        MutableLiveData<GraphData> data;
        if (map != null && map.containsKey(key)) {
            data = map.get(key);
        } else data = null;
        if (createIfNull && data == null) {
            data = new MutableLiveData<>();
            if (map != null) map.put(key, data);
        }
        return data;
    }


    void putData(String key, GraphData graphData) {
        HashMap<String, MutableLiveData<GraphData>> map = graphDataMap.getValue();
        if (map == null) {
            Log.e("DataStorage putData", "map is null");
            return;
        }
        MutableLiveData<GraphData> data = null;
        if (map.containsKey(key)) {
            data = map.get(key);
        }
        if (data == null) data = new MutableLiveData<>();
        map.put(key, data);
        data.postValue(graphData);
    }

    @Override
    public void setChat(Chat c) {
        chat.postValue(c);
    }

    void runRunnable(String key) {
        Runnable r = Objects.requireNonNull(runnableMap.getValue()).get(key);
        if (r != null) r.run();
    }

    void runRunnableInThread(String key) {
        Runnable r = Objects.requireNonNull(runnableMap.getValue()).get(key);
        if (r != null) new Thread(r).start();
    }

    void putRunnable(String key, Runnable r) {
        Objects.requireNonNull(runnableMap.getValue()).put(key, r);
    }
}