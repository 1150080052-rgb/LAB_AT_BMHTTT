package week_03;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class frm_KetNoiSQL extends JFrame {

    private final JTextField txt_server = new JTextField("127.0.0.1", 24);
    private final JTextField txt_port = new JTextField("14333", 24);
    private final JTextField txt_database = new JTextField("LAB3_MaHoaKhoaCongKhai", 24);
    private final JTextField txt_user = new JTextField("lab3_user", 24);
    private final JPasswordField txt_password = new JPasswordField(24);
    private final JButton btn_test = new JButton("Test Connection");
    private final JButton btn_history = new JButton("Xem lịch sử");
    private final JLabel lbl_status = new JLabel("Chưa kết nối.");

    public frm_KetNoiSQL() {
        super("Kết nối Microsoft SQL Server - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 10, 5, 10);
        g.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("KẾT NỐI MICROSOFT SQL SERVER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        add(title, g);

        g.gridwidth = 1;
        addRow(g, 1, "Server:", txt_server);
        addRow(g, 2, "Port:", txt_port);
        addRow(g, 3, "Database:", txt_database);
        addRow(g, 4, "User:", txt_user);
        addRow(g, 5, "Password:", txt_password);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.add(btn_test);
        buttons.add(btn_history);
        g.gridx = 0; g.gridy = 6; g.gridwidth = 2;
        add(buttons, g);

        lbl_status.setPreferredSize(new java.awt.Dimension(460, 50));
        lbl_status.setVerticalAlignment(JLabel.TOP);
        g.gridy = 7;
        add(lbl_status, g);

        btn_test.addActionListener(e -> testConnection());
        btn_history.addActionListener(e -> new frm_LichSu().setVisible(true));

        pack();
        setLocationRelativeTo(null);
    }

    private void addRow(GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy = row;
        g.gridx = 0;
        add(new JLabel(label), g);
        g.gridx = 1;
        add(field, g);
    }

    private void testConnection() {
        try {
            int port = Integer.parseInt(txt_port.getText().trim());
            DbConfig.set(txt_server.getText().trim(), port, txt_database.getText().trim(),
                    txt_user.getText().trim(), txt_password.getPassword());
            lbl_status.setText("<html>" + DbConfig.test() + "</html>");
        } catch (Exception ex) {
            DbConfig.clear();
            lbl_status.setText("<html>Kết nối thất bại: " + ex.getMessage() + "</html>");
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Không kết nối được SQL Server", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_KetNoiSQL().setVisible(true));
    }
}