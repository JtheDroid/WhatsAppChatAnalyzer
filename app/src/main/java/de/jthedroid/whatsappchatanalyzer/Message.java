package de.jthedroid.whatsappchatanalyzer;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Message {
    private static DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.GERMANY);
    private static SimpleDateFormat sdf = new SimpleDateFormat("M/d/yy, HH:mm");
    private Date date;
    private boolean hasSender;
    private String msg, senderStr;
    private Sender sender = null;

    Message(String s, Chat c) {
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
            if (c.senders.containsKey(senderStr)) {
                sender = c.senders.get(senderStr);
            } else {
                sender = new Sender(senderStr);
                c.senders.put(senderStr, sender);
            }
        }
        if (hasSender) {
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
