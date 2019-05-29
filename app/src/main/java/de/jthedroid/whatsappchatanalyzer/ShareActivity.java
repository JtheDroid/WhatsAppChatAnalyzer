package de.jthedroid.whatsappchatanalyzer;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static de.jthedroid.whatsappchatanalyzer.LoadingStage.ERROR;

public class ShareActivity extends ThemeMenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DataStorage ds = DataStorage.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
        final Activity thisActivity = this;
        final Observer<Chat> chatObserver = new Observer<Chat>() {
            private FragmentTransaction transaction;

            @Override
            public void onChanged(@Nullable Chat c) {
                if (c != null) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    String tag = "headingGraph1";
                    if (fragmentIsNew(tag)) {
                        HeadingFragment heading = HeadingFragment.newInstance(getString(R.string.total_messages_time));
                        addFragment(heading, tag);
                    }
                    tag = "graphView1";
                    if (fragmentIsNew(tag)) {
                        String key = tag + "_data";
                        ds.putData(key, c.getTotalMessagesGraph());
                        TimeGraphFragment tgf = TimeGraphFragment.newInstance(key);
                        addFragment(tgf, tag);
                    }
                    tag = "headingGraph2";
                    if (fragmentIsNew(tag)) {
                        HeadingFragment heading = HeadingFragment.newInstance(getString(R.string.messages_per_day));
                        addFragment(heading, tag);
                    }
                    tag = "graphView2";
                    if (fragmentIsNew(tag)) {
                        String key = tag + "_data";
                        ds.putData(key, c.getMessagesPerDayGraph());
                        TimeGraphFragment tgf = TimeGraphFragment.newInstance(key);
                        addFragment(tgf, tag);
                    }
                    tag = "buttonOpenSenderList";
                    if (fragmentIsNew(tag)) {
                        String key = "showSenderListActivity";
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(thisActivity, SenderListActivity.class);
                                startActivity(intent);
                            }
                        };
                        DataStorage.getInstance().putRunnable(key, r);
                        addFragment(ButtonFragment.newInstance(getString(R.string.show_sender_list), key), tag);
                    }
                    tag = "buttonOpenMessages";
                    if (fragmentIsNew(tag)) {
                        String key = "showMessagesActivity";
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(thisActivity, MessagesActivity.class);
                                startActivity(intent);
                            }
                        };
                        DataStorage.getInstance().putRunnable(key, r);
                        addFragment(ButtonFragment.newInstance(getString(R.string.show_messages), key), tag);
                    }
                    transaction.commit();
                }
            }

            private boolean fragmentIsNew(String tag) {
                return getSupportFragmentManager().findFragmentByTag(tag) == null;
            }

            private void addFragment(Fragment f, String tag) {
                transaction.add(R.id.linearLayoutSender, f, tag);
            }
        };
        final Observer<LoadingStage> loadingStageObserver = new Observer<LoadingStage>() {
            @Override
            public void onChanged(@Nullable LoadingStage loadingStage) {
                if (loadingStage != null) {
                    TextView textView = findViewById(R.id.textViewLoading);
                    textView.setVisibility(View.VISIBLE);
                    findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
                    switch (loadingStage) {
                        case OPENING_FILE:
                            textView.setText(R.string.opening_file);
                            break;
                        case LOADING_FILE:
                            textView.setText(R.string.loading);
                            break;
                        case PROCESSING:
                            textView.setText(R.string.processing);
                            break;
                        case DONE:
                            findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            break;
                        case ERROR:
                            findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                            textView.setText(R.string.error_loading);
                            break;
                    }
                }
            }
        };
        Intent intent = getIntent();
        if (savedInstanceState == null) {

            //String action = intent.getAction();
            String type = intent.getType();

            if (type != null) {
                //save title String in viewModel
                String title = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (title.contains("\"")) {
                    title = title.substring(title.indexOf('"') + 1, title.lastIndexOf('"'));
                }
                setTitle(title);
                ds.title.setValue(title);
                //read uri from intent
                Uri uri = null;
                ArrayList<Parcelable> extraList = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if (extraList != null && !extraList.isEmpty()) {
                    Parcelable p = extraList.get(0);
                    if (p instanceof Uri) {
                        uri = (Uri) p;
                    }
                }
                if (uri == null) {
                    Toast.makeText(this, R.string.toast_faulty_data, Toast.LENGTH_LONG).show();
                    ds.loadingStage.setValue(ERROR);
                }
                //start loading the text file in a new Thread
                else {
                    ChatLoadingThread clt = new ChatLoadingThread(getContentResolver(), uri, ds);
                    clt.start();
                }

            } else ds.loadingStage.setValue(ERROR);
        } else {
            //load title
            setTitle(ds.title.getValue());
            //load data from Chat
            chatObserver.onChanged(ds.chat.getValue());
            loadingStageObserver.onChanged(ds.loadingStage.getValue());
        }
        ds.chat.observe(this, chatObserver);
        ds.loadingStage.observe(this, loadingStageObserver);
    }
}
