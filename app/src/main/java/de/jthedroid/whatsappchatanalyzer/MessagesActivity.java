package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.Objects;

public class MessagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int msgCount = Objects.requireNonNull(DataStorage.getInstance().chat.getValue()).getMsgCount();
        String tag;
        for (int i = 0; i < msgCount; i++) {
            tag = "msg" + i;
            if (manager.findFragmentByTag(tag) == null)
                transaction.add(R.id.linearLayoutMessages, MessageFragment.newInstance(i), tag);
        }
        transaction.commit();
    }
}