--Create database CoffeeShop
--use CoffeeShop
--drop database CoffeeShop

-- Sản Phẩm
CREATE TABLE SanPham (
    MaSanPham INT PRIMARY KEY IDENTITY(1,1),
    TenSanPham NVARCHAR(100) NOT NULL,
    LoaiSanPham NVARCHAR(20) CHECK (LoaiSanPham IN (N'Đồ uống', N'Đồ ăn', N'Khác')) NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL,
    MoTa NVARCHAR(MAX),
    HinhAnh NVARCHAR(255),
    TrangThai NVARCHAR(10) DEFAULT N'Còn' CHECK (TrangThai IN (N'Còn', N'Hết', N'Ngưng'))
);
GO

-- Topping Table
CREATE TABLE Topping (
    MaTopping INT PRIMARY KEY IDENTITY(1,1),
    TenTopping NVARCHAR(100) NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL,
    MoTa NVARCHAR(MAX),
    TrangThai NVARCHAR(10) DEFAULT N'Còn' CHECK (TrangThai IN (N'Còn', N'Hết', N'Ngưng'))
);
GO

-- Bàn (Tables)
CREATE TABLE Ban (
    MaBan INT PRIMARY KEY IDENTITY(1,1),
    TenBan NVARCHAR(50) NOT NULL UNIQUE,
    TrangThai NVARCHAR(20) DEFAULT N'Trống' CHECK (TrangThai IN (N'Trống', N'Đang sử dụng', N'Đặt trước')),
    GhiChu NVARCHAR(MAX)
);
GO

-- Hóa Đơn (Orders)
CREATE TABLE HoaDon (
    MaHoaDon INT PRIMARY KEY IDENTITY(1,1),
    MaBan INT,
    NgayLap DATETIME DEFAULT GETDATE(),
    TongTien DECIMAL(10,2) DEFAULT 0,
    GiamGia DECIMAL(10,2) DEFAULT 0,
    TrangThai NVARCHAR(20) DEFAULT N'Chưa thanh toán' 
        CHECK (TrangThai IN (N'Chưa thanh toán', N'Đã thanh toán', N'Hủy')),
    FOREIGN KEY (MaBan) REFERENCES Ban(MaBan)
);
GO

-- Chi Tiết Hóa Đơn (Order Details)
CREATE TABLE ChiTietHoaDon (
    MaChiTiet INT PRIMARY KEY IDENTITY(1,1),
    MaHoaDon INT,
    MaSanPham INT,
    SoLuong INT NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL,
    GhiChu NVARCHAR(MAX),
    FOREIGN KEY (MaHoaDon) REFERENCES HoaDon(MaHoaDon),
    FOREIGN KEY (MaSanPham) REFERENCES SanPham(MaSanPham)
);
GO

-- Chi Tiết Topping (Topping Details for each order item)
CREATE TABLE ChiTietTopping (
    MaChiTietTopping INT PRIMARY KEY IDENTITY(1,1),
    MaChiTietHD INT,  -- References ChiTietHoaDon
    MaTopping INT,
    SoLuong INT DEFAULT 1,
    DonGia DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (MaChiTietHD) REFERENCES ChiTietHoaDon(MaChiTiet),
    FOREIGN KEY (MaTopping) REFERENCES Topping(MaTopping)
);
GO

-- Nguyên Liệu (Ingredients)
CREATE TABLE NguyenLieu (
    MaNguyenLieu INT PRIMARY KEY IDENTITY(1,1),
    TenNguyenLieu NVARCHAR(100) NOT NULL,
    DonViTinh NVARCHAR(50) NOT NULL,
    SoLuongTon DECIMAL(10,2) DEFAULT 0,
    DonGiaNhap DECIMAL(10,2) NOT NULL
);
GO

-- Phiếu Nhập Kho (Inventory Import)
CREATE TABLE PhieuNhapKho (
    MaPhieuNhap INT PRIMARY KEY IDENTITY(1,1),
    NgayNhap DATETIME DEFAULT GETDATE(),
    TongTien DECIMAL(10,2) DEFAULT 0,
    GhiChu NVARCHAR(MAX)
);
GO

-- Chi Tiết Phiếu Nhập Kho (Inventory Import Details)
CREATE TABLE ChiTietPhieuNhap (
    MaChiTietNhap INT PRIMARY KEY IDENTITY(1,1),
    MaPhieuNhap INT,
    MaNguyenLieu INT,
    SoLuong DECIMAL(10,2) NOT NULL,
    DonGia DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (MaPhieuNhap) REFERENCES PhieuNhapKho(MaPhieuNhap),
    FOREIGN KEY (MaNguyenLieu) REFERENCES NguyenLieu(MaNguyenLieu)
);
GO

-- Phiếu Xuất Kho (Inventory Export)
CREATE TABLE PhieuXuatKho (
    MaPhieuXuat INT PRIMARY KEY IDENTITY(1,1),
    NgayXuat DATETIME DEFAULT GETDATE(),
    LyDo NVARCHAR(255),
    GhiChu NVARCHAR(MAX)
);
GO

-- Chi Tiết Phiếu Xuất Kho (Inventory Export Details)
CREATE TABLE ChiTietPhieuXuat (
    MaChiTietXuat INT PRIMARY KEY IDENTITY(1,1),
    MaPhieuXuat INT,
    MaNguyenLieu INT,
    SoLuong DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (MaPhieuXuat) REFERENCES PhieuXuatKho(MaPhieuXuat),
    FOREIGN KEY (MaNguyenLieu) REFERENCES NguyenLieu(MaNguyenLieu)
);
GO

-- Indexes for performance
CREATE INDEX idx_san_pham_loai ON SanPham(LoaiSanPham);
CREATE INDEX idx_hoa_don_ban ON HoaDon(MaBan);
CREATE INDEX idx_chi_tiet_hoa_don ON ChiTietHoaDon(MaHoaDon, MaSanPham);
CREATE INDEX idx_chi_tiet_topping ON ChiTietTopping(MaChiTietHD);
CREATE INDEX idx_nguyen_lieu_ton ON NguyenLieu(SoLuongTon);
GO

-- Mock Data
INSERT INTO Ban (TenBan, TrangThai, GhiChu) VALUES
(N'Bàn 1', N'Trống', N'Bàn gần cửa sổ'),
(N'Bàn 2', N'Đang sử dụng', N'Bàn gần cửa sổ'),
(N'Bàn 3', N'Trống', N'Bàn trung tâm'),
(N'Bàn 4', N'Đang sử dụng', N'Bàn trung tâm'),
(N'Bàn 5', N'Đặt trước', N'Bàn góc'),
(N'Bàn 6', N'Trống', N'Bàn góc'),
(N'Bàn 7', N'Đang sử dụng', N'Bàn ngoài sân'),
(N'Bàn 8', N'Trống', N'Bàn ngoài sân'),
(N'Bàn 9', N'Đặt trước', N'Bàn VIP'),
(N'Bàn 10', N'Trống', N'Bàn VIP'),
(N'Bàn 11', N'Đang sử dụng', N'Bàn tầng 2'),
(N'Bàn 12', N'Trống', N'Bàn tầng 2');
GO

INSERT INTO SanPham (TenSanPham, LoaiSanPham, DonGia, MoTa, HinhAnh, TrangThai) VALUES
(N'Cà phê đen', N'Đồ uống', 25000, N'Cà phê đen truyền thống', N'ca-phe-den.jpg', N'Còn'),
(N'Cà phê sữa', N'Đồ uống', 30000, N'Cà phê sữa đặc', N'ca-phe-sua.jpg', N'Còn'),
(N'Bạc xỉu', N'Đồ uống', 35000, N'Cà phê sữa nhiều sữa', N'bac-xiu.jpg', N'Còn'),
(N'Trà đào', N'Đồ uống', 45000, N'Trà đào cam sả', N'tra-dao.jpg', N'Còn'),
(N'Trà sữa trân châu', N'Đồ uống', 50000, N'Trà sữa với trân châu đường đen', N'tra-sua.jpg', N'Còn'),
(N'Bánh mì thịt', N'Đồ ăn', 35000, N'Bánh mì kẹp thịt', N'banh-mi.jpg', N'Còn'),
(N'Croissant', N'Đồ ăn', 40000, N'Bánh croissant bơ', N'croissant.jpg', N'Còn'),
(N'Tiramisu', N'Đồ ăn', 55000, N'Bánh tiramisu Ý', N'tiramisu.jpg', N'Hết'),
(N'Nước suối', N'Đồ uống', 15000, N'Nước suối đóng chai', N'nuoc-suoi.jpg', N'Còn'),
(N'Túi vải', N'Khác', 75000, N'Túi vải canvas thương hiệu quán', N'tui-vai.jpg', N'Còn'),
(N'Smoothie dâu', N'Đồ uống', 60000, N'Sinh tố dâu tây', N'smoothie-dau.jpg', N'Còn'),
(N'Matcha đá xay', N'Đồ uống', 65000, N'Matcha đá xay kem sữa', N'matcha.jpg', N'Còn'),
(N'Espresso', N'Đồ uống', 40000, N'Cà phê espresso nguyên chất', N'espresso.jpg', N'Còn');
GO

INSERT INTO Topping (TenTopping, DonGia, MoTa, TrangThai) VALUES
(N'Trân châu đen', 10000, N'Trân châu đường đen', N'Còn'),
(N'Trân châu trắng', 10000, N'Trân châu trắng', N'Còn'),
(N'Thạch cà phê', 8000, N'Thạch làm từ cà phê', N'Còn'),
(N'Kem phô mai', 15000, N'Kem phô mai béo', N'Còn'),
(N'Pudding', 12000, N'Pudding trứng', N'Còn'),
(N'Đào miếng', 12000, N'Miếng đào tươi', N'Còn'),
(N'Hạt chia', 8000, N'Hạt chia nhập khẩu', N'Hết'),
(N'Whipping cream', 10000, N'Kem tươi đánh bông', N'Còn'),
(N'Shot espresso', 15000, N'Thêm một shot espresso', N'Còn'),
(N'Sốt caramel', 8000, N'Sốt caramel ngọt', N'Còn'),
(N'Thạch nha đam', 10000, N'Thạch nha đam tươi', N'Còn'),
(N'Thạch matcha', 12000, N'Thạch matcha Nhật Bản', N'Còn');
GO

INSERT INTO NguyenLieu (TenNguyenLieu, DonViTinh, SoLuongTon, DonGiaNhap) VALUES
(N'Cà phê hạt', N'kg', 50, 200000),
(N'Sữa tươi', N'lít', 30, 40000),
(N'Sữa đặc', N'hộp', 45, 25000),
(N'Trà đen', N'kg', 15, 350000),
(N'Trà xanh', N'kg', 10, 400000),
(N'Đường', N'kg', 100, 20000),
(N'Đào tươi', N'kg', 8, 80000),
(N'Trân châu đen', N'kg', 12, 90000),
(N'Bột matcha', N'kg', 5, 800000),
(N'Dâu tây', N'kg', 7, 120000),
(N'Bột cacao', N'kg', 8, 280000),
(N'Whipping cream', N'lít', 15, 70000),
(N'Thịt nguội', N'kg', 10, 300000),
(N'Bột mì', N'kg', 40, 25000),
(N'Bơ', N'kg', 20, 180000);
GO

INSERT INTO PhieuNhapKho (NgayNhap, TongTien, GhiChu) VALUES
('2024-03-01', 500000, N'Nhập hàng từ nhà cung cấp A'),
('2024-03-02', 750000, N'Nhập hàng từ nhà cung cấp B'),
('2024-03-03', 620000, N'Nhập hàng từ nhà cung cấp C'),
('2024-03-04', 830000, N'Nhập hàng từ nhà cung cấp D'),
('2024-03-05', 910000, N'Nhập hàng từ nhà cung cấp E'),
('2024-03-06', 480000, N'Nhập hàng từ nhà cung cấp F'),
('2024-03-07', 670000, N'Nhập hàng từ nhà cung cấp G'),
('2024-03-08', 720000, N'Nhập hàng từ nhà cung cấp H'),
('2024-03-09', 550000, N'Nhập hàng từ nhà cung cấp I'),
('2024-03-10', 890000, N'Nhập hàng từ nhà cung cấp J');
GO

INSERT INTO PhieuXuatKho (NgayXuat, LyDo, GhiChu) VALUES
('2024-03-01', N'Xuất hàng cho quán A', N'Xuất nguyên liệu pha chế'),
('2024-03-02', N'Xuất hàng cho quán B', N'Xuất nguyên liệu pha chế'),
('2024-03-03', N'Xuất hàng cho quán C', N'Xuất nguyên liệu pha chế'),
('2024-03-04', N'Xuất hàng cho quán D', N'Xuất nguyên liệu pha chế'),
('2024-03-05', N'Xuất hàng cho quán E', N'Xuất nguyên liệu pha chế'),
('2024-03-06', N'Nguyên liệu hết hạn', N'Hủy nguyên liệu'),
('2024-03-07', N'Xuất hàng cho quán F', N'Xuất nguyên liệu pha chế'),
('2024-03-08', N'Sự kiện coffee tasting', N'Xuất nguyên liệu cho sự kiện'),
('2024-03-09', N'Nguyên liệu hỏng', N'Hủy nguyên liệu'),
('2024-03-10', N'Xuất hàng cho quán G', N'Xuất nguyên liệu pha chế');
GO

INSERT INTO HoaDon (MaBan, NgayLap, TongTien, GiamGia, TrangThai) VALUES
(1, '2024-03-01 08:15:00', 150000, 10000, N'Đã thanh toán'),
(2, '2024-03-01 09:30:00', 200000, 15000, N'Đã thanh toán'),
(3, '2024-03-01 12:45:00', 180000, 12000, N'Chưa thanh toán'),
(4, '2024-03-02 10:20:00', 250000, 20000, N'Đã thanh toán'),
(5, '2024-03-02 14:10:00', 300000, 25000, N'Hủy'),
(6, '2024-03-03 11:30:00', 170000, 5000, N'Chưa thanh toán'),
(7, '2024-03-03 16:45:00', 220000, 10000, N'Đã thanh toán'),
(8, '2024-03-04 09:15:00', 210000, 15000, N'Chưa thanh toán'),
(9, '2024-03-04 13:20:00', 275000, 5000, N'Đã thanh toán'),
(10, '2024-03-05 10:00:00', 320000, 20000, N'Đã thanh toán');
GO

INSERT INTO ChiTietHoaDon (MaHoaDon, MaSanPham, SoLuong, DonGia, GhiChu) VALUES
(1, 1, 2, 25000, N'Đá ít'),
(1, 2, 1, 30000, N'Đường nhiều'),
(1, 5, 1, 50000, NULL),
(2, 3, 3, 35000, N'Không đá'),
(2, 6, 1, 35000, NULL),
(2, 9, 1, 15000, NULL),
(3, 4, 1, 45000, N'Ít đường'),
(3, 5, 2, 50000, NULL),
(3, 7, 1, 40000, NULL),
(4, 2, 2, 30000, N'Đá nhiều'),
(4, 8, 1, 55000, NULL),
(4, 11, 2, 60000, N'Ít đường'),
(5, 1, 1, 25000, NULL),
(5, 3, 1, 35000, NULL),
(5, 5, 3, 50000, N'Thêm trân châu'),
(6, 2, 2, 30000, N'Nhiều đá'),
(6, 4, 1, 45000, N'Ít đường'),
(7, 5, 3, 50000, NULL),
(7, 6, 1, 35000, NULL),
(8, 7, 2, 40000, NULL),
(8, 9, 1, 15000, NULL),
(9, 8, 2, 55000, N'Thêm kem'),
(9, 11, 2, 60000, N'Ít đường'),
(10, 12, 3, 65000, NULL),
(10, 13, 2, 40000, NULL);
GO

INSERT INTO ChiTietTopping (MaChiTietHD, MaTopping, SoLuong, DonGia) VALUES
(1, 1, 1, 10000),
(2, 4, 1, 15000),
(3, 2, 2, 10000),
(5, 5, 1, 12000),
(7, 6, 1, 12000),
(8, 8, 1, 10000),
(9, 6, 2, 12000),
(11, 9, 1, 15000),
(13, 10, 1, 8000),
(15, 3, 1, 8000),
(16, 1, 1, 10000),
(18, 4, 1, 15000),
(20, 5, 1, 12000),
(22, 8, 1, 10000),
(23, 2, 1, 10000),
(3, 10, 1, 8000),
(7, 11, 1, 10000),
(9, 9, 1, 15000),
(13, 3, 1, 8000),
(15, 4, 1, 15000);
GO

INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaNguyenLieu, SoLuong, DonGia) VALUES
(1, 1, 10, 200000),
(1, 2, 5, 40000),
(1, 3, 10, 25000),
(2, 4, 3, 350000),
(2, 5, 2, 400000),
(2, 6, 20, 20000),
(3, 7, 5, 80000),
(3, 8, 8, 90000),
(3, 9, 1, 800000),
(4, 10, 5, 120000),
(4, 11, 3, 280000),
(4, 12, 8, 70000),
(5, 13, 5, 300000),
(5, 14, 15, 25000),
(5, 15, 8, 180000),
(6, 1, 8, 200000),
(6, 3, 15, 25000),
(7, 7, 3, 80000),
(7, 10, 4, 120000),
(8, 9, 2, 800000),
(8, 11, 3, 280000),
(9, 12, 10, 70000),
(9, 14, 10, 25000),
(10, 2, 15, 40000),
(10, 15, 5, 180000);
GO

INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaNguyenLieu, SoLuong) VALUES
(1, 1, 2),
(1, 2, 1),
(1, 3, 2),
(2, 4, 0.5),
(2, 6, 3),
(2, 7, 1),
(3, 8, 1.5),
(3, 9, 0.2),
(3, 12, 1),
(4, 10, 1),
(4, 11, 0.5),
(4, 15, 1),
(5, 13, 1.5),
(5, 14, 2),
(5, 1, 1),
(6, 2, 2),
(6, 7, 1.5),
(7, 8, 2),
(7, 11, 1),
(8, 1, 3),
(8, 9, 0.5),
(9, 14, 1.5),
(9, 15, 0.8),
(10, 3, 2.5),
(10, 6, 4);
Go



--Working/Testing

--TRUNCATE TABLE Ban;
--TRUNCATE TABLE ChiTietHoaDon;
--TRUNCATE TABLE ChiTietPhieuNhap;
--TRUNCATE TABLE ChiTietPhieuXuat;
--TRUNCATE TABLE ChiTietTopping;
--TRUNCATE TABLE HoaDon;
--TRUNCATE TABLE NguyenLieu;
--TRUNCATE TABLE PhieuNhapKho;
--TRUNCATE TABLE PhieuXuatKho;
--TRUNCATE TABLE SanPham;
--TRUNCATE TABLE Topping;

