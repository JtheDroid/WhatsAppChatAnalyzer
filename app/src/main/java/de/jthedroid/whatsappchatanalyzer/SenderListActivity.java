package de.jthedroid.whatsappchatanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class SenderListActivity extends ThemeMenuActivity {
    public static String SENDER_NAME_EXTRA = "SENDER_NAME_EXTRA";

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

    public void toggleSenderButtonVisibility(View v) {
        final View viewToToggle = v.findViewById(R.id.buttonGotoSender);
        final int visibility = viewToToggle.getVisibility();
        viewToToggle.animate().setDuration(200).alpha(visibility > 0 ? 1 : 0).withStartAction(new Runnable() {
            @Override
            public void run() {
                viewToToggle.setVisibility(View.VISIBLE);
            }
        }).withEndAction(new Runnable() {
            @Override
            public void run() {
                viewToToggle.setVisibility(View.GONE - visibility);
            }
        }).start();
    }

    public void showSenderDetails(View v) {
        CharSequence name = ((TextView) ((View) v.getParent()).findViewById(R.id.textViewName)).getText();
        Intent intent = new Intent(this, SenderActivity.class);
        intent.putExtra(SENDER_NAME_EXTRA, name);
        startActivity(intent);
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
        holder.buttonDetails.setVisibility(View.GONE);
        holder.buttonDetails.setAlpha(0);
    }

    @Override
    public int getItemCount() {
        return senders.size();
    }

    public static class SenderOverviewViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName, tvCount;
        final ProgressBar progressBar;
        final Button buttonDetails;

        SenderOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName);
            tvCount = itemView.findViewById(R.id.textViewCount);
            progressBar = itemView.findViewById(R.id.progressBarMsgCount);
            buttonDetails = itemView.findViewById(R.id.buttonGotoSender);
        }
    }
}