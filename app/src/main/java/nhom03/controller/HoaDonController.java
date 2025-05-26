package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import nhom03.model.dao.BanDao;
import nhom03.model.dao.ChiTietHoaDonDao;
import nhom03.model.dao.HoaDonDao;
import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class HoaDonController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<HoaDon> tableView;

    @FXML
    private TableView<ChiTietHoaDon> detailsTableView;

    @FXML
    private TextField tfMaHoaDon;

    @FXML
    private ComboBox<Ban> cbBan;

    @FXML
    private DatePicker dpNgayLap;

    @FXML
    private TextField tfTongTien;

    @FXML
    private TextField tfGiamGia;

    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private ComboBox<SanPham> cbSanPham;

    @FXML
    private TextField tfSoLuong;

    @FXML
    private TextField tfDonGia;

    @FXML
    private TextArea taGhiChu;

    private HoaDonDao hoaDonDao;
    private BanDao banDao;
    private SanPhamDao sanPhamDao;
    private ChiTietHoaDonDao chiTietHoaDonDao;

    private ObservableList<HoaDon> hoaDonList;
    private ObservableList<ChiTietHoaDon> chiTietList;
    private ObservableList<Ban> banList;
    private ObservableList<SanPham> sanPhamList;

    public void initialize() {
        // Initialize table columns for orders
        TableColumn<HoaDon, Integer> colMaHoaDon = new TableColumn<>("Mã");
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));

        TableColumn<HoaDon, Ban> colBan = new TableColumn<>("Bàn");
        colBan.setCellValueFactory(new PropertyValueFactory<>("ban"));
        colBan.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Ban ban, boolean empty) {
                super.updateItem(ban, empty);
                if (empty || ban == null) {
                    setText(null);
                } else {
                    setText(ban.getTenBan());
                }
            }
        });

        TableColumn<HoaDon, LocalDateTime> colNgayLap = new TableColumn<>("Ngày lập");
        colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
        colNgayLap.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        TableColumn<HoaDon, BigDecimal> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        TableColumn<HoaDon, TrangThaiHoaDon> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaHoaDon, colBan, colNgayLap, colTongTien, colTrangThai);

        // Initialize table columns for order details
        TableColumn<ChiTietHoaDon, Integer> colMaChiTiet = new TableColumn<>("Mã CT");
        colMaChiTiet.setCellValueFactory(new PropertyValueFactory<>("maChiTiet"));

        TableColumn<ChiTietHoaDon, SanPham> colSanPham = new TableColumn<>("Sản phẩm");
        colSanPham.setCellValueFactory(new PropertyValueFactory<>("sanPham"));
        colSanPham.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(SanPham sanPham, boolean empty) {
                super.updateItem(sanPham, empty);
                if (empty || sanPham == null) {
                    setText(null);
                } else {
                    setText(sanPham.getTenSanPham());
                }
            }
        });

        TableColumn<ChiTietHoaDon, Integer> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietHoaDon, BigDecimal> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        TableColumn<ChiTietHoaDon, String> colGhiChu = new TableColumn<>("Ghi chú");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        detailsTableView.getColumns().addAll(colMaChiTiet, colSanPham, colSoLuong, colDonGia, colGhiChu);

        // Initialize combo boxes
        cbTrangThai.getItems().addAll("Chưa thanh toán", "Đã thanh toán", "Hủy");

        // Set up converters for combo boxes
        cbBan.setConverter(new StringConverter<>() {
            @Override
            public String toString(Ban ban) {
                return ban != null ? ban.getTenBan() : "";
            }

            @Override
            public Ban fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });

        cbSanPham.setConverter(new StringConverter<>() {
            @Override
            public String toString(SanPham sanPham) {
                return sanPham != null ? sanPham.getTenSanPham() : "";
            }

            @Override
            public SanPham fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });

        // Add selection listeners
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showHoaDonDetails(newSelection);
                loadOrderDetails(newSelection);
            }
        });

        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    public void setDaos(HoaDonDao hoaDonDao, BanDao banDao, SanPhamDao sanPhamDao, ChiTietHoaDonDao chiTietHoaDonDao) {
        this.hoaDonDao = hoaDonDao;
        this.banDao = banDao;
        this.sanPhamDao = sanPhamDao;
        this.chiTietHoaDonDao = chiTietHoaDonDao;
    }

    public void loadData() {
        // Load orders
        List<HoaDon> hoaDons = hoaDonDao.findAll();
        hoaDonList = FXCollections.observableArrayList(hoaDons);
        tableView.setItems(hoaDonList);

        // Load tables
        List<Ban> bans = banDao.findAll();
        banList = FXCollections.observableArrayList(bans);
        cbBan.setItems(banList);

        // Load products
        List<SanPham> sanPhams = sanPhamDao.findAll();
        sanPhamList = FXCollections.observableArrayList(sanPhams);
        cbSanPham.setItems(sanPhamList);

        // Initialize empty order details list
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void searchHoaDon() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<HoaDon> filteredList = FXCollections.observableArrayList();
        for (HoaDon hoaDon : hoaDonList) {
            if (hoaDon.getBan().getTenBan().toLowerCase().contains(searchText)) {
                filteredList.add(hoaDon);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showHoaDonDetails(HoaDon hoaDon) {
        tfMaHoaDon.setText(String.valueOf(hoaDon.getMaHoaDon()));
        cbBan.setValue(hoaDon.getBan());
        // Set date picker value from LocalDateTime
        dpNgayLap.setValue(hoaDon.getNgayLap().toLocalDate());
        tfTongTien.setText(hoaDon.getTongTien().toString());
        tfGiamGia.setText(hoaDon.getGiamGia().toString());

        switch (hoaDon.getTrangThai()) {
            case CHUA_THANH_TOAN -> cbTrangThai.setValue("Chưa thanh toán");
            case DA_THANH_TOAN -> cbTrangThai.setValue("Đã thanh toán");
            case HUY -> cbTrangThai.setValue("Hủy");
        }
    }

    private void showChiTietDetails(ChiTietHoaDon chiTiet) {
        cbSanPham.setValue(chiTiet.getSanPham());
        tfSoLuong.setText(String.valueOf(chiTiet.getSoLuong()));
        tfDonGia.setText(chiTiet.getDonGia().toString());
        taGhiChu.setText(chiTiet.getGhiChu());
    }

    private void loadOrderDetails(HoaDon hoaDon) {
        // Load order details from the database
        List<ChiTietHoaDon> chiTietHoaDons = chiTietHoaDonDao.findByMaHoaDon(hoaDon.getMaHoaDon());
        chiTietList = FXCollections.observableArrayList(chiTietHoaDons);
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void clearOrderForm() {
        tfMaHoaDon.clear();
        cbBan.setValue(null);
        dpNgayLap.setValue(null);
        tfTongTien.clear();
        tfGiamGia.clear();
        cbTrangThai.setValue(null);
        tableView.getSelectionModel().clearSelection();

        // Clear order details
        chiTietList.clear();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void saveHoaDon() {
        try {
            // Validate input
            if (cbBan.getValue() == null ||
                dpNgayLap.getValue() == null ||
                tfGiamGia.getText().trim().isEmpty() ||
                cbTrangThai.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            Ban ban = cbBan.getValue();
            LocalDateTime ngayLap = dpNgayLap.getValue().atStartOfDay();
            BigDecimal tongTien = tfTongTien.getText().isEmpty() ?
                BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
            BigDecimal giamGia = new BigDecimal(tfGiamGia.getText().trim());
            TrangThaiHoaDon trangThai = TrangThaiHoaDon.fromString(cbTrangThai.getValue());

            // Create or update HoaDon
            if (tfMaHoaDon.getText().isEmpty()) {
                // Create new HoaDon
                HoaDon hoaDon = new HoaDon(0, ban, ngayLap, tongTien, giamGia, trangThai);
                hoaDonDao.insert(hoaDon);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order added successfully");
            } else {
                // Update existing HoaDon
                int maHoaDon = Integer.parseInt(tfMaHoaDon.getText());
                HoaDon hoaDon = new HoaDon(maHoaDon, ban, ngayLap, tongTien, giamGia, trangThai);
                hoaDonDao.update(hoaDon);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully");
            }

            // Reload data
            loadData();
            clearOrderForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteHoaDon() {
        if (tfMaHoaDon.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No order selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this order?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maHoaDon = Integer.parseInt(tfMaHoaDon.getText());
                hoaDonDao.deleteById(maHoaDon);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully");
                loadData();
                clearOrderForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting order: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addOrderDetail() {
        // In a real application, you would add the order detail to the database
        // For this example, we'll just add it to the table
        try {
            // Validate input
            if (tfMaHoaDon.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please save the order first");
                return;
            }

            if (cbSanPham.getValue() == null ||
                tfSoLuong.getText().trim().isEmpty() ||
                tfDonGia.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            HoaDon hoaDon = tableView.getSelectionModel().getSelectedItem();
            SanPham sanPham = cbSanPham.getValue();
            int soLuong = Integer.parseInt(tfSoLuong.getText().trim());
            BigDecimal donGia = new BigDecimal(tfDonGia.getText().trim());
            String ghiChu = taGhiChu.getText().trim();

            // Create ChiTietHoaDon
            ChiTietHoaDon chiTiet = new ChiTietHoaDon(0, hoaDon, sanPham, soLuong, donGia, ghiChu);

            // Add to list
            chiTietList.add(chiTiet);

            // Update total
            BigDecimal total = donGia.multiply(new BigDecimal(soLuong));
            BigDecimal currentTotal = tfTongTien.getText().isEmpty() ?
                BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
            tfTongTien.setText(currentTotal.add(total).toString());

            // Clear form
            cbSanPham.setValue(null);
            tfSoLuong.clear();
            tfDonGia.clear();
            taGhiChu.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding order detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void removeOrderDetail() {
        ChiTietHoaDon selectedDetail = detailsTableView.getSelectionModel().getSelectedItem();
        if (selectedDetail == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No order detail selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to remove this order detail?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Remove");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                // Update total
                BigDecimal total = selectedDetail.getDonGia().multiply(new BigDecimal(selectedDetail.getSoLuong()));
                BigDecimal currentTotal = tfTongTien.getText().isEmpty() ?
                    BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
                tfTongTien.setText(currentTotal.subtract(total).toString());

                // Remove from list
                chiTietList.remove(selectedDetail);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error removing order detail: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
