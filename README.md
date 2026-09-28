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

## Gửi email bằng Resend

Ứng dụng dùng Resend HTTPS API để gửi email. Đặt API key trong cùng cửa sổ
PowerShell trước khi chạy ứng dụng:

```powershell
$env:RESEND_API_KEY = "re_your_api_key"
$env:RESEND_FROM = "Email Servlet <onboarding@resend.dev>"
.\run-email.ps1
```

Không ghi API key trực tiếp vào source code. Sender `onboarding@resend.dev`
phù hợp để thử nghiệm. Để gửi production, hãy xác minh domain trong Resend và
đổi `RESEND_FROM`, ví dụ `Email Servlet <noreply@example.com>`.

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
3. Nhập `RESEND_API_KEY` khi Render yêu cầu. Không lưu API key trong
   `render.yaml`.
4. Render build image, tạo PostgreSQL và kiểm tra endpoint `/health`.

Khi không có `DATABASE_URL`, ứng dụng dùng H2 local. Khi Render cung cấp
`DATABASE_URL`, ứng dụng tự chuyển sang PostgreSQL.

Resend dùng HTTPS nên hoạt động trên Render Free. PostgreSQL Free hết hạn sau
30 ngày và không có backup.
