package week_03;

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

public class frm_ECC extends JFrame {

    private final ECCCipher ecc = new ECCCipher();

    private final JTextArea txt_public = new JTextArea(2, 40);
    private final JTextArea txt_private = new JTextArea(2, 40);
    private final JButton btn_generate = new JButton("Generate Keys");
    private final JButton btn_savekeys = new JButton("Save Keys");
    private final JButton btn_loadkeys = new JButton("Load Keys");
    private final JTextArea txt_plaintext = new JTextArea(2, 40);
    private final JTextArea txt_ciphertext = new JTextArea(3, 40);
    private final JButton btn_encrypt = new JButton("Encrypt");
    private final JButton btn_decrypt = new JButton("Decrypt");
    private final JLabel lbl_status = new JLabel("Bấm Generate Keys hoặc Load Keys.");

    public frm_ECC() {
        super("ECC Cipher - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        txt_public.setLineWrap(true);
        txt_public.setEditable(false);
        txt_private.setLineWrap(true);
        txt_private.setEditable(false);
        txt_plaintext.setLineWrap(true);
        txt_ciphertext.setLineWrap(true);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("MẬT MÃ ĐƯỜNG CONG ELLIPTIC (ECC - secp256r1)");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(title, g);

        addRow(g, 1, "Public key:", txt_public, 0.15);
        addRow(g, 2, "Private key:", txt_private, 0.15);

        JPanel keyButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        keyButtons.add(btn_generate);
        keyButtons.add(btn_savekeys);
        keyButtons.add(btn_loadkeys);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2; g.weighty = 0; g.fill = GridBagConstraints.NONE;
        add(keyButtons, g);

        addRow(g, 4, "Plaintext:", txt_plaintext, 0.25);
        addRow(g, 5, "Ciphertext:", txt_ciphertext, 0.45);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.add(btn_encrypt);
        buttons.add(btn_decrypt);
        g.gridy = 6; g.gridx = 0; g.gridwidth = 2; g.weighty = 0; g.fill = GridBagConstraints.NONE;
        add(buttons, g);

        g.gridy = 7; g.fill = GridBagConstraints.HORIZONTAL;
        add(lbl_status, g);

        btn_generate.addActionListener(e -> generate());
        btn_savekeys.addActionListener(e -> saveKeys());
        btn_loadkeys.addActionListener(e -> loadKeys());
        btn_encrypt.addActionListener(e -> encrypt());
        btn_decrypt.addActionListener(e -> decrypt());

        setSize(760, 600);
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
        txt_public.setText(ecc.getPublicKeyBase64());
        txt_private.setText(ecc.getPrivateKeyBase64());
        txt_public.setCaretPosition(0);
        txt_private.setCaretPosition(0);
    }

    private void generate() {
        try {
            ecc.generateKeys();
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
        String path = FileTextIO.saveString(this, content, "ecc_keys_test.txt");
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
            ecc.loadKeys(keys[0], keys[1]);
            txt_public.setText(keys[0]);
            txt_private.setText(keys[1]);
            lbl_status.setText("Đã nạp khóa từ tệp.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Tệp khóa không hợp lệ: " + ex.getMessage(),
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
            String ciphertext = ecc.encrypt(plaintext);
            txt_ciphertext.setText(ciphertext);
            txt_ciphertext.setCaretPosition(0);
            lbl_status.setText("Đã mã hóa ECC (" + ciphertext.length() + " ký tự Base64). "
                    + HistoryDao.tryInsert("ECC", "ENCRYPT", plaintext.length(), true));
        } catch (Exception ex) {
            HistoryDao.tryInsert("ECC", "ENCRYPT", plaintext.length(), false);
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
            String plaintext = ecc.decrypt(ciphertext);
            txt_plaintext.setText(plaintext);
            txt_plaintext.setCaretPosition(0);
            lbl_status.setText("Đã giải mã ECC. "
                    + HistoryDao.tryInsert("ECC", "DECRYPT", ciphertext.length(), true));
        } catch (Exception ex) {
            HistoryDao.tryInsert("ECC", "DECRYPT", ciphertext.length(), false);
            lbl_status.setText("Giải mã ECC THẤT BẠI (sai khóa hoặc bản mã bị sửa).");
            JOptionPane.showMessageDialog(this,
                    "Giải mã thất bại: sai khóa riêng hoặc bản mã bị sửa.\n" + ex,
                    "Giải mã thất bại", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_ECC().setVisible(true));
    }
}