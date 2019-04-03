package de.jthedroid.whatsappchatanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Chat {
    static HashMap<String, Sender> senders = new HashMap<>();
    static HashMap<Sender, Integer> messageCount = new HashMap<>();

    void init(BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<Message> msgs = new ArrayList<>();
        while(br.ready()){
            //             1/1/17, 05:55 -
            //             12/12/17, 05:55 -
            if (t.matches("^(\\d\\d?/){2}\\d\\d?, \\d\\d?:\\d\\d? - .*")) {
                lines.add(t);
            } else {
                if (!lines.isEmpty()) {
                    String line = lines.get(lines.size() - 1);
                    lines.remove(line);
                    lines.add(line + "\n" + t);
                }
            }

        }
        for (String line : lines) {
            Message m = new Message(line);
            m.init();
            msgs.add(m);
        }
        for (Message msg : msgs) {
            System.out.println(msg.toString());
        }
        System.out.println("\n\n");
        for (Sender sender : WhatsAppChatAnalyzer.messageCount.keySet()) {
            System.out.println(sender.name + ": " + WhatsAppChatAnalyzer.messageCount.get(sender));
        }
        System.out.println("\n\n");
        Object[] senderList =  WhatsAppChatAnalyzer.messageCount.keySet().toArray();
        BinTree senderTree = new BinTree((Sender)senderList[0]);
        for (int i = 1; i < senderList.length; i++) {
            Sender sender = (Sender)senderList[i];
            senderTree.addContent(sender);
        }
        ArrayList<Sender> sortedSenders = senderTree.sort();
        for(Sender sender : sortedSenders){
            System.out.println(sender.name+" : " + sender.getMsgCount() + " messages");
        }
    }
}
