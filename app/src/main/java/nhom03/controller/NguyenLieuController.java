package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.entity.NguyenLieu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class NguyenLieuController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<NguyenLieu> tableView;

    @FXML
    private TextField tfMaNguyenLieu;

    @FXML
    private TextField tfTenNguyenLieu;

    @FXML
    private TextField tfDonViTinh;

    @FXML
    private TextField tfSoLuongTon;

    @FXML
    private TextField tfDonGiaNhap;

    private NguyenLieuDao nguyenLieuDao;
    private ObservableList<NguyenLieu> nguyenLieuList;

    public void initialize() {
        // Initialize table columns
        TableColumn<NguyenLieu, Integer> colMaNguyenLieu = new TableColumn<>("Mã");
        colMaNguyenLieu.setCellValueFactory(new PropertyValueFactory<>("maNguyenLieu"));

        TableColumn<NguyenLieu, String> colTenNguyenLieu = new TableColumn<>("Tên");
        colTenNguyenLieu.setCellValueFactory(new PropertyValueFactory<>("tenNguyenLieu"));

        TableColumn<NguyenLieu, String> colDonViTinh = new TableColumn<>("Đơn vị");
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));

        TableColumn<NguyenLieu, BigDecimal> colSoLuongTon = new TableColumn<>("Số lượng tồn");
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));

        TableColumn<NguyenLieu, BigDecimal> colDonGiaNhap = new TableColumn<>("Giá nhập");
        colDonGiaNhap.setCellValueFactory(new PropertyValueFactory<>("donGiaNhap"));

        tableView.getColumns().addAll(colMaNguyenLieu, colTenNguyenLieu, colDonViTinh, colSoLuongTon, colDonGiaNhap);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showNguyenLieuDetails(newSelection);
            }
        });
    }

    public void setNguyenLieuDao(NguyenLieuDao nguyenLieuDao) {
        this.nguyenLieuDao = nguyenLieuDao;
    }

    public void loadData() {
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        tableView.setItems(nguyenLieuList);
    }

    @FXML
    private void searchNguyenLieu() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<NguyenLieu> filteredList = FXCollections.observableArrayList();
        for (NguyenLieu nguyenLieu : nguyenLieuList) {
            if (nguyenLieu.getTenNguyenLieu().toLowerCase().contains(searchText)) {
                filteredList.add(nguyenLieu);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showNguyenLieuDetails(NguyenLieu nguyenLieu) {
        tfMaNguyenLieu.setText(String.valueOf(nguyenLieu.getMaNguyenLieu()));
        tfTenNguyenLieu.setText(nguyenLieu.getTenNguyenLieu());
        tfDonViTinh.setText(nguyenLieu.getDonViTinh());
        tfSoLuongTon.setText(nguyenLieu.getSoLuongTon().toString());
        tfDonGiaNhap.setText(nguyenLieu.getDonGiaNhap().toString());
    }

    @FXML
    private void clearForm() {
        tfMaNguyenLieu.clear();
        tfTenNguyenLieu.clear();
        tfDonViTinh.clear();
        tfSoLuongTon.clear();
        tfDonGiaNhap.clear();
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void saveNguyenLieu() {
        try {
            // Validate input
            if (tfTenNguyenLieu.getText().trim().isEmpty() ||
                tfDonViTinh.getText().trim().isEmpty() ||
                tfSoLuongTon.getText().trim().isEmpty() ||
                tfDonGiaNhap.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            String tenNguyenLieu = tfTenNguyenLieu.getText().trim();
            String donViTinh = tfDonViTinh.getText().trim();
            BigDecimal soLuongTon = new BigDecimal(tfSoLuongTon.getText().trim());
            BigDecimal donGiaNhap = new BigDecimal(tfDonGiaNhap.getText().trim());

            // Create or update NguyenLieu
            if (tfMaNguyenLieu.getText().isEmpty()) {
                // Create new NguyenLieu
                NguyenLieu nguyenLieu = new NguyenLieu(0, tenNguyenLieu, donViTinh, soLuongTon, donGiaNhap);
                nguyenLieuDao.insert(nguyenLieu);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient added successfully");
            } else {
                // Update existing NguyenLieu
                int maNguyenLieu = Integer.parseInt(tfMaNguyenLieu.getText());
                NguyenLieu nguyenLieu = new NguyenLieu(maNguyenLieu, tenNguyenLieu, donViTinh, soLuongTon, donGiaNhap);
                nguyenLieuDao.update(nguyenLieu);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient updated successfully");
            }

            // Reload data
            loadData();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving ingredient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteNguyenLieu() {
        if (tfMaNguyenLieu.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No ingredient selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this ingredient?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maNguyenLieu = Integer.parseInt(tfMaNguyenLieu.getText());
                nguyenLieuDao.deleteById(maNguyenLieu);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ingredient deleted successfully");
                loadData();
                clearForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting ingredient: " + e.getMessage());
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
