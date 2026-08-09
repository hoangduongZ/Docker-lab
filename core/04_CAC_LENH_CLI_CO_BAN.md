# BÀI 4 — CÁC LỆNH CLI CỐT LÕI

## 1. Vì sao cần biết các lệnh này?

Bài 2 và 3 đã cho bạn khái niệm image, container, Dockerfile. Bài này là bộ lệnh để **thao tác** với chúng — gần như chắc chắn xuất hiện trong bất kỳ buổi phỏng vấn Docker junior nào.

Ẩn dụ xuyên suốt: bạn là người quản lý một cảng nhỏ, các lệnh dưới đây là công cụ hằng ngày.

## 2. Nhóm lệnh về Image

| Lệnh | Ý nghĩa | Ẩn dụ |
| --- | --- | --- |
| `docker build -t ten-app:tag .` | Đọc Dockerfile trong thư mục hiện tại, tạo ra một image mới | Đóng gói kiện hàng theo hướng dẫn |
| `docker images` | Liệt kê các image đang có trên máy | Xem danh sách khuôn đúc trong kho |
| `docker pull ten-image` | Tải image có sẵn từ registry (Docker Hub…) về máy | Nhập khuôn đúc có sẵn từ kho trung tâm |
| `docker rmi ten-image` | Xóa một image khỏi máy | Dỡ bỏ khuôn đúc không dùng nữa |

Dấu `.` cuối lệnh `docker build` là **build context** — thư mục Docker được phép đọc file từ đó để `COPY` vào image; đây cũng là lý do vì sao thư mục project lộn xộn, chứa file thừa (như `node_modules`) có thể làm build chậm, học sâu hơn ở phần trung cấp (`docker/medium`).

## 3. Nhóm lệnh về Container

| Lệnh | Ý nghĩa | Ẩn dụ |
| --- | --- | --- |
| `docker run ten-image` | Tạo và khởi động một container mới từ image | Mở một kiện hàng mới từ khuôn, bắt đầu vận hành |
| `docker ps` | Liệt kê container đang chạy | Xem kiện hàng đang hoạt động tại cảng |
| `docker ps -a` | Liệt kê mọi container, kể cả đã dừng | Xem cả kiện hàng đang hoạt động lẫn đã ngừng |
| `docker stop ten-container` | Dừng container đang chạy | Tạm ngừng hoạt động, kiện hàng vẫn còn đó |
| `docker start ten-container` | Khởi động lại container đã dừng | Vận hành lại kiện hàng cũ, không tạo mới |
| `docker rm ten-container` | Xóa hẳn container | Dỡ bỏ kiện hàng khỏi cảng |
| `docker logs ten-container` | Xem nhật ký (output) của container | Xem sổ nhật ký hoạt động của kiện hàng |
| `docker exec -it ten-container sh` | Mở một phiên làm việc bên trong container đang chạy | Cử người vào tận bên trong kiện hàng để kiểm tra |

## 4. Vài cờ (flag) hay gặp của `docker run`

```bash
docker run -d -p 8080:80 --name web nginx
```

- `-d`: chạy nền (detached) — container chạy phía sau, không chiếm màn hình terminal.
- `-p 8080:80`: map port máy host `8080` vào port `80` trong container (chi tiết ở bài 6).
- `--name web`: đặt tên riêng cho container, thay vì để Docker tự sinh tên ngẫu nhiên.
- `nginx`: tên image dùng để tạo container.

```bash
docker exec -it web sh
```

- `-it`: kết hợp chế độ tương tác (`-i`) và giả lập terminal (`-t`) để bạn gõ lệnh trực tiếp bên trong container như một terminal thật.

## 5. `stop` khác `rm` như thế nào?

Đây là điểm rất hay bị nhầm:

- `docker stop`: tạm dừng tiến trình bên trong container. Container và mọi dữ liệu trong lớp ghi được (nhắc ở bài 2) **vẫn còn nguyên**, có thể `docker start` lại.
- `docker rm`: xóa hẳn container khỏi máy, kể cả lớp ghi được đó. Dữ liệu chưa được lưu ở volume sẽ mất vĩnh viễn.

Ẩn dụ: `stop` giống tắt đèn kho hàng, hàng vẫn còn trong đó; `rm` giống dỡ bỏ hẳn cái kho.

## 6. Hiểu lầm thường gặp

- "Xóa container (`rm`) thì image cũng mất theo" — **sai**, image độc lập, xem bằng `docker images`.
- "`docker stop` là xóa luôn container" — **sai**, xem mục 5.
- "Chạy `docker run` nhiều lần trên cùng image sẽ chỉ có một container" — **sai**, mỗi lần `docker run` (không có `--name` trùng) tạo một container mới, riêng biệt.

## Kiểm tra nhanh

1. Phân biệt `docker stop` và `docker rm` bằng một câu cho mỗi lệnh.
2. Bạn muốn xem log của một container đang chạy để tìm lỗi. Dùng lệnh nào?
3. Lệnh `docker exec -it ten-container sh` dùng để làm gì, và vì sao cần `-it`?

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 5 sau khi đã được nhận xét.
