package week_03;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class frm_ECC_Signature extends JFrame {

    private final ECCSignature ecdsa = new ECCSignature();

    private final JTextArea txt_public = new JTextArea(2, 40);
    private final JTextArea txt_private = new JTextArea(2, 40);
    private final JButton btn_generate = new JButton("Generate Keys");
    private final JButton btn_savekeys = new JButton("Save Keys");
    private final JButton btn_loadkeys = new JButton("Load Keys");
    private final JTextArea txt_message = new JTextArea(2, 40);
    private final JTextArea txt_signature = new JTextArea(2, 40);
    private final JButton btn_sign = new JButton("Sign");
    private final JButton btn_verify = new JButton("Verify");
    private final JLabel lbl_result = new JLabel(" ");
    private final JLabel lbl_status = new JLabel("Bấm Generate Keys hoặc Load Keys.");

    public frm_ECC_Signature() {
        super("ECDSA Signature - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        txt_public.setLineWrap(true);
        txt_public.setEditable(false);
        txt_private.setLineWrap(true);
        txt_private.setEditable(false);
        txt_message.setLineWrap(true);
        txt_signature.setLineWrap(true);
        lbl_result.setFont(new Font("Segoe UI", Font.BOLD, 20));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("CHỮ KÝ SỐ ECDSA (SHA256withECDSA)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(title, g);

        addRow(g, 1, "Public key:", txt_public, 0.2);
        addRow(g, 2, "Private key:", txt_private, 0.2);

        JPanel keyButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        keyButtons.add(btn_generate);
        keyButtons.add(btn_savekeys);
        keyButtons.add(btn_loadkeys);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2; g.weighty = 0; g.fill = GridBagConstraints.NONE;
        add(keyButtons, g);

        addRow(g, 4, "Message:", txt_message, 0.3);
        addRow(g, 5, "Signature:", txt_signature, 0.3);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.add(btn_sign);
        buttons.add(btn_verify);
        buttons.add(lbl_result);
        g.gridy = 6; g.gridx = 0; g.gridwidth = 2; g.weighty = 0; g.fill = GridBagConstraints.NONE;
        add(buttons, g);

        g.gridy = 7; g.fill = GridBagConstraints.HORIZONTAL;
        add(lbl_status, g);

        btn_generate.addActionListener(e -> generate());
        btn_savekeys.addActionListener(e -> saveKeys());
        btn_loadkeys.addActionListener(e -> loadKeys());
        btn_sign.addActionListener(e -> sign());
        btn_verify.addActionListener(e -> verify());

        setSize(760, 580);
        setLocationRelativeTo(null);
    }

    private void addRow(GridBagConstraints g, int row, String label, JTextArea area, double weightY) {
        g.gridy = row; g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.gridx = 0; g.weightx = 0; g.weighty = 0;
        add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1; g.weighty = weightY; g.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(area), g);
    }

    /** Hiện khóa công khai / khóa riêng (Base64) lên form. */
    private void showKeys() {
        txt_public.setText(ecdsa.getPublicKeyBase64());
        txt_private.setText(ecdsa.getPrivateKeyBase64());
        txt_public.setCaretPosition(0);
        txt_private.setCaretPosition(0);
    }

    private void generate() {
        try {
            ecdsa.generateKeys();
            showKeys();
            lbl_status.setText("Đã tạo cặp khóa mới. Bản mã/chữ ký cũ sẽ không dùng được với khóa mới.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tạo được khóa: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveKeys() {
        if (txt_public.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có khóa để lưu.");
            return;
        }
        String content = CryptoSupport.toKeyFile(txt_public.getText(), txt_private.getText());
        String path = FileTextIO.saveString(this, content, "ecdsa_keys_test.txt");
        if (path != null) {
            lbl_status.setText("Đã lưu khóa vào: " + path + " (giữ bí mật dòng PRIVATE).");
        }
    }

    private void loadKeys() {
        String content = FileTextIO.openString(this);
        if (content == null) {
            return;
        }
        try {
            String[] keys = CryptoSupport.fromKeyFile(content);
            ecdsa.loadKeys(keys[0], keys[1]);
            txt_public.setText(keys[0]);
            txt_private.setText(keys[1]);
            lbl_status.setText("Đã nạp khóa từ tệp.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Tệp khóa không hợp lệ: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sign() {
        String message = txt_message.getText();
        try {
            txt_signature.setText(ecdsa.sign(message));
            lbl_result.setText(" ");
            lbl_status.setText("Đã ký bằng KHÓA RIÊNG. "
                    + HistoryDao.tryInsert("ECDSA", "SIGN", message.length(), true));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ký thất bại: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verify() {
        String message = txt_message.getText();
        try {
            boolean ok = ecdsa.verify(message, txt_signature.getText());
            lbl_result.setText(ok ? "VALID" : "INVALID");
            lbl_result.setForeground(ok ? new Color(0, 128, 0) : Color.RED);
            lbl_status.setText("Đã xác minh bằng KHÓA CÔNG KHAI. "
                    + HistoryDao.tryInsert("ECDSA", "VERIFY", message.length(), ok));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Xác minh thất bại: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_ECC_Signature().setVisible(true));
    }
}