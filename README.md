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
