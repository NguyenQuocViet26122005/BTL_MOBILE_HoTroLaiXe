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
        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        applyTheme();
        setContentView(R.layout.activity_settings);
        
        androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(
                androidx.core.view.WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupViews();
        loadSettings();
        setupListeners();
    }

    private void applyTheme() {
        int mode = prefs.getBoolean("dark_mode", false) ? 
            AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private void setupViews() {
        rgLicenseType = findViewById(R.id.rgLicenseType);
        switchDarkMode = findViewById(R.id.switchDarkMode);
    }

    private void loadSettings() {
        String licenseType = prefs.getString("license_type", "A1");
        rgLicenseType.check(licenseType.equals("A") ? R.id.rbA : R.id.rbA1);
        switchDarkMode.setChecked(prefs.getBoolean("dark_mode", false));
    }

    private void setupListeners() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        rgLicenseType.setOnCheckedChangeListener((group, checkedId) -> {
            String type = checkedId == R.id.rbA ? "A" : "A1";
            prefs.edit().putString("license_type", type).apply();
            ((android.widget.TextView) findViewById(R.id.tvCurrentLicense)).setText(type);
            Toast.makeText(this, "Đã chuyển sang hạng " + type, Toast.LENGTH_SHORT).show();
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? 
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, isChecked ? "Đã bật chế độ tối" : "Đã tắt chế độ tối", 
                Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnAbout).setOnClickListener(v -> showAboutDialog());
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
                    "• Xóa lịch sử trả lời\n• Xóa câu hỏi đã đánh dấu\n• Đặt lại cài đặt về mặc định\n\n" +
                    "Hành động này không thể hoàn tác.")
            .setPositiveButton("Đặt lại", (dialog, which) -> {
                com.example.btl_banglaixe.database.HistoryDAO historyDAO = 
                    new com.example.btl_banglaixe.database.HistoryDAO(this);
                historyDAO.clearHistory();
                historyDAO.close();
                
                android.database.sqlite.SQLiteDatabase db = 
                    new com.example.btl_banglaixe.database.DatabaseHelper(this).getWritableDatabase();
                db.delete("bookmarks", null, null);
                
                prefs.edit().clear()
                    .putString("license_type", "A1")
                    .putBoolean("dark_mode", false).apply();
                
                Toast.makeText(this, "Đã đặt lại tất cả về ban đầu", Toast.LENGTH_SHORT).show();
                recreate();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}
