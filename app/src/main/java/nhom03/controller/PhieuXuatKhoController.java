package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import nhom03.model.dao.ChiTietPhieuXuatDao;
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.dao.PhieuXuatKhoDao;
import nhom03.model.entity.ChiTietPhieuXuat;
import nhom03.model.entity.NguyenLieu;
import nhom03.model.entity.PhieuXuatKho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PhieuXuatKhoController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<PhieuXuatKho> tableView;

    @FXML
    private TableView<ChiTietPhieuXuat> detailsTableView;

    @FXML
    private TextField tfMaPhieuXuat;

    @FXML
    private DatePicker dpNgayXuat;

    @FXML
    private TextField tfLyDo;

    @FXML
    private TextArea taGhiChu;

    @FXML
    private ComboBox<NguyenLieu> cbNguyenLieu;

    @FXML
    private TextField tfSoLuong;

    private PhieuXuatKhoDao phieuXuatKhoDao;
    private NguyenLieuDao nguyenLieuDao;
    private ChiTietPhieuXuatDao chiTietPhieuXuatDao;

    private ObservableList<PhieuXuatKho> phieuXuatList;
    private ObservableList<ChiTietPhieuXuat> chiTietList;
    private ObservableList<NguyenLieu> nguyenLieuList;

    public void initialize() {
        // Initialize table columns for export receipts
        TableColumn<PhieuXuatKho, Integer> colMaPhieuXuat = new TableColumn<>("Mã");
        colMaPhieuXuat.setCellValueFactory(new PropertyValueFactory<>("maPhieuXuat"));

        TableColumn<PhieuXuatKho, LocalDateTime> colNgayXuat = new TableColumn<>("Ngày xuất");
        colNgayXuat.setCellValueFactory(new PropertyValueFactory<>("ngayXuat"));
        colNgayXuat.setCellFactory(column -> new TableCell<>() {
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

        TableColumn<PhieuXuatKho, String> colLyDo = new TableColumn<>("Lý do");
        colLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        TableColumn<PhieuXuatKho, String> colGhiChu = new TableColumn<>("Ghi chú");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        tableView.getColumns().addAll(colMaPhieuXuat, colNgayXuat, colLyDo, colGhiChu);

        // Initialize table columns for export details
        TableColumn<ChiTietPhieuXuat, Integer> colMaChiTietXuat = new TableColumn<>("Mã CT");
        colMaChiTietXuat.setCellValueFactory(new PropertyValueFactory<>("maChiTietXuat"));

        TableColumn<ChiTietPhieuXuat, NguyenLieu> colNguyenLieu = new TableColumn<>("Nguyên liệu");
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

        TableColumn<ChiTietPhieuXuat, BigDecimal> colSoLuong = new TableColumn<>("Số lượng");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        detailsTableView.getColumns().addAll(colMaChiTietXuat, colNguyenLieu, colSoLuong);

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
                showPhieuXuatDetails(newSelection);
                loadExportDetails(newSelection);
            }
        });

        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    public void setDaos(PhieuXuatKhoDao phieuXuatKhoDao, NguyenLieuDao nguyenLieuDao, ChiTietPhieuXuatDao chiTietPhieuXuatDao) {
        this.phieuXuatKhoDao = phieuXuatKhoDao;
        this.nguyenLieuDao = nguyenLieuDao;
        this.chiTietPhieuXuatDao = chiTietPhieuXuatDao;
    }

    public void loadData() {
        // Load export receipts
        List<PhieuXuatKho> phieuXuats = phieuXuatKhoDao.findAll();
        phieuXuatList = FXCollections.observableArrayList(phieuXuats);
        tableView.setItems(phieuXuatList);

        // Load ingredients
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        cbNguyenLieu.setItems(nguyenLieuList);

        // Initialize empty export details list
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void searchPhieuXuat() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<PhieuXuatKho> filteredList = FXCollections.observableArrayList();
        for (PhieuXuatKho phieuXuat : phieuXuatList) {
            if (phieuXuat.getNgayXuat().toString().toLowerCase().contains(searchText)) {
                filteredList.add(phieuXuat);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showPhieuXuatDetails(PhieuXuatKho phieuXuat) {
        tfMaPhieuXuat.setText(String.valueOf(phieuXuat.getMaPhieuXuat()));
        // Set date picker value from LocalDateTime
        dpNgayXuat.setValue(phieuXuat.getNgayXuat().toLocalDate());
        tfLyDo.setText(phieuXuat.getLyDo());
        taGhiChu.setText(phieuXuat.getGhiChu());
    }

    private void showChiTietDetails(ChiTietPhieuXuat chiTiet) {
        cbNguyenLieu.setValue(chiTiet.getNguyenLieu());
        tfSoLuong.setText(chiTiet.getSoLuong().toString());
    }

    private void loadExportDetails(PhieuXuatKho phieuXuat) {
        try {
            // Load export details from the database
            List<ChiTietPhieuXuat> chiTietPhieuXuats = chiTietPhieuXuatDao.findByMaPhieuXuat(phieuXuat.getMaPhieuXuat());
            chiTietList = FXCollections.observableArrayList(chiTietPhieuXuats);
            detailsTableView.setItems(chiTietList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading export details: " + e.getMessage());
            e.printStackTrace();
            // Initialize empty list in case of error
            chiTietList = FXCollections.observableArrayList();
            detailsTableView.setItems(chiTietList);
        }
    }

    @FXML
    private void clearExportForm() {
        tfMaPhieuXuat.clear();
        dpNgayXuat.setValue(null);
        tfLyDo.clear();
        taGhiChu.clear();
        tableView.getSelectionModel().clearSelection();

        // Clear export details
        chiTietList.clear();
        detailsTableView.setItems(chiTietList);
    }

    @FXML
    private void savePhieuXuat() {
        try {
            // Validate input
            if (dpNgayXuat.getValue() == null || tfLyDo.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            LocalDateTime ngayXuat = dpNgayXuat.getValue().atStartOfDay();
            String lyDo = tfLyDo.getText().trim();
            String ghiChu = taGhiChu.getText().trim();

            // Create or update PhieuXuatKho
            if (tfMaPhieuXuat.getText().isEmpty()) {
                // Create new PhieuXuatKho
                PhieuXuatKho phieuXuat = new PhieuXuatKho(0, ngayXuat, lyDo, ghiChu);
                phieuXuatKhoDao.insert(phieuXuat);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Export receipt added successfully");
            } else {
                // Update existing PhieuXuatKho
                int maPhieuXuat = Integer.parseInt(tfMaPhieuXuat.getText());
                PhieuXuatKho phieuXuat = new PhieuXuatKho(maPhieuXuat, ngayXuat, lyDo, ghiChu);
                phieuXuatKhoDao.update(phieuXuat);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Export receipt updated successfully");
            }

            // Reload data
            loadData();
            clearExportForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving export receipt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deletePhieuXuat() {
        if (tfMaPhieuXuat.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No export receipt selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this export receipt?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maPhieuXuat = Integer.parseInt(tfMaPhieuXuat.getText());
                phieuXuatKhoDao.deleteById(maPhieuXuat);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Export receipt deleted successfully");
                loadData();
                clearExportForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting export receipt: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addExportDetail() {
        // In a real application, you would add the export detail to the database
        // For this example, we'll just add it to the table
        try {
            // Validate input
            if (tfMaPhieuXuat.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please save the export receipt first");
                return;
            }

            if (cbNguyenLieu.getValue() == null ||
                tfSoLuong.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            PhieuXuatKho phieuXuat = tableView.getSelectionModel().getSelectedItem();
            NguyenLieu nguyenLieu = cbNguyenLieu.getValue();
            BigDecimal soLuong = new BigDecimal(tfSoLuong.getText().trim());

            // Create ChiTietPhieuXuat
            ChiTietPhieuXuat chiTiet = new ChiTietPhieuXuat(0, phieuXuat, nguyenLieu, soLuong);

            // Add to list
            chiTietList.add(chiTiet);

            // Clear form
            cbNguyenLieu.setValue(null);
            tfSoLuong.clear();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error adding export detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void removeExportDetail() {
        ChiTietPhieuXuat selectedDetail = detailsTableView.getSelectionModel().getSelectedItem();
        if (selectedDetail == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No export detail selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to remove this export detail?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Remove");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                // Remove from list
                chiTietList.remove(selectedDetail);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error removing export detail: " + e.getMessage());
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
