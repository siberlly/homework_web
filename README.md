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
