# BÀI 6 — NETWORKING CƠ BẢN: PORT VÀ BRIDGE NETWORK

## 1. Vấn đề: container là một "hộp kín"

Container chạy cô lập với máy host và với các container khác. Mặc định, không ai từ bên ngoài gõ cửa được vào trong, và bản thân container cũng không tự nhiên "nhìn thấy" container khác theo tên.

Bài này giải quyết hai câu hỏi:

1. Làm sao máy host (hoặc trình duyệt của bạn) truy cập được vào một service đang chạy trong container?
2. Làm sao hai container "nói chuyện" được với nhau?

## 2. Port mapping — mở một cửa nối ra ngoài

```bash
docker run -d -p 8080:80 nginx
```

Cú pháp `-p HOST:CONTAINER` nghĩa là: nối cửa `8080` ở ngoài máy host với cửa `80` bên trong container.

Ẩn dụ: container giống một tòa nhà có cửa `80` cố định bên trong (nginx luôn lắng nghe ở cửa 80). Nhưng tòa nhà này đặt bên trong khu vực riêng của cảng, người ngoài không tự đi vào được. `-p 8080:80` giống việc xây một hành lang nối từ cổng chính của cảng (host, cổng 8080) thẳng tới đúng cửa 80 của tòa nhà đó.

Không có `-p`, container vẫn chạy nginx bình thường ở cửa 80 của riêng nó, nhưng máy host và bên ngoài không có đường vào.

## 3. Container có IP riêng trong một mạng ảo

Khi bạn không chỉ định gì thêm, Docker tự đặt container vào một mạng mặc định gọi là **bridge**. Đây là một mạng nội bộ ảo do Docker tạo ra — giống một khu vực sân chung trong cảng, nơi mọi kiện hàng mới được đặt vào, mỗi kiện có một địa chỉ IP nội bộ riêng trong khu đó.

Hai container trong cùng mạng bridge mặc định này **có thể** gọi nhau qua địa chỉ IP nội bộ.

## 4. Hiểu lầm quan trọng: bridge mặc định KHÔNG cho gọi nhau bằng tên

Đây là điểm rất nhiều người mới hiểu sai: trên mạng bridge **mặc định**, container A **không thể** gọi container B chỉ bằng tên (`--name`) của B — nó phải biết IP nội bộ của B, mà IP đó có thể đổi mỗi lần container được tạo lại. Không có "danh bạ" tên tự động ở đây.

Để hai container gọi nhau bằng **tên** một cách ổn định (giống có DNS nội bộ), bạn cần tạo một **mạng do người dùng định nghĩa (user-defined network)**:

```bash
docker network create mang-cua-toi
docker run -d --name db --network mang-cua-toi postgres
docker run -d --name api --network mang-cua-toi ten-api
```

Trong `mang-cua-toi`, container `api` có thể kết nối tới database chỉ bằng tên `db` (ví dụ `db:5432`), Docker tự phân giải tên đó thành đúng IP nội bộ. Việc tạo network riêng và cách dùng sâu hơn thuộc phần trung cấp (`docker/medium`); ở bài này bạn chỉ cần nhớ **có sự khác biệt** giữa bridge mặc định và network tự tạo.

Ghi nhớ nhanh: đây chính xác là cơ chế mà Docker Compose (bài 7) tự động thiết lập giúp bạn phía sau — lý do vì sao trong Compose, các service gọi nhau bằng tên lại hoạt động ngay mà không cần cấu hình IP.

## 5. Container tự gọi host, và host tự gọi container — hai chuyện khác nhau

- Máy host gọi vào container: cần `-p` như mục 2.
- Container gọi ra ngoài Internet (ví dụ gọi API bên thứ ba): thường hoạt động sẵn, Docker cho container đi ra ngoài qua NAT của máy host, không cần cấu hình thêm trong phạm vi cơ bản này.

## 6. Hiểu lầm thường gặp

- "Không cần `-p` thì trình duyệt trên máy vẫn vào được web server trong container" — **sai**, xem mục 2.
- "Hai container bất kỳ luôn gọi được nhau bằng tên" — **sai**, chỉ đúng trong network tự tạo (hoặc trong Compose), không đúng trên bridge mặc định — xem mục 4.
- "`-p 8080:80` nghĩa là container lắng nghe ở port 8080" — **sai**, container vẫn lắng nghe đúng port khai báo bên trong nó (80); `8080` chỉ là cổng phía host dùng để trỏ vào.

## Kiểm tra nhanh

1. Câu lệnh `docker run -d -p 3000:3000 ten-api` dùng để làm gì? Nếu bỏ `-p` thì khác gì?
2. Hai container chạy trên mạng bridge mặc định có tự gọi nhau bằng tên (`--name`) được không? Muốn gọi nhau bằng tên ổn định thì làm sao?
3. Trong cặp `-p 8080:80`, số nào là port của host, số nào là port bên trong container?

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 7 sau khi đã được nhận xét.
