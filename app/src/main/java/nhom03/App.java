package nhom03;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nhom03.model.dao.*;
import nhom03.model.dao.impl.*;
import nhom03.ui.*;

public class App extends Application {
    private BorderPane mainLayout;
    private SanPhamDao sanPhamDao;
    private BanDao banDao;
    private HoaDonDao hoaDonDao;
    private NguyenLieuDao nguyenLieuDao;
    private PhieuNhapKhoDao phieuNhapKhoDao;
    private PhieuXuatKhoDao phieuXuatKhoDao;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // Initialize DAOs
        sanPhamDao = new SanPhamJdbc();
        banDao = new BanJdbc();
        hoaDonDao = new HoaDonJdbc();
        nguyenLieuDao = new NguyenLieuJdbc();
        phieuNhapKhoDao = new PhieuNhapKhoJdbc();
        phieuXuatKhoDao = new PhieuXuatKhoJdbc();

        // Create main layout
        mainLayout = new BorderPane();

        // Create navigation sidebar
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);

        // Set default content
        Label welcomeLabel = new Label("Welcome to Coffee Shop Management System");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        mainLayout.setCenter(welcomeLabel);

        // Create scene
        Scene scene = new Scene(mainLayout, 1200, 800);
        stage.setTitle("Coffee Shop Management System");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #f0f0f0;");

        Label menuLabel = new Label("MENU");
        menuLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button btnSanPham = new Button("Food/Drinks Management");
        btnSanPham.setMaxWidth(Double.MAX_VALUE);
        btnSanPham.setOnAction(e -> showSanPhamManagement());

        Button btnBan = new Button("Table Management");
        btnBan.setMaxWidth(Double.MAX_VALUE);
        btnBan.setOnAction(e -> showBanManagement());

        Button btnHoaDon = new Button("Order Management");
        btnHoaDon.setMaxWidth(Double.MAX_VALUE);
        btnHoaDon.setOnAction(e -> showHoaDonManagement());

        Button btnNguyenLieu = new Button("Ingredient Management");
        btnNguyenLieu.setMaxWidth(Double.MAX_VALUE);
        btnNguyenLieu.setOnAction(e -> showNguyenLieuManagement());

        Button btnPhieuNhap = new Button("Import Management");
        btnPhieuNhap.setMaxWidth(Double.MAX_VALUE);
        btnPhieuNhap.setOnAction(e -> showPhieuNhapManagement());

        Button btnPhieuXuat = new Button("Export Management");
        btnPhieuXuat.setMaxWidth(Double.MAX_VALUE);
        btnPhieuXuat.setOnAction(e -> showPhieuXuatManagement());

        sidebar.getChildren().addAll(
            menuLabel,
            btnSanPham,
            btnBan,
            btnHoaDon,
            btnNguyenLieu,
            btnPhieuNhap,
            btnPhieuXuat
        );

        return sidebar;
    }

    private void showSanPhamManagement() {
        SanPhamManagementUI sanPhamUI = new SanPhamManagementUI(sanPhamDao);
        mainLayout.setCenter(sanPhamUI.getView());
    }

    private void showBanManagement() {
        BanManagementUI banUI = new BanManagementUI(banDao);
        mainLayout.setCenter(banUI.getView());
    }

    private void showHoaDonManagement() {
        HoaDonManagementUI hoaDonUI = new HoaDonManagementUI(hoaDonDao, banDao, sanPhamDao);
        mainLayout.setCenter(hoaDonUI.getView());
    }

    private void showNguyenLieuManagement() {
        NguyenLieuManagementUI nguyenLieuUI = new NguyenLieuManagementUI(nguyenLieuDao);
        mainLayout.setCenter(nguyenLieuUI.getView());
    }

    private void showPhieuNhapManagement() {
        PhieuNhapKhoManagementUI phieuNhapUI = new PhieuNhapKhoManagementUI(phieuNhapKhoDao, nguyenLieuDao);
        mainLayout.setCenter(phieuNhapUI.getView());
    }

    private void showPhieuXuatManagement() {
        PhieuXuatKhoManagementUI phieuXuatUI = new PhieuXuatKhoManagementUI(phieuXuatKhoDao, nguyenLieuDao);
        mainLayout.setCenter(phieuXuatUI.getView());
    }
}
