package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;

public class SenderActivity extends ThemeMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);
        final Chat c = DataStorage.getInstance().chat.getValue();
        String name = getIntent().getStringExtra(SenderListActivity.SENDER_NAME_EXTRA);
        setTitle(name);
        final Sender sender = c.senders.get(name);
        final DataStorage ds = DataStorage.getInstance();
        final String key = "senderTotalMessagesGraph" + name;
        TimeGraphFragment tgf = (TimeGraphFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentSenderTotalGraph);
        if (tgf != null) {
            Bundle args = new Bundle();
            args.putString(TimeGraphFragment.GRAPH_DATA, key);
            tgf.init(args);
            tgf.initGraphView();
            if (!ds.hasRunnable(key)) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        GraphData gD = c.createTotalMessagesGraph(sender);
                        ds.putData(key, gD);
                    }
                };
                ds.putRunnable(key, r);
                ds.runRunnableInThread(key);
            }
        }
    }
}
