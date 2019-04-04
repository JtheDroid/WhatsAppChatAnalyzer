package de.jthedroid.whatsappchatanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.jthedroid.whatsappchatanalyzer.bintree.BinTree;

class Chat {
    HashMap<String, Sender> senders = new HashMap<>();
    HashMap<Sender, Integer> messageCount = new HashMap<>();
    ArrayList<Sender> sortedSenders;

    void init(BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Message> msgs = new ArrayList<>();
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
            m.init();
            msgs.add(m);
        }
        Object[] senderList = messageCount.keySet().toArray();
        BinTree<Sender> senderTree = new BinTree<>((Sender) Objects.requireNonNull(senderList)[0]);
        for (int i = 1; i < senderList.length; i++) {
            Sender sender = (Sender) senderList[i];
            senderTree.addContent(sender);
        }
        sortedSenders = senderTree.sort();
    }

    int getMaxMsgCount(){
        return sortedSenders.get(sortedSenders.size()-1).getMsgCount();
    }

    @Override
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
