package de.jthedroid.whatsappchatanalyzer;

import java.util.ArrayList;

public class Sender implements bintree.Sortable {
    String name;
    ArrayList<Message> messages;

    Sender(String s) {
        name = s;
        messages = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addMessage(Message m){
        messages.add(m);
    }

    public int getMsgCount(){
        return messages.size();
    }

    @Override
    public int getNum() {
        return getMsgCount();
    }
}
