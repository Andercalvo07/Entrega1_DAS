package com.example.trainup;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.migym.R;
import com.example.migym.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Setup Bottom Navigation
        BottomNavigationView navView = binding.bottomNavView;
        NavigationUI.setupWithNavController(navView, navController);

        // Apply saved settings
        applySettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem darkModeItem = menu.findItem(R.id.action_dark_mode);
        if (darkModeItem != null) {
            darkModeItem.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_language) {
            showLanguageDialog();
            return true;
        } else if (id == R.id.action_dark_mode) {
            toggleDarkMode(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Español"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.language)
                .setItems(languages, (dialog, which) -> {
                    Locale locale;
                    switch (which) {
                        case 0:
                            locale = new Locale("en");
                            break;
                        case 1:
                            locale = new Locale("es");
                            break;
                        default:
                            locale = Locale.getDefault();
                            break;
                    }
                    updateLocale(locale);
                })
                .show();
    }

    private void toggleDarkMode(MenuItem item) {
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES)
                ? AppCompatDelegate.MODE_NIGHT_NO
                : AppCompatDelegate.MODE_NIGHT_YES;

        AppCompatDelegate.setDefaultNightMode(newMode);
        item.setChecked(newMode == AppCompatDelegate.MODE_NIGHT_YES);

        // Save the setting
        getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putInt("night_mode", newMode)
                .apply();
    }

    private void updateLocale(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration(getResources().getConfiguration());
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Save the setting
        getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString("language", locale.getLanguage())
                .apply();

        // Recreate the activity to apply changes
        recreate();
    }

    private void applySettings() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        // Apply night mode
        int nightMode = prefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(nightMode);

        // Apply language
        String language = prefs.getString("language", null);
        if (language != null) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);
            Configuration config = new Configuration(getResources().getConfiguration());
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }
}