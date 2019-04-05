package de.jthedroid.whatsappchatanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openWhatsAppButtonPressed(View v) {
        PackageManager manager = getPackageManager();

        Intent i = manager.getLaunchIntentForPackage("com.whatsapp");
        if (i == null) {
            Toast.makeText(this, R.string.whatsapp_open_error, Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(i);
    }


    public void selectFile(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Uri uri = resultData.getData();
                Intent sendIntent = new Intent(this, ShareActivity.class);
                sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "WhatsApp Chat");
                ArrayList<Uri> uriList = new ArrayList<>();
                uriList.add(uri);
                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        }
    }
}
