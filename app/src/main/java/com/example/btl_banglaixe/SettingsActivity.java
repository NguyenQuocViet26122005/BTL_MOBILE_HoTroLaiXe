package com.example.btl_banglaixe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private RadioGroup rgLicenseType;
    private SwitchCompat switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply theme before setContentView
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        applyTheme();
        
        setContentView(R.layout.activity_settings);

        // Xử lý system bars
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main),
            (v, insets) -> {
                androidx.core.graphics.Insets systemBars = insets.getInsets(
                    androidx.core.view.WindowInsetsCompat.Type.systemBars()
                );
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            }
        );

        setupViews();
        loadSettings();
        setupListeners();
    }

    private void applyTheme() {
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setupViews() {
        rgLicenseType = findViewById(R.id.rgLicenseType);
        switchDarkMode = findViewById(R.id.switchDarkMode);
    }

    private void loadSettings() {
        // Load license type
        String licenseType = prefs.getString("license_type", "A1");
        if (licenseType.equals("A")) {
            rgLicenseType.check(R.id.rbA);
        } else {
            rgLicenseType.check(R.id.rbA1);
        }

        // Load dark mode
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);
    }

    private void setupListeners() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // License type change
        rgLicenseType.setOnCheckedChangeListener((group, checkedId) -> {
            String licenseType = checkedId == R.id.rbA ? "A" : "A1";
            prefs.edit().putString("license_type", licenseType).apply();
            
            android.widget.TextView tvCurrent = findViewById(R.id.tvCurrentLicense);
            tvCurrent.setText(licenseType);
            
            Toast.makeText(this, "Đã chuyển sang hạng " + licenseType, Toast.LENGTH_SHORT).show();
        });

        // Dark mode switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            
            // Apply theme immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            
            Toast.makeText(this, 
                isChecked ? "Đã bật chế độ tối" : "Đã tắt chế độ tối", 
                Toast.LENGTH_SHORT).show();
        });

        // About button
        findViewById(R.id.btnAbout).setOnClickListener(v -> showAboutDialog());

        // Reset progress button
        findViewById(R.id.btnResetProgress).setOnClickListener(v -> showResetDialog());
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Về ứng dụng")
            .setMessage("Ứng dụng Thi Bằng Lái Xe\n\n" +
                    "Phiên bản: 1.0.0\n\n" +
                    "Ứng dụng hỗ trợ học và thi thử bằng lái xe hạng A và A1.\n\n" +
                    "© 2026 - Nguyễn Quốc Việt")
            .setPositiveButton("Đóng", null)
            .show();
    }

    private void showResetDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đặt lại tiến độ")
            .setMessage("Bạn có chắc chắn muốn đặt lại toàn bộ?\n\n" +
                    "• Xóa lịch sử trả lời\n" +
                    "• Xóa câu hỏi đã đánh dấu\n" +
                    "• Đặt lại cài đặt về mặc định\n\n" +
                    "Hành động này không thể hoàn tác.")
            .setPositiveButton("Đặt lại", (dialog, which) -> {
                // Xóa lịch sử
                com.example.btl_banglaixe.database.HistoryDAO historyDAO = 
                    new com.example.btl_banglaixe.database.HistoryDAO(this);
                historyDAO.clearHistory();
                historyDAO.close();
                
                // Xóa bookmarks
                android.database.sqlite.SQLiteDatabase db = 
                    new com.example.btl_banglaixe.database.DatabaseHelper(this).getWritableDatabase();
                db.delete("bookmarks", null, null);
                db.close();
                
                // Reset cài đặt
                prefs.edit()
                    .clear()
                    .putString("license_type", "A1")
                    .putBoolean("dark_mode", false)
                    .apply();
                
                Toast.makeText(this, "Đã đặt lại tất cả về ban đầu", Toast.LENGTH_SHORT).show();
                
                // Restart activity để áp dụng theme mặc định
                recreate();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
