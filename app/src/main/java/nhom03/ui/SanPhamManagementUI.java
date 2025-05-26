package nhom03.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.LoaiSanPham;
import nhom03.model.entity.SanPham;
import nhom03.model.entity.TrangThaiSanPham;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class SanPhamManagementUI {
    private final SanPhamDao sanPhamDao;
    private BorderPane view;
    private TableView<SanPham> tableView;
    private ObservableList<SanPham> sanPhamList;

    // Form fields
    private TextField tfMaSanPham;
    private TextField tfTenSanPham;
    private ComboBox<String> cbLoaiSanPham;
    private TextField tfDonGia;
    private TextArea taMoTa;
    private TextField tfHinhAnh;
    private ComboBox<String> cbTrangThai;
    private TextField tfSearch;

    public SanPhamManagementUI(SanPhamDao sanPhamDao) {
        this.sanPhamDao = sanPhamDao;
        createView();
        loadData();
    }

    public BorderPane getView() {
        return view;
    }

    private void createView() {
        view = new BorderPane();

        // Create top section with title and search
        HBox topSection = createTopSection();
        view.setTop(topSection);

        // Create table view
        createTableView();
        view.setCenter(tableView);

        // Create form
        VBox formSection = createFormSection();
        view.setRight(formSection);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Food/Drinks Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by name...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchSanPham());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SanPham, Integer> colMaSanPham = new TableColumn<>("ID");
        colMaSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));

        TableColumn<SanPham, String> colTenSanPham = new TableColumn<>("Name");
        colTenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));

        TableColumn<SanPham, LoaiSanPham> colLoaiSanPham = new TableColumn<>("Type");
        colLoaiSanPham.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));

        TableColumn<SanPham, BigDecimal> colDonGia = new TableColumn<>("Price");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        TableColumn<SanPham, TrangThaiSanPham> colTrangThai = new TableColumn<>("Status");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaSanPham, colTenSanPham, colLoaiSanPham, colDonGia, colTrangThai);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showSanPhamDetails(newSelection);
            }
        });
    }

    private VBox createFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        Label formTitle = new Label("Product Details");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaSanPham = new TextField();
        tfMaSanPham.setEditable(false);
        tfMaSanPham.setPromptText("Auto-generated");

        tfTenSanPham = new TextField();

        cbLoaiSanPham = new ComboBox<>();
        cbLoaiSanPham.getItems().addAll("Đồ uống", "Đồ ăn", "Khác");

        tfDonGia = new TextField();

        taMoTa = new TextArea();
        taMoTa.setPrefRowCount(3);

        tfHinhAnh = new TextField();

        cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("Còn", "Hết", "Ngưng");

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaSanPham, 1, row++);

        form.add(new Label("Name:"), 0, row);
        form.add(tfTenSanPham, 1, row++);

        form.add(new Label("Type:"), 0, row);
        form.add(cbLoaiSanPham, 1, row++);

        form.add(new Label("Price:"), 0, row);
        form.add(tfDonGia, 1, row++);

        form.add(new Label("Description:"), 0, row);
        form.add(taMoTa, 1, row++);

        form.add(new Label("Image:"), 0, row);
        form.add(tfHinhAnh, 1, row++);

        form.add(new Label("Status:"), 0, row);
        form.add(cbTrangThai, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> saveSanPham());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteSanPham());

        buttonBox.getChildren().addAll(btnNew, btnSave, btnDelete);

        formSection.getChildren().addAll(formTitle, form, buttonBox);

        return formSection;
    }

    private void loadData() {
        List<SanPham> sanPhams = sanPhamDao.findAll();
        sanPhamList = FXCollections.observableArrayList(sanPhams);
        tableView.setItems(sanPhamList);
    }

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

        switch (sanPham.getTrangThai()) {
            case CON -> cbTrangThai.setValue("Còn");
            case HET -> cbTrangThai.setValue("Hết");
            case NGUNG -> cbTrangThai.setValue("Ngưng");
        }
    }

    private void clearForm() {
        tfMaSanPham.clear();
        tfTenSanPham.clear();
        cbLoaiSanPham.setValue(null);
        tfDonGia.clear();
        taMoTa.clear();
        tfHinhAnh.clear();
        cbTrangThai.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

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
}
