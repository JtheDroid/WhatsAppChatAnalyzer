package de.jthedroid.whatsappchatanalyzer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ThemeMenuActivity extends AppCompatActivity {
    private boolean darkTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        darkTheme = sharedPreferences.getBoolean(getString(R.string.preference_key_theme), false);
        setTheme(darkTheme ? R.style.AppThemeDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(darkTheme ? R.string.switch_theme_light : R.string.switch_theme_dark).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String themeKey = getString(R.string.preference_key_theme);
                darkTheme = sharedPreferences.getBoolean(themeKey, false);
                menuItem.setTitle(darkTheme ? R.string.switch_theme_light : R.string.switch_theme_dark);
                sharedPreferences.edit().putBoolean(themeKey, !darkTheme).apply();
                recreate();
                return true;
            }
        });
        return true;
    }
}
