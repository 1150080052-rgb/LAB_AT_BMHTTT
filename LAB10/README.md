
# LAB 10 - Remote DNS Cache Poisoning — Tiến độ hiện tại

## Đã làm xong

### 1. Snapshot
- Đã tạo snapshot `LAB10_START_FROM_LAB9` cho pfSense và Kali trước khi đổi mạng.

### 2. pfSense — Thêm ATTACKNET
- Thêm interface mới, Description = ATTACKNET, Static IPv4 `10.10.0.1/24`, không gán Upstream Gateway.
- Apply Changes thành công. **Có ảnh minh chứng.**

### 3. Kali — Chuyển sang ATTACKNET
- Đổi card mạng từ `dns-lab` sang `attack-net`.
- Gán IP tĩnh `10.10.0.200/24`, gateway `10.10.0.1` (qua nmcli, interface eth2).

### 4. Firewall — Cập nhật ruleset DNSLAB + thêm ATTACKNET
- DNSLAB: cập nhật đủ 7 rule (Victim↔resolver, resolver↔AUTH-DNS thật, resolver↔NS giả sau poison, Victim xác nhận web redirect, 3 rule Block cô lập).
- Đã Apply Changes. **Có ảnh minh chứng ruleset.**
- ATTACKNET: **chưa tạo 5 rule** (Pass attacker→resolver, Pass attacker→AUTH-DNS UDP 5300, 3 rule Block) — còn thiếu.

### 5. DNS-SERVER-LAB10
- Clone từ Ubuntu-24.04-Base, mật khẩu `Ubuntu@1001`.
- Gán IP tĩnh `10.9.0.10/24`, gateway `10.9.0.1`, card `dns-lab`.
- Cài + cấu hình BIND9 (named.conf.options + named.conf.local, forward zone `example.lab10.test` → `172.16.6.20`).
- `named-checkconf` pass, `systemctl status named` = active (running). **Có ảnh minh chứng.**

### 6. VICTIM-LAB10
- Clone từ Ubuntu-24.04-Base, mật khẩu `Ubuntu@1001`.
- Gán IP tĩnh `10.9.0.100/24`, gateway `10.9.0.1`, card `dns-lab`.

### 7. Đã có sẵn để nộp tạm
- 12 câu trả lời mục K (câu hỏi phân tích) — đã soạn xong, gửi trong chat.
- Khung báo cáo Word (phần 1 môi trường + phần 8 câu hỏi có nội dung thật; phần 2–7 đánh dấu "chưa thực hiện").

## Chưa làm (còn thiếu để hoàn chỉnh LAB10)

1. Tạo 5 rule firewall cho ATTACKNET (chưa làm).
2. Dựng/cấu hình AUTH-DNS relay (`auth_dns_relay_lab10.py`) trên APP-TEST (172.16.6.20).
3. Baseline: test DNS resolve từ VICTIM qua resolver → AUTH-DNS thật.
4. Xác nhận Kali (ATTACKNET) không sniff được traffic DNSLAB (chụp Wireshark).
5. Chạy `vuln_resolver_lab10.py` trên DNS-SERVER (tắt BIND9 trước).
6. Task 1: chạy `kaminsky_remote_lab10.py` trên Kali, xem log PENDING/HIT/CACHE, xác nhận VICTIM bị redirect tới `10.10.0.200`.
7. Chụp packet capture (tcpdump/Wireshark) trên DNS-SERVER hoặc APP-TEST theo bảng yêu cầu mục G.3.
8. Task 2: bảng so sánh trước/sau cache + trả lời câu hỏi bailiwick/glue.
9. Khôi phục BIND9, kiểm tra lại source-port randomization, tắt rule UDP/5300 sau khi xong.

## Việc cần làm tiếp (theo thứ tự)
1. Thêm 5 rule ATTACKNET trên pfSense.
2. Dựng AUTH-DNS relay trên 172.16.6.20, test baseline DNS.
3. Xác nhận cô lập Kali khỏi DNSLAB (ảnh Wireshark).
4. Chạy resolver dễ tổn thương + chạy attack script Task 1, thu log + packet capture.
5. Hoàn thành Task 2 (bảng so sánh cache).
6. Khôi phục + hardening, tắt rule race relay.
