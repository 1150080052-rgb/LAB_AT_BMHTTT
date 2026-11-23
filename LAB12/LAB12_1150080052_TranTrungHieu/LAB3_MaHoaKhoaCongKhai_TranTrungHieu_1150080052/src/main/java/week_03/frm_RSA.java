package week_03;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class frm_RSA extends JFrame {

    /** Đối tượng RSA giữ cặp khóa trong suốt thời gian form mở. */
    private final RSACipher rsa = new RSACipher();

    private final JComboBox<String> cbo_bits = new JComboBox<>(new String[] {"2048", "3072"});
    private final JButton btn_generate = new JButton("Generate Keys");
    private final JLabel lbl_keyinfo = new JLabel("Chưa có khóa.");
    private final JTextArea txt_plaintext = new JTextArea(2, 40);
    private final JTextArea txt_ciphertext = new JTextArea(5, 40);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JLabel lbl_status = new JLabel("Bấm Generate Keys để tạo cặp khóa.");

    public frm_RSA() {
        super("RSA Cipher - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("MẬT MÃ RSA (OAEP - SHA-256)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 4;
        add(title, g);

        g.gridwidth = 1; g.gridy = 1;
        g.gridx = 0; add(new JLabel("Key size:"), g);
        g.gridx = 1; add(cbo_bits, g);
        g.gridx = 2; add(btn_generate, g);
        g.gridx = 3; add(lbl_keyinfo, g);

        txt_plaintext.setLineWrap(true);
        txt_ciphertext.setLineWrap(true);
        g.fill = GridBagConstraints.BOTH;
        g.weightx = 1;

        g.gridy = 2; g.gridx = 0; g.weightx = 0; g.weighty = 0;
        add(new JLabel("Plaintext:"), g);
        g.gridx = 1; g.gridwidth = 3; g.weightx = 1; g.weighty = 0.3;
        add(new JScrollPane(txt_plaintext), g);

        g.gridy = 3; g.gridx = 0; g.gridwidth = 1; g.weightx = 0; g.weighty = 0;
        add(new JLabel("Ciphertext:"), g);
        g.gridx = 1; g.gridwidth = 3; g.weightx = 1; g.weighty = 0.7;
        add(new JScrollPane(txt_ciphertext), g);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        g.gridy = 4; g.gridx = 0; g.gridwidth = 4; g.weighty = 0; g.fill = GridBagConstraints.NONE;
        add(buttons, g);

        g.gridy = 5; g.fill = GridBagConstraints.HORIZONTAL;
        add(lbl_status, g);

        btn_generate.addActionListener(e -> generate());
        btn_encrypt.addActionListener(e -> encrypt());
        btn_decrypt.addActionListener(e -> decrypt());

        setSize(720, 480);
        setLocationRelativeTo(null);
    }

    private void generate() {
        int bits = Integer.parseInt((String) cbo_bits.getSelectedItem());
        try {
            long start = System.currentTimeMillis();
            rsa.generateKeys(bits);
            long ms = System.currentTimeMillis() - start;
            lbl_keyinfo.setText("RSA-" + bits + ": tạo khóa " + ms + " ms, tối đa "
                    + rsa.maxPlaintextBytes() + " byte");
            lbl_status.setText("Đã tạo cặp khóa. Khóa công khai dùng để mã hóa, khóa riêng để giải mã.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được khóa: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void encrypt() {
        String plaintext = txt_plaintext.getText();
        if (plaintext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Plaintext trước khi mã hóa.");
            return;
        }
        try {
            String ciphertext = rsa.encrypt(plaintext);
            txt_ciphertext.setText(ciphertext);
            txt_ciphertext.setCaretPosition(0);
            lbl_status.setText("Đã mã hóa RSA (" + ciphertext.length() + " ký tự Base64). "
                    + HistoryDao.tryInsert("RSA", "ENCRYPT", plaintext.length(), true));
        } catch (Exception ex) {
            HistoryDao.tryInsert("RSA", "ENCRYPT", plaintext.length(), false);
            JOptionPane.showMessageDialog(this, "Mã hóa thất bại: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decrypt() {
        String ciphertext = txt_ciphertext.getText();
        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hãy nhập Ciphertext trước khi giải mã.");
            return;
        }
        try {
            String plaintext = rsa.decrypt(ciphertext);
            txt_plaintext.setText(plaintext);
            txt_plaintext.setCaretPosition(0);
            lbl_status.setText("Đã giải mã RSA. "
                    + HistoryDao.tryInsert("RSA", "DECRYPT", ciphertext.length(), true));
        } catch (Exception ex) {
            HistoryDao.tryInsert("RSA", "DECRYPT", ciphertext.length(), false);
            lbl_status.setText("Giải mã RSA THẤT BẠI (sai khóa hoặc bản mã bị sửa).");
            JOptionPane.showMessageDialog(this,
                    "Giải mã thất bại: sai khóa riêng hoặc bản mã bị sửa.\n" + ex,
                    "Giải mã thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_RSA().setVisible(true));
    }
}