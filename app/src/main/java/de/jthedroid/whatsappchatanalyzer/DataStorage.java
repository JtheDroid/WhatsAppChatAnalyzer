package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Objects;

enum LoadingStage {
    OPENING_FILE,
    LOADING_FILE,
    PROCESSING,
    DONE,
    ERROR
}

class DataStorage implements DataProvider, LoadingInfoProvider {

    private static final DataStorage instance = new DataStorage();
    final private MutableLiveData<HashMap<String, ChatData>> dataMap;
    final private MutableLiveData<HashMap<String, Runnable>> runnableMap;

    private DataStorage() {
        dataMap = new MutableLiveData<>();
        dataMap.setValue(new HashMap<String, ChatData>());
        runnableMap = new MutableLiveData<>();
        runnableMap.setValue(new HashMap<String, Runnable>());
    }

    static DataStorage getInstance() {
        return instance;
    }

    @Override
    public ChatData getData(String key) {
        return Objects.requireNonNull(dataMap.getValue()).get(key);
    }

    @Override
    public void putData(String key, ChatData chatData) {
        Objects.requireNonNull(dataMap.getValue()).put(key, chatData);
    }

    @Override
    public void setChat(Chat c) {
        chat.postValue(c);
    }

    void runRunnable(String key) {
        Runnable r = Objects.requireNonNull(runnableMap.getValue()).get(key);
        if (r != null) r.run();
    }

    public void runRunnableInThread(String key) {
        Runnable r = Objects.requireNonNull(runnableMap.getValue()).get(key);
        if (r != null) new Thread(r).start();
    }

    void putRunnable(String key, Runnable r) {
        Objects.requireNonNull(runnableMap.getValue()).put(key, r);
    }
}