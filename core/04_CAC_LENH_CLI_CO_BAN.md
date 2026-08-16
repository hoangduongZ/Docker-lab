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
> docker stop là tạm dừng container đang chạy, còn docker rm là gỡ hẳn container, nếu không lưu trữ volumn thì mất luôn dữ liệu
-> **AI Review:** Đúng ý chính. Hai điểm nhỏ cần sửa: (1) chính tả đúng là **volume**, không phải "volumn"; (2) nên nhấn mạnh rằng `docker stop` chỉ dừng tiến trình — container và dữ liệu trong writable layer **vẫn còn nguyên**, có thể `docker start` lại bất cứ lúc nào. Còn `docker rm` mới thực sự xóa container cùng writable layer đó.
2. Bạn muốn xem log của một container đang chạy để tìm lỗi. Dùng lệnh nào?
> Tao dùng docker logs <container-name>
-> **AI Review:** Đúng. `docker logs <container-name>` là lệnh chính xác. Bổ sung: có thể thêm cờ `-f` (`docker logs -f`) để theo dõi log realtime (giống `tail -f`), rất hữu ích khi debug lỗi đang phát sinh.
3. Lệnh `docker exec -it ten-container sh` dùng để làm gì, và vì sao cần `-it`?
> Lệnh này để tương tác thật với hệ thống thư mục của container đó.`
-> **AI Review:** Đúng một phần, nhưng thiếu. Câu trả lời chỉ nói "tương tác với hệ thống thư mục" — thực tế `docker exec -it <container> sh` mở một **shell session bên trong container đang chạy**, cho phép bạn làm mọi thứ (chạy lệnh, xem log, kiểm tra process, debug…), không chỉ duyệt file. Phần thứ hai của câu hỏi — **vì sao cần `-it`** — bạn chưa trả lời. `-i` (interactive) giữ stdin mở để bạn gõ lệnh vào, `-t` (tty) cấp một pseudo-terminal để hiển thị output đúng định dạng. Thiếu một trong hai thì shell sẽ không hoạt động đúng cách.
## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 5 sau khi đã được nhận xét.

---

## 🧪 Thực hành — Tự tay, tự mắt, tự giải thích

> Nguyên tắc Feynman: nếu bạn không thể giải thích đơn giản thì bạn chưa hiểu. Mỗi bài dưới đây yêu cầu bạn **chạy lệnh thật**, **quan sát output**, rồi **viết giải thích bằng lời của mình** vào chỗ trống.

### Bài tập 1 — Vòng đời một container

Chạy lần lượt từng lệnh dưới đây. Sau mỗi bước, ghi lại bạn thấy gì.

```bash
# Bước 1: Tạo và chạy container từ image nginx, đặt tên là "thu-nghiem"
docker run -d --name thu-nghiem nginx

# Bước 2: Xác nhận container đang chạy
docker ps
```

> Ghi lại: bạn thấy container "thu-nghiem" ở trạng thái gì?
> Có thấy đang ở trạng thái running, nhưng không chỉ định port thì forward port như thế nào ?
-> **AI Review:** Quan sát đúng — container ở trạng thái `Up`. Câu hỏi thêm của bạn rất hay: khi không có `-p`, container **vẫn chạy bình thường bên trong**, nginx vẫn lắng nghe port 80 trong mạng nội bộ của container, nhưng **không có cổng nào được map ra máy host**, nên bạn không thể truy cập từ trình duyệt. Muốn truy cập được phải thêm `-p`, ví dụ `-p 8080:80`. Chi tiết sẽ được học kỹ ở bài 6.

```bash
# Bước 3: Dừng container
docker stop thu-nghiem

# Bước 4: Kiểm tra lại
docker ps
docker ps -a
```

> `docker ps` còn thấy "thu-nghiem" không? `docker ps -a` thì sao? Giải thích vì sao hai lệnh cho kết quả khác nhau.
> Không còn thấy thu-nghiem tại trạng thái running, docker ps -a thì vẫn còn vì nó bao gồm tất cả container bao gồm cả đang không running
-> **AI Review:** Chính xác. `docker ps` chỉ hiện container có status `Up` (đang chạy). `docker ps -a` hiện **tất cả** container bao gồm cả status `Exited` (đã dừng). Container sau khi `stop` vẫn tồn tại trên máy, chỉ là tiến trình bên trong không còn chạy.

```bash
# Bước 5: Khởi động lại container đã dừng
docker start thu-nghiem
docker ps
```

> Container có tạo mới không, hay là cái cũ sống lại? Làm sao bạn biết?
> Container không tạo mới, cái cũ running lại, vì trước đó nó còn ở docker ps -a trạng thái đang dừng hoạt động, nó start lại nó lại quay lại trạng thái running
-> **AI Review:** Đúng hoàn toàn. `docker start` khởi động lại container **đã tồn tại** — cùng Container ID, cùng tên, cùng dữ liệu trong writable layer. Cách xác nhận dễ nhất: so sánh **CONTAINER ID** trước khi stop và sau khi start — nếu trùng nhau thì chắc chắn là cùng một container. Đây là điểm khác biệt quan trọng so với `docker run` (luôn tạo container mới).

```bash
# Bước 6: Dọn dẹp
docker stop thu-nghiem
docker rm thu-nghiem
docker ps -a
```

> Sau `docker rm`, chạy `docker ps -a` có còn thấy "thu-nghiem" không? So sánh với bước 4.
> Tôi không còn thấy thử nghiệm, vì container thu-nghiem đã bị xoá khỏi docker container
-> **AI Review:** Chính xác. Sau `docker rm`, container biến mất hoàn toàn khỏi cả `docker ps` lẫn `docker ps -a`. So với bước 4 (chỉ `stop`): ở bước 4 container vẫn còn trong `docker ps -a` với trạng thái `Exited`, còn ở đây nó đã bị xóa sạch — đây chính là sự khác biệt cốt lõi giữa `stop` và `rm`.

### Bài tập 2 — `docker run` tạo container mới mỗi lần

```bash
docker run -d nginx
docker run -d nginx
docker run -d nginx
docker ps
```

> Bạn thấy bao nhiêu container đang chạy? Chúng có cùng tên không? Giải thích vì sao `docker run` 3 lần tạo ra 3 container riêng biệt từ cùng một image.
> tôi thấy 3 contaiiner của nginx đang chạy với 3 tên container khác nhau, có vẻ khi không chỉ định --name cho container mặc định nó sẽ sinh tên container ngẫu nhiên, còn việc nó sinh ra 3 container riêng biệt vì image là bất biến là khuôn mẫu sinh ra 3 container độc lập về môi trường không chạm tới nhau
-> **AI Review:** Rất tốt — nắm được cả 3 ý quan trọng: (1) không có `--name` thì Docker tự sinh tên ngẫu nhiên (dạng `tính_từ_tên_người`, ví dụ `happy_einstein`); (2) image là bất biến, chỉ đọc; (3) mỗi container có writable layer và namespace riêng, hoàn toàn độc lập. Lưu ý nhỏ: chính tả "contaiiner" → **container**.

```bash
# Dọn dẹp: dừng và xóa tất cả container vừa tạo
docker stop $(docker ps -q)
docker rm $(docker ps -aq)
```

### Bài tập 3 — Chui vào bên trong container

```bash
docker run -d --name hop-den nginx
docker exec -it hop-den sh
```

Khi đã ở bên trong container, thử chạy:

```bash
ls /
hostname
cat /etc/os-release
exit
```

> Hệ điều hành bên trong container có giống máy Mac của bạn không? `hostname` trả về gì? Giải thích bằng lời của bạn: vì sao bên trong container là một "thế giới" riêng.
> Hệ điều hành bên trong container không giống máy mac bên trong container là Linux 13 Debian | Hostname trả về id của container | Bên trong container là 1 thế giới riêng vì nó sinh ra để độc lập môi trường phục vụ cho app hiện tại, nhưng nó vẫn dùng chung kernel với mac của tôi
-> **AI Review:** Phần quan sát rất tốt — đúng là bên trong là Debian Linux, hostname là Container ID. Giải thích "thế giới riêng" cũng đúng hướng. Tuy nhiên có **một điểm cần sửa quan trọng**: trên macOS, container **không dùng chung kernel với Mac**. macOS dùng kernel Darwin (không phải Linux), nên Docker Desktop tạo một **máy ảo Linux nhẹ** (LinuxKit VM) chạy ngầm, và các container dùng chung kernel Linux của VM đó. Nói cách khác: Mac → VM Linux → container dùng chung kernel Linux của VM. Trên máy Linux thật thì mới đúng là container dùng chung kernel trực tiếp với host.

```bash
# Dọn dẹp
docker stop hop-den && docker rm hop-den
```

### Bài tập 4 — Xóa container không ảnh hưởng image

```bash
docker images | grep nginx
docker run -d --name tam-thoi nginx
docker rm -f tam-thoi
docker images | grep nginx
```

> Image `nginx` có bị xóa theo container không? Giải thích mối quan hệ giữa image và container bằng một câu đơn giản nhất bạn có thể nghĩ ra.
> 2 môi trường của image và container không liên quan đến nhau, nên khi xoá container không ảnh hưởng đến image nginx? nginx image là khuôn để sinh ra nhiều container
-> **AI Review:** Ý đúng, nhưng cách diễn đạt "không liên quan đến nhau" hơi quá — thực ra image và container **có liên quan**: container được tạo **từ** image (các read-only layer của image là nền tảng của container). Chính xác hơn nên nói: image là **nguồn gốc** (bản thiết kế), container là **sản phẩm** được sinh ra từ đó. Xóa sản phẩm không ảnh hưởng bản thiết kế — đó là lý do `docker rm` container mà image vẫn còn nguyên. Câu cuối "nginx image là khuôn để sinh ra nhiều container" — rất chuẩn.

### Bài tập 5 — Feynman cuối cùng: dạy lại

Tưởng tượng một người bạn chưa biết gì về Docker hỏi bạn: *"Docker có mấy lệnh cơ bản? Chúng khác nhau chỗ nào?"*

> Viết câu trả lời của bạn ở đây, dùng ngôn ngữ đời thường, không copy bài học. Nếu viết được dưới 5 câu mà người bạn hiểu — bạn đã nắm vững bài này.
> Docker có nhiều lệnh cơ bản, loanh quanh về đọc, chạy, xoá, dừng, tương tác terminal bên trong container, trong đầu cứ nhớ thành 2 khối images và container xong rồi nhớ tới lệnh đọc, chạy, xoá ... tương ứng của nó là gì, 1 image có thể sinh ra nhiều container, container thì khác với image nó là instance sinh ra từ image nó sẽ có thêm trạng thái running, stop như 1 thực thể có các hành động, về image thì nó là bản vẽ bất biến, nó có tự build image hoặc kéo image đã được tạo sẵn trên docker hub
-> **AI Review:** Nắm được ý cốt lõi và cách chia 2 nhóm (image vs container) rất đúng tư duy. Một vài góp ý để câu trả lời sắc hơn: (1) Hơi dài — thử rút gọn, ví dụ: *"Lệnh Docker chia 2 nhóm: nhóm image (build, pull, images, rmi) và nhóm container (run, ps, stop, start, rm, logs, exec). Image là bản thiết kế bất biến, mỗi lần `run` sinh ra một container mới — một instance đang sống có trạng thái riêng."* — 3 câu, người bạn sẽ hiểu ngay. (2) Bạn dùng từ "instance" rất chuẩn — đó đúng là thuật ngữ chuyên môn cho mối quan hệ image↔container. (3) Điểm hay: bạn nhắc được cả `build` lẫn `pull` — hai cách có image. Tổng thể: **đạt** — bạn đã hiểu bài này.
