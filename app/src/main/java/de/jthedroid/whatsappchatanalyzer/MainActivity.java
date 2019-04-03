package de.jthedroid.whatsappchatanalyzer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
        //i.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(i);
    }
}
