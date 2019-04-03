package de.jthedroid.whatsappchatanalyzer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    Date date;
    boolean hasSender;
    String msg, senderStr;
    Sender sender = null;
    static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
    static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, HH:mm");

    Message(String s) throws ParseException {
        ParsePosition pp = new ParsePosition(0);
        date = sdf.parse(s, pp);
        int senderPos = pp.getIndex() + 3, colPos = s.indexOf(":", pp.getIndex());
        if (colPos < 0) {
            hasSender = false;
            senderStr = "";
            msg = s.substring(senderPos);
        } else {
            hasSender = true;
            senderStr = s.substring(senderPos, colPos);
            msg = s.substring(colPos + 1);
            if (WhatsAppChatAnalyzer.senders.containsKey(senderStr)) {
                sender = WhatsAppChatAnalyzer.senders.get(senderStr);
            } else {
                sender = new Sender(senderStr);
                WhatsAppChatAnalyzer.senders.put(senderStr, sender);
            }
            WhatsAppChatAnalyzer.messageCount.put(sender, WhatsAppChatAnalyzer.messageCount.getOrDefault(sender, 0) + 1);
        }
    }

    public void init(){
        if(hasSender){
            sender.addMessage(this);
        }
    }

    @Override
    public String toString() {
        return df.format(date) + (hasSender ? "" : " : " + senderStr) + " : " + msg;
    }

    public Date getDate() {
        return date;
    }

    public String getMsg() {
        return msg;
    }

    public Sender getSender() {
        return sender;
    }
}
