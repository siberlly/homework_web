<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cảm ơn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/survey/assets/css/survey.css">
</head>
<body>
    <main class="survey-page">
        <h1>Cảm ơn bạn đã tham gia khảo sát!</h1>

        <ul class="summary">
            <li><strong>Họ:</strong> ${submission.lastName}</li>
            <li><strong>Tên:</strong> ${submission.firstName}</li>
            <li><strong>Email:</strong> ${submission.email}</li>
            <li><strong>Ngày sinh:</strong> ${submission.dateOfBirth}</li>
            <li><strong>Nguồn:</strong> ${submission.source}</li>
            <li><strong>Nhận ưu đãi CD:</strong> ${submission.cdOffers ? 'Có' : 'Không'}</li>
            <li><strong>Nhận thông báo email:</strong> ${submission.emailAnnouncements ? 'Có' : 'Không'}</li>
            <li><strong>Liên hệ qua:</strong> ${submission.contactMethod}</li>
        </ul>

        <a href="${pageContext.request.contextPath}/survey/">Quay lại trang khảo sát</a>
    </main>
</body>
</html>
