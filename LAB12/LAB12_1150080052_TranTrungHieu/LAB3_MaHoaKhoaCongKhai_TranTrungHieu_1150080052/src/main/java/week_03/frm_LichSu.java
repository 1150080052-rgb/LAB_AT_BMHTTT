package week_03;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class frm_LichSu extends JFrame {

    private final JTable tbl_history = new JTable();
    private final JButton btn_refresh = new JButton("Tải lại");
    private final JLabel lbl_status = new JLabel("Đang tải...");

    public frm_LichSu() {
        super("Lịch sử thao tác (SQL Server) - Trần Trung Hiếu - 1150080052");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel title = new JLabel("LỊCH SỬ THAO TÁC - BẢNG DBO.HISTORY", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 8, 0, 8));
        add(title, BorderLayout.NORTH);

        add(new JScrollPane(tbl_history), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        bottom.add(btn_refresh);
        bottom.add(lbl_status);
        add(bottom, BorderLayout.SOUTH);

        btn_refresh.addActionListener(e -> loadData());

        setSize(800, 420);
        setLocationRelativeTo(null);
        tbl_history.setModel(HistoryDao.emptyModel());
        loadData();
    }

    /** Đọc 50 dòng mới nhất từ SQL Server và hiện lên JTable. */
    private void loadData() {
        try {
            tbl_history.setModel(HistoryDao.loadLatest(50));
            lbl_status.setText("Đã tải " + tbl_history.getRowCount() + " dòng mới nhất từ dbo.History.");
        } catch (java.sql.SQLException ex) {
            lbl_status.setText("Không đọc được lịch sử: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new frm_LichSu().setVisible(true));
    }
}