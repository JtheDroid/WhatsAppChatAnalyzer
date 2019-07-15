package de.jthedroid.whatsappchatanalyzer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;


public class HeadingFragment extends Fragment {

    private static final String TEXT = "text", SIZE = "size";
    private String text;
    private float size = Float.NaN;

    public HeadingFragment() {
    }

    public static HeadingFragment newInstance(String text) {
        HeadingFragment fragment = new HeadingFragment();
        Bundle args = new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    public static HeadingFragment newInstance(String text, float size) {
        HeadingFragment hf = newInstance(text);
        Objects.requireNonNull(hf.getArguments()).putFloat(SIZE, size);
        return hf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            text = getArguments().getString(TEXT);
            if (getArguments().containsKey(SIZE)) {
                size = getArguments().getFloat(SIZE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_heading, container, false);
        ((TextView) v.findViewById(R.id.textViewHeading)).setText(text);
        if (!Float.isNaN(size)) {
            ((TextView) v.findViewById(R.id.textViewHeading)).setTextSize(size);
        }
        return v;
    }
}
