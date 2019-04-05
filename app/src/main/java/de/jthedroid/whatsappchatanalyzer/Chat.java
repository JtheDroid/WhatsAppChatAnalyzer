package de.jthedroid.whatsappchatanalyzer;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.jthedroid.whatsappchatanalyzer.bintree.BinTree;

class Chat {
    final HashMap<String, Sender> senders = new HashMap<>();
    ArrayList<Sender> sortedSenders;
    private final ArrayList<Message> messages = new ArrayList<>();

    void init(BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        while (br.ready()) {
            lines.add(br.readLine());
        }
        br.close();
        for (String l : lines) {
            //             1/1/17, 05:55 -
            //             12/12/17, 05:55 -
            if (l.matches("^(\\d\\d?/){2}\\d\\d?, \\d\\d?:\\d\\d? - .*")) {
                strings.add(l);
            } else {
                if (!strings.isEmpty()) {
                    String line = strings.get(strings.size() - 1);
                    strings.remove(line);
                    strings.add(line + "\n" + l);
                }
            }
        }
        for (String s : strings) {
            Message m = new Message(s, this);
            messages.add(m);
        }
        ArrayList<Sender> senderList = new ArrayList<>(senders.values());
        BinTree<Sender> senderTree = new BinTree<>(senderList.get(0));
        for (int i = 1; i < senderList.size(); i++) {
            Sender sender = senderList.get(i);
            senderTree.addContent(sender);
        }
        ArrayList<Sender> tempList = senderTree.sort();
        sortedSenders = new ArrayList<>(tempList);
        int size = tempList.size();
        sortedSenders.ensureCapacity(size);
        for (int i = size - 1; i >= 0; i--) {
            sortedSenders.set(size - 1 - i, tempList.get(i));
        }
    }

    int getMaxMsgCount() {
        return sortedSenders.get(0).getMsgCount();
    }

    int getMsgCount() {
        return messages.size();
    }

    @Override
    @NonNull
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Sender sender : sortedSenders) {
            s.append(sender.getName());
            s.append(", ");
            s.append(sender.getMsgCount());
            s.append("\n");
        }
        return s.toString();
    }
}
