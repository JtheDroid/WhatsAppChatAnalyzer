package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.net.Uri;

import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
public class LoadingViewModel extends ViewModel {
    static final int OPENING_FILE = 0, LOADING_FILE = 1, PROCESSING = 2, DONE = 3, ERROR = -1;
    final MutableLiveData<Chat> chat;
    final MutableLiveData<String> title;
    final MutableLiveData<Integer> loadingStage;
    final MutableLiveData<HashMap<String, ChatData>> dataMap;

    public LoadingViewModel() {
        chat = new MutableLiveData<>();
        title = new MutableLiveData<>();
        loadingStage = new MutableLiveData<>();
        dataMap = new MutableLiveData<>();
    }

    void load(ContentResolver contentResolver, Uri uri) {
        if (uri != null) {
            ChatLoadingThread clt = new ChatLoadingThread(contentResolver, uri, this);
            clt.start();
        } else loadingStage.setValue(ERROR);
    }

    void setChat(Chat c) {
        chat.postValue(c);
    }
}
