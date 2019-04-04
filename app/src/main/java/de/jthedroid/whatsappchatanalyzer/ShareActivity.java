package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        final LoadingViewModel viewModel = android.arch.lifecycle.ViewModelProviders.of(this).get(LoadingViewModel.class);
        final Observer<Chat> chatObserver = new Observer<Chat>() {
            @Override
            public void onChanged(@Nullable Chat c) {
                ((TextView) findViewById(R.id.textViewInfo)).setText(c == null ? "null" : "Geladen!"/*c.toString()*/);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (Sender sender : c.sortedSenders) {
                    if (getSupportFragmentManager().findFragmentByTag(sender.toString()) == null) {
                        SenderFragment sf = new SenderFragment();
                        Bundle b = new Bundle();
                        b.putString("name", sender.name);
                        b.putInt("count", sender.getMsgCount());
                        b.putInt("maxCount",c.getMaxMsgCount());
                        sf.setArguments(b);
                        transaction.add(R.id.linearLayoutSender, sf, sender.toString());
                    }

                }
                transaction.commit();
            }
        };
        TextView infoTextView = findViewById(R.id.textViewInfo);
        if (savedInstanceState == null) {
            infoTextView.setText(R.string.loading);
            Intent intent = getIntent();
            //String action = intent.getAction();
            String type = intent.getType();


            if (type != null) {
                //String text = intent.getStringExtra(Intent.EXTRA_TEXT);
                String uriStr = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM).get(0).toString();
                Uri uri = Uri.parse(uriStr);
                Log.d("ShareActivity", uri.toString());

                viewModel.load(getContentResolver(), uri);
                //infoTextView.setText(action + " " + type + "\nText:\n" + text + "\n" + uri + "\n");
                viewModel.chat.observe(this, chatObserver);
            }
        } else {
            //infoTextView.setText("Reloaded");
            chatObserver.onChanged(viewModel.chat.getValue());
        }
    }
}
