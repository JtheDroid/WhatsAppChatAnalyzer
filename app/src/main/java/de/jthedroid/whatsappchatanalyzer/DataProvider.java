package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;

interface DataProvider {
    ChatData getData(String key);

    void putData(String key, ChatData chatData);
}

interface ChatData {
}

interface LoadingInfoProvider {
    MutableLiveData<Chat> chat = new MutableLiveData<>();
    MutableLiveData<String> title = new MutableLiveData<>();
    MutableLiveData<LoadingStage> loadingStage = new MutableLiveData<>();

    void setChat(Chat c);
}