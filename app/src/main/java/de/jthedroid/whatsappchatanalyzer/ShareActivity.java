package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        final LoadingViewModel viewModel = android.arch.lifecycle.ViewModelProviders.of(this).get(LoadingViewModel.class);
        final Observer<Chat> chatObserver = new Observer<Chat>() {
            @Override
            public void onChanged(@Nullable Chat c) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (Sender sender : c.sortedSenders) {
                    if (getSupportFragmentManager().findFragmentByTag(sender.toString()) == null) {
                        SenderFragment sf = SenderFragment.newInstance(sender.name,sender.getMsgCount(),c.getMaxMsgCount());
                        transaction.add(R.id.linearLayoutSender, sf, sender.toString());
                    }
                }
                transaction.commit();
                findViewById(R.id.progressBarLoading).setVisibility(View.GONE);
                findViewById(R.id.textViewLoading).setVisibility(View.GONE);
            }
        };
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            //String action = intent.getAction();
            String type = intent.getType();


            if (type != null) {
                String uriStr = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM).get(0).toString();
                Uri uri = Uri.parse(uriStr);
                viewModel.load(getContentResolver(), uri);
                viewModel.chat.observe(this, chatObserver);
            }
        } else {
            chatObserver.onChanged(viewModel.chat.getValue());
        }
    }
}
