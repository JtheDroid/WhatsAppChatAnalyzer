package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
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

class MessagesRecyclerViewAdapter extends RecyclerView.Adapter {
    private final DateFormat dateFormat;
    private final ArrayList<Message> messages;


    MessagesRecyclerViewAdapter(ArrayList<Message> messages) {
        this.messages = messages;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MessagesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_message, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        onBindViewHolder((MessagesViewHolder) viewHolder, i);
    }

    private void onBindViewHolder(MessagesViewHolder holder, int i) {
        Message m = messages.get(i);
        holder.tvMessage.setText(m.getMsg());
        holder.tvDate.setText(dateFormat.format(m.getDate()));
        Sender s = m.getSender();
        holder.tvSender.setVisibility(s == null ? View.GONE : View.VISIBLE);
        if (s != null) holder.tvSender.setText(m.getSender().getName());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        final TextView tvMessage, tvSender, tvDate;

        MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.textViewMessage);
            tvSender = itemView.findViewById(R.id.textViewSender);
            tvDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}