package de.jthedroid.whatsappchatanalyzer;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        final LoadingViewModel viewModel = android.arch.lifecycle.ViewModelProviders.of(this).get(LoadingViewModel.class);
        final Observer<ArrayList<String>> lineObserver = new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> s) {
                ((TextView) findViewById(R.id.textViewInfo)).setText(Integer.toString(s.size()));
            }
        };

        if (type != null) {
            TextView infoTextView = findViewById(R.id.textViewInfo);
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            String uriStr = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM).get(0).toString();
            Uri uri = Uri.parse(uriStr);
            Log.d("ShareActivity", uri.toString());

            viewModel.load(getContentResolver(), uri);
            infoTextView.setText(action + " " + type + "\nText:\n" + text + "\n" + uri + "\n");
            viewModel.line.observe(this, lineObserver);
        }
    }
}
