package com.example.wowtime.ui.others;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.wowtime.MainApplication;
import com.example.wowtime.R;

public class SettingsActivity extends AppCompatActivity {

    Integer themeNumber;
    SharedPreferences themeSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeSp = super.getSharedPreferences("theme", MODE_PRIVATE);
        Intent intent = getIntent();
        if (intent != null) { themeNumber = intent.getIntExtra("theme", -1); }
        if (themeNumber == -1) { themeNumber = themeSp.getInt("theme", 0); } else {
            MainApplication.setThemeNumber(themeNumber);
            SharedPreferences.Editor editor = themeSp.edit();
            editor.putInt("theme", themeNumber);
            editor.apply();
        }
        if (themeNumber == 1) { setTheme(R.style.DarkTheme); } else { setTheme(R.style.AppTheme); }
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference blowSettingButton = findPreference("game_setting_button");
            if (blowSettingButton != null) {
                blowSettingButton.setOnPreferenceClickListener(preference -> {
                    Intent intent = new Intent(getActivity(), GameSettingsActivity.class);
                    startActivity(intent);
                    return true;
                });
            }
            Preference themeChangeButton = findPreference("theme");
            assert themeChangeButton != null;
            themeChangeButton.setOnPreferenceChangeListener((preference, newValue) -> {
                Integer theme = 0;
                if (newValue.toString().equals("true")) {
                    assert getActivity() != null;
                    theme = 1;
                }
                Toast toast = Toast
                        .makeText(getActivity(), newValue.toString(), Toast.LENGTH_SHORT);
                toast.show();
                startActivity(
                        new Intent(getActivity(), SettingsActivity.class).putExtra("theme", theme));
                getActivity().finish();
                return true;
            });
        }
    }

}