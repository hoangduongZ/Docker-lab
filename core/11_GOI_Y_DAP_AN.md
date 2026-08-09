# BÀI 11 — GỢI Ý ĐÁP ÁN

Chỉ đọc file này sau khi bạn đã tự làm bài 10.

## Tình huống 1

1. Không hẳn bị xóa. `docker ps` chỉ hiện container đang chạy; container đã dừng vẫn còn, xem bằng `docker ps -a`.
2. `docker logs api` để xem output/lỗi cuối cùng trước khi thoát; `docker ps -a` để xem exit code và trạng thái.
3. Ví dụ: `CMD` trỏ sai file (file không tồn tại), ứng dụng bị lỗi ngay khi khởi động (thiếu biến môi trường, thiếu dependency), hoặc tiến trình chính tự thoát vì công việc đã xong (container chỉ sống khi tiến trình chính còn chạy).

## Tình huống 2

1. Ứng dụng chạy đúng bên trong container ở port 3000 của chính nó, nhưng không có cầu nối nào từ máy host vào port đó — thiếu `-p`.
2. `docker run -d -p 3000:3000 --name web my-api:1.0` (hoặc bất kỳ cặp `HOST:CONTAINER` hợp lệ nào miễn container luôn là 3000).
3. `http://localhost:8080` — vì cú pháp là `-p HOST:CONTAINER`, số bên trái (8080) là port bạn gõ trên trình duyệt, số bên phải (3000) vẫn là port cố định bên trong container.

## Tình huống 3

1. Container không có volume nghĩa là dữ liệu database nằm trong lớp ghi tạm của chính container đó; `docker rm` xóa hẳn container bao gồm cả lớp ghi đó, image gốc (postgres) không hề chứa dữ liệu này nên container mới tạo ra trắng trơn.
2. Ví dụ: `docker run -d --name db -v du-lieu-db:/var/lib/postgresql/data postgres`. Từ đó, dù `db` bị xóa và tạo lại, gắn lại đúng volume `du-lieu-db` sẽ khôi phục dữ liệu.
3. Không. `docker compose down` (không `-v`) chỉ xóa container và network, named volume được định nghĩa trong `volumes:` vẫn được giữ nguyên; chỉ `docker compose down -v` mới xóa cả volume.

## Một bài kể chuyện mẫu

> Tôi có một API và một database. Đầu tiên tôi viết một Dockerfile cho API: chọn image nền Node, copy code, cài dependencies, khai báo lệnh chạy. Tôi `docker build` để tạo ra một image. Thay vì chạy tay từng container, tôi viết một file Docker Compose khai báo hai service: `api` (build từ Dockerfile, map port ra ngoài) và `db` (dùng image `postgres` có sẵn, gắn một named volume để dữ liệu không mất khi container bị xóa hay tạo lại). Nhờ Compose tự tạo network riêng, `api` gọi được `db` chỉ bằng tên service, không cần biết địa chỉ IP. Tôi chạy `docker compose up -d` để khởi động toàn bộ hệ thống bằng một lệnh, xem log bằng `docker compose logs`, và khi cần dừng, tôi dùng `docker compose down` — biết chắc dữ liệu trong volume vẫn còn nguyên cho lần chạy tiếp theo.

## Tự đánh giá

Bạn đã đạt nền tảng junior nếu có thể:

- Phân biệt rõ image và container, giải thích được vì sao xóa container không làm mất image.
- Viết được một Dockerfile cơ bản và giải thích từng dòng.
- Dùng đúng các lệnh CLI cốt lõi: build, run, ps, logs, exec, stop, rm.
- Giải thích vì sao cần volume, và biết dùng `-v` hoặc named volume trong Compose.
- Giải thích được `-p HOST:CONTAINER` và vì sao container không tự động gọi nhau bằng tên trên bridge mặc định.
- Đọc và viết được một file `docker-compose.yml` đơn giản cho app + database.

Nếu còn nhầm, hãy quay lại đúng bài tương ứng thay vì đọc lại toàn bộ. Khi đã tự tin với toàn bộ checklist trên, bạn đã sẵn sàng học tiếp phần trung cấp ở `docker/medium` để hướng tới trình độ middle backend dev.
