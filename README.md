# Email Servlet

Project Jakarta Servlet 6 chạy trên Java 17, đóng gói thành WAR `email-servlet`.
Ứng dụng gồm hai module:

- **Email**: form đăng ký nhận tin, lưu người đăng ký vào database và gửi email
  chào mừng qua Brevo.
- **Survey**: form khảo sát tiếng Việt, lưu câu trả lời trong session và hiển thị
  trang tổng kết.

Khi chạy, ứng dụng phục vụ tại root URL (`/`):

- Trang chủ: `/`
- Form đăng ký email: `/email/`
- Form khảo sát: `/survey/`
- Kiểm tra database: `/health`

## Yêu cầu

- JDK 17
- Maven 3.9+

## Build

```powershell
mvn clean package
```

File WAR được tạo tại `target/email-servlet.war`.

## Cấu hình gửi email (Brevo)

Ứng dụng gửi email qua **Brevo HTTP API** (`https://api.brevo.com/v3/smtp/email`).
Brevo dùng HTTPS (cổng 443) nên hoạt động trên Render free, khác với Gmail SMTP
(bị Render chặn cổng 25/465/587).

Cấu hình được đọc từ **biến môi trường** (không lưu trong source code):

- `BREVO_API_KEY`: API key tạo tại https://app.brevo.com/settings/keys/api
- `MAIL_FROM`: sender **đã xác minh trong Brevo**, định dạng `Tên <email>`.
  Gmail cũng dùng được, ví dụ `Email Servlet <you@gmail.com>`.

Hai biến này được đọc theo thứ tự ưu tiên: **system property** (`-DBREVO_API_KEY=...`)
trước, rồi tới **biến môi trường**.

### Chạy local

Tạo file `.env` từ mẫu rồi điền giá trị:

```powershell
Copy-Item .env.example .env
```

```dotenv
BREVO_API_KEY=xkeysib-your-api-key
MAIL_FROM=Email Servlet <you@gmail.com>
```

`run-email.ps1` sẽ tự nạp `.env` khi khởi động. File `.env` đã được bỏ qua bởi
Git và Docker nên key không bị commit hay đóng gói vào image.

Hoặc set biến môi trường trực tiếp trong cửa sổ PowerShell:

```powershell
$env:BREVO_API_KEY = "xkeysib-your-api-key"
$env:MAIL_FROM = "Email Servlet <you@gmail.com>"
```

### Trên Render

Khai báo `BREVO_API_KEY` và `MAIL_FROM` trong phần Environment của Web Service
(`render.yaml` đã đánh dấu `sync: false` để Render hỏi khi deploy). Không cần
`.env` trên Render.

Sau khi xác minh sender, Brevo cho phép gửi tới **mọi địa chỉ email** (gói free
300 email/ngày). Gửi từ địa chỉ Gmail có thể vào hộp thư spam; muốn deliverability
tốt hơn hãy xác minh một domain riêng trong Brevo rồi đổi `MAIL_FROM`.

## Chạy local

Cách nhanh nhất:

```powershell
.\run-email.ps1
```

Script sẽ nạp `.env`, build, giải phóng cổng 8080 nếu còn Jetty cũ và chạy ứng
dụng tại `http://localhost:8080/`.

Hoặc chạy trực tiếp bằng Maven (khi đó phải tự set biến môi trường):

```powershell
mvn jetty:run
```

## Kiến trúc và JPA

- Presentation: `EmailListServlet` và các trang JSP.
- Business: `UserService` xử lý đăng ký và gửi email chào mừng.
- Data: `UserRepository` dùng JPA/Hibernate để lưu `User`.

Ứng dụng chọn database tự động: nếu không có biến `DATABASE_URL` thì dùng H2 file
(`data/`, không commit lên Git); nếu có `DATABASE_URL` (Render cấp) thì chuyển
sang PostgreSQL. Hibernate tự tạo bảng `email_users`.

## Deploy lên Render

Project đã có sẵn `Dockerfile` và `render.yaml` để deploy dưới dạng Web Service.

1. Push source code lên GitHub/GitLab.
2. Trên Render, chọn **New > Blueprint** và chọn repository.
3. Nhập `BREVO_API_KEY` và `MAIL_FROM` khi Render yêu cầu.
4. Render build image, tạo PostgreSQL và kiểm tra endpoint `/health`.

`render.yaml` tạo hai tài nguyên trong cùng region Singapore:

- Web Service chạy Docker (tự nhận biến `PORT` do Render cấp).
- PostgreSQL database được truyền vào ứng dụng qua `DATABASE_URL`.

Lưu ý: PostgreSQL Free hết hạn sau 30 ngày và không có backup.
