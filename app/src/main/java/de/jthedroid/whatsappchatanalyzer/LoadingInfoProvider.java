package de.jthedroid.whatsappchatanalyzer;

import androidx.lifecycle.MutableLiveData;

interface LoadingInfoProvider {
    MutableLiveData<Chat> chat = new MutableLiveData<>();
    MutableLiveData<String> title = new MutableLiveData<>();
    MutableLiveData<LoadingStage> loadingStage = new MutableLiveData<>();

    void setChat(Chat c);
}