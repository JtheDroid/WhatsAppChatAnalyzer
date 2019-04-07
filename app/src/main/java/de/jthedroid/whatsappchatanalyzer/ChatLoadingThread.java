package de.jthedroid.whatsappchatanalyzer;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class ChatLoadingThread extends Thread {
    private final ContentResolver contentResolver;
    private final Uri uri;
    private final LoadingViewModel lvm;

    ChatLoadingThread(ContentResolver contentResolver, Uri uri, LoadingViewModel lvm) {
        this.contentResolver = contentResolver;
        this.uri = uri;
        this.lvm = lvm;
    }

    @Override
    public void run() {
        super.run();
        Chat chat = new Chat();
        try {
            lvm.loadingStage.postValue(LoadingViewModel.OPENING_FILE);
            InputStream is = contentResolver.openInputStream(uri);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            lvm.loadingStage.postValue(LoadingViewModel.LOADING_FILE);
            chat.init(br, lvm);
            lvm.setChat(chat.isValid() ? chat : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
