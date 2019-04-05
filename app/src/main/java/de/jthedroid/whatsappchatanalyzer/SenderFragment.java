package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class SenderFragment extends Fragment {

    private static final String NAME = "name", COUNT = "count", MAX_COUNT = "maxCount";
    private String name;
    private int count, maxCount;


    public static SenderFragment newInstance(String name, int count, int countMax) {
        SenderFragment sf = new SenderFragment();
        Bundle b = new Bundle();
        b.putString(NAME, name);
        b.putInt(COUNT, count);
        b.putInt(MAX_COUNT, countMax);
        sf.setArguments(b);
        return sf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            name = b.getString(NAME);
            count = b.getInt(COUNT);
            maxCount = b.getInt(MAX_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sender, container, false);
        ((TextView) v.findViewById(R.id.textViewName)).setText(name);
        ((TextView) v.findViewById(R.id.textViewCount)).setText(String.format(Locale.getDefault(),"%d",count));
        ProgressBar progressBar = v.findViewById(R.id.progressBarMsgCount);
        progressBar.setMax(maxCount);
        progressBar.setProgress(count);
        return v;
    }
}
