# BÀI 10 — KIỂM TRA TỔNG HỢP

Không mở file đáp án trước khi hoàn thành bài này. Mục tiêu là giải thích bằng tư duy, không phải chép định nghĩa.

## Tình huống 1 — Container thoát ngay sau khi chạy

Bạn build image rồi chạy:

```bash
docker run -d --name api my-api:1.0
docker ps
```

`docker ps` không thấy `api` đâu cả, dù không có báo lỗi rõ ràng lúc build.

Câu hỏi:

1. Container "biến mất" khỏi `docker ps` có nghĩa nó bị xóa luôn không? Lệnh nào giúp bạn thấy nó?
2. Nêu hai lệnh giúp bạn tìm nguyên nhân container dừng ngay sau khi khởi động.
3. Nêu một lỗi thường gặp trong Dockerfile có thể khiến container thoát ngay lập tức.

## Tình huống 2 — Build được, chạy được, nhưng trình duyệt không vào được

Bạn chạy:

```bash
docker run -d --name web my-api:1.0
```

Ứng dụng bên trong log ra "Server đang chạy ở port 3000", nhưng `http://localhost:3000` trên trình duyệt không phản hồi.

Câu hỏi:

1. Ứng dụng đã chạy đúng bên trong container. Vậy vì sao trình duyệt vẫn không vào được?
2. Sửa câu lệnh `docker run` ở trên để trình duyệt truy cập được.
3. Nếu đổi cổng máy host thành `8080` thay vì `3000`, bạn sẽ gõ URL nào trên trình duyệt?

## Tình huống 3 — Mất dữ liệu sau khi cập nhật image

Team bạn chạy PostgreSQL bằng:

```bash
docker run -d --name db postgres
```

Không có gì khác. Vài tuần sau, để cập nhật phiên bản mới, ai đó chạy `docker rm -f db` rồi tạo container `db` mới từ image mới. Toàn bộ dữ liệu biến mất.

Câu hỏi:

1. Vì sao dữ liệu biến mất dù chỉ "cập nhật container", không ai chủ ý xóa dữ liệu?
2. Sửa lại câu lệnh `docker run` ban đầu để tình huống này không xảy ra nữa.
3. Nếu hệ thống đã dùng Docker Compose với volume đặt tên, `docker compose down` (không có `-v`) có gây ra sự cố tương tự không?

## Bài kể chuyện cuối khóa

Không nhìn tài liệu, hãy hoàn thành đoạn sau bằng lời của bạn:

> Tôi có một API và một database. Đầu tiên tôi viết…

Trong câu trả lời bắt buộc có: Dockerfile, image, container, volume, port, network hoặc Compose.

## DỪNG TẠI ĐÂY

Gửi toàn bộ câu trả lời cho ChatGPT/Claude để được chấm và giải thích phần chưa chắc. Chỉ sau đó mới mở `11_GOI_Y_DAP_AN.md`.
