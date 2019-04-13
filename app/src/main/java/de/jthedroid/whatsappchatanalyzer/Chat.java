package de.jthedroid.whatsappchatanalyzer;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LongSparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.jthedroid.whatsappchatanalyzer.bintree.BinTree;

class Chat {
    private static final int MAX_GRAPH_POINTS = 10000;

    final HashMap<String, Sender> senders = new HashMap<>();
    private final ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<Sender> sortedSenders;
    private GraphData totalMessagesGraph, messagesPerDayGraph;
    private boolean valid = true;

    void init(BufferedReader br, LoadingViewModel lvm) throws IOException {
        ArrayList<String> lines = readLines(br);
        ArrayList<String> strings = createMessageStrings(lines);
        if (!valid) {
            return;
        }
        lvm.loadingStage.postValue(LoadingViewModel.PROCESSING);
        addMessages(strings);
        if (messages.isEmpty()) {
            valid = false;
            return;
        }
        sortedSenders = createSortedSenderList();
        if (sortedSenders.isEmpty()) {
            valid = false;
            return;
        }
        totalMessagesGraph = createTotalMessagesGraph();
        messagesPerDayGraph = createMessagesPerDayGraph();
    }

    private ArrayList<String> readLines(@NonNull BufferedReader br) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        while (br.ready()) {
            lines.add(br.readLine());
        }
        br.close();
        return lines;
    }

    private ArrayList<String> createMessageStrings(@NonNull ArrayList<String> lines) {
        ArrayList<String> strings = new ArrayList<>();
        for (String l : lines) {
            //                   1/1/17, 05:55 -
            //                   12/12/17, 05:55 -
            //                   05.04.19, 16:53 -
            if (l.matches("^\\d+.*\\d+, \\d+.* - .*")) {
                strings.add(l);
            } else {
                if (!strings.isEmpty()) {
                    String line = strings.get(strings.size() - 1);
                    strings.remove(line);
                    strings.add(line + "\n" + l);
                }
            }
        }
        if (strings.isEmpty()) {
            valid = false;
        }
        return strings;
    }

    private void addMessages(ArrayList<String> strings) {
        int consecutiveParseEx = 0;
        for (String s : strings) {
            try {
                Message m = new Message(s, this);
                messages.add(m);
                consecutiveParseEx = 0;
            } catch (ParseException e) {
                Log.e("Chat ParseException", e.toString());
                consecutiveParseEx++;
                if (consecutiveParseEx > 50) {
                    valid = false;
                    return;
                }
            }
        }
    }

    private ArrayList<Sender> createSortedSenderList() {
        ArrayList<Sender> senderList = new ArrayList<>(senders.values());
        ArrayList<Sender> sorted = new ArrayList<>();
        if (!senders.isEmpty()) {
            BinTree<Sender> senderTree = new BinTree<>(senderList.get(0));
            for (int i = 1; i < senderList.size(); i++) {
                Sender sender = senderList.get(i);
                senderTree.addContent(sender);
            }
            ArrayList<Sender> tempList = senderTree.sort();
            sorted = new ArrayList<>(tempList);
            int size = tempList.size();
            sorted.ensureCapacity(size);
            for (int i = size - 1; i >= 0; i--) {
                sorted.set(size - 1 - i, tempList.get(i));
            }
        }
        return sorted;
    }

    private GraphData createTotalMessagesGraph() {
        float[] xData, yData;
        int msgCount = messages.size();
        if (msgCount == 0) return null;
        float step;
        if (msgCount <= MAX_GRAPH_POINTS) {
            xData = new float[msgCount];
            yData = new float[msgCount];
            step = 1;
        } else {
            xData = new float[MAX_GRAPH_POINTS];
            yData = new float[MAX_GRAPH_POINTS];
            step = msgCount / (float) MAX_GRAPH_POINTS;
        }
        String[] xDesc = new String[xData.length], yDesc = new String[yData.length];
        DateFormat df = DateFormat.getDateTimeInstance();
        for (int i = 0; i < xData.length; i++) {
            Message msg = messages.get((int) (i * step));
            xData[i] = msg.getDate().getTime(); //timecode
            xDesc[i] = df.format(msg.getDate());
            yData[i] = i * step;  //total messages at this point
            yDesc[i] = "" + (int) yData[i];
        }
        GraphData gD = new GraphData(xData, yData, xDesc, yDesc);
        gD.scale();
        return gD;
    }

    private GraphData createMessagesPerDayGraph() {
        int msgCount = messages.size();
        if (msgCount == 0) return null;
        Calendar calendar = Calendar.getInstance();
        LongSparseArray<Integer> dayCounts = new LongSparseArray<>();
        for (Message m : messages) {
            calendar.setTime(m.getDate());
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long millis = calendar.getTimeInMillis();
            int count = dayCounts.get(millis, 0);
            dayCounts.put(millis, count + 1);
        }
        float[] xData = new float[dayCounts.size()],
                yData = new float[dayCounts.size()];
        String[] xDesc = new String[dayCounts.size()],
                yDesc = new String[dayCounts.size()];
        DateFormat df = DateFormat.getDateInstance();
        Date d = new Date();
        for (int i = 0; i < dayCounts.size(); i++) {
            long time = dayCounts.keyAt(i);
            d.setTime(time);
            xData[i] = time;  //timecode
            xDesc[i] = df.format(d);    //day
            int messageCount = dayCounts.valueAt(i);
            yData[i] = messageCount; //messages per day
            yDesc[i] = "" + messageCount; //messages per day
        }
        GraphData gD = new GraphData(xData, yData, xDesc, yDesc);
        gD.scale();
        return gD;
    }

    ArrayList<Sender> getSortedSenders() {
        return sortedSenders;
    }

    GraphData getTotalMessagesGraph() {
        return totalMessagesGraph;
    }

    GraphData getMessagesPerDayGraph() {
        return messagesPerDayGraph;
    }

    int getMaxMsgCount() {
        return sortedSenders.get(0).getMsgCount();
    }

    int getMsgCount() {
        return messages.size();
    }

    boolean isValid() {
        return valid;
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
