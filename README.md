# Email Servlet

Project Jakarta Servlet 6 chạy trên Java 17, đóng gói thành WAR `email-servlet`.
Ứng dụng gồm hai module:

- **Email**: form đăng ký nhận tin, lưu người đăng ký vào database và gửi email
  chào mừng qua Resend.
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

## Chạy local

Cách nhanh nhất:

```powershell
.\run-email.ps1
```

Script sẽ build, giải phóng cổng 8080 nếu còn Jetty cũ và chạy ứng dụng tại
`http://localhost:8080/`.

Hoặc chạy trực tiếp bằng Maven:

```powershell
mvn jetty:run
```

## Cấu hình Resend (gửi email)

Ứng dụng gửi email qua Resend HTTPS API. API key được đọc theo thứ tự ưu tiên:

1. System property (`-DRESEND_API_KEY=...`)
2. Biến môi trường `RESEND_API_KEY`
3. File `config.properties` ở thư mục chạy ứng dụng

**Cách dễ nhất khi chạy local:** copy file mẫu rồi điền key:

```powershell
Copy-Item config.properties.example config.properties
```

Mở `config.properties` và sửa:

```properties
RESEND_API_KEY=re_your_api_key
RESEND_FROM=Email Servlet <onboarding@resend.dev>
```

File `config.properties` đã được bỏ qua bởi Git và Docker, nên key không bị
commit hay đóng gói vào image.

Nếu không dùng file, có thể đặt biến môi trường trước khi chạy:

```powershell
$env:RESEND_API_KEY = "re_your_api_key"
$env:RESEND_FROM = "Email Servlet <onboarding@resend.dev>"
.\run-email.ps1
```

Không ghi API key trực tiếp vào source code. Sender `onboarding@resend.dev` chỉ
để thử nghiệm; muốn gửi production hãy xác minh domain trong Resend rồi đổi
`RESEND_FROM`, ví dụ `Email Servlet <noreply@example.com>`.

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
3. Nhập `RESEND_API_KEY` khi Render yêu cầu (được khai báo `sync: false` nên
   Render sẽ hỏi, không lưu key trong `render.yaml`).
4. Render build image, tạo PostgreSQL và kiểm tra endpoint `/health`.

`render.yaml` tạo hai tài nguyên trong cùng region Singapore:

- Web Service chạy Docker (tự nhận biến `PORT` do Render cấp).
- PostgreSQL database được truyền vào ứng dụng qua `DATABASE_URL`.

Lưu ý: Resend dùng HTTPS nên hoạt động trên gói Render Free. PostgreSQL Free hết
hạn sau 30 ngày và không có backup.
