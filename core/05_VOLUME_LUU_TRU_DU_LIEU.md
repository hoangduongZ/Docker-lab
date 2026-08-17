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
> Vì khi không có volume, dữ liệu chỉ được lưu tạm thời trong container, cần container để chúng lưu trữ dữ liệu mapping ra ngoài container
-> **AI Review:** Hướng đúng nhưng chưa chạm đúng bản chất. Lý do kỹ thuật chính xác: container ghi dữ liệu vào **writable layer** (lớp ghi được) nằm trên cùng của image. Lớp này **gắn liền với container** — khi `docker rm`, Docker xóa container kèm cả writable layer đó, nên dữ liệu mất. Volume giải quyết vấn đề này vì nó là vùng lưu trữ **nằm ngoài container**, tồn tại độc lập — container bị xóa thì volume vẫn còn. Nên nhớ thuật ngữ "writable layer" vì phỏng vấn hay hỏi.
2. Bạn đang code một API và muốn mỗi lần sửa file trên máy thì container tự thấy thay đổi ngay — nên dùng named volume hay bind mount?
> Cái này là source code thì thay đổi thường xuyên, cần dùng bind mount tới source code của mình
-> **AI Review:** Đúng. Bind mount là lựa chọn chính xác cho trường hợp này vì nó nối thẳng thư mục trên máy host vào container — khi bạn sửa file trên máy, container thấy thay đổi ngay lập tức (và ngược lại). Named volume không phù hợp ở đây vì Docker quản lý vị trí lưu, bạn không dễ trỏ nó tới đúng thư mục source code đang làm việc.
3. `docker stop` một container chạy database (không có volume) rồi `docker start` lại — dữ liệu còn không? Nếu đổi thành `docker rm` rồi tạo container mới thì sao?
> Dữ liệu vẫn còn vì khi docker stop thì chỉ dừng hoạt động container, chứ không xoá hẳn container đi như docker rm
-> **AI Review:** Phần `docker stop` → `docker start`: **Đúng**. Dữ liệu vẫn còn vì writable layer không bị xóa khi stop. Tuy nhiên, bạn **chưa trả lời phần hai** của câu hỏi: nếu `docker rm` rồi tạo container mới thì sao? Đáp án: dữ liệu **mất hết**, vì `rm` xóa container kèm writable layer, và do không có volume nên không có nơi nào giữ lại dữ liệu. Container mới tạo từ cùng image sẽ bắt đầu với writable layer trống hoàn toàn. Đây chính là lý do bài này nhấn mạnh phải gắn volume cho database.
## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 6 sau khi đã được nhận xét.

---

## 🧪 Thực hành — Tự tay, tự mắt, tự giải thích

> Nguyên tắc Feynman: làm thật, thấy thật, rồi giải thích bằng lời của mình. Không giải thích được = chưa hiểu.

### Bài tập 1 — Chứng minh dữ liệu "hay quên" của container

Bài này bạn sẽ **tận mắt thấy** dữ liệu biến mất khi không dùng volume.

```bash
# Bước 1: Tạo container, ghi một file vào bên trong
docker run -d --name db-khong-volume nginx
docker exec db-khong-volume sh -c 'echo "du lieu quan trong" > /tmp/ghi-chu.txt'

# Bước 2: Kiểm tra — file có tồn tại không?
docker exec db-khong-volume cat /tmp/ghi-chu.txt
```

> Bạn thấy nội dung gì? File đang nằm ở đâu (trong container hay trên máy host)?
> File đang trên container

```bash
# Bước 3: Xóa container
docker rm -f db-khong-volume

# Bước 4: Tạo container MỚI cùng tên
docker run -d --name db-khong-volume nginx
docker exec db-khong-volume cat /tmp/ghi-chu.txt
```

> Lệnh cuối có báo lỗi không? File `ghi-chu.txt` đã đi đâu? Giải thích bằng khái niệm writable layer.
> Container khi bị remove đi sẽ đồng thời xoá đi writable layer, ghi-chu.txt nằm trong layer này nên sẽ bị xoá đi

```bash
# Dọn dẹp
docker rm -f db-khong-volume
```

### Bài tập 2 — Named volume cứu dữ liệu

Lặp lại kịch bản trên, nhưng lần này **có volume**.

```bash
# Bước 1: Tạo volume và gắn vào container
docker volume create kho-du-lieu
docker run -d --name db-co-volume -v kho-du-lieu:/du-lieu nginx

# Bước 2: Ghi dữ liệu vào THƯ MỤC VOLUME (không phải /tmp)
docker exec db-co-volume sh -c 'echo "du lieu duoc bao ve" > /du-lieu/ghi-chu.txt'
docker exec db-co-volume cat /du-lieu/ghi-chu.txt
```

> File này đang được lưu ở volume hay writable layer? Làm sao bạn phân biệt?
> File này đang được lưu ở volume. vì tôi nhớ đã mapping volume đó vào thư mục trong container, hoặc tôi xoá đi map lại vẫn còn dữ liệu

```bash
# Bước 3: Xóa container, tạo container MỚI gắn lại cùng volume
docker rm -f db-co-volume
docker run -d --name db-moi -v kho-du-lieu:/du-lieu nginx
docker exec db-moi cat /du-lieu/ghi-chu.txt
```

> Dữ liệu còn không? So sánh với bài tập 1, bước 4. Giải thích vì sao kết quả khác nhau.
> Dữ liệu còn. cái trước không có volume để lưu dữ liệu ra ngoài, còn cái này có

```bash
# Dọn dẹp
docker rm -f db-moi
docker volume rm kho-du-lieu
```

### Bài tập 3 — Bind mount: thay đổi trên máy host ↔ container thời gian thực

```bash
# Bước 1: Tạo một thư mục trên máy host
mkdir -p /tmp/docker-lab-test

# Bước 2: Chạy container, bind mount thư mục vừa tạo
docker run -d --name live-sync -v /tmp/docker-lab-test:/app nginx
```

```bash
# Bước 3: Tạo file TRÊN MÁY HOST
echo "tao tao file nay tren mac" > /tmp/docker-lab-test/hello.txt

# Bước 4: Đọc file TỪ TRONG CONTAINER
docker exec live-sync cat /app/hello.txt
```

> Container có thấy file bạn vừa tạo trên máy host không? Bạn không hề copy gì vào container — vậy tại sao nó thấy được?
> vẫn thấy trong container, tôi đang hiểu, là nó vừa lưu vào container vừa lưu vào volume, hoặc là nó link cái gì đó chỉ lưu vào volume khi truy cập từ container nó trỏ sang volume

```bash
# Bước 5: Tạo file TỪ TRONG CONTAINER
docker exec live-sync sh -c 'echo "container ghi nguoc ra" > /app/phan-hoi.txt'

# Bước 6: Đọc file TRÊN MÁY HOST
cat /tmp/docker-lab-test/phan-hoi.txt
```

> Chiều ngược lại có hoạt động không? Giải thích bind mount hoạt động theo chiều nào: một chiều hay hai chiều?
> Có hoạt động. bind mount này hoạt động theo 2 chiều 

```bash
# Dọn dẹp
docker rm -f live-sync
rm -rf /tmp/docker-lab-test
```

### Bài tập 4 — `docker stop` có mất dữ liệu volume không?

```bash
docker volume create du-lieu-test
docker run -d --name kiem-tra -v du-lieu-test:/data nginx
docker exec kiem-tra sh -c 'echo "truoc khi stop" > /data/note.txt'

# Stop rồi start lại (KHÔNG rm)
docker stop kiem-tra
docker start kiem-tra
docker exec kiem-tra cat /data/note.txt
```

> Dữ liệu còn không? Bây giờ hãy tự trả lời: `docker stop` có ảnh hưởng gì tới volume không?
>

```bash
# Dọn dẹp
docker rm -f kiem-tra
docker volume rm du-lieu-test
```

### Bài tập 5 — Feynman cuối cùng: dạy lại

Một đồng nghiệp mới hỏi bạn: *"Tao chạy database trong Docker, mỗi lần tạo lại container là mất hết data. Có cách nào giữ lại không? Bind mount với volume khác nhau chỗ nào?"*

> Viết câu trả lời ở đây, dùng ngôn ngữ bình thường, không copy bài học. Gợi ý: kể lại những gì bạn vừa thấy ở bài tập 1–4 để minh họa.
>
