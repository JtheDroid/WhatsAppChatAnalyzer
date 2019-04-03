package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ContentResolver;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class LoadingViewModel extends ViewModel {
    MutableLiveData<Chat> chat;
    ChatLoadingThread clt;

    LoadingViewModel() {
        chat = new MutableLiveData<>();
    }

    void load(ContentResolver conres, Uri uri) {

        try {
            InputStream is = conres.openInputStream(uri);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            clt = new ChatLoadingThread(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setChat(Chat c){
        chat.setValue(c);
    }
}
