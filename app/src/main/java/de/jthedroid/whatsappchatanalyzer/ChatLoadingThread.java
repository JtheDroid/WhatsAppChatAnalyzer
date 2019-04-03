package de.jthedroid.whatsappchatanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

class ChatLoadingThread extends Thread {
    Chat chat;
    private BufferedReader br;

    ChatLoadingThread(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        super.run();
        chat = new Chat();
        chat.init(br);
    }
}
