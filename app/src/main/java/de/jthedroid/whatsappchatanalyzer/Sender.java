package de.jthedroid.whatsappchatanalyzer;

import java.util.ArrayList;

import de.jthedroid.whatsappchatanalyzer.bintree.Sortable;

public class Sender implements Sortable {
    final String name;
    private final ArrayList<Message> messages;

    Sender(String s) {
        name = s;
        messages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    void addMessage(Message m) {
        messages.add(m);
    }

    int getMsgCount() {
        return messages.size();
    }

    @Override
    public int getNum() {
        return getMsgCount();
    }
}
