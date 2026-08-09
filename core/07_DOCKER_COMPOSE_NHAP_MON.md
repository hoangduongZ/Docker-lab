# BÀI 7 — DOCKER COMPOSE NHẬP MÔN

## 1. Vấn đề: ứng dụng thật hiếm khi chỉ có một container

Một API thực tế thường cần ít nhất một database, có khi thêm cache, message queue... Nếu làm thủ công, mỗi lần khởi động bạn phải gõ nhiều lệnh `docker run` dài dòng, nhớ đúng thứ tự, đúng network, đúng volume.

Docker Compose giải quyết việc này: mô tả **toàn bộ** tập container cần thiết trong **một file YAML**, rồi khởi động tất cả bằng một lệnh.

## 2. Ẩn dụ: bản kế hoạch điều phối cả dây chuyền

Nếu từng lệnh `docker run` giống việc bạn tự tay mở từng kiện hàng một, thì Docker Compose giống một **bản kế hoạch tổng thể**: ghi rõ cảng cần bao nhiêu loại kiện hàng, kiện nào nối cửa nào, kiện nào cần kho bãi riêng, kiện nào cần đứng cùng khu để nói chuyện với nhau — tất cả viết sẵn trong một bản kế hoạch, thay vì ra lệnh thủ công từng kiện.

## 3. Một file `docker-compose.yml` tối thiểu

Ví dụ một API kèm database:

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

Đọc theo từng khối:

- `services`: danh sách các container cần chạy, mỗi service là một container.
- `api.build: .`: build image từ Dockerfile trong thư mục hiện tại (thay vì tải sẵn).
- `api.ports`: giống hệt `-p` đã học ở bài 6.
- `api.environment`: biến môi trường truyền vào container, ở đây API dùng để biết địa chỉ database.
- `db.image: postgres:16`: dùng thẳng image có sẵn từ Docker Hub, không cần build.
- `db.environment`: ba biến `POSTGRES_USER`, `POSTGRES_PASSWORD`, `POSTGRES_DB` là quy ước riêng của image `postgres` chính thức, dùng để tạo sẵn user/mật khẩu/database khi container khởi động lần đầu — phải khớp với thông tin trong `DATABASE_URL` ở service `api`.
- `db.volumes`: gắn named volume `du-lieu-db`, giữ dữ liệu database bền vững — đúng nguyên tắc học ở bài 5.
- `depends_on`: `api` chờ `db` được khởi động trước (chỉ đảm bảo thứ tự khởi động, **chưa chắc** database đã sẵn sàng nhận kết nối — điểm này học kỹ hơn ở phần trung cấp).

## 4. Điều kỳ diệu: gọi nhau bằng tên service

Để ý dòng `DATABASE_URL=postgres://user:pass@db:5432/...` — service `api` gọi thẳng tới `db` bằng đúng **tên service**, không cần biết IP.

Đây chính là điều bài 6 đã nhắc trước: Compose tự động tạo một **network riêng do người dùng định nghĩa** cho toàn bộ file, nên mọi service trong cùng file có thể gọi nhau bằng tên, giống có sẵn một "danh bạ nội bộ" miễn phí.

## 5. Các lệnh Compose cơ bản

```bash
docker compose up -d        # đọc docker-compose.yml, tạo và chạy toàn bộ service (nền)
docker compose ps           # xem các service đang chạy trong dự án này
docker compose logs -f api  # xem log riêng của service api, theo thời gian thực
docker compose down         # dừng và xóa toàn bộ container + network đã tạo (volume mặc định được GIỮ LẠI)
```

`docker compose down` không xóa named volume theo mặc định — đúng tinh thần bài 5: dữ liệu bền vững tách khỏi vòng đời container. Muốn xóa cả volume, phải thêm cờ `-v` (`docker compose down -v`), và đây là thao tác **không thể hoàn tác**.

## 6. Hiểu lầm thường gặp

- "Docker Compose là công cụ triển khai production quy mô lớn, thay thế được Kubernetes" — **sai**, Compose rất hợp cho máy dev, demo, hệ thống nhỏ; điều phối production quy mô lớn thuộc nhóm "orchestration", học ở phần trung cấp/nâng cao (`docker/medium`).
- "`depends_on` đảm bảo database đã sẵn sàng nhận kết nối trước khi api chạy" — **sai**, nó chỉ đảm bảo thứ tự container được khởi động, không chờ ứng dụng bên trong "sẵn sàng" thật sự.
- "`docker compose down` xóa luôn dữ liệu database" — **sai** theo mặc định, xem mục 5.

## Kiểm tra nhanh

1. Vì sao service `api` trong file trên gọi được database chỉ bằng tên `db` mà không cần biết IP?
2. `docker compose down` có xóa volume `du-lieu-db` không? Muốn xóa luôn thì thêm gì?
3. `depends_on` giữa `api` và `db` đảm bảo điều gì, và KHÔNG đảm bảo điều gì?

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 8 sau khi đã được nhận xét.
