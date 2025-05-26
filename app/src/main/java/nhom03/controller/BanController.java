package nhom03.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import nhom03.model.dao.BanDao;
import nhom03.model.entity.Ban;
import nhom03.model.entity.TrangThaiBan;

import java.util.List;
import java.util.Optional;

public class BanController {
    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<Ban> tableView;

    @FXML
    private TextField tfMaBan;

    @FXML
    private TextField tfTenBan;

    @FXML
    private TextArea taGhiChu;

    @FXML
    private ComboBox<String> cbTrangThai;

    private BanDao banDao;
    private ObservableList<Ban> banList;

    public void initialize() {
        // Initialize table columns
        TableColumn<Ban, Integer> colMaBan = new TableColumn<>("Mã");
        colMaBan.setCellValueFactory(new PropertyValueFactory<>("maBan"));

        TableColumn<Ban, String> colTenBan = new TableColumn<>("Tên");
        colTenBan.setCellValueFactory(new PropertyValueFactory<>("tenBan"));

        TableColumn<Ban, String> colGhiChu = new TableColumn<>("Ghi chú");
        colGhiChu.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

        TableColumn<Ban, TrangThaiBan> colTrangThai = new TableColumn<>("Trạng thái");
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        tableView.getColumns().addAll(colMaBan, colTenBan, colGhiChu, colTrangThai);

        // Initialize combo box
        cbTrangThai.getItems().addAll("Trống", "Đang sử dụng", "Đặt trước");

        // Add selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showBanDetails(newSelection);
            }
        });
    }

    public void setBanDao(BanDao banDao) {
        this.banDao = banDao;
    }

    public void loadData() {
        List<Ban> bans = banDao.findAll();
        banList = FXCollections.observableArrayList(bans);
        tableView.setItems(banList);
    }

    @FXML
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

    @FXML
    private void clearForm() {
        tfMaBan.clear();
        tfTenBan.clear();
        taGhiChu.clear();
        cbTrangThai.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
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

    @FXML
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
