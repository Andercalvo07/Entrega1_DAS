package com.example.migym.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.example.migym.R;
import com.example.migym.databinding.FragmentSettingsBinding;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SharedPreferences prefs;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        setupLanguageSpinner();
        setupNotificationsSwitch();
        setup24HourFormatSwitch();
        setupExportButton();

        return binding.getRoot();
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.languageSpinner.setAdapter(adapter);

        String currentLanguage = prefs.getString("language", "en");
        int position = currentLanguage.equals("en") ? 1 : 0;
        binding.languageSpinner.setSelection(position);

        binding.languageSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = position == 1 ? "en" : "es";
                if (!selectedLanguage.equals(prefs.getString("language", "en"))) {
                    prefs.edit().putString("language", selectedLanguage).apply();
                    updateLocale(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });
    }

    private void updateLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(config, requireContext().getResources().getDisplayMetrics());
        Toast.makeText(requireContext(), R.string.language_changed, Toast.LENGTH_SHORT).show();
        handler.postDelayed(() -> {
            if (isAdded()) {
                requireActivity().recreate();
            }
        }, 500);
    }

    private void setupNotificationsSwitch() {
        boolean notificationsEnabled = prefs.getBoolean("notifications_enabled", true);
        binding.notificationsSwitch.setChecked(notificationsEnabled);

        binding.notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply();
            Toast.makeText(requireContext(), 
                isChecked ? R.string.notifications_enabled : R.string.notifications_disabled, 
                Toast.LENGTH_SHORT).show();
        });
    }

    private void setup24HourFormatSwitch() {
        boolean is24HourFormat = prefs.getBoolean("24h_format", true);
        binding.format24hSwitch.setChecked(is24HourFormat);

        binding.format24hSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("24h_format", isChecked).apply();
            handler.postDelayed(() -> {
                if (isAdded()) {
                    requireActivity().recreate();
                }
            }, 500);
        });
    }

    private void setupExportButton() {
        binding.exportSettingsButton.setOnClickListener(v -> {
            StringBuilder content = new StringBuilder();
            content.append(getString(R.string.settings_language))
                    .append(": ")
                    .append(prefs.getString("language", "en"))
                    .append("\n");

            content.append(getString(R.string.enable_notifications))
                    .append(": ")
                    .append(prefs.getBoolean("notifications_enabled", true))
                    .append("\n");

            content.append(getString(R.string.pref_24h_format))
                    .append(": ")
                    .append(prefs.getBoolean("24h_format", true))
                    .append("\n");

            String filename = "migym_settings_summary.txt";
            try (FileOutputStream fos = requireContext().openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(content.toString().getBytes(StandardCharsets.UTF_8));
                Toast.makeText(requireContext(),
                        getString(R.string.settings_export_success, filename),
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(requireContext(),
                        R.string.settings_export_error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
        binding = null;
    }
} 