# Email Servlet

Project Jakarta Servlet 6 chạy trên Tomcat 10.1 và Java 17.

## Build

Cài Maven rồi chạy:

```powershell
mvn clean package
```

File WAR được tạo tại `target/email-servlet.war`.

## Chạy với Tomcat

1. Tải Tomcat 10.1.
2. Chép `target/email-servlet.war` vào thư mục `webapps` của Tomcat.
3. Khởi động Tomcat.
4. Mở `http://localhost:8080/email-servlet/hello`.

Lưu ý: Tomcat 10+ dùng package `jakarta.servlet`. Không dùng Tomcat 9 cho project này.

## Deploy lên Render

Project đã có sẵn `Dockerfile` và `render.yaml` để deploy dưới dạng Web Service.

1. Push source code lên GitHub/GitLab.
2. Trên Render chọn **New > Blueprint** và chọn repository.
3. Render sẽ đọc `render.yaml`, build image bằng Docker và tự cấp biến `PORT`.
4. Mở URL Render được cấp sau khi deploy hoàn tất.

Ứng dụng chạy tại root URL (`/`), không cần thêm `/email-servlet`.

## Gửi email bằng Gmail

Ứng dụng đọc tài khoản Gmail và App Password từ biến môi trường. Đặt hai
biến sau trong cùng cửa sổ PowerShell trước khi chạy ứng dụng:

```powershell
$env:SMTP_USERNAME = "your-account@gmail.com"
$env:SMTP_PASSWORD = "your-google-app-password"
.\run-email.ps1
```

Không dùng mật khẩu Gmail thông thường và không ghi App Password trực tiếp
vào source code.

## Kiến trúc 3 lớp và JPA

- Presentation: `EmailListServlet` và các trang JSP.
- Business: `UserService` xử lý đăng ký và gửi email chào mừng.
- Data: `UserRepository` dùng JPA/Hibernate để lưu `User`.

Dự án dùng H2 file database. Hibernate tự tạo bảng `email_users` và dữ liệu
local được lưu trong thư mục `data/` (thư mục này không được commit lên Git).

## Deploy Docker lên Render

`render.yaml` tạo hai tài nguyên trong cùng region Singapore:

- Web Service chạy Docker.
- PostgreSQL database được truyền vào ứng dụng qua `DATABASE_URL`.

Các bước deploy:

1. Push source code lên GitHub hoặc GitLab.
2. Trên Render, chọn **New > Blueprint** và chọn repository.
3. Nhập `SMTP_USERNAME` và `SMTP_PASSWORD` khi Render yêu cầu. Không lưu hai
   giá trị này trong `render.yaml`.
4. Render build image, tạo PostgreSQL và kiểm tra endpoint `/health`.

Khi không có `DATABASE_URL`, ứng dụng dùng H2 local. Khi Render cung cấp
`DATABASE_URL`, ứng dụng tự chuyển sang PostgreSQL.

Lưu ý về gói Free của Render:

- Web Service Free chặn outbound SMTP trên port 25, 465 và 587, do đó Gmail
  SMTP không gửi được. Cần nâng Web Service lên gói trả phí hoặc thay Gmail
  SMTP bằng một email API chạy qua HTTPS.
- PostgreSQL Free hết hạn sau 30 ngày và không có backup.
