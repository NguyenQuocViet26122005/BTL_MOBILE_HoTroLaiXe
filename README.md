# BTL_MOBILE - Ứng Dụng Hỗ Trợ Học Lái Xe

## 📱 Giới thiệu đề tài

### Bài toán
Việc học và ôn tập câu hỏi thi bằng lái xe hiện nay chủ yếu dựa vào sách giáo trình truyền thống, gây khó khăn cho người học trong việc theo dõi tiến độ và ôn tập hiệu quả.

### Mục tiêu
Xây dựng ứng dụng Android hỗ trợ học viên:
- Ôn tập câu hỏi thi bằng lái xe hạng A (250 câu)
- Theo dõi tiến độ học tập theo từng chủ đề
- Lưu trữ câu hỏi sai, câu hỏi khó để ôn tập lại
- Thi thử với giao diện thân thiện, dễ sử dụng

---

## 📊 Dataset

### Nguồn dữ liệu
- Bộ câu hỏi thi bằng lái xe theo quy định của Bộ Giao thông Vận tải Việt Nam
- Tổng số: **250 câu hỏi** cho bằng lái hạng A

### Cấu trúc dữ liệu
| Chủ đề | Số câu | Mô tả |
|--------|--------|-------|
| Câu hỏi điểm liệt | 20 | Câu hỏi quan trọng, sai 1 câu là trượt |
| Khái niệm và quy tắc | 100 | Luật giao thông, quy tắc an toàn |
| Văn hóa và đạo đức | 10 | Ứng xử khi tham gia giao thông |
| Kỹ thuật lái xe | 15 | Kỹ năng điều khiển phương tiện |
| Biển báo đường bộ | 65 | Nhận biết và hiểu ý nghĩa biển báo |
| Sa hình | 35 | Xử lý tình huống thực tế |

### Link tải data
- Dữ liệu được tích hợp sẵn trong ứng dụng
- File JSON/SQLite trong thư mục `app/src/main/assets/` (sẽ cập nhật)

---

## 🔄 Pipeline

```
1. Thiết kế giao diện (XML Layout)
   ↓
2. Xây dựng cấu trúc dữ liệu câu hỏi
   ↓
3. Implement logic ôn tập và thi thử
   ↓
4. Theo dõi tiến độ học tập
   ↓
5. Lưu trữ kết quả (SharedPreferences/SQLite)
   ↓
6. Testing và tối ưu hóa
```

---

## 🏗️ Công nghệ sử dụng

### Platform & Language
- **Platform:** Android (API 24+)
- **Language:** Java
- **IDE:** Android Studio

### Architecture & Libraries
- **UI Framework:** Android XML Layout, Material Design Components
- **Navigation:** BottomNavigationView
- **Data Storage:** SharedPreferences / SQLite (sẽ implement)
- **Build System:** Gradle (Kotlin DSL)

### Lý do chọn
- Java: Ngôn ngữ phổ biến, tài liệu phong phú
- Material Design: Giao diện hiện đại, thân thiện
- SQLite: Lưu trữ offline, không cần internet

---

## 📈 Kết quả

### Tính năng đã hoàn thành
- ✅ Giao diện trang chủ với menu chức năng
- ✅ Hiển thị tiến độ ôn tập theo chủ đề
- ✅ Thanh điều hướng (Bottom Navigation)
- ✅ Responsive layout với NestedScrollView

### Tính năng đang phát triển
- 🔄 Chức năng thi thử
- 🔄 Lưu câu hỏi sai/khó
- 🔄 Xem biển báo đường bộ
- 🔄 Mẹo ghi nhớ câu hỏi

### Metrics (Dự kiến)
- Thời gian phản hồi: < 100ms
- Dung lượng ứng dụng: < 20MB
- Hỗ trợ: Android 7.0+ (API 24+)

---

## 🚀 Hướng dẫn chạy

### 1. Cài đặt môi trường

#### Yêu cầu hệ thống
- **Android Studio:** Hedgehog (2023.1.1) trở lên
- **JDK:** 17 trở lên
- **Android SDK:** API 24-34
- **Gradle:** 8.0+

#### Cài đặt Android Studio
1. Tải Android Studio: https://developer.android.com/studio
2. Cài đặt Android SDK và các tools cần thiết
3. Cấu hình JDK trong Android Studio

### 2. Clone và mở project

```bash
# Clone repository
git clone https://github.com/NguyenQuocViet26122005/BTL_MOBILE_HoTroLaiXe.git

# Di chuyển vào thư mục project
cd BTL_MOBILE_HoTroLaiXe

# Mở bằng Android Studio
# File -> Open -> Chọn thư mục BTL_MOBILE_HoTroLaiXe
```

### 3. Build và chạy ứng dụng

#### Chạy trên Emulator
1. Mở Android Studio
2. Tạo AVD (Android Virtual Device):
   - Tools -> Device Manager -> Create Device
   - Chọn Pixel 5 hoặc thiết bị tương tự
   - Chọn System Image: API 34 (Android 14)
3. Click nút **Run** (▶️) hoặc `Shift + F10`

#### Chạy trên thiết bị thật
1. Bật chế độ Developer Options trên điện thoại
2. Bật USB Debugging
3. Kết nối điện thoại với máy tính qua USB
4. Chọn thiết bị trong Android Studio và click **Run**

#### Build APK
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# APK output: app/build/outputs/apk/
```

### 4. Chạy demo

Xem thư mục `demo/` để:
- Xem screenshots giao diện
- Tải APK demo để cài đặt thử
- Xem video hướng dẫn sử dụng

---

## 📁 Cấu trúc thư mục dự án

```
BTL_MOBILE_HoTroLaiXe/
│
├── app/                          # Source code chính
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/btl_banglaixe/
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/
│   │   │   │   ├── layout/      # Giao diện XML
│   │   │   │   ├── drawable/    # Icons, backgrounds
│   │   │   │   ├── values/      # Colors, strings, themes
│   │   │   │   └── menu/        # Bottom navigation menu
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/         # Integration tests
│   │   └── test/                # Unit tests
│   ├── build.gradle.kts         # App-level Gradle config
│   └── proguard-rules.pro       # ProGuard rules
│
├── demo/                         # Demo và screenshots
│   └── README.md
│
├── data/                         # Dữ liệu câu hỏi
│   └── README.md
│
├── reports/                      # Báo cáo đồ án
│   └── README.md
│
├── slides/                       # Slide thuyết trình
│   └── README.md
│
├── gradle/                       # Gradle wrapper
│   └── wrapper/
│
├── build.gradle.kts             # Project-level Gradle config
├── settings.gradle.kts          # Gradle settings
├── gradle.properties            # Gradle properties
├── requirements.txt             # Dependencies info
├── .gitignore                   # Git ignore rules
└── README.md                    # File này
```

---

## 👥 Tác giả

| Họ và tên | Mã sinh viên | Lớp | Vai trò |
|-----------|--------------|-----|---------|
| [Tên SV 1] | [MSSV 1] | [Lớp] | Leader, Backend |
| [Tên SV 2] | [MSSV 2] | [Lớp] | UI/UX Designer |
| [Tên SV 3] | [MSSV 3] | [Lớp] | Developer |

**Giảng viên hướng dẫn:** [Tên giảng viên]

**Môn học:** Lập trình di động / Mobile Programming

**Học kỳ:** [HK] - Năm học [20XX-20XX]

---

## 📝 License

Dự án này được phát triển cho mục đích học tập tại [Tên trường].

---

## 📞 Liên hệ

- **GitHub:** https://github.com/NguyenQuocViet26122005/BTL_MOBILE_HoTroLaiXe
- **Email:** [email liên hệ]

---

**⭐ Nếu thấy project hữu ích, hãy cho chúng mình một star nhé!**
