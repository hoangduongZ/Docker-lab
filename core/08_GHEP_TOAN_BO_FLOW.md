# BÀI 8 — GHÉP TOÀN BỘ FLOW: TỪ CODE ĐẾN CONTAINER CHẠY THẬT

Giả sử bạn có một API Node.js đơn giản, cần kết nối tới PostgreSQL. Hãy theo dõi toàn bộ hành trình từ code tới lúc chạy được bằng Docker.

## Bước 1 — Viết Dockerfile cho ứng dụng

```dockerfile
FROM node:18
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
EXPOSE 3000
CMD ["node", "server.js"]
```

Đây là tờ hướng dẫn đóng gói (bài 3): chọn nền Node 18, cài dependencies, copy code, khai báo cửa 3000, chạy `node server.js` khi container khởi động.

## Bước 2 — Build image

```bash
docker build -t my-api:1.0 .
```

Docker đọc Dockerfile, thực hiện từng bước, tạo ra image `my-api:1.0` gồm nhiều layer xếp chồng (bài 2).

## Bước 3 — Chạy thử một container độc lập

```bash
docker run -d -p 3000:3000 --name api-test my-api:1.0
```

`docker run` tạo một container từ image (bài 2), `-p 3000:3000` mở đường từ host vào cửa 3000 của container (bài 6).

```bash
docker logs api-test
curl http://localhost:3000
```

Xem log để chắc chắn ứng dụng khởi động không lỗi, gọi thử API để xác nhận nó phản hồi.

## Bước 4 — Nhận ra vấn đề: chưa có database, và dữ liệu chưa bền vững

API cần PostgreSQL. Nếu bạn chạy `docker run postgres` riêng lẻ mà không có volume, dữ liệu sẽ mất khi container database bị xóa (bài 5). Ngoài ra, api-test và container postgres cần gọi được nhau — trên bridge mặc định, việc này không ổn định (bài 6).

## Bước 5 — Mô tả toàn bộ hệ thống bằng Docker Compose

```yaml
services:
  api:
    build: .
    ports:
      - "3000:3000"
    environment:
      - DATABASE_URL=postgres://user:pass@db:5432/mydb
    depends_on:
      - db

  db:
    image: postgres:16
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=pass
      - POSTGRES_DB=mydb
    volumes:
      - du-lieu-db:/var/lib/postgresql/data

volumes:
  du-lieu-db:
```

File này giải quyết cả hai vấn đề ở bước 4 cùng lúc: `db` có volume riêng nên dữ liệu bền vững; `api` gọi `db` bằng tên nhờ network riêng Compose tự tạo (bài 7).

## Bước 6 — Khởi động toàn bộ hệ thống

```bash
docker compose up -d
docker compose ps
docker compose logs -f api
```

Một lệnh duy nhất thay thế cho việc gõ tay nhiều lệnh `docker run`, tạo network, tạo volume riêng lẻ.

## Bước 7 — Dừng, khởi động lại, và dọn dẹp đúng cách

```bash
docker compose down       # dừng, xóa container + network, GIỮ LẠI volume du-lieu-db
docker compose up -d      # khởi động lại, dữ liệu database vẫn còn nguyên
docker compose down -v    # chỉ dùng khi thực sự muốn xóa luôn dữ liệu
```

## Bảng tổng kết vai trò

| Thành phần | Câu hỏi nó trả lời | Bài học |
| --- | --- | --- |
| Dockerfile | Đóng gói ứng dụng như thế nào? | Bài 3 |
| Image | Bản thiết kế bất biến, dùng để tạo container | Bài 2 |
| Container | Tiến trình đang chạy thật, tạo từ image | Bài 1, 2 |
| Volume | Dữ liệu bền vững nằm ở đâu khi container bị xóa? | Bài 5 |
| Port mapping / Network | Ai gọi được ai, từ đâu? | Bài 6 |
| Docker Compose | Nhiều container phối hợp thế nào bằng một file? | Bài 7 |

## Kiểm tra nhanh

1. Hãy tự kể lại luồng từ code tới lúc hệ thống chạy đủ api + database, dùng ít nhất năm từ khóa: Dockerfile, image, container, volume, Compose.
2. Nếu bạn xóa nhầm container `db` (không dùng Compose, không có volume), điều gì xảy ra với dữ liệu? Cách nào trong bài này giúp tránh việc đó?
3. Vì sao `api` trong file Compose gọi được `db` chỉ bằng tên, trong khi ở bài 6 hai container chạy tay trên bridge mặc định lại không làm được vậy?

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 9 sau khi đã được nhận xét.
