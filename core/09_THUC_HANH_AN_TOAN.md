# BÀI 9 — THỰC HÀNH AN TOÀN VỚI DOCKER

Mục tiêu của bài này là tự tay chạy các lệnh đã học, quan sát kết quả thật. Yêu cầu: đã cài Docker Desktop (Windows/macOS) hoặc Docker Engine (Linux) và đã khởi động.

## Thực hành 1 — Kiểm tra Docker đã sẵn sàng

```bash
docker --version
docker run hello-world
```

`hello-world` là một image cực nhỏ, chỉ để in ra một đoạn text xác nhận Docker hoạt động đúng. Đọc kỹ output — nó tự giải thích các bước Docker vừa làm (tải image, tạo container, chạy, in kết quả).

## Thực hành 2 — Tải và chạy một web server có sẵn

```bash
docker pull nginx
docker run -d -p 8080:80 --name web nginx
```

Mở trình duyệt tới `http://localhost:8080` — bạn sẽ thấy trang chào mừng mặc định của nginx. Đây chính là port mapping học ở bài 6.

## Thực hành 3 — Quan sát container đang chạy

```bash
docker ps
docker logs web
docker exec -it web sh
```

Trong phiên `exec`, thử gõ `ls /usr/share/nginx/html` để xem file HTML mặc định, rồi gõ `exit` để thoát ra (không làm dừng container).

## Thực hành 4 — Thử volume (bind mount)

```bash
mkdir demo-html
echo "<h1>Trang cua toi</h1>" > demo-html/index.html
docker run -d -p 8081:80 -v "$(pwd)/demo-html:/usr/share/nginx/html" --name web-custom nginx
```

Mở `http://localhost:8081`, bạn sẽ thấy đúng nội dung file bạn vừa tạo — đây là bind mount (bài 5), nối thẳng thư mục trên máy bạn vào container.

## Thực hành 5 — Dừng và dọn dẹp đúng cách

```bash
docker stop web web-custom
docker ps -a
docker rm web web-custom
docker rmi nginx
```

Quan sát: sau `stop`, container vẫn xuất hiện trong `docker ps -a` (chỉ dừng, chưa mất). Sau `rm`, container biến mất khỏi danh sách.

## Thực hành 6 — Build image của chính bạn

Tạo một thư mục mới với hai file:

`Dockerfile`:

```dockerfile
FROM node:18
WORKDIR /app
COPY server.js .
CMD ["node", "server.js"]
```

`server.js`:

```javascript
require("http").createServer((req, res) => {
  res.end("Xin chao tu container cua toi!");
}).listen(3000);
```

Build và chạy:

```bash
docker build -t demo-api:1.0 .
docker run -d -p 3000:3000 --name demo demo-api:1.0
curl http://localhost:3000
```

Nếu thấy dòng chữ "Xin chao tu container cua toi!" — bạn vừa tự tay đi hết vòng đời: viết Dockerfile, build image, chạy container, kiểm tra bằng port mapping.

## Lưu ý an toàn

- Chỉ chạy image bạn tự build hoặc image chính thức, có nguồn gốc rõ ràng (như `nginx`, `node`, `postgres` từ Docker Hub chính thức). Không chạy tùy tiện image lạ không rõ nguồn gốc, đặc biệt tránh chạy với quyền mở rộng không cần thiết.
- Nhớ `docker rm`/`docker rmi` dọn dẹp những gì bạn tạo ra trong bài thực hành này để máy không đầy container/image thử nghiệm.

## Nhật ký thực hành

Ghi lại theo mẫu:

```text
1. docker run hello-world in ra thông báo gì?
2. Truy cập localhost:8080 thấy nội dung gì?
3. Sau khi bind mount demo-html, localhost:8081 hiển thị gì?
4. Sau docker stop, container còn thấy trong docker ps -a không? Sau docker rm thì sao?
5. curl localhost:3000 trả về gì sau khi build và chạy demo-api?
```

## Kiểm tra nhanh

1. Sau `docker stop web`, container `web` có còn trong `docker ps -a` không? Sau `docker rm web` thì sao?
2. Vì sao truy cập `http://localhost:8081` lại thấy đúng nội dung file bạn tạo trong thư mục `demo-html`?
3. Nếu bạn quên `-p` khi `docker run` container `demo-api`, `curl http://localhost:3000` sẽ ra sao?

## DỪNG TẠI ĐÂY

Gửi nhật ký và câu trả lời cho ChatGPT/Claude. Chỉ mở bài 10 sau khi đã được nhận xét.
