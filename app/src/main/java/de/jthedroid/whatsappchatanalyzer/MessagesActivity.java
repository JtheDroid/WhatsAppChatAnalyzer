package de.jthedroid.whatsappchatanalyzer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MessagesActivity extends ThemeMenuActivity implements DateReceiver {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        recyclerView = findViewById(R.id.recyclerViewMessages);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new MessagesRecyclerViewAdapter(Objects.requireNonNull(DataStorage.getInstance().chat.getValue()).getMessages());
        recyclerView.setAdapter(adapter);
        final View scrollTop = findViewById(R.id.buttonScrollTop), scrollBottom = findViewById(R.id.buttonScrollBottom);
        scrollTop.animate().setDuration(500);
        scrollBottom.animate().setDuration(500);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrollTop.animate().alpha(0).setStartDelay(2500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            scrollTop.setVisibility(View.GONE);
                        }
                    }).start();
                    scrollBottom.animate().alpha(0).setStartDelay(2500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            scrollBottom.setVisibility(View.GONE);
                        }
                    }).start();
                } else {
                    scrollTop.setVisibility(View.VISIBLE);
                    scrollBottom.setVisibility(View.VISIBLE);
                    scrollTop.animate().alpha(1).setStartDelay(0).setListener(new AnimatorListenerAdapter() {
                    }).start();
                    scrollBottom.animate().alpha(1).setStartDelay(0).setListener(new AnimatorListenerAdapter() {
                    }).start();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MessagesActivity thisActivity = this;
        menu.add(R.string.go_to_date).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                fragment.setDateReceiver(thisActivity);
                fragment.show(getSupportFragmentManager(), "datePickerDialogFragment");
                return true;
            }
        });
        return true;
    }

    public void scrollTop(View v) {
        recyclerView.scrollToPosition(0);
    }

    public void scrollBottom(View v) {
        recyclerView.scrollToPosition(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() - 1);
    }

    @Override
    public void onReceiveDate(Date d) {
        Chat chat = DataStorage.chat.getValue();
        if (chat == null) return;
        int index = chat.getIndexForDate(d.getTime());
        recyclerView.scrollToPosition(index);
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