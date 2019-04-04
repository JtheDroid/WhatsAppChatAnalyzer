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

public class SenderFragment extends Fragment {


    public static SenderFragment newInstance() {
        return new SenderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sender_fragment, container, false);
        if (getArguments() != null) {
            Bundle b = getArguments();
            ((TextView) v.findViewById(R.id.textView1)).setText(b.getString("name"));
            ProgressBar progressBar = v.findViewById(R.id.progressBarMsgCount);
            progressBar.setMax(b.getInt("maxCount"));
            progressBar.setProgress(b.getInt("count"));
        }
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

}
