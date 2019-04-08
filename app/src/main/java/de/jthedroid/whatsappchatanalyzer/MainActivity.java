package de.jthedroid.whatsappchatanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager manager = getPackageManager();
        intent = manager.getLaunchIntentForPackage("com.whatsapp");
        findViewById(R.id.buttonOpenWhatsApp).setEnabled(intent != null);
    }

    public void openWhatsAppButtonPressed(View v) {
        startActivity(intent);
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
