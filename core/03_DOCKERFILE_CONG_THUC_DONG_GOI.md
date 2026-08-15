# BÀI 3 — DOCKERFILE: TỜ HƯỚNG DẪN ĐÓNG GÓI

## 1. Dockerfile là gì?

Dockerfile là một file text, chứa các bước tuần tự mô tả cách đóng gói một image — chọn nền tảng gì, cài gì, copy code nào vào, và khi kiện hàng được mở ra thì tự động chạy lệnh gì.

Ẩn dụ: đây chính là **tờ hướng dẫn đóng gói** mà nhân viên ở khu đóng hàng làm theo, từng bước một, để tạo ra một kiện hàng chuẩn.

Lệnh `docker build` đọc Dockerfile và thực hiện lần lượt từng bước, mỗi bước tạo ra một layer (nhắc lại từ bài 2).

## 2. Các chỉ thị (instruction) cốt lõi

### FROM — chọn nền tảng có sẵn

```dockerfile
FROM node:18
```

Chọn một image có sẵn làm điểm khởi đầu — giống chọn loại vỏ kiện hàng nền có sẵn thay vì đúc từ số 0. Hầu như mọi Dockerfile bắt đầu bằng `FROM`.

### WORKDIR — chọn thư mục làm việc

```dockerfile
WORKDIR /app
```

Giống chỉ định "từ giờ, mọi việc đóng gói tiếp theo diễn ra trong phòng /app của kiện hàng". Các lệnh `COPY`, `RUN` phía sau sẽ tính từ thư mục này.

### COPY — đặt hàng hóa (code) vào kiện hàng

```dockerfile
COPY package.json .
COPY . .
```

Sao chép file từ máy bạn (build context) vào bên trong image. Đây là lúc code thật của bạn được "xếp vào kiện hàng".

### RUN — thực hiện thao tác lúc đóng gói

```dockerfile
RUN npm install
```

Chạy một lệnh **ngay lúc build image**, kết quả được lưu lại thành một layer. Ví dụ cài đặt thư viện. Đây là việc làm một lần khi đóng gói, không phải mỗi lần container khởi động.

### CMD — lệnh mặc định khi kiện hàng được mở ra

```dockerfile
CMD ["node", "server.js"]
```

Lệnh sẽ tự chạy khi container khởi động (`docker run`), khác với `RUN` (chạy lúc build). Một Dockerfile thường chỉ có một `CMD` hiệu lực — nếu viết nhiều lần, chỉ dòng cuối được dùng.

### EXPOSE — khai báo cửa giao tiếp

```dockerfile
EXPOSE 3000
```

Ghi chú "kiện hàng này dự định giao tiếp qua cửa 3000". Đây chỉ là **tài liệu/khai báo ý định**, nó không tự động mở port ra ngoài máy host — việc thật sự mở cửa ra ngoài nằm ở `docker run -p`, học tại bài 6.

### ENV — dán nhãn cấu hình

```dockerfile
ENV NODE_ENV=production
```

Đặt sẵn một biến môi trường có trong container khi nó chạy — giống dán nhãn thông tin cấu hình lên kiện hàng để bên trong biết cách vận hành.

## 3. Một Dockerfile hoàn chỉnh, đơn giản

```dockerfile
FROM node:18
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
EXPOSE 3000
CMD ["node", "server.js"]
```

Đọc theo đúng thứ tự: chọn nền Node 18 → vào phòng /app → copy khai báo dependencies → cài dependencies → copy toàn bộ code còn lại → khai báo cửa 3000 → khi container khởi động thì chạy `node server.js`.

## 4. Vì sao thứ tự các dòng lại quan trọng?

Nhớ lại bài 2: mỗi dòng tạo một layer, và Docker cache layer không đổi để build nhanh hơn lần sau.

Ở ví dụ trên, `COPY package.json .` và `RUN npm install` được đặt **trước** `COPY . .` (copy toàn bộ code) một cách có chủ đích: package.json (khai báo thư viện) ít thay đổi hơn code hằng ngày. Nếu bạn chỉ sửa một dòng code, layer cài `npm install` vẫn được cache, không phải cài lại từ đầu. Nếu gộp `COPY . .` lên trước, chỉ cần đổi một dòng code là toàn bộ `npm install` phải chạy lại.

Đây là kỹ thuật quan trọng nhưng đơn giản: **những gì ít đổi thì đặt lên trên, những gì hay đổi thì đặt xuống dưới**.

## 5. Hiểu lầm thường gặp

- "RUN và CMD giống nhau, chỉ là cách viết khác" — **sai**. `RUN` thực thi lúc build (tạo layer, chạy một lần); `CMD` là lệnh mặc định chạy lúc container khởi động (chạy mỗi lần start).
- "EXPOSE tự động cho máy khác truy cập vào container" — **sai**, xem mục 2, phần EXPOSE. Không có `-p` khi `docker run` thì bên ngoài vẫn không vào được.
- "Sắp xếp thứ tự lệnh trong Dockerfile không quan trọng, miễn kết quả đúng" — **sai về mặt hiệu năng build**, dù kết quả cuối có thể giống nhau, thời gian build có thể chênh lệch rất lớn.

## Kiểm tra nhanh

1. Phân biệt `RUN` và `CMD` bằng đúng một câu cho mỗi lệnh.
> RUN dùng khi build chạy 1 lần, còn CMD chạy mỗi lần container khởi động thường dùng cho việc khởi chạy dự án
-> **AI Review:** ✅ Đúng cốt lõi. Bổ sung cho chính xác hơn: **RUN** thực thi lệnh **lúc build image** và kết quả được lưu thành một layer; **CMD** khai báo lệnh **mặc định** sẽ chạy mỗi khi container khởi động. Cụm "thường dùng cho việc khởi chạy dự án" hơi mơ hồ — nói chính xác hơn là CMD định nghĩa **process chính** (main process / PID 1) của container.
2. Vì sao nên `COPY package.json .` và `RUN npm install` trước khi `COPY . .` toàn bộ code?
> Vì RUN npm install để cài node_module phụ thuộc vào package.json nên package.json cần copy trước, còn COPY . . chạy sau cùng vì ngày xưa ấy nghĩ rằng tay trong tay
-> **AI Review:** ⚠️ Nửa đầu đúng, nửa sau lạc đề. Lý do chính không chỉ là "npm install phụ thuộc vào package.json" (đó là điều kiện cần), mà quan trọng hơn là **để tận dụng Docker layer cache**: `package.json` ít thay đổi, nên layer `RUN npm install` sẽ được cache lại qua các lần build. Nếu đặt `COPY . .` trước, mỗi khi bạn sửa bất kỳ dòng code nào, layer đó bị invalidate → `npm install` phải chạy lại từ đầu, rất tốn thời gian. Nguyên tắc: **thứ ít thay đổi đặt trước, thứ hay thay đổi đặt sau**.
3. `EXPOSE 3000` trong Dockerfile có tự động cho phép truy cập từ trình duyệt ở máy khác không? Vì sao?
> Không, EXPOSE 3000 không cho phép tự động truy cập từ trình duyệt bên ngoài, cần phải -p để map cổng port của host vào cổng port đó trong docker file
-> **AI Review:** ✅ Đúng. `EXPOSE` chỉ là **metadata/khai báo ý định** (documentation), nó không thực sự mở port. Cần dùng `docker run -p <host_port>:<container_port>` để thật sự ánh xạ (publish) port từ container ra máy host. Một điểm nhỏ về thuật ngữ: nói "cổng port đó trong docker file" nên sửa thành "cổng port của **container**" — vì khi container chạy, nó không còn liên quan trực tiếp đến Dockerfile nữa.
## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 4 sau khi đã được nhận xét.
