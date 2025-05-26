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
import nhom03.model.dao.PhieuXuatKhoDao;
import nhom03.model.entity.ChiTietPhieuXuat;
import nhom03.model.entity.NguyenLieu;
import nhom03.model.entity.PhieuXuatKho;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class PhieuXuatKhoManagementUI {
    private final PhieuXuatKhoDao phieuXuatKhoDao;
    private final NguyenLieuDao nguyenLieuDao;
    private BorderPane view;
    private TableView<PhieuXuatKho> tableView;
    private TableView<ChiTietPhieuXuat> detailsTableView;
    private ObservableList<PhieuXuatKho> phieuXuatList;
    private ObservableList<ChiTietPhieuXuat> chiTietList;
    private ObservableList<NguyenLieu> nguyenLieuList;

    // Form fields
    private TextField tfMaPhieuXuat;
    private DatePicker dpNgayXuat;
    private TextField tfLyDo;
    private TextArea taGhiChu;
    private TextField tfSearch;

    // Export detail form fields
    private ComboBox<NguyenLieu> cbNguyenLieu;
    private TextField tfSoLuong;

    public PhieuXuatKhoManagementUI(PhieuXuatKhoDao phieuXuatKhoDao, NguyenLieuDao nguyenLieuDao) {
        this.phieuXuatKhoDao = phieuXuatKhoDao;
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

        // Create export table
        VBox exportSection = new VBox(10);
        exportSection.setPadding(new Insets(10));
        Label exportLabel = new Label("Export Receipts");
        exportLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createExportTableView();
        exportSection.getChildren().addAll(exportLabel, tableView);

        // Create export details table
        VBox detailsSection = new VBox(10);
        detailsSection.setPadding(new Insets(10));
        Label detailsLabel = new Label("Export Details");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createDetailsTableView();
        detailsSection.getChildren().addAll(detailsLabel, detailsTableView);

        splitPane.getItems().addAll(exportSection, detailsSection);
        splitPane.setDividerPositions(0.6);
        view.setCenter(splitPane);

        // Create form
        TabPane formPane = new TabPane();
        formPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab exportTab = new Tab("Export Receipt");
        exportTab.setContent(createExportFormSection());

        Tab detailsTab = new Tab("Export Details");
        detailsTab.setContent(createDetailsFormSection());

        formPane.getTabs().addAll(exportTab, detailsTab);
        view.setRight(formPane);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Ingredient Export Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by date...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchPhieuXuat());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createExportTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PhieuXuatKho, Integer> colMaPhieuXuat = new TableColumn<>("ID");
        colMaPhieuXuat.setCellValueFactory(new PropertyValueFactory<>("maPhieuXuat"));

        TableColumn<PhieuXuatKho, LocalDateTime> colNgayXuat = new TableColumn<>("Date");
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

        TableColumn<PhieuXuatKho, String> colLyDo = new TableColumn<>("Reason");
        colLyDo.setCellValueFactory(new PropertyValueFactory<>("lyDo"));

        TableColumn<PhieuXuatKho, String> colGhiChu = new TableColumn<>("Note");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        tableView.getColumns().addAll(colMaPhieuXuat, colNgayXuat, colLyDo, colGhiChu);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPhieuXuatDetails(newSelection);
                loadExportDetails(newSelection);
            }
        });
    }

    private void createDetailsTableView() {
        detailsTableView = new TableView<>();
        detailsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChiTietPhieuXuat, Integer> colMaChiTietXuat = new TableColumn<>("ID");
        colMaChiTietXuat.setCellValueFactory(new PropertyValueFactory<>("maChiTietXuat"));

        TableColumn<ChiTietPhieuXuat, NguyenLieu> colNguyenLieu = new TableColumn<>("Ingredient");
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

        TableColumn<ChiTietPhieuXuat, BigDecimal> colSoLuong = new TableColumn<>("Quantity");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        detailsTableView.getColumns().addAll(colMaChiTietXuat, colNguyenLieu, colSoLuong);

        // Add selection listener
        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    private VBox createExportFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaPhieuXuat = new TextField();
        tfMaPhieuXuat.setEditable(false);
        tfMaPhieuXuat.setPromptText("Auto-generated");

        dpNgayXuat = new DatePicker();

        tfLyDo = new TextField();

        taGhiChu = new TextArea();
        taGhiChu.setPrefRowCount(3);

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaPhieuXuat, 1, row++);

        form.add(new Label("Date:"), 0, row);
        form.add(dpNgayXuat, 1, row++);

        form.add(new Label("Reason:"), 0, row);
        form.add(tfLyDo, 1, row++);

        form.add(new Label("Note:"), 0, row);
        form.add(taGhiChu, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearExportForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> savePhieuXuat());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deletePhieuXuat());

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

        // Add fields to form
        int row = 0;
        form.add(new Label("Ingredient:"), 0, row);
        form.add(cbNguyenLieu, 1, row++);

        form.add(new Label("Quantity:"), 0, row);
        form.add(tfSoLuong, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnAddDetail = new Button("Add");
        btnAddDetail.setOnAction(e -> addExportDetail());

        Button btnRemoveDetail = new Button("Remove");
        btnRemoveDetail.setOnAction(e -> removeExportDetail());

        buttonBox.getChildren().addAll(btnAddDetail, btnRemoveDetail);

        formSection.getChildren().addAll(form, buttonBox);

        return formSection;
    }

    private void loadData() {
        // Load exports
        List<PhieuXuatKho> phieuXuats = phieuXuatKhoDao.findAll();
        phieuXuatList = FXCollections.observableArrayList(phieuXuats);
        tableView.setItems(phieuXuatList);

        // Load ingredients
        List<NguyenLieu> nguyenLieus = nguyenLieuDao.findAll();
        nguyenLieuList = FXCollections.observableArrayList(nguyenLieus);
        cbNguyenLieu.setItems(nguyenLieuList);
    }

    private void loadExportDetails(PhieuXuatKho phieuXuat) {
        // In a real application, you would load the export details from the database
        // For this example, we'll just clear the table
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

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
        // dpNgayXuat.setValue(phieuXuat.getNgayXuat().toLocalDate());
        tfLyDo.setText(phieuXuat.getLyDo());
        taGhiChu.setText(phieuXuat.getGhiChu());
    }

    private void showChiTietDetails(ChiTietPhieuXuat chiTiet) {
        cbNguyenLieu.setValue(chiTiet.getNguyenLieu());
        tfSoLuong.setText(chiTiet.getSoLuong().toString());
    }

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

    private void addExportDetail() {
        // In a real application, you would add the export detail to the database
        // For this example, we'll just add it to the table
        try {
            // Validate input
            if (tfMaPhieuXuat.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please save the export receipt first");
                return;
            }

            if (cbNguyenLieu.getValue() == null || tfSoLuong.getText().trim().isEmpty()) {
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
