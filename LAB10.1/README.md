
# LAB 1 - Mã hóa đối xứng cổ điển

Sinh viên: Trần Trung Hiếu - MSSV 1150080052 - Lớp CNPM1

Chương trình Java Swing cài đặt 6 thuật toán mã hóa đối xứng cổ điển, kèm form tổng hợp có lưu lịch sử thao tác vào Microsoft SQL Server.

## Công nghệ

- Java (biên dịch với `maven.compiler.release=17`)
- Apache Maven, Apache NetBeans (GUI Builder: `.java` đi kèm `.form`)
- Java Swing (Nimbus look and feel)
- Microsoft SQL Server Express (instance `SQLEXPRESS`, cổng TCP `14333`)
- Microsoft JDBC Driver `mssql-jdbc`

## Cấu trúc thư mục

```
LAB1_TuLam/
├── pom.xml
├── .gitignore
├── src/main/java/week_01/
│   ├── CaesarCipher.java, VigenereCipher.java, RailFenceCipher.java
│   ├── PlayfairCipher.java, TranspositionCipher.java, AutokeyCipher.java
│   ├── CipherTongHop.java        gọi đúng thuật toán theo tên chọn trên JComboBox
│   ├── DbConfig.java             giữ thông tin kết nối SQL Server (trong bộ nhớ)
│   ├── HistoryDao.java           ghi và đọc bảng dbo.history
│   ├── FileTextIO.java           mở và lưu tệp văn bản
│   ├── frm_Caesar, frm_Vigenere, frm_RailFence,
│   │   frm_Playfair, frm_Transposition, frm_Autokey   (mỗi form: .java + .form)
│   └── frm_TongHop, frm_KetNoiSQL, frm_LichSu           (mỗi form: .java + .form)
├── sql/
│   ├── 01_Tao_CSDL.sql           tạo database LAB1_MaHoaCoDien và bảng dbo.history
│   └── 03_Kiem_Tra_Du_Lieu.sql   truy vấn kiểm tra dữ liệu lịch sử
└── BaoCao/                       báo cáo Word
```

Tệp tạo tài khoản đăng nhập SQL Server (`02_Tao_Login_MAU.sql`) chứa mật khẩu nên **không** đưa vào kho mã nguồn.

## Chạy chương trình

1. Mở thư mục `LAB1_TuLam` bằng NetBeans (File > Open Project).
2. Clean and Build (Shift+F11), kết quả phải là `BUILD SUCCESS`.
3. Chạy từng form: nhấp phải tệp `frm_*.java` > Run File (Shift+F6).

Lưu ý: `pom.xml` có thể khai báo `exec.mainClass=week_01.frm_Menu`. Nếu form menu chưa có thì Run Project (F6) sẽ báo lỗi không tìm thấy lớp chính; khi đó dùng Run File.

## Thiết lập SQL Server (chỉ cần cho `frm_TongHop` lưu lịch sử)

1. Cài SQL Server Express và SSMS. Bật TCP/IP, đặt `IPAll > TCP Port = 14333`, khởi động lại dịch vụ.
2. Bật chế độ đăng nhập SQL Server and Windows Authentication, khởi động lại server.
3. Trong SSMS chạy `sql/01_Tao_CSDL.sql`.
4. Tạo tài khoản `lab1_user` chỉ có quyền SELECT, INSERT, DELETE trên `dbo.history` (tệp script này giữ cục bộ, không nộp).
5. Trong chương trình: `frm_TongHop` > Kết nối SQL Server > nhập mật khẩu của `lab1_user` > Test Connection. Mật khẩu chỉ nằm trong bộ nhớ khi chương trình chạy.

Thông số mặc định trên form kết nối: Server `127.0.0.1`, Port `14333`, Database `LAB1_MaHoaCoDien`, User `lab1_user`.

Cấu hình kết nối chỉ tồn tại trong lần chạy hiện tại, vì vậy mở `frm_LichSu` từ `frm_TongHop` hoặc `frm_KetNoiSQL` sau khi Test Connection thành công.

## Dữ liệu kiểm thử

| Thuật toán | Plaintext | Key | Ciphertext |
|---|---|---|---|
| Caesar | HELLO | 3 | KHOOR |
| Vigenère | HELLO | KEY | RIJVS |
| Rail Fence | HELLO WORLD | 3 | HOREL OLLWD |
| Playfair | HELLOWORLD | KEYWORD | GYIZSCOKCFBU |
| Transposition | BAOMAT | 2,3,1 | AAOTBM |
| Autokey | HELLO | KEY | RIJSS |

Giải mã Playfair của `GYIZSCOKCFBU` cho `HELXLOWORLDX` (có ký tự đệm X, đúng theo thuật toán).

Kiểm thử khóa sai:

| Thuật toán | Khóa sai | Thông báo |
|---|---|---|
| Rail Fence | 1 | Số hàng phải từ 2 đến 1000. |
| Playfair | KEY WORD | Khóa Playfair chỉ gồm A-Z. |
| Transposition | 1,1,2 | Khóa phải là hoán vị 1..m. |
| Autokey | 123 | Khóa Autokey chỉ gồm A-Z. |

## Ghi chú

- Câu lệnh SQL dùng `PreparedStatement` (tham số `?`).
- Bảng `dbo.history` có ràng buộc `CHECK` cho cột `operation` (`ENCRYPT` hoặc `DECRYPT`).
- Bài nâng cao 02 (phân tích mật mã) và form menu chính: chưa có trong phiên bản này.
