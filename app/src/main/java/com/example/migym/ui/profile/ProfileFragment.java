package com.example.migym.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.example.migym.R;
import com.example.migym.databinding.FragmentProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SharedPreferences prefs;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        // Just show the selected image in this session
                        if (binding != null) {
                            binding.profileImage.setImageURI(result.getData().getData());
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        // Load saved data (defensive to avoid crashes)
        try {
            loadProfileData();
        } catch (Exception e) {
            // If anything goes wrong loading profile, ignore so app doesn't crash
        }
        
        // Set up change photo button
        binding.changePhotoButton.setOnClickListener(v -> openImagePicker());
        
        // Set up heart problems switch
        binding.heartProblemsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.heartProblemsDetailsLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });
        
        // Set up save button
        binding.saveButton.setOnClickListener(v -> saveProfileData());
        
        return binding.getRoot();
    }

    private void openImagePicker() {
        if (getContext() == null) return;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void loadProfileData() {
        if (binding == null || prefs == null) return;

        binding.nameInput.setText(prefs.getString("profile_name", ""));
        binding.ageInput.setText(prefs.getString("profile_age", ""));
        binding.weightInput.setText(prefs.getString("profile_weight", ""));
        binding.heightInput.setText(prefs.getString("profile_height", ""));
        
        // Load heart problems data
        boolean hasHeartProblems = prefs.getBoolean("profile_heart_problems", false);
        binding.heartProblemsSwitch.setChecked(hasHeartProblems);
        binding.heartProblemsDetailsLayout.setVisibility(hasHeartProblems ? View.VISIBLE : View.GONE);
        binding.heartProblemsDetailsInput.setText(prefs.getString("profile_heart_problems_details", ""));
        
        // No longer loading a persisted profile image to avoid crashes with invalid URIs
    }

    private void saveProfileData() {
        if (binding == null || prefs == null) return;

        String name = getTextFromInput(binding.nameInput);
        String age = getTextFromInput(binding.ageInput);
        String weight = getTextFromInput(binding.weightInput);
        String height = getTextFromInput(binding.heightInput);
        boolean hasHeartProblems = binding.heartProblemsSwitch.isChecked();
        String heartProblemsDetails = hasHeartProblems ? 
            getTextFromInput(binding.heartProblemsDetailsInput) : "";

        // Validate inputs
        if (name.isEmpty() || age.isEmpty() || weight.isEmpty() || height.isEmpty()) {
            Toast.makeText(requireContext(), R.string.profile_fill_required, Toast.LENGTH_SHORT).show();
            return;
        }

        if (hasHeartProblems && heartProblemsDetails.isEmpty()) {
            Toast.makeText(requireContext(), R.string.profile_heart_details_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences (synchronously to ensure persistence before exit)
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profile_name", name);
        editor.putString("profile_age", age);
        editor.putString("profile_weight", weight);
        editor.putString("profile_height", height);
        editor.putBoolean("profile_heart_problems", hasHeartProblems);
        editor.putString("profile_heart_problems_details", heartProblemsDetails);
        editor.commit();

        Toast.makeText(requireContext(), R.string.profile_saved, Toast.LENGTH_SHORT).show();
    }

    private String getTextFromInput(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 