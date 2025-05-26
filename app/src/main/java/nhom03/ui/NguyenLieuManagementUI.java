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
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.entity.NguyenLieu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class NguyenLieuManagementUI {
    private final NguyenLieuDao nguyenLieuDao;
    private BorderPane view;
    private TableView<NguyenLieu> tableView;
    private ObservableList<NguyenLieu> nguyenLieuList;

    // Form fields
    private TextField tfMaNguyenLieu;
    private TextField tfTenNguyenLieu;
    private TextField tfDonViTinh;
    private TextField tfSoLuongTon;
    private TextField tfDonGiaNhap;
    private TextField tfSearch;

    public NguyenLieuManagementUI(NguyenLieuDao nguyenLieuDao) {
        this.nguyenLieuDao = nguyenLieuDao;
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

        Label titleLabel = new Label("Ingredient Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by name...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchNguyenLieu());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<NguyenLieu, Integer> colMaNguyenLieu = new TableColumn<>("ID");
        colMaNguyenLieu.setCellValueFactory(new PropertyValueFactory<>("maNguyenLieu"));

        TableColumn<NguyenLieu, String> colTenNguyenLieu = new TableColumn<>("Name");
        colTenNguyenLieu.setCellValueFactory(new PropertyValueFactory<>("tenNguyenLieu"));

        TableColumn<NguyenLieu, String> colDonViTinh = new TableColumn<>("Unit");
        colDonViTinh.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));

        TableColumn<NguyenLieu, BigDecimal> colSoLuongTon = new TableColumn<>("Stock");
        colSoLuongTon.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));

        TableColumn<NguyenLieu, BigDecimal> colDonGiaNhap = new TableColumn<>("Price");
        colDonGiaNhap.setCellValueFactory(new PropertyValueFactory<>("donGiaNhap"));

        tableView.getColumns().addAll(colMaNguyenLieu, colTenNguyenLieu, colDonViTinh, colSoLuongTon, colDonGiaNhap);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showNguyenLieuDetails(newSelection);
            }
        });
    }

    private VBox createFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        Label formTitle = new Label("Ingredient Details");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaNguyenLieu = new TextField();
        tfMaNguyenLieu.setEditable(false);
        tfMaNguyenLieu.setPromptText("Auto-generated");

        tfTenNguyenLieu = new TextField();
        tfDonViTinh = new TextField();
        tfSoLuongTon = new TextField();
        tfDonGiaNhap = new TextField();

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaNguyenLieu, 1, row++);

        form.add(new Label("Name:"), 0, row);
        form.add(tfTenNguyenLieu, 1, row++);

        form.add(new Label("Unit:"), 0, row);
        form.add(tfDonViTinh, 1, row++);

        form.add(new Label("Stock:"), 0, row);
        form.add(tfSoLuongTon, 1, row++);

        form.add(new Label("Price:"), 0, row);
        form.add(tfDonGiaNhap, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> saveNguyenLieu());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteNguyenLieu());

        buttonBox.getChildren().addAll(btnNew, btnSave, btnDelete);

        formSection.getChildren().addAll(formTitle, form, buttonBox);

        return formSection;
    }

    private void loadData() {
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        tableView.setItems(nguyenLieuList);
    }

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

    private void clearForm() {
        tfMaNguyenLieu.clear();
        tfTenNguyenLieu.clear();
        tfDonViTinh.clear();
        tfSoLuongTon.clear();
        tfDonGiaNhap.clear();
        tableView.getSelectionModel().clearSelection();
    }

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
