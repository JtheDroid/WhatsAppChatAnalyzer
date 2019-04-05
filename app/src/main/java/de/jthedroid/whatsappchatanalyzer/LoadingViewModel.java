package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class LoadingViewModel extends ViewModel {
    final MutableLiveData<Chat> chat;
    final MutableLiveData<String> title;

    LoadingViewModel() {
        chat = new MutableLiveData<>();
        title = new MutableLiveData<>();
    }

    void load(ContentResolver contentResolver, Uri uri) {
        if (uri != null) {
            try {
                InputStream is = contentResolver.openInputStream(uri);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                ChatLoadingThread clt = new ChatLoadingThread(br, this);
                clt.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void setChat(Chat c) {
        chat.postValue(c);
    }
}
