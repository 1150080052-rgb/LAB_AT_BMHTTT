package week_03;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class frm_Menu extends JFrame {

    public frm_Menu() {
        super("LAB 3 - Mã hóa khóa công khai - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel p = new JPanel(new GridLayout(0, 1, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("LAB 3 - MÃ HÓA KHÓA CÔNG KHAI", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        p.add(title);
        p.add(new JLabel("Chọn một bài để mở form (bài 3.1 chạy RSADemo.java bằng Run File):", JLabel.CENTER));

        addButton(p, "3.2 RSA (Swing)", () -> new frm_RSA().setVisible(true));
        addButton(p, "3.3 ECC", () -> new frm_ECC().setVisible(true));
        addButton(p, "3.4 RSA + AES", () -> new frm_RSA_AES().setVisible(true));
        addButton(p, "3.5 Chữ ký ECDSA", () -> new frm_ECC_Signature().setVisible(true));
        addButton(p, "Kết nối SQL Server", () -> new frm_KetNoiSQL().setVisible(true));
        addButton(p, "Xem lịch sử", () -> new frm_LichSu().setVisible(true));

        add(p);
        pack();
        setSize(Math.max(getWidth(), 520), getHeight());
        setLocationRelativeTo(null);
    }

    private void addButton(JPanel p, String text, Runnable action) {
        JButton b = new JButton(text);
        b.addActionListener(e -> action.run());
        p.add(b);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_Menu().setVisible(true));
    }
}