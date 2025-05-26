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
import nhom03.model.dao.BanDao;
import nhom03.model.dao.HoaDonDao;
import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class HoaDonManagementUI {
    private final HoaDonDao hoaDonDao;
    private final BanDao banDao;
    private final SanPhamDao sanPhamDao;
    private BorderPane view;
    private TableView<HoaDon> tableView;
    private TableView<ChiTietHoaDon> detailsTableView;
    private ObservableList<HoaDon> hoaDonList;
    private ObservableList<ChiTietHoaDon> chiTietList;
    private ObservableList<Ban> banList;
    private ObservableList<SanPham> sanPhamList;

    // Form fields
    private TextField tfMaHoaDon;
    private ComboBox<Ban> cbBan;
    private DatePicker dpNgayLap;
    private TextField tfTongTien;
    private TextField tfGiamGia;
    private ComboBox<String> cbTrangThai;
    private TextField tfSearch;

    // Order detail form fields
    private ComboBox<SanPham> cbSanPham;
    private TextField tfSoLuong;
    private TextField tfDonGia;
    private TextArea taGhiChu;

    public HoaDonManagementUI(HoaDonDao hoaDonDao, BanDao banDao, SanPhamDao sanPhamDao) {
        this.hoaDonDao = hoaDonDao;
        this.banDao = banDao;
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

        // Create center section with tables
        SplitPane splitPane = new SplitPane();

        // Create order table
        VBox orderSection = new VBox(10);
        orderSection.setPadding(new Insets(10));
        Label orderLabel = new Label("Orders");
        orderLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createOrderTableView();
        orderSection.getChildren().addAll(orderLabel, tableView);

        // Create order details table
        VBox detailsSection = new VBox(10);
        detailsSection.setPadding(new Insets(10));
        Label detailsLabel = new Label("Order Details");
        detailsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        createDetailsTableView();
        detailsSection.getChildren().addAll(detailsLabel, detailsTableView);

        splitPane.getItems().addAll(orderSection, detailsSection);
        splitPane.setDividerPositions(0.6);
        view.setCenter(splitPane);

        // Create form
        TabPane formPane = new TabPane();
        formPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab orderTab = new Tab("Order");
        orderTab.setContent(createOrderFormSection());

        Tab detailsTab = new Tab("Order Details");
        detailsTab.setContent(createDetailsFormSection());

        formPane.getTabs().addAll(orderTab, detailsTab);
        view.setRight(formPane);
    }

    private HBox createTopSection() {
        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));

        Label titleLabel = new Label("Order Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by table name...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchHoaDon());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createOrderTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<HoaDon, Integer> colMaHoaDon = new TableColumn<>("ID");
        colMaHoaDon.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));

        TableColumn<HoaDon, Ban> colBan = new TableColumn<>("Table");
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

        TableColumn<HoaDon, LocalDateTime> colNgayLap = new TableColumn<>("Date");
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

        TableColumn<HoaDon, BigDecimal> colTongTien = new TableColumn<>("Total");
        colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        TableColumn<HoaDon, TrangThaiHoaDon> colTrangThai = new TableColumn<>("Status");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaHoaDon, colBan, colNgayLap, colTongTien, colTrangThai);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showHoaDonDetails(newSelection);
                loadOrderDetails(newSelection);
            }
        });
    }

    private void createDetailsTableView() {
        detailsTableView = new TableView<>();
        detailsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ChiTietHoaDon, Integer> colMaChiTiet = new TableColumn<>("ID");
        colMaChiTiet.setCellValueFactory(new PropertyValueFactory<>("maChiTiet"));

        TableColumn<ChiTietHoaDon, SanPham> colSanPham = new TableColumn<>("Product");
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

        TableColumn<ChiTietHoaDon, Integer> colSoLuong = new TableColumn<>("Quantity");
        colSoLuong.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        TableColumn<ChiTietHoaDon, BigDecimal> colDonGia = new TableColumn<>("Price");
        colDonGia.setCellValueFactory(new PropertyValueFactory<>("donGia"));

        TableColumn<ChiTietHoaDon, String> colGhiChu = new TableColumn<>("Note");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        detailsTableView.getColumns().addAll(colMaChiTiet, colSanPham, colSoLuong, colDonGia, colGhiChu);

        // Add selection listener
        detailsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showChiTietDetails(newSelection);
            }
        });
    }

    private VBox createOrderFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaHoaDon = new TextField();
        tfMaHoaDon.setEditable(false);
        tfMaHoaDon.setPromptText("Auto-generated");

        cbBan = new ComboBox<>();
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

        dpNgayLap = new DatePicker();

        tfTongTien = new TextField();
        tfTongTien.setEditable(false);

        tfGiamGia = new TextField();

        cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("Chưa thanh toán", "Đã thanh toán", "Hủy");

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaHoaDon, 1, row++);

        form.add(new Label("Table:"), 0, row);
        form.add(cbBan, 1, row++);

        form.add(new Label("Date:"), 0, row);
        form.add(dpNgayLap, 1, row++);

        form.add(new Label("Total:"), 0, row);
        form.add(tfTongTien, 1, row++);

        form.add(new Label("Discount:"), 0, row);
        form.add(tfGiamGia, 1, row++);

        form.add(new Label("Status:"), 0, row);
        form.add(cbTrangThai, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearOrderForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> saveHoaDon());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteHoaDon());

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
        cbSanPham = new ComboBox<>();
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

        tfSoLuong = new TextField();
        tfDonGia = new TextField();
        taGhiChu = new TextArea();
        taGhiChu.setPrefRowCount(3);

        // Add fields to form
        int row = 0;
        form.add(new Label("Product:"), 0, row);
        form.add(cbSanPham, 1, row++);

        form.add(new Label("Quantity:"), 0, row);
        form.add(tfSoLuong, 1, row++);

        form.add(new Label("Price:"), 0, row);
        form.add(tfDonGia, 1, row++);

        form.add(new Label("Note:"), 0, row);
        form.add(taGhiChu, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnAddDetail = new Button("Add");
        btnAddDetail.setOnAction(e -> addOrderDetail());

        Button btnRemoveDetail = new Button("Remove");
        btnRemoveDetail.setOnAction(e -> removeOrderDetail());

        buttonBox.getChildren().addAll(btnAddDetail, btnRemoveDetail);

        formSection.getChildren().addAll(form, buttonBox);

        return formSection;
    }

    private void loadData() {
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
    }

    private void loadOrderDetails(HoaDon hoaDon) {
        // In a real application, you would load the order details from the database
        // For this example, we'll just clear the table
        chiTietList = FXCollections.observableArrayList();
        detailsTableView.setItems(chiTietList);
    }

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
        // dpNgayLap.setValue(hoaDon.getNgayLap().toLocalDate());
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
