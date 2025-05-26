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
import javafx.util.StringConverter;
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

public class PhieuNhapKhoManagementUI {
    private final PhieuNhapKhoDao phieuNhapKhoDao;
    private final NguyenLieuDao nguyenLieuDao;
    private BorderPane view;
    private TableView<PhieuNhapKho> tableView;
    private TableView<ChiTietPhieuNhap> detailsTableView;
    private ObservableList<PhieuNhapKho> phieuNhapList;
    private ObservableList<ChiTietPhieuNhap> chiTietList;
    private ObservableList<NguyenLieu> nguyenLieuList;

    // Form fields
    private TextField tfMaPhieuNhap;
    private DatePicker dpNgayNhap;
    private TextField tfTongTien;
    private TextArea taGhiChu;
    private TextField tfSearch;

    // Import detail form fields
    private ComboBox<NguyenLieu> cbNguyenLieu;
    private TextField tfSoLuong;
    private TextField tfDonGia;

    public PhieuNhapKhoManagementUI(PhieuNhapKhoDao phieuNhapKhoDao, NguyenLieuDao nguyenLieuDao) {
        this.phieuNhapKhoDao = phieuNhapKhoDao;
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

        // Create center section with tables
        SplitPane splitPane = new SplitPane();

        // Create import table
        VBox importSection = new VBox(10);
        importSection.setPadding(new Insets(10));
        Label importLabel = new Label("Import Receipts");
        importLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createImportTableView();
        importSection.getChildren().addAll(importLabel, tableView);

        // Create import details table
        VBox detailsSection = new VBox(10);
        detailsSection.setPadding(new Insets(10));
        Label detailsLabel = new Label("Import Details");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createDetailsTableView();
        detailsSection.getChildren().addAll(detailsLabel, detailsTableView);

        splitPane.getItems().addAll(importSection, detailsSection);
        splitPane.setDividerPositions(0.6);
        view.setCenter(splitPane);

        // Create form
        TabPane formPane = new TabPane();
        formPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab importTab = new Tab("Import Receipt");
        importTab.setContent(createImportFormSection());

        Tab detailsTab = new Tab("Import Details");
        detailsTab.setContent(createDetailsFormSection());

        formPane.getTabs().addAll(importTab, detailsTab);
        view.setRight(formPane);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Ingredient Import Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by date...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchPhieuNhap());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createImportTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PhieuNhapKho, Integer> colMaPhieuNhap = new TableColumn<>("ID");
        colMaPhieuNhap.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));

        TableColumn<PhieuNhapKho, LocalDateTime> colNgayNhap = new TableColumn<>("Date");
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

        TableColumn<PhieuNhapKho, BigDecimal> colTongTien = new TableColumn<>("Total");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        TableColumn<PhieuNhapKho, String> colGhiChu = new TableColumn<>("Note");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        tableView.getColumns().addAll(colMaPhieuNhap, colNgayNhap, colTongTien, colGhiChu);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPhieuNhapDetails(newSelection);
                loadImportDetails(newSelection);
            }
        });
    }

    private void createDetailsTableView() {
        detailsTableView = new TableView<>();
        detailsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChiTietPhieuNhap, Integer> colMaChiTietNhap = new TableColumn<>("ID");
        colMaChiTietNhap.setCellValueFactory(new PropertyValueFactory<>("maChiTietNhap"));

        TableColumn<ChiTietPhieuNhap, NguyenLieu> colNguyenLieu = new TableColumn<>("Ingredient");
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

        TableColumn<ChiTietPhieuNhap, BigDecimal> colSoLuong = new TableColumn<>("Quantity");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietPhieuNhap, BigDecimal> colDonGia = new TableColumn<>("Price");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        detailsTableView.getColumns().addAll(colMaChiTietNhap, colNguyenLieu, colSoLuong, colDonGia);

        // Add selection listener
        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    private VBox createImportFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaPhieuNhap = new TextField();
        tfMaPhieuNhap.setEditable(false);
        tfMaPhieuNhap.setPromptText("Auto-generated");

        dpNgayNhap = new DatePicker();

        tfTongTien = new TextField();
        tfTongTien.setEditable(false);

        taGhiChu = new TextArea();
        taGhiChu.setPrefRowCount(3);

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaPhieuNhap, 1, row++);

        form.add(new Label("Date:"), 0, row);
        form.add(dpNgayNhap, 1, row++);

        form.add(new Label("Total:"), 0, row);
        form.add(tfTongTien, 1, row++);

        form.add(new Label("Note:"), 0, row);
        form.add(taGhiChu, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearImportForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> savePhieuNhap());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deletePhieuNhap());

        buttonBox.getChildren().addAll(btnNew, btnSave, btnDelete);

        formSection.getChildren().addAll(form, buttonBox);

        return formSection;
    }

    private VBox createDetailsFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        cbNguyenLieu = new ComboBox<>();
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

        tfSoLuong = new TextField();
        tfDonGia = new TextField();

        // Add fields to form
        int row = 0;
        form.add(new Label("Ingredient:"), 0, row);
        form.add(cbNguyenLieu, 1, row++);

        form.add(new Label("Quantity:"), 0, row);
        form.add(tfSoLuong, 1, row++);

        form.add(new Label("Price:"), 0, row);
        form.add(tfDonGia, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnAddDetail = new Button("Add");
        btnAddDetail.setOnAction(e -> addImportDetail());

        Button btnRemoveDetail = new Button("Remove");
        btnRemoveDetail.setOnAction(e -> removeImportDetail());

        buttonBox.getChildren().addAll(btnAddDetail, btnRemoveDetail);

        formSection.getChildren().addAll(form, buttonBox);

        return formSection;
    }

    private void loadData() {
        // Load imports
        List<PhieuNhapKho> phieuNhaps = phieuNhapKhoDao.findAll();
        phieuNhapList = FXCollections.observableArrayList(phieuNhaps);
        tableView.setItems(phieuNhapList);

        // Load ingredients
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        cbNguyenLieu.setItems(nguyenLieuList);
    }

    private void loadImportDetails(PhieuNhapKho phieuNhap) {
        // In a real application, you would load the import details from the database
        // For this example, we'll just clear the table
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

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
        // dpNgayNhap.setValue(phieuNhap.getNgayNhap().toLocalDate());
        tfTongTien.setText(phieuNhap.getTongTien().toString());
        taGhiChu.setText(phieuNhap.getGhiChu());
    }

    private void showChiTietDetails(ChiTietPhieuNhap chiTiet) {
        cbNguyenLieu.setValue(chiTiet.getNguyenLieu());
        tfSoLuong.setText(chiTiet.getSoLuong().toString());
        tfDonGia.setText(chiTiet.getDonGia().toString());
    }

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
