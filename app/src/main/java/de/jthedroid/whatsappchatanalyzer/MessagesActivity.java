package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Objects;

public class MessagesActivity extends ThemeMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new MessagesRecyclerViewAdapter(Objects.requireNonNull(DataStorage.getInstance().chat.getValue()).getMessages());
        recyclerView.setAdapter(adapter);
    }
}