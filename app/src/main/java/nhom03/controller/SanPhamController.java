package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.LoaiSanPham;
import nhom03.model.entity.SanPham;
import nhom03.model.entity.TrangThaiSanPham;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SanPhamController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<SanPham> tableView;

    @FXML
    private TextField tfMaSanPham;

    @FXML
    private TextField tfTenSanPham;

    @FXML
    private ComboBox<String> cbLoaiSanPham;

    @FXML
    private TextField tfDonGia;

    @FXML
    private TextArea taMoTa;

    @FXML
    private TextField tfHinhAnh;

    @FXML
    private ComboBox<String> cbTrangThai;

    @FXML
    private ImageView ivHinhAnh;

    private SanPhamDao sanPhamDao;
    private ObservableList<SanPham> sanPhamList;

    public void initialize() {
        // Initialize table columns
        TableColumn<SanPham, Integer> colMaSanPham = new TableColumn<>("Mã");
        colMaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));

        TableColumn<SanPham, String> colTenSanPham = new TableColumn<>("Tên");
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));

        TableColumn<SanPham, LoaiSanPham> colLoaiSanPham = new TableColumn<>("Loại");
        colLoaiSanPham.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));

        TableColumn<SanPham, BigDecimal> colDonGia = new TableColumn<>("Đơn giá");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        TableColumn<SanPham, TrangThaiSanPham> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaSanPham, colTenSanPham, colLoaiSanPham, colDonGia, colTrangThai);

        // Initialize combo boxes
        cbLoaiSanPham.getItems().addAll("Đồ uống", "Đồ ăn", "Khác");
        cbTrangThai.getItems().addAll("Còn", "Hết", "Ngưng");

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showSanPhamDetails(newSelection);
            }
        });

        // Add listener to image path text field to update image when path changes
        tfHinhAnh.textProperty().addListener((observable, oldValue, newValue) -> {
            loadProductImage(newValue);
        });
    }

    public void setSanPhamDao(SanPhamDao sanPhamDao) {
        this.sanPhamDao = sanPhamDao;
    }

    public void loadData() {
        List<SanPham> sanPhams = sanPhamDao.findAll();
        sanPhamList = FXCollections.observableArrayList(sanPhams);
        tableView.setItems(sanPhamList);
    }

    @FXML
    private void searchSanPham() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<SanPham> filteredList = FXCollections.observableArrayList();
        for (SanPham sanPham : sanPhamList) {
            if (sanPham.getTenSanPham().toLowerCase().contains(searchText)) {
                filteredList.add(sanPham);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showSanPhamDetails(SanPham sanPham) {
        tfMaSanPham.setText(String.valueOf(sanPham.getMaSanPham()));
        tfTenSanPham.setText(sanPham.getTenSanPham());

        switch (sanPham.getLoaiSanPham()) {
            case DO_UONG -> cbLoaiSanPham.setValue("Đồ uống");
            case DO_AN -> cbLoaiSanPham.setValue("Đồ ăn");
            case KHAC -> cbLoaiSanPham.setValue("Khác");
        }

        tfDonGia.setText(sanPham.getDonGia().toString());
        taMoTa.setText(sanPham.getMoTa());
        tfHinhAnh.setText(sanPham.getHinhAnh());

        // Load and display the image
        loadProductImage(sanPham.getHinhAnh());

        switch (sanPham.getTrangThai()) {
            case CON -> cbTrangThai.setValue("Còn");
            case HET -> cbTrangThai.setValue("Hết");
            case NGUNG -> cbTrangThai.setValue("Ngưng");
        }
    }

    private void loadProductImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    ivHinhAnh.setImage(image);
                } else {
                    // Clear the image if file doesn't exist
                    ivHinhAnh.setImage(null);
                }
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                ivHinhAnh.setImage(null);
            }
        } else {
            // Clear the image if path is null or empty
            ivHinhAnh.setImage(null);
        }
    }

    @FXML
    private void clearForm() {
        tfMaSanPham.clear();
        tfTenSanPham.clear();
        cbLoaiSanPham.setValue(null);
        tfDonGia.clear();
        taMoTa.clear();
        tfHinhAnh.clear();
        cbTrangThai.setValue(null);
        // Clear the image
        ivHinhAnh.setImage(null);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void saveSanPham() {
        try {
            // Validate input
            if (tfTenSanPham.getText().trim().isEmpty() ||
                cbLoaiSanPham.getValue() == null ||
                tfDonGia.getText().trim().isEmpty() ||
                cbTrangThai.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            String tenSanPham = tfTenSanPham.getText().trim();
            LoaiSanPham loaiSanPham = LoaiSanPham.fromString(cbLoaiSanPham.getValue());
            BigDecimal donGia = new BigDecimal(tfDonGia.getText().trim());
            String moTa = taMoTa.getText().trim();
            String hinhAnh = tfHinhAnh.getText().trim();
            TrangThaiSanPham trangThai = TrangThaiSanPham.fromString(cbTrangThai.getValue());

            // Create or update SanPham
            if (tfMaSanPham.getText().isEmpty()) {
                // Create new SanPham
                SanPham sanPham = new SanPham(0, tenSanPham, loaiSanPham, donGia, moTa, hinhAnh, trangThai);
                sanPhamDao.insert(sanPham);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully");
            } else {
                // Update existing SanPham
                int maSanPham = Integer.parseInt(tfMaSanPham.getText());
                SanPham sanPham = new SanPham(maSanPham, tenSanPham, loaiSanPham, donGia, moTa, hinhAnh, trangThai);
                sanPhamDao.update(sanPham);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully");
            }

            // Reload data
            loadData();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid price format");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteSanPham() {
        if (tfMaSanPham.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No product selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this product?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maSanPham = Integer.parseInt(tfMaSanPham.getText());
                sanPhamDao.deleteById(maSanPham);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully");
                loadData();
                clearForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting product: " + e.getMessage());
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

    @FXML
    private void browseImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");

        // Set initial directory (optional)
        String currentPath = tfHinhAnh.getText();
        if (currentPath != null && !currentPath.isEmpty()) {
            File currentFile = new File(currentPath);
            if (currentFile.exists()) {
                fileChooser.setInitialDirectory(currentFile.getParentFile());
            }
        }

        // Add file extension filters
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Show the dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(tfHinhAnh.getScene().getWindow());

        // Update the image path text field with the selected file path
        if (selectedFile != null) {
            tfHinhAnh.setText(selectedFile.getAbsolutePath());
        }
    }
}
