
# LAB 8 - Local DNS Attack — Tiến độ hiện tại

## Đã làm xong

### 1. pfSense — Dựng mạng DNSLAB
- Thêm Network Adapter 5 trên pfSense (VMware LAN Segment `dns-lab`).
- Gán interface `em4` → đặt tên **DNSLAB**, IP tĩnh `10.9.0.1/24`, không gán Upstream Gateway.
- Tạo đủ 5 firewall rule trên tab DNSLAB:
  1. Pass: `10.9.0.10` → `172.16.6.20` UDP/TCP 53 (resolver → auth-DNS)
  2. Pass: `10.9.0.0/24` → `172.16.6.10` TCP 80 (victim → web DMZ)
  3. Block+Log: `10.9.0.0/24` → `10.6.0.0/24` (bảo vệ LAN-DOMAIN)
  4. Block+Log: `10.9.0.0/24` → `192.168.56.0/24` (bảo vệ MGMT)
  5. Block+Log: `10.9.0.0/24` → Any (cô lập mặc định)
- Đã Apply Changes. **Có ảnh minh chứng đầy đủ** (Hình 1–3).

### 2. Kali — Gán card DNSLAB
- Thêm Network Adapter mới, gán LAN Segment `dns-lab`, giữ nguyên card NAT cũ.

### 3. VM Ubuntu gốc (Ubuntu-24.04-Base)
- Cài xong Ubuntu Server 24.04.3 LTS (NAT tạm để cài gói).
- `sudo apt update && sudo apt upgrade -y` — xong.
- Cài sẵn: `bind9`, `bind9-utils`, `dnsutils`, `curl`, `net-tools`, `python3`, `python3-pip`.
- VM này **chưa clone** ra 3 máy DNS-SERVER / VICTIM / INFRA và **chưa gán card dns-lab**.

## Chưa làm

- Clone VM gốc → DNS-SERVER (10.9.0.10), VICTIM (10.9.0.100), INFRA/AUTH-DNS (172.16.6.20) + DMZ-WEB (172.16.6.10).
- Cấu hình BIND9 (named.conf, zone forward/reverse) trên DNS-SERVER.
- Cấu hình Netplan IP tĩnh cho VICTIM, Kali (10.9.0.200).
- Baseline: `www.external.lab9.test` → `172.16.6.10`.
- Task 1 (hosts file redirect), Task 2 (direct DNS spoofing), Task 3 (cache poisoning) — chưa chạy, chưa có ảnh Wireshark.
- Khôi phục môi trường + trả Promiscuous Mode về Deny.

