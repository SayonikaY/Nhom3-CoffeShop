package nhom03.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import nhom03.model.dao.*;
import nhom03.model.dao.impl.*;

import java.io.IOException;

public class MainController {
    @FXML
    private BorderPane mainLayout;

    @FXML
    private Button btnSanPham;

    @FXML
    private Button btnBan;

    @FXML
    private Button btnHoaDon;

    @FXML
    private Button btnNguyenLieu;

    @FXML
    private Button btnPhieuNhap;

    @FXML
    private Button btnPhieuXuat;

    private SanPhamDao sanPhamDao;
    private BanDao banDao;
    private HoaDonDao hoaDonDao;
    private ChiTietHoaDonDao chiTietHoaDonDao;
    private NguyenLieuDao nguyenLieuDao;
    private PhieuNhapKhoDao phieuNhapKhoDao;
    private PhieuXuatKhoDao phieuXuatKhoDao;
    private ChiTietPhieuNhapDao chiTietPhieuNhapDao;
    private ChiTietPhieuXuatDao chiTietPhieuXuatDao;

    public void initialize() {
        sanPhamDao = new SanPhamJdbc();
        banDao = new BanJdbc();
        hoaDonDao = new HoaDonJdbc();
        chiTietHoaDonDao = new ChiTietHoaDonJdbc();
        nguyenLieuDao = new NguyenLieuJdbc();
        phieuNhapKhoDao = new PhieuNhapKhoJdbc();
        phieuXuatKhoDao = new PhieuXuatKhoJdbc();
        chiTietPhieuNhapDao = new ChiTietPhieuNhapJdbc();
        chiTietPhieuXuatDao = new ChiTietPhieuXuatJdbc();
    }

    @FXML
    private void showSanPhamManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/san_pham.fxml"));
            mainLayout.setCenter(loader.load());

            SanPhamController controller = loader.getController();
            controller.setSanPhamDao(sanPhamDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBanManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/ban.fxml"));
            mainLayout.setCenter(loader.load());

            BanController controller = loader.getController();
            controller.setBanDao(banDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showHoaDonManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/hoa_don.fxml"));
            mainLayout.setCenter(loader.load());

            HoaDonController controller = loader.getController();
            controller.setDaos(hoaDonDao, banDao, sanPhamDao, chiTietHoaDonDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNguyenLieuManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/nguyen_lieu.fxml"));
            mainLayout.setCenter(loader.load());

            NguyenLieuController controller = loader.getController();
            controller.setNguyenLieuDao(nguyenLieuDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPhieuNhapManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/phieu_nhap_kho.fxml"));
            mainLayout.setCenter(loader.load());

            PhieuNhapKhoController controller = loader.getController();
            controller.setDaos(phieuNhapKhoDao, nguyenLieuDao, chiTietPhieuNhapDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPhieuXuatManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/nhom03/fxml/phieu_xuat_kho.fxml"));
            mainLayout.setCenter(loader.load());

            PhieuXuatKhoController controller = loader.getController();
            controller.setDaos(phieuXuatKhoDao, nguyenLieuDao, chiTietPhieuXuatDao);
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
