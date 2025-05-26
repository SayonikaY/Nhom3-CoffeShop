package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import nhom03.model.dao.ChiTietPhieuNhapDao;
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.dao.PhieuNhapKhoDao;
import nhom03.model.entity.ChiTietPhieuNhap;
import nhom03.model.entity.NguyenLieu;
import nhom03.model.entity.PhieuNhapKho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PhieuNhapKhoController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<PhieuNhapKho> tableView;

    @FXML
    private TableView<ChiTietPhieuNhap> detailsTableView;

    @FXML
    private TextField tfMaPhieuNhap;

    @FXML
    private DatePicker dpNgayNhap;

    @FXML
    private TextField tfTongTien;

    @FXML
    private TextArea taGhiChu;

    @FXML
    private ComboBox<NguyenLieu> cbNguyenLieu;

    @FXML
    private TextField tfSoLuong;

    @FXML
    private TextField tfDonGia;

    private PhieuNhapKhoDao phieuNhapKhoDao;
    private NguyenLieuDao nguyenLieuDao;
    private ChiTietPhieuNhapDao chiTietPhieuNhapDao;

    private ObservableList<PhieuNhapKho> phieuNhapList;
    private ObservableList<ChiTietPhieuNhap> chiTietList;
    private ObservableList<NguyenLieu> nguyenLieuList;

    public void initialize() {
        // Initialize table columns for import receipts
        TableColumn<PhieuNhapKho, Integer> colMaPhieuNhap = new TableColumn<>("Mã");
        colMaPhieuNhap.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));

        TableColumn<PhieuNhapKho, LocalDateTime> colNgayNhap = new TableColumn<>("Ngày nhập");
        colNgayNhap.setCellValueFactory(new PropertyValueFactory<>("ngayNhap"));
        colNgayNhap.setCellFactory(column -> new TableCell<>() {
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

        TableColumn<PhieuNhapKho, BigDecimal> colTongTien = new TableColumn<>("Tổng tiền");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        TableColumn<PhieuNhapKho, String> colGhiChu = new TableColumn<>("Ghi chú");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        tableView.getColumns().addAll(colMaPhieuNhap, colNgayNhap, colTongTien, colGhiChu);

        // Initialize table columns for import details
        TableColumn<ChiTietPhieuNhap, Integer> colMaChiTietNhap = new TableColumn<>("Mã CT");
        colMaChiTietNhap.setCellValueFactory(new PropertyValueFactory<>("maChiTietNhap"));

        TableColumn<ChiTietPhieuNhap, NguyenLieu> colNguyenLieu = new TableColumn<>("Nguyên liệu");
        colNguyenLieu.setCellValueFactory(new PropertyValueFactory<>("nguyenLieu"));
        colNguyenLieu.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(NguyenLieu nguyenLieu, boolean empty) {
                super.updateItem(nguyenLieu, empty);
                if (empty || nguyenLieu == null) {
                    setText(null);
                } else {
                    setText(nguyenLieu.getTenNguyenLieu());
                }
            }
        });

        TableColumn<ChiTietPhieuNhap, BigDecimal> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietPhieuNhap, BigDecimal> colDonGia = new TableColumn<>("Giá nhập");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        detailsTableView.getColumns().addAll(colMaChiTietNhap, colNguyenLieu, colSoLuong, colDonGia);

        // Set up converter for combo box
        cbNguyenLieu.setConverter(new StringConverter<>() {
            @Override
            public String toString(NguyenLieu nguyenLieu) {
                return nguyenLieu != null ? nguyenLieu.getTenNguyenLieu() : "";
            }

            @Override
            public NguyenLieu fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });

        // Add selection listeners
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPhieuNhapDetails(newSelection);
                loadImportDetails(newSelection);
            }
        });

        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    public void setDaos(PhieuNhapKhoDao phieuNhapKhoDao, NguyenLieuDao nguyenLieuDao, ChiTietPhieuNhapDao chiTietPhieuNhapDao) {
        this.phieuNhapKhoDao = phieuNhapKhoDao;
        this.nguyenLieuDao = nguyenLieuDao;
        this.chiTietPhieuNhapDao = chiTietPhieuNhapDao;
    }

    public void loadData() {
        // Load import receipts
        List<PhieuNhapKho> phieuNhaps = phieuNhapKhoDao.findAll();
        phieuNhapList = FXCollections.observableArrayList(phieuNhaps);
        tableView.setItems(phieuNhapList);

        // Load ingredients
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        cbNguyenLieu.setItems(nguyenLieuList);

        // Initialize empty import details list
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void searchPhieuNhap() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<PhieuNhapKho> filteredList = FXCollections.observableArrayList();
        for (PhieuNhapKho phieuNhap : phieuNhapList) {
            if (phieuNhap.getNgayNhap().toString().toLowerCase().contains(searchText)) {
                filteredList.add(phieuNhap);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showPhieuNhapDetails(PhieuNhapKho phieuNhap) {
        tfMaPhieuNhap.setText(String.valueOf(phieuNhap.getMaPhieuNhap()));
        // Set date picker value from LocalDateTime
        dpNgayNhap.setValue(phieuNhap.getNgayNhap().toLocalDate());
        tfTongTien.setText(phieuNhap.getTongTien().toString());
        taGhiChu.setText(phieuNhap.getGhiChu());
    }

    private void showChiTietDetails(ChiTietPhieuNhap chiTiet) {
        cbNguyenLieu.setValue(chiTiet.getNguyenLieu());
        tfSoLuong.setText(chiTiet.getSoLuong().toString());
        tfDonGia.setText(chiTiet.getDonGia().toString());
    }

    private void loadImportDetails(PhieuNhapKho phieuNhap) {
        try {
            // Load import details from the database
            List<ChiTietPhieuNhap> chiTietPhieuNhaps = chiTietPhieuNhapDao.findByMaPhieuNhap(phieuNhap.getMaPhieuNhap());
            chiTietList = FXCollections.observableArrayList(chiTietPhieuNhaps);
            detailsTableView.setItems(chiTietList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading import details: " + e.getMessage());
            e.printStackTrace();
            // Initialize empty list in case of error
            chiTietList = FXCollections.observableArrayList();
            detailsTableView.setItems(chiTietList);
        }
    }

    @FXML
    private void clearImportForm() {
        tfMaPhieuNhap.clear();
        dpNgayNhap.setValue(null);
        tfTongTien.clear();
        taGhiChu.clear();
        tableView.getSelectionModel().clearSelection();

        // Clear import details
        chiTietList.clear();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void savePhieuNhap() {
        try {
            // Validate input
            if (dpNgayNhap.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            LocalDateTime ngayNhap = dpNgayNhap.getValue().atStartOfDay();
            BigDecimal tongTien = tfTongTien.getText().isEmpty() ?
                BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
            String ghiChu = taGhiChu.getText().trim();

            // Create or update PhieuNhapKho
            if (tfMaPhieuNhap.getText().isEmpty()) {
                // Create new PhieuNhapKho
                PhieuNhapKho phieuNhap = new PhieuNhapKho(0, ngayNhap, tongTien, ghiChu);
                phieuNhapKhoDao.insert(phieuNhap);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Import receipt added successfully");
            } else {
                // Update existing PhieuNhapKho
                int maPhieuNhap = Integer.parseInt(tfMaPhieuNhap.getText());
                PhieuNhapKho phieuNhap = new PhieuNhapKho(maPhieuNhap, ngayNhap, tongTien, ghiChu);
                phieuNhapKhoDao.update(phieuNhap);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Import receipt updated successfully");
            }

            // Reload data
            loadData();
            clearImportForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving import receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePhieuNhap() {
        if (tfMaPhieuNhap.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No import receipt selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this import receipt?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maPhieuNhap = Integer.parseInt(tfMaPhieuNhap.getText());
                phieuNhapKhoDao.deleteById(maPhieuNhap);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Import receipt deleted successfully");
                loadData();
                clearImportForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting import receipt: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addImportDetail() {
        // In a real application, you would add the import detail to the database
        // For this example, we'll just add it to the table
        try {
            // Validate input
            if (tfMaPhieuNhap.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please save the import receipt first");
                return;
            }

            if (cbNguyenLieu.getValue() == null ||
                tfSoLuong.getText().trim().isEmpty() ||
                tfDonGia.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            PhieuNhapKho phieuNhap = tableView.getSelectionModel().getSelectedItem();
            NguyenLieu nguyenLieu = cbNguyenLieu.getValue();
            BigDecimal soLuong = new BigDecimal(tfSoLuong.getText().trim());
            BigDecimal donGia = new BigDecimal(tfDonGia.getText().trim());

            // Create ChiTietPhieuNhap
            ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap(0, phieuNhap, nguyenLieu, soLuong, donGia);

            // Add to list
            chiTietList.add(chiTiet);

            // Update total
            BigDecimal total = donGia.multiply(soLuong);
            BigDecimal currentTotal = tfTongTien.getText().isEmpty() ?
                BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
            tfTongTien.setText(currentTotal.add(total).toString());

            // Clear form
            cbNguyenLieu.setValue(null);
            tfSoLuong.clear();
            tfDonGia.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding import detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void removeImportDetail() {
        ChiTietPhieuNhap selectedDetail = detailsTableView.getSelectionModel().getSelectedItem();
        if (selectedDetail == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No import detail selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to remove this import detail?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Remove");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                // Update total
                BigDecimal total = selectedDetail.getDonGia().multiply(selectedDetail.getSoLuong());
                BigDecimal currentTotal = tfTongTien.getText().isEmpty() ?
                    BigDecimal.ZERO : new BigDecimal(tfTongTien.getText().trim());
                tfTongTien.setText(currentTotal.subtract(total).toString());

                // Remove from list
                chiTietList.remove(selectedDetail);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error removing import detail: " + e.getMessage());
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
