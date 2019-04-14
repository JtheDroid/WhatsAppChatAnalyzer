package de.jthedroid.whatsappchatanalyzer;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static de.jthedroid.whatsappchatanalyzer.LoadingStage.DONE;
import static de.jthedroid.whatsappchatanalyzer.LoadingStage.ERROR;
import static de.jthedroid.whatsappchatanalyzer.LoadingStage.LOADING_FILE;
import static de.jthedroid.whatsappchatanalyzer.LoadingStage.OPENING_FILE;

class ChatLoadingThread extends Thread {
    private final ContentResolver contentResolver;
    private final Uri uri;
    private final LoadingInfoProvider lip;

    ChatLoadingThread(ContentResolver contentResolver, Uri uri, LoadingInfoProvider lip) {
        this.contentResolver = contentResolver;
        this.uri = uri;
        this.lip = lip;
    }

    @Override
    public void run() {
        super.run();
        Chat chat = new Chat();
        try {
            lip.loadingStage.postValue(OPENING_FILE);
            InputStream is = contentResolver.openInputStream(uri);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            lip.loadingStage.postValue(LOADING_FILE);
            chat.init(br, lip);
            lip.loadingStage.postValue(chat.isValid() ? DONE : ERROR);
            lip.setChat(chat.isValid() ? chat : null);
        } catch (IOException e) {
            e.printStackTrace();
            lip.loadingStage.postValue(ERROR);
        }
    }
}
