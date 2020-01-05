package de.jthedroid.whatsappchatanalyzer;

import java.util.ArrayList;

public class Sender {
    private final String name;
    private final ArrayList<Message> messages;

    Sender(String s) {
        name = s;
        messages = new ArrayList<>();
    }

    String getName() {
        return name;
    }

    void addMessage(Message m) {
        messages.add(m);
    }

    int getMsgCount() {
        return messages.size();
    }

    ArrayList<Message> getMessages() {
        return messages;
    }
}
