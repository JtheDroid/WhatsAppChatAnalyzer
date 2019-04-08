package de.jthedroid.whatsappchatanalyzer;

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
import java.util.Locale;

public class ShareActivity extends ThemeMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
        findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
        final LoadingViewModel viewModel = android.arch.lifecycle.ViewModelProviders.of(this).get(LoadingViewModel.class);
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
                        TimeGraphFragment tgf = TimeGraphFragment.newInstance(c.getTotalMessagesGraph());
                        addFragment(tgf, tag);
                    }
                    tag = "headingGraph2";
                    if (fragmentIsNew(tag)) {
                        HeadingFragment heading = HeadingFragment.newInstance(getString(R.string.messages_per_day));
                        addFragment(heading, tag);
                    }
                    tag = "graphView2";
                    if (fragmentIsNew(tag)) {
                        TimeGraphFragment tgf = TimeGraphFragment.newInstance(c.getMessagesPerDayGraph());
                        addFragment(tgf, tag);
                    }
                    tag = "headingSender";
                    if (fragmentIsNew(tag)) {
                        HeadingFragment heading = HeadingFragment.newInstance(String.format(Locale.getDefault(), "%s (%d)", getString(R.string.sent_messages), c.getMsgCount()));
                        addFragment(heading, tag);
                    }
                    for (Sender sender : c.getSortedSenders()) {
                        tag = sender.toString();
                        if (fragmentIsNew(tag)) {
                            SenderFragment sf = SenderFragment.newInstance(sender.name, sender.getMsgCount(), c.getMaxMsgCount());
                            addFragment(sf, tag);
                        }
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
        final Observer<Integer> loadingStageObserver = new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer != null) {
                    TextView textView = findViewById(R.id.textViewLoading);
                    textView.setVisibility(View.VISIBLE);
                    findViewById(R.id.progressBarLoading).setVisibility(View.VISIBLE);
                    switch (integer) {
                        case LoadingViewModel.OPENING_FILE:
                            textView.setText(R.string.opening_file);
                            break;
                        case LoadingViewModel.LOADING_FILE:
                            textView.setText(R.string.loading);
                            break;
                        case LoadingViewModel.PROCESSING:
                            textView.setText(R.string.processing);
                            break;
                        case LoadingViewModel.DONE:
                            findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            break;
                        case LoadingViewModel.ERROR:
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
                viewModel.title.setValue(title);
                //start loading chat .txt file
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
                }
                viewModel.load(getContentResolver(), uri);
            }
        } else {
            //load title
            setTitle(viewModel.title.getValue());
            //load data from Chat
            chatObserver.onChanged(viewModel.chat.getValue());
            loadingStageObserver.onChanged(viewModel.loadingStage.getValue());
        }
        viewModel.chat.observe(this, chatObserver);
        viewModel.loadingStage.observe(this, loadingStageObserver);
    }
}
