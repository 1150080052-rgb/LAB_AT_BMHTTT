
# LAB 3 - Mã hóa khóa công khai (RSA, ECC, RSA+AES, ECDSA)

- Họ tên: Trần Trung Hiếu
- MSSV: 1150080052
- Môn: An toàn và bảo mật thông tin

## Nội dung
| Bài | Nội dung | File chính (package `week_03`) |
|---|---|---|
| 3.1 | RSA-OAEP 2048/3072 bit, chạy trên Output | `RSACipher.java`, `RSADemo.java` |
| 3.2 | RSA giao diện Swing | `frm_RSA.java` |
| 3.3 | ECC secp256r1 (ECDH + HKDF + AES-GCM), Save/Load Keys | `ECCCipher.java`, `frm_ECC.java` |
| 3.4 | Hệ mã lai RSA + AES-GCM, văn bản > 1000 ký tự | `RSA_AES_Cipher.java`, `frm_RSA_AES.java` |
| 3.5 | Chữ ký số ECDSA (VALID / INVALID) | `ECCSignature.java`, `frm_ECC_Signature.java` |
| Chung | Menu, kết nối SQL Server, lịch sử | `frm_Menu.java`, `frm_KetNoiSQL.java`, `frm_LichSu.java`, `DbConfig.java`, `HistoryDao.java`, `CryptoSupport.java`, `FileTextIO.java` |

## Cấu trúc thư mục
- `LAB3_MaHoaKhoaCongKhai_TranTrungHieu_1150080052/`: mã nguồn Maven (NetBeans), có `pom.xml`.
- `sql/`: `LAB3_MaHoaKhoaCongKhai.sql` (tạo database, bảng `dbo.History`, tài khoản `lab3_user`), `03_KiemTra_LichSu.sql` (kiểm tra lịch sử).
- `ANH/`: hình ảnh kết quả các bài.
- Báo cáo Word: `Lab12_CNPM1_1150080052_TranTrungHieu.docx`.
- Video quay bài làm: https://youtu.be/cZY25j6kC8Y

## Cách chạy
1. Cần JDK 17 trở lên, NetBeans (Maven), SQL Server (bật TCP/IP cổng 14333 và Mixed Mode).
2. Mở file `sql/LAB3_MaHoaKhoaCongKhai.sql` trong SSMS. Trong file, thay `<DOI_MAT_KHAU_THEO_DE_BAI>` bằng mật khẩu `lab3_user` theo đề bài rồi Execute.
3. Mở thư mục mã nguồn bằng NetBeans, Clean and Build.
4. Bài 3.1: chuột phải `RSADemo.java` -> Run File, nhập 2048 hoặc 3072 và bản rõ `HELLO RSA`.
5. Các bài còn lại: Run Project (F6) mở `frm_Menu`. Bấm "Kết nối SQL Server", nhập password `lab3_user`, Test Connection, rồi mở từng bài. Lịch sử xem ở nút "Xem lịch sử".

## Lưu ý
- Mật khẩu không nằm trong mã nguồn hay tệp SQL; chỉ nhập trên form khi chạy.
- Các khóa hiện trong ảnh minh họa là khóa thử nghiệm, tạo ngẫu nhiên khi chạy. Không có khóa riêng thật trong repository.
