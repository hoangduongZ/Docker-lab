# Docker Cơ Bản

## Tổng quan

### Định nghĩa
- Docker là công cụ đóng gói ứng dụng cùng toàn bộ môi trường nó cần (thư viện, runtime, cấu hình) thành một đơn vị chạy độc lập gọi là container.
- Container dùng chung kernel của hệ điều hành host, khác với máy ảo (VM) có hệ điều hành riêng đầy đủ.
- Bộ tài liệu này là phần core — nền tảng Docker cho junior backend dev.

### Mục đích dùng map này
- Xem trước khi bắt đầu học 00-11 để có bức tranh tổng thể.
- Xem lại sau khi học xong để ôn và tự kiểm tra.
- Tra nhanh khi cần debug lỗi Docker cơ bản.

### Khi nào cần dùng
- Trước lúc học chi tiết từng bài.
- Sau khi hoàn thành bài 11.
- Khi container lỗi và cần xác định vấn đề nằm ở build, run, volume hay network.

### Kết quả mong muốn
- Tự kể lại luồng từ code tới container chạy được bằng các từ khóa: Dockerfile, image, container, volume, port, network, Compose.
- Phân biệt được lỗi build, lỗi container tự thoát, lỗi thiếu port mapping và lỗi mất dữ liệu do thiếu volume.

## Ẩn dụ trung tâm

### Cảng container hàng hải
- Ứng dụng là hàng hóa cần vận chuyển.
- Image là bản thiết kế/khuôn đúc kiện hàng.
- Container là kiện hàng thật, đã đóng gói, niêm phong, chạy được ở bất kỳ đâu.
- Dockerfile là tờ hướng dẫn đóng gói viết ra từng bước.
- Docker Hub/registry là kho bãi trung tâm lưu và chia sẻ mẫu kiện hàng.
- Volume là kho ngoài bãi, tách khỏi kiện hàng, hàng không mất dù kiện hàng bị dỡ.
- Port/network là hệ cửa và đường nội bộ trong cảng để kiện hàng giao tiếp.
- Docker Compose là bản kế hoạch điều phối nhiều kiện hàng cùng vận hành.

### Các nhân vật chính
- Image: bản thiết kế bất biến.
- Container: thực thể đang chạy, tạo ra từ image.
- Dockerfile: hướng dẫn tạo image.
- Volume: nơi giữ dữ liệu qua khỏi vòng đời container.
- Port mapping: cầu nối giữa host và container.
- Docker Compose: điều phối nhiều container bằng một file.

## Bản chất cốt lõi

### Nguyên lý chính
- Đóng gói ứng dụng và dependencies thành image bất biến.
- Một image có thể tạo ra nhiều container độc lập.
- Container không tự lưu trạng thái lâu dài trừ khi gắn volume.
- Container mặc định cô lập, cần chủ động mở port và network để giao tiếp.

### Vấn đề nó giải quyết
- "Chạy trên máy tôi thì được, lên server thì lỗi" — image đóng gói toàn bộ môi trường, giảm khác biệt giữa các máy.
- Một máy chủ chạy được nhiều ứng dụng có phiên bản ngôn ngữ/thư viện khác nhau mà không xung đột.
- Tái tạo nhanh môi trường chạy giống hệt nhau ở dev, staging, production.

### Cách tư duy đúng
- Image là bản thiết kế tĩnh; container là thực thể sống được sinh ra từ bản thiết kế đó.
- Container nên được coi là có thể xóa/tạo lại bất cứ lúc nào; dữ liệu quan trọng phải nằm ở volume, không nằm trong container.
- Muốn container giao tiếp ra ngoài hoặc với nhau, phải chủ động cấu hình port/network, không có gì tự động "nhìn thấy" nhau theo tên trừ khi dùng network riêng hoặc Compose.

### Hiểu lầm thường gặp
- Container là máy ảo thu nhỏ — sai, container chia sẻ kernel host, không có hệ điều hành riêng.
- Xóa container thì mất luôn image — sai, image độc lập, chỉ mất phần dữ liệu ghi thêm trong container đó.
- `EXPOSE` trong Dockerfile tự động cho phép truy cập từ bên ngoài — sai, đó chỉ là khai báo ý định, cần `-p` khi chạy.
- Hai container bất kỳ luôn gọi được nhau bằng tên — sai, chỉ đúng trong network tự tạo hoặc trong Docker Compose.

## Cấu trúc kiến thức

### Container vs VM
- Container dùng chung kernel host, nhẹ, khởi động nhanh, mật độ cao.
- VM có kernel riêng cho mỗi máy ảo, cô lập mạnh hơn nhưng nặng và chậm hơn.
- Docker không thay thế hoàn toàn VM; nhiều hệ thống dùng cả hai lớp cùng lúc.

### Image vs Container
- Image là khuôn đúc bất biến, gồm nhiều layer xếp chồng, có thể cache để build nhanh hơn.
- Container là kiện hàng thật tạo từ image, có thêm một lớp ghi được tạm thời trên cùng.
- Một image tạo ra được nhiều container độc lập.

### Dockerfile
- FROM chọn nền tảng có sẵn, WORKDIR chọn thư mục làm việc.
- COPY đưa code vào image, RUN thực hiện thao tác lúc build.
- CMD là lệnh mặc định chạy lúc container khởi động, khác với RUN chạy lúc build.
- EXPOSE chỉ khai báo ý định, ENV dán nhãn cấu hình.
- Thứ tự dòng lệnh ảnh hưởng hiệu quả cache: ít đổi đặt trên, hay đổi đặt dưới.

### CLI cơ bản
- Nhóm image: build, images, pull, rmi.
- Nhóm container: run, ps, ps -a, stop, start, rm, logs, exec.
- `stop` chỉ tạm dừng, dữ liệu còn nguyên; `rm` xóa hẳn container.

### Volume
- Dữ liệu ghi trong container mất khi container bị xóa nếu không gắn volume.
- Named volume do Docker quản lý vị trí lưu; bind mount nối thẳng một thư mục cụ thể trên host.
- Volume tồn tại độc lập với vòng đời container, phải chủ động gắn bằng `-v`.

### Networking cơ bản
- Port mapping `-p HOST:CONTAINER` nối cổng ngoài host với cổng trong container.
- Mạng bridge mặc định cho container IP nội bộ nhưng không tự động phân giải tên.
- Network do người dùng tự tạo (hoặc Compose tự tạo) mới cho phép gọi nhau bằng tên service.

### Docker Compose
- Một file YAML mô tả nhiều service, mỗi service là một container.
- `build`, `image`, `ports`, `environment`, `volumes`, `depends_on` là các khóa cơ bản.
- Compose tự tạo network riêng nên các service gọi nhau bằng tên.
- `docker compose down` giữ lại named volume theo mặc định, chỉ mất khi thêm `-v`.

### Mối quan hệ giữa các phần
- Dockerfile mô tả cách tạo image.
- Image dùng để tạo container.
- Volume giữ dữ liệu bền vững tách khỏi container.
- Port/network quyết định ai gọi được ai.
- Compose gói toàn bộ các container liên quan lại thành một hệ thống chạy bằng một lệnh.

## Cách hoạt động

### Bước 1-2: Viết Dockerfile và build image
- Dockerfile mô tả các bước đóng gói.
- `docker build` tạo ra image gồm nhiều layer.

### Bước 3-4: Chạy container và kiểm tra
- `docker run` tạo container từ image.
- `docker logs`, `docker ps` để xác nhận container chạy đúng.

### Bước 5-6: Gắn volume và mở port
- Gắn volume nếu cần giữ dữ liệu qua khỏi vòng đời container.
- Dùng `-p` để máy host/bên ngoài truy cập được service trong container.

### Bước 7-8: Nhiều container phối hợp bằng Compose
- Mô tả toàn bộ service trong một file `docker-compose.yml`.
- `docker compose up` khởi động tất cả, các service gọi nhau bằng tên nhờ network riêng.

### Bước 9: Vận hành và dọn dẹp
- `docker compose down` dừng hệ thống, giữ lại dữ liệu volume.
- Chỉ xóa volume khi chắc chắn không cần dữ liệu đó nữa.

## Ví dụ dễ hiểu

### Ví dụ đúng luồng
- Một API Node.js: viết Dockerfile, `docker build` ra image, `docker run -p` để test, viết Compose thêm database có volume, `docker compose up -d` chạy cả hệ thống.

### Ví dụ sai lầm phổ biến
- Chạy container database bằng `docker run postgres` không gắn volume; sau này xóa container để cập nhật phiên bản mới, mất sạch dữ liệu.

### Ví dụ đời thường tương ứng
- Image như khuôn bánh, container như những cái bánh đúc ra từ khuôn.
- Volume như kho ngoài bãi, tách khỏi kiện hàng.
- Port mapping như hành lang nối cổng cảng vào đúng cửa của một tòa nhà bên trong.

## Ứng dụng thực tế: thực hành quan sát

### Kiểm tra Docker và chạy thử
- `docker run hello-world` để xác nhận Docker hoạt động.

### Chạy web server có sẵn và xem port mapping
- `docker run -d -p 8080:80 nginx` rồi mở `localhost:8080`.

### Vào bên trong container đang chạy
- `docker exec -it ten-container sh` để kiểm tra trực tiếp.

### Build và chạy image tự viết
- Viết Dockerfile + code mẫu, `docker build`, `docker run -p`, `curl` để xác nhận.

## Lỗi thường gặp khi debug

### Container thoát ngay sau khi chạy
- Kiểm tra `docker logs` và `docker ps -a` để xem exit code và log lỗi cuối cùng.
- Thường do CMD sai, thiếu file, hoặc tiến trình chính tự kết thúc.

### Chạy được nhưng không truy cập được từ bên ngoài
- Kiểm tra đã có `-p HOST:CONTAINER` chưa; `EXPOSE` trong Dockerfile không đủ để mở cổng ra ngoài.

### Mất dữ liệu sau khi xóa/tạo lại container
- Do container không có volume; dữ liệu nằm ở lớp ghi tạm, mất khi `docker rm`.

### Hai container không gọi được nhau
- Kiểm tra có đang ở cùng một network do người dùng tạo (hoặc cùng một file Compose) hay không; bridge mặc định không tự phân giải tên.

### Cách tránh nhầm lẫn
- Luôn xác định lỗi đang ở giai đoạn nào: build image, khởi động container, gắn volume, hay networking giữa các container.

## Checklist ghi nhớ

### Câu hỏi tự kiểm tra
- Tôi có phân biệt được image và container không.
- Tôi có biết dữ liệu nằm ở đâu khi container bị xóa không.
- Tôi có phân biệt được `docker stop` và `docker rm` không.
- Tôi có thể tự vẽ lại luồng: Dockerfile, image, container, volume, port, network, Compose.

### Dấu hiệu đã hiểu
- Nhìn một lỗi Docker và biết ngay nó thuộc giai đoạn build, run, volume hay network.
- Giải thích được Docker cho người không biết kỹ thuật bằng ẩn dụ cảng container.
- Viết được một Dockerfile và một file Compose đơn giản mà không cần xem mẫu.

### Dấu hiệu chưa hiểu
- Gộp mọi lỗi Docker thành một câu chung chung như "container bị lỗi".
- Nhầm `docker stop` với `docker rm`, hoặc nhầm image với container.

### Hành động tiếp theo
- Nếu còn nhầm, quay lại đúng bài 00-11 tương ứng thay vì đọc lại toàn bộ.
- Khi đã vững, học tiếp phần trung cấp tại `docker/medium` để hướng tới trình độ middle backend dev.

## Tóm tắt một câu

### Ý chính cần nhớ
- Docker đóng gói ứng dụng thành image bất biến qua Dockerfile, chạy thành các container cô lập nhưng nhẹ, dùng volume để giữ dữ liệu bền vững, dùng port/network để quyết định ai gọi được ai, và dùng Docker Compose để điều phối nhiều container như một hệ thống duy nhất.
