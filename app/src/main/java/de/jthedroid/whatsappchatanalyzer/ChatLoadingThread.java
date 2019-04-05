package de.jthedroid.whatsappchatanalyzer;

import java.io.BufferedReader;
import java.io.IOException;

class ChatLoadingThread extends Thread {
    private final BufferedReader br;
    private final LoadingViewModel lvm;

    ChatLoadingThread(BufferedReader br, LoadingViewModel lvm) {
        this.br = br;
        this.lvm = lvm;
    }

    @Override
    public void run() {
        super.run();
        Chat chat = new Chat();
        try {
            chat.init(br);
            if (chat.sortedSenders.isEmpty()) {
                lvm.setChat(null);
            } else {
                lvm.setChat(chat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
