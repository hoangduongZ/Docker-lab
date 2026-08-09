# BÀI 5 — VOLUME: NƠI LƯU DỮ LIỆU BỀN VỮNG

## 1. Vấn đề: container vốn "hay quên"

Ở bài 2, bạn đã biết container có một lớp ghi được nằm trên image. Khi container bị `rm`, lớp đó biến mất. Điều này có nghĩa: **mặc định, mọi dữ liệu ghi bên trong container đều tạm bợ**.

Với một web server không lưu gì quan trọng thì không sao. Nhưng với database, file upload người dùng, hay log cần giữ lại — mất dữ liệu khi container bị xóa là thảm họa.

## 2. Ẩn dụ: kho ngoài bãi, tách khỏi kiện hàng

Volume là một **kho lưu trữ nằm ngoài kiện hàng**, do Docker quản lý, tồn tại độc lập với vòng đời của container.

Container gắn vào kho đó, đọc/ghi bình thường như một thư mục thật. Khi kiện hàng (container) bị dỡ bỏ, kho ngoài bãi (volume) vẫn còn nguyên, sẵn sàng gắn vào một kiện hàng mới.

Đây là nguyên tắc quan trọng nhất của bài này: **container nên được coi là thứ có thể xóa/tạo lại bất cứ lúc nào; dữ liệu cần sống lâu hơn container phải nằm ở volume.**

## 3. Hai cách gắn lưu trữ ngoài phổ biến

### Named volume — kho do Docker quản lý

```bash
docker volume create du-lieu-db
docker run -d -v du-lieu-db:/var/lib/postgresql/data postgres
```

Docker tự quyết định lưu ở đâu trên máy host, bạn không cần quan tâm đường dẫn vật lý chính xác — giống thuê một kho bãi do chính cảng quản lý và cấp phát, bạn chỉ cần biết tên kho.

### Bind mount — nối thẳng một thư mục có sẵn trên máy host

```bash
docker run -d -v /duong-dan/tren-may-that:/app/data ten-image
```

Đây là một lối đi tắt nối thẳng một thư mục **cụ thể, do bạn chỉ định** trên máy host vào bên trong container. Hay dùng khi lập trình để đồng bộ code máy bạn với container theo thời gian thực.

### So sánh nhanh

| | Named volume | Bind mount |
| --- | --- | --- |
| Ai quản lý vị trí lưu | Docker tự quản lý | Bạn chỉ định đường dẫn cụ thể |
| Dùng phổ biến khi | Lưu dữ liệu production (database…) | Đồng bộ code lúc dev, đọc file cấu hình có sẵn |
| Portable giữa các máy | Dễ hơn | Phụ thuộc đường dẫn máy host có tồn tại không |

## 4. Volume không tự động có — bạn phải chủ động gắn

Nếu bạn `docker run postgres` mà **không** thêm `-v`, container vẫn chạy bình thường, nhưng toàn bộ dữ liệu database nằm trong lớp ghi tạm của container. `docker rm` container đó là mất sạch dữ liệu.

Đây là lỗi rất phổ biến với người mới: chạy thử container database, thấy hoạt động tốt, nhưng quên gắn volume — đến khi cần xóa/tạo lại container (ví dụ để update image), toàn bộ dữ liệu biến mất.

## 5. Xem và dọn dẹp volume

```bash
docker volume ls          # liệt kê các volume đang có
docker volume inspect ten # xem chi tiết một volume
docker volume rm ten      # xóa một volume không dùng nữa
```

Xóa volume là thao tác **không thể hoàn tác** — giống dỡ bỏ hẳn cái kho ngoài bãi, hàng bên trong mất theo. Chỉ xóa khi chắc chắn không cần dữ liệu đó nữa.

## 6. Hiểu lầm thường gặp

- "Không gắn volume thì database chạy trong Docker không lưu được dữ liệu gì cả" — không hẳn **sai** nhưng cần làm rõ: nó vẫn ghi được trong lúc container sống, chỉ là dữ liệu **biến mất khi container bị xóa**, chứ không phải không ghi được.
- "`docker stop` container có volume thì mất dữ liệu" — **sai**, `stop` không đụng tới volume lẫn lớp ghi tạm; chỉ `rm` container (khi không có volume) mới mất dữ liệu ghi trong container.
- "Bind mount và named volume là một, chỉ khác cách gọi" — **sai**, khác nhau ở chỗ ai kiểm soát vị trí lưu trữ vật lý, xem bảng mục 3.

## Kiểm tra nhanh

1. Vì sao dữ liệu ghi trong container "biến mất" khi container bị xóa nếu không dùng volume?
2. Bạn đang code một API và muốn mỗi lần sửa file trên máy thì container tự thấy thay đổi ngay — nên dùng named volume hay bind mount?
3. `docker stop` một container chạy database (không có volume) rồi `docker start` lại — dữ liệu còn không? Nếu đổi thành `docker rm` rồi tạo container mới thì sao?

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 6 sau khi đã được nhận xét.
