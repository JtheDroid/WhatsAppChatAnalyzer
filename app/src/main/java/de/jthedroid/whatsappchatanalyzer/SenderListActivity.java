package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SenderListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_list);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSenderList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Chat chat = Objects.requireNonNull(DataStorage.getInstance().chat.getValue());
        RecyclerView.Adapter adapter = new SenderOverviewRecyclerViewAdapter(chat.getSortedSenders(), chat.getMaxMsgCount());
        recyclerView.setAdapter(adapter);
    }
}


class SenderOverviewRecyclerViewAdapter extends RecyclerView.Adapter {
    private final ArrayList<Sender> senders;
    private final int maxCount;


    SenderOverviewRecyclerViewAdapter(ArrayList<Sender> senders, int maxCount) {
        this.senders = senders;
        this.maxCount = maxCount;
    }

    @NonNull
    @Override
    public SenderOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SenderOverviewViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sender, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        onBindViewHolder((SenderOverviewViewHolder) viewHolder, i);
    }

    private void onBindViewHolder(SenderOverviewViewHolder holder, int i) {
        Sender s = senders.get(i);
        holder.tvName.setText(s.getName());
        holder.tvCount.setText(String.format(Locale.getDefault(), "%d", s.getMsgCount()));
        holder.progressBar.setMax(maxCount);
        holder.progressBar.setProgress(s.getMsgCount());
    }

    @Override
    public int getItemCount() {
        return senders.size();
    }

    public static class SenderOverviewViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName, tvCount;
        final ProgressBar progressBar;

        SenderOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName);
            tvCount = itemView.findViewById(R.id.textViewCount);
            progressBar = itemView.findViewById(R.id.progressBarMsgCount);
        }
    }
}