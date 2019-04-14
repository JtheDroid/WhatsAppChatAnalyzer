package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ButtonFragment extends Fragment {
    private static final String TEXT = "text", KEY_RUNNABLE = "runnableKey";
    private String text, runnableKey;

    public ButtonFragment() {
    }

    public static ButtonFragment newInstance(String text, String runnableKey) {
        ButtonFragment fragment = new ButtonFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        args.putString(KEY_RUNNABLE, runnableKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            runnableKey = getArguments().getString(KEY_RUNNABLE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_button, container, false);
        Button b = v.findViewById(R.id.buttonFragment);
        b.setText(text);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStorage.getInstance().runRunnable(runnableKey);
            }
        });
        return v;
    }
}
