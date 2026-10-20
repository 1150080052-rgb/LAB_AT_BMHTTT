
# LAB 7 – Web Protection với Sophos Firewall

Triển khai Web Protection bằng **Sophos Firewall (SFOS 22.0.2)** thay cho việc cài Squid trên pfSense.

## Mục tiêu

- Xây dựng chính sách truy cập website
- Phân loại / chặn / cảnh báo theo nhóm nội dung
- Giới hạn thời gian truy cập (quota)
- Quét virus khi tải file, giới hạn kích thước download
- Mở khóa tạm thời (policy override)
- HTTPS inspection

## Mô hình mạng

```
Win11 (172.16.16.17) ── VMnet2 ── [PortA  Sophos Firewall  PortB] ── NAT ── Internet
                                   172.16.16.16/24   192.168.250.130 (DHCP)
```

| Máy | Card mạng | IP |
|---|---|---|
| Sophos Firewall (SFOS 22.0.2) | PortA – LAN (VMnet2, Host-only) | 172.16.16.16/24 |
| | PortB – WAN (NAT) | 192.168.250.130 (DHCP) |
| Windows 11 (client) | VMnet2 | 172.16.16.17 (DHCP từ Sophos) |

Môi trường: VMware Workstation.

## Các bước đã thực hiện

1. **Mạng ảo VMware** – tạo VMnet2 (Host-only, 172.16.16.0/24, tắt DHCP của VMware); Sophos có 2 card: VMnet2 (LAN) và NAT (WAN); Win11 nối VMnet2.
2. **Kiểm tra IP** – PortB nhận IP WAN qua DHCP; Win11 nhận 172.16.16.17, gateway 172.16.16.16, ping gateway thành công.
3. **Đăng ký license** – claim firewall trên Sophos Central, nhận license dùng thử 30 ngày (Xstream Protection, gồm Web Protection).
4. **Setup Wizard**
   - Đặt mật khẩu admin, Secure storage master key
   - Hostname `LAB7-FW`, múi giờ GMT+7
   - LAN PortA route mode, bật DHCP (172.16.16.17–254)
   - Network protection: bật *lọc website độc hại* và *quét malware file tải về*
5. **Đăng nhập giao diện quản trị** `https://172.16.16.16:4444` – vào được Control center.

## Phần chưa hoàn thành (kế hoạch)

| Bước | Nội dung | Vị trí cấu hình |
|---|---|---|
| 1 | Firewall rule LAN→WAN, bật *Scan HTTP and decrypted HTTPS* | Rules and policies → Firewall rules |
| 2 | Web policy chặn / cảnh báo theo category | Web → Policies |
| 3 | Quota giới hạn thời gian | Web → Policies (action Quota) |
| 4 | Quét virus (test EICAR), giới hạn dung lượng download | Web → General settings |
| 5 | Mở khóa tạm thời | Web → Policy override |
| 6 | HTTPS inspection, cài CA Sophos lên client | Rules and policies → SSL/TLS inspection rules |

## Kết luận

Đã dựng xong hạ tầng: Sophos Firewall làm gateway cho mạng LAN, license Web Protection đã kích hoạt, client ra mạng qua firewall. Các chính sách Web Protection chi tiết sẽ cấu hình ở bước tiếp theo.
