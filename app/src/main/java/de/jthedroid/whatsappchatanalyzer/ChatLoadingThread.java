package de.jthedroid.whatsappchatanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class ChatLoadingThread extends Thread {
    Chat chat;
    private BufferedReader br;
    private LoadingViewModel lvm;

    ChatLoadingThread(BufferedReader br, LoadingViewModel lvm) {
        this.br = br;
        this.lvm = lvm;
    }

    @Override
    public void run() {
        super.run();
        chat = new Chat();
        try {
            chat.init(br);
            lvm.setChat(chat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
