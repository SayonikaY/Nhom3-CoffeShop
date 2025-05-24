/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.DB;
import Models.Ban;
import Models.ChiTietHoaDon;
import Models.HoaDon;
import Models.SanPham;
import Models.Topping;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author ACER
 */
public class QLDatHang extends javax.swing.JFrame {
    private DefaultTableModel modelSanPham;
    private DefaultTableModel modelBangGia;
    private String topping = "";
    private String ghiChu = "";


    /**
     * Creates new form QLDatHang
     */
    public QLDatHang() {
        initComponents();
        khoiTaoForm();
    }
    
    public QLDatHang(int maBan, String tenBan) {
        initComponents();
        khoiTaoForm();
        
        txtMaBan.setText(String.valueOf(maBan));
        txtTenBan.setText(tenBan);
        txtMaBan.setEnabled(false);
        txtTenBan.setEnabled(false);
    }

    private void khoiTaoForm() {
        initTableSanPham();
        initTableBangGia();
        // Đặt renderer cho cột trạng thái 
        tblChiTietDon.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = value == null ? "" : value.toString();
                if ("Đã thanh toán".equalsIgnoreCase(status)) {
                    c.setForeground(java.awt.Color.GREEN.darker());
                } else if ("Chưa thanh toán".equalsIgnoreCase(status)) {
                    c.setForeground(java.awt.Color.RED);
                } else {
                    c.setForeground(java.awt.Color.BLACK);
                }
                return c;
            }
        });
        loadSanPhamToTable();
        tinhTongTien();
    }

    private void initTableSanPham() {
        String[] cols = {"Tên sản phẩm", "Loại", "Giá", "Mô tả", "Trạng thái"};
        modelSanPham = new DefaultTableModel(cols, 0);
        tblSanPham.setModel(modelSanPham);   
    }

    private void initTableBangGia() {
        String[] cols = {"Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền","Topping", "Ghi chú","Trạng thái"};
        modelBangGia = new DefaultTableModel(cols, 0);
        tblChiTietDon.setModel(modelBangGia);     
    }

    private void loadSanPhamToTable() {
        modelSanPham.setRowCount(0);
        Session session = DB.openSession();
        Transaction tx = session.beginTransaction();
        try {
            List<SanPham> listSP = session.createQuery("FROM SanPham", SanPham.class).list();
            for (SanPham sp : listSP) {
                Object[] row = {
                    sp.getTenSanPham(),
                    sp.getLoaiSanPham(),
                    sp.getDonGia(),
                    sp.getMoTa(),
                    sp.getTrangThai()
                };
                modelSanPham.addRow(row);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    // Thêm sản phẩm từ tblSanPham vào tblBangGia 
    private void addSanPhamToBangGia() {
        int row = tblSanPham.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để thêm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tenSP = modelSanPham.getValueAt(row, 0).toString();
        double gia = Double.parseDouble(modelSanPham.getValueAt(row, 2).toString());
        String trangThaiSP = modelSanPham.getValueAt(row, 4).toString().trim();
        
        // Kiểm tra nếu sản phẩm đã hết
        if (trangThaiSP.trim().equalsIgnoreCase("Hết") || trangThaiSP.equalsIgnoreCase("Hết hàng")) {
            JOptionPane.showMessageDialog(this, "Sản phẩm '" + tenSP + "' hiện đã hết!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
    // Kiểm tra topping nếu có
//    if (!topping.isEmpty()) {
//        List<String> dsTopping = Arrays.asList(topping.split("\\s*,\\s*"));
//        List<String> toppingHet = new ArrayList<>();
//        
//        for (String tp : dsTopping) {
//            String sql = "FROM Topping WHERE lower(trim(tenTopping))  = :ten AND trangThai = 'Hết'";
//            try (Session session = DB.openSession()) {
//                Topping t = session.createQuery(sql, Topping.class)
//                                                   .setParameter("ten", tp.trim().toLowerCase())
////                                   .setParameter("ten", tp)
//                                   .uniqueResult();
//                if (t != null) {
//                    toppingHet.add(tp);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (!toppingHet.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Topping đã hết: " + String.join(", ", toppingHet), "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//    }

    
        // Hỏi số lượng
        String slStr = JOptionPane.showInputDialog(this, "Nhập số lượng cho sản phẩm: " + tenSP, "Nhập số lượng", JOptionPane.QUESTION_MESSAGE);
        if (slStr == null) return;
        int soLuong;
        try {
            soLuong = Integer.parseInt(slStr);
            if (soLuong <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ. Vui lòng nhập số nguyên dương.");
            return;
        }    
        
        boolean found = false;
        for (int i = 0; i < modelBangGia.getRowCount(); i++) {
            if (modelBangGia.getValueAt(i, 0).toString().equals(tenSP)) {
                int sl = Integer.parseInt(modelBangGia.getValueAt(i, 1).toString()) + 1;
                double thanhTien = sl * gia;
                
                modelBangGia.setValueAt(sl, i, 1);
                modelBangGia.setValueAt(thanhTien, i, 3);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Object[] rowBangGia = {tenSP, 1, gia, gia, topping, ghiChu,"Chưa thanh toán"};
            modelBangGia.addRow(rowBangGia);
        }
        tinhTongTien();
    }

    // Tính tổng tiền dựa trên tblBangGia và giảm giá
    private void tinhTongTien() {
        if (modelBangGia == null) return;
        double tong = 0;
        boolean daCoTopping = false; // dùng để kiểm tra có topping nào không
       
        // Bảng giá topping
        Map<String, Double> bangGiaTopping = new HashMap<>();
        bangGiaTopping.put("Trân châu đen", 10000.0);
        bangGiaTopping.put("Trân châu trắng", 10000.0);
        bangGiaTopping.put("Thạch cà phê", 8000.0);
        bangGiaTopping.put("Kem phô mai", 15000.0);
        bangGiaTopping.put("Pudding", 12000.0);
        bangGiaTopping.put("Đào miếng", 12000.0);
        bangGiaTopping.put("Whipping cream", 10000.0);
        bangGiaTopping.put("Shot espresso", 15000.0);
        bangGiaTopping.put("Sốt caramel", 8000.0);
        bangGiaTopping.put("Thạch nha đam", 10000.0);
        bangGiaTopping.put("Thạch matcha", 12000.0);
        
        try {
            for (int i = 0; i < modelBangGia.getRowCount(); i++) {
                int sl = Integer.parseInt(modelBangGia.getValueAt(i, 1).toString()); // số lượng
                double donGia = Double.parseDouble(modelBangGia.getValueAt(i, 2).toString()); // đơn giá
                double thanhTien = sl * donGia;
                // Lấy topping ở cột 5
                String toppingStr = "";
                if (modelBangGia.getColumnCount() > 4 && modelBangGia.getValueAt(i, 4) != null) {
                    toppingStr = modelBangGia.getValueAt(i, 4).toString().trim();
                }
                
                if (!toppingStr.isEmpty()) {
                    List<String> dsTopping = Arrays.asList(toppingStr.split("\\s*,\\s*"));
                    double tienTopping = 0;
                    for (String topping : dsTopping) {
                        if (bangGiaTopping.containsKey(topping)) {
                            tienTopping += bangGiaTopping.get(topping);
                        }
                    }
                    thanhTien += sl * tienTopping;
                    daCoTopping = true; // đánh dấu đã có topping
                }
                tong += thanhTien;
            }
             // Xử lý giảm giá
            double giamGia = 0;
            String giamGiaStr = txtGiamGia.getText().trim();
            if (!giamGiaStr.isEmpty()) {
                giamGia = Double.parseDouble(giamGiaStr);
            }
            if (giamGia < 0 || giamGia > 100) {
                JOptionPane.showMessageDialog(this, "Giảm giá phải từ 0 đến 100%", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double tienSauGiam = tong * (1 - giamGia / 100);
            txtTongTien.setText(String.format("%.0f", tienSauGiam));
            
            // Thông báo nếu có topping đã được cộng
            if (daCoTopping) {
                JOptionPane.showMessageDialog(this, "Đã cộng tiền topping vào tổng tiền.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tính tổng tiền: " + e.getMessage());
        }
    }

    private void luuDonHang() {
         try {
                 // Lấy và kiểm tra các giá trị đầu vào
            String maBanStr = txtMaBan.getText().trim();
            String tenBan = txtTenBan.getText().trim();
            String giamGiaStr = txtGiamGia.getText().trim().replace(",", ".");
            String tongTienStr = txtTongTien.getText().trim().replace(",", ".");

//        // Kiểm tra rỗng
//        if (maBanStr.isEmpty() || tenBan.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin đơn hàng!");
//            return;
//        }
//
//        if (modelBangGia.getRowCount() == 0) {
//            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong đơn!");
//            return;
//        }

        // Parse giá trị
        int maBan;
        try {
            maBan = Integer.parseInt(maBanStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã bàn phải là số!");
            return;
        }
        double giamGia = 0;
        double tongTien = 0;

        try {
            if (!giamGiaStr.isEmpty()) {
                giamGia = Double.parseDouble(giamGiaStr);
            }
            if (!tongTienStr.isEmpty()) {
                tongTien = Double.parseDouble(tongTienStr);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giảm giá hoặc Tổng tiền không đúng định dạng!");
            return;
        }

        // Mở session
        Session session = DB.openSession();
        Transaction tx = session.beginTransaction();

        // Tạo hóa đơn mới
        HoaDon hd = new HoaDon();
        hd.setMaBan(session.get(Ban.class, maBan));
        hd.setGiamGia(BigDecimal.valueOf(giamGia));
        hd.setTongTien(BigDecimal.valueOf(tongTien));
        session.save(hd); // ID sẽ tự động tăng
        int maHoaDonMoi = hd.getId(); // Dùng nếu cần
        txtMaDon.setText(String.valueOf(maHoaDonMoi));

        // Duyệt sản phẩm và lưu chi tiết
        for (int i = 0; i < modelBangGia.getRowCount(); i++) {
            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setMaHoaDon(hd);

            String tenSP = modelBangGia.getValueAt(i, 0).toString();
            Query<SanPham> query = session.createQuery("FROM SanPham WHERE tenSanPham = :ten", SanPham.class);
            query.setParameter("ten", tenSP);
            SanPham sp = query.uniqueResult();

            if (sp == null) {
                throw new Exception("Không tìm thấy sản phẩm: " + tenSP);
            }

            ct.setMaSanPham(sp);

            int soLuong = Integer.parseInt(modelBangGia.getValueAt(i, 1).toString());
            ct.setSoLuong(soLuong);

            double donGia = Double.parseDouble(modelBangGia.getValueAt(i, 2).toString());
            ct.setDonGia(BigDecimal.valueOf(donGia));

            String topping = modelBangGia.getValueAt(i, 4) != null ? modelBangGia.getValueAt(i, 4).toString() : "";
            String ghiChu = modelBangGia.getValueAt(i, 5) != null ? modelBangGia.getValueAt(i, 5).toString() : "";
            ct.setGhiChu("Topping: " + topping + " | " + ghiChu);

            session.save(ct);
        }
        
        tx.commit();
        JOptionPane.showMessageDialog(this, "Lưu đơn hàng thành công!");
//        taoMoi();
         } catch (Exception ex) {
             ex.printStackTrace();
             JOptionPane.showMessageDialog(this, "Lỗi lưu đơn hàng: " + ex.getMessage());
         }
    }

    private void taoMoi() {
        txtMaBan.setText("");
        txtTenBan.setText("");
        txtMaDon.setText("");
        txtGiamGia.setText("0");
        txtTongTien.setText("0");
        modelBangGia.setRowCount(0);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMaBan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTenBan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaDon = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnToppingGhiChu = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTietDon = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtGiamGia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        btnThanhToan = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnTaoMoi = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mã bàn:");

        jLabel2.setText("Tên bàn:");

        jLabel3.setText("Mã đơn:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtMaBan)
                    .addComponent(txtTenBan, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addGap(56, 56, 56)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtMaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtTenBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tên sản phẩm", "Loại", "Giá", "Mô tả", "Trạng thái"
            }
        ));
        jScrollPane1.setViewportView(tblSanPham);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Danh sách sản phẩm:");

        btnThem.setText("Thêm ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnToppingGhiChu.setText("Topping & Ghi chú");
        btnToppingGhiChu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToppingGhiChuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnToppingGhiChu)
                        .addGap(69, 69, 69)
                        .addComponent(btnThem))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnToppingGhiChu)
                    .addComponent(btnThem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Chi tiết đơn:");

        tblChiTietDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền", "Topping", "Ghi chú", "Trạng thái"
            }
        ));
        tblChiTietDon.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tblChiTietDonPropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(tblChiTietDon);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Giảm giá (%):");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Tổng tiền:");

        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnTaoMoi.setText("Tạo mới");
        btnTaoMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnThanhToan)
                        .addGap(30, 30, 30)
                        .addComponent(btnLuu)))
                .addGap(36, 36, 36)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTaoMoi))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan)
                    .addComponent(btnLuu)
                    .addComponent(btnTaoMoi))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 52, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMoiActionPerformed
        taoMoi();
    }//GEN-LAST:event_btnTaoMoiActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
         luuDonHang();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        tinhTongTien();
        for (int i = 0; i < modelBangGia.getRowCount(); i++) {
            modelBangGia.setValueAt("Đã thanh toán", i, 6);
        }
        JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nTổng tiền: " + txtTongTien.getText());
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void tblChiTietDonPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tblChiTietDonPropertyChange
        tinhTongTien();
    }//GEN-LAST:event_tblChiTietDonPropertyChange

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        addSanPhamToBangGia();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnToppingGhiChuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToppingGhiChuActionPerformed
        List<Topping> danhSachTopping = new ArrayList<>();
        try (Session session = DB.openSession()) {
            danhSachTopping = session.createQuery("FROM Topping", Topping.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi lấy topping từ DB!");
            return;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        List<JCheckBox> checkboxes = new ArrayList<>();
        for (Topping toppingObj : danhSachTopping) {
            JCheckBox cb = new JCheckBox(toppingObj.getTenTopping() + " (" + toppingObj.getDonGia().intValue() + "đ)");
            if ("Hết".equalsIgnoreCase(toppingObj.getTrangThai())) {
                cb.setEnabled(false);
                cb.setText(toppingObj.getTenTopping() + " (Hết)");
            }
            panel.add(cb);
            checkboxes.add(cb);
        }
        
        JTextArea txtGhiChu = new JTextArea(3, 20);
        panel.add(new JLabel("Ghi chú:"));
        panel.add(new JScrollPane(txtGhiChu));
        int result = JOptionPane.showConfirmDialog(null, panel, "Chọn Topping và Ghi chú",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            List<String> toppingChon = new ArrayList<>();
            for (JCheckBox cb : checkboxes) {
                if (cb.isSelected()) toppingChon.add(cb.getText().replaceAll(" \\(.*\\)$", ""));
            }
            topping = String.join(",", toppingChon);
            ghiChu = txtGhiChu.getText().trim();
        }
    }//GEN-LAST:event_btnToppingGhiChuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QLDatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLDatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLDatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLDatHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLDatHang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnTaoMoi;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnToppingGhiChu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblChiTietDon;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtMaBan;
    private javax.swing.JTextField txtMaDon;
    private javax.swing.JTextField txtTenBan;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
