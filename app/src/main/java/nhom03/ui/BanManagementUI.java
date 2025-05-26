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
import nhom03.model.dao.BanDao;
import nhom03.model.entity.Ban;
import nhom03.model.entity.TrangThaiBan;

import java.util.List;
import java.util.Optional;

public class BanManagementUI {
    private final BanDao banDao;
    private BorderPane view;
    private TableView<Ban> tableView;
    private ObservableList<Ban> banList;

    // Form fields
    private TextField tfMaBan;
    private TextField tfTenBan;
    private TextArea taGhiChu;
    private ComboBox<String> cbTrangThai;
    private TextField tfSearch;

    public BanManagementUI(BanDao banDao) {
        this.banDao = banDao;
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

        Label titleLabel = new Label("Table Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        tfSearch = new TextField();
        tfSearch.setPromptText("Search by name...");
        tfSearch.setPrefWidth(300);

        Button btnSearch = new Button("Search");
        btnSearch.setOnAction(e -> searchBan());

        topSection.getChildren().addAll(titleLabel, tfSearch, btnSearch);

        return topSection;
    }

    private void createTableView() {
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Ban, Integer> colMaBan = new TableColumn<>("ID");
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));

        TableColumn<Ban, String> colTenBan = new TableColumn<>("Name");
        colTenBan.setCellValueFactory(new PropertyValueFactory<>("tenBan"));

        TableColumn<Ban, String> colGhiChu = new TableColumn<>("Note");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        TableColumn<Ban, TrangThaiBan> colTrangThai = new TableColumn<>("Status");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaBan, colTenBan, colGhiChu, colTrangThai);

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showBanDetails(newSelection);
            }
        });
    }

    private VBox createFormSection() {
        VBox formSection = new VBox(10);
        formSection.setPadding(new Insets(10));
        formSection.setPrefWidth(300);

        Label formTitle = new Label("Table Details");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        // Create form fields
        tfMaBan = new TextField();
        tfMaBan.setEditable(false);
        tfMaBan.setPromptText("Auto-generated");

        tfTenBan = new TextField();

        taGhiChu = new TextArea();
        taGhiChu.setPrefRowCount(3);

        cbTrangThai = new ComboBox<>();
        cbTrangThai.getItems().addAll("Trống", "Đang sử dụng", "Đặt trước");

        // Add fields to form
        int row = 0;
        form.add(new Label("ID:"), 0, row);
        form.add(tfMaBan, 1, row++);

        form.add(new Label("Name:"), 0, row);
        form.add(tfTenBan, 1, row++);

        form.add(new Label("Note:"), 0, row);
        form.add(taGhiChu, 1, row++);

        form.add(new Label("Status:"), 0, row);
        form.add(cbTrangThai, 1, row++);

        // Create buttons
        HBox buttonBox = new HBox(10);

        Button btnNew = new Button("New");
        btnNew.setOnAction(e -> clearForm());

        Button btnSave = new Button("Save");
        btnSave.setOnAction(e -> saveBan());

        Button btnDelete = new Button("Delete");
        btnDelete.setOnAction(e -> deleteBan());

        buttonBox.getChildren().addAll(btnNew, btnSave, btnDelete);

        formSection.getChildren().addAll(formTitle, form, buttonBox);

        return formSection;
    }

    private void loadData() {
        List<Ban> bans = banDao.findAll();
        banList = FXCollections.observableArrayList(bans);
        tableView.setItems(banList);
    }

    private void searchBan() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        ObservableList<Ban> filteredList = FXCollections.observableArrayList();
        for (Ban ban : banList) {
            if (ban.getTenBan().toLowerCase().contains(searchText)) {
                filteredList.add(ban);
            }
        }

        tableView.setItems(filteredList);
    }

    private void showBanDetails(Ban ban) {
        tfMaBan.setText(String.valueOf(ban.getMaBan()));
        tfTenBan.setText(ban.getTenBan());
        taGhiChu.setText(ban.getGhiChu());

        switch (ban.getTrangThai()) {
            case TRONG -> cbTrangThai.setValue("Trống");
            case DANG_SU_DUNG -> cbTrangThai.setValue("Đang sử dụng");
            case DAT_TRUOC -> cbTrangThai.setValue("Đặt trước");
        }
    }

    private void clearForm() {
        tfMaBan.clear();
        tfTenBan.clear();
        taGhiChu.clear();
        cbTrangThai.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private void saveBan() {
        try {
            // Validate input
            if (tfTenBan.getText().trim().isEmpty() || cbTrangThai.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields");
                return;
            }

            // Parse values
            String tenBan = tfTenBan.getText().trim();
            String ghiChu = taGhiChu.getText().trim();
            TrangThaiBan trangThai = TrangThaiBan.fromString(cbTrangThai.getValue());

            // Create or update Ban
            if (tfMaBan.getText().isEmpty()) {
                // Create new Ban
                Ban ban = new Ban(0, tenBan, ghiChu, trangThai);
                banDao.insert(ban);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table added successfully");
            } else {
                // Update existing Ban
                int maBan = Integer.parseInt(tfMaBan.getText());
                Ban ban = new Ban(maBan, tenBan, ghiChu, trangThai);
                banDao.update(ban);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table updated successfully");
            }

            // Reload data
            loadData();
            clearForm();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error saving table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteBan() {
        if (tfMaBan.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No table selected");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
            "Are you sure you want to delete this table?",
            ButtonType.YES, ButtonType.NO);
        confirmDialog.setTitle("Confirm Delete");
        Optional<ButtonType> result = confirmDialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                int maBan = Integer.parseInt(tfMaBan.getText());
                banDao.deleteById(maBan);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Table deleted successfully");
                loadData();
                clearForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting table: " + e.getMessage());
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
