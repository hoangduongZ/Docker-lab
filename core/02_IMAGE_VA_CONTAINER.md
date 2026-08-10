# BÀI 2 — IMAGE VÀ CONTAINER: KHUÔN ĐÚC VÀ KIỆN HÀNG THẬT

## 1. Vấn đề: cần một "bản thiết kế" trước khi có "hàng thật"

Ở bài 1 bạn đã biết container là một kiện hàng chuẩn hóa, chạy được ở bất kỳ đâu. Nhưng trước khi có kiện hàng, phải có một thứ mô tả: đóng gói gì, gói theo thứ tự nào, cấu hình mặc định ra sao. Thứ đó gọi là **image**.

## 2. Ẩn dụ: khuôn đúc và vật đúc ra

Image giống một cái **khuôn đúc kiện hàng chuẩn**: nó định nghĩa sẵn bên trong có gì — hệ điều hành nền, các thư viện, code ứng dụng, cấu hình mặc định.

Container là **kiện hàng thật** được đúc ra từ khuôn đó, đang tồn tại và có thể hoạt động (chạy tiến trình, ghi log, nhận request).

Điểm quan trọng nhất: **từ một image, bạn có thể tạo ra nhiều container** — giống một cái khuôn bánh có thể đúc ra hàng chục cái bánh giống hệt nhau. Mỗi cái bánh (container) là một thực thể riêng, độc lập, dù chung một khuôn.

Nếu bạn quen lập trình hướng đối tượng, có một cách nói khác cùng ý: **image giống class, container giống instance/object** được khởi tạo từ class đó.

## 3. Image được ghép từ nhiều lớp (layer)

Image không phải một khối duy nhất. Nó được xây dựng thành nhiều **layer** xếp chồng lên nhau, mỗi layer là kết quả của một bước đóng gói (ví dụ: "cài hệ điều hành nền", "cài thư viện", "copy code vào").

Ẩn dụ: giống xếp chồng nhiều tấm kính trong suốt, mỗi tấm vẽ thêm một phần nội dung. Nhìn từ trên xuống, bạn thấy toàn bộ bức tranh hoàn chỉnh (image), nhưng thực chất nó gồm nhiều tấm riêng biệt.

Lợi ích của việc chia layer: nếu layer nào không đổi (ví dụ layer cài hệ điều hành nền), Docker có thể **dùng lại (cache)** thay vì làm lại từ đầu, giúp build nhanh hơn. Bài học sâu hơn về tối ưu layer sẽ có ở phần trung cấp (`docker/medium`); ở đây chỉ cần nhớ nguyên lý.

## 4. Container thêm gì so với image?

Container = image (chỉ đọc, bất biến) + một **lớp ghi được** mỏng nằm trên cùng, nơi lưu những thay đổi phát sinh khi container chạy (file tạo ra, log, dữ liệu ghi thêm).

Vì vậy:

- Image không tự thay đổi khi container chạy.
- Hai container chạy từ cùng một image là hai thực thể độc lập; thay đổi trong container này không ảnh hưởng container kia hay ảnh hưởng chính image gốc.
- Nếu container bị xóa mà không có nơi lưu riêng (volume — học ở bài 5), lớp ghi được đó mất theo, image gốc vẫn còn nguyên.

## 5. Image đến từ đâu?

Hai nguồn phổ biến:

1. **Tải về** một image có sẵn từ kho lưu trữ công khai, phổ biến nhất là Docker Hub — giống lấy một khuôn đúc có sẵn từ kho bãi trung tâm (ví dụ image `nginx`, `postgres`, `node`).
2. **Tự xây dựng (build)** image riêng từ một tờ hướng dẫn đóng gói gọi là **Dockerfile** — bạn sẽ học cách viết ở bài 3.

Một image thường có tên dạng `tên:tag`, ví dụ `node:18`, `nginx:latest`. Tag giống như "phiên bản" của khuôn đúc; `latest` là quy ước ngầm định cho "bản mới nhất", không phải một phép thuật đặc biệt.

## 6. Hiểu lầm thường gặp

- "Sửa code trong container là sửa luôn image" — **sai**. Container chỉ có một lớp ghi tạm trên cùng; muốn thay đổi thực sự vào image, bạn phải build lại từ Dockerfile (cách đúng) hoặc dùng `docker commit` (hiếm khi nên dùng trong thực tế, dễ mất kiểm soát).
- "Xóa container thì mất luôn image" — **sai**. Image tồn tại độc lập trên máy (xem bằng `docker images`); xóa container không đụng đến image nó được tạo ra từ đó.
- "`latest` luôn là bản ổn định nhất/mới nhất thật sự" — **sai**, đó chỉ là một tag do người build đặt tên theo quy ước, không có gì đảm bảo nội dung.

## Kiểm tra nhanh

1. Một image có thể tạo ra bao nhiêu container? Việc sửa dữ liệu trong một container có ảnh hưởng container khác cùng image không?
-> 1 image có thể tạo ra nhiều container, việc sửa dữ liệu trong 1 container không ảnh hưởng gì đến image, vì image chỉ có trách nhiệm tạo ra instance container
-> **AI Review:** Đúng hướng nhưng trả lời chưa đầy đủ. Câu hỏi gồm hai phần: (1) một image tạo được bao nhiêu container — bạn trả lời đúng: **nhiều container** (không giới hạn). (2) sửa dữ liệu trong container A có ảnh hưởng container B cùng image không — bạn chỉ nói "không ảnh hưởng đến image" mà chưa trả lời thẳng phần này. Đáp án đầy đủ: **Không ảnh hưởng container khác**, vì mỗi container có **lớp ghi riêng (writable layer)** độc lập; thay đổi chỉ nằm trong lớp ghi của container đó, không lan sang container khác và cũng không thay đổi image gốc (read-only). Ngoài ra, nói "image chỉ có trách nhiệm tạo ra instance container" hơi thiếu chính xác — image là **template bất biến (immutable)** chứa filesystem và metadata; Docker Engine mới là thứ tạo container từ image.
2. Vì sao chia image thành nhiều layer lại giúp build nhanh hơn?
-> Vì khi có nhiều layer, docker có cơ chế cache, tức không chạy lại từ đầu các layer không bị thay đổi
-> **AI Review:** Chính xác! Đây là ý cốt lõi: Docker cache từng layer và chỉ build lại từ layer đầu tiên bị thay đổi trở đi. Bổ sung thêm: ngoài tăng tốc build, việc chia layer còn giúp **tiết kiệm dung lượng lưu trữ và băng thông** — nhiều image có thể **chia sẻ chung các layer giống nhau** (ví dụ cùng dùng base image `ubuntu:22.04` thì layer đó chỉ lưu một lần trên đĩa). Thuật ngữ kỹ thuật liên quan là **content-addressable storage** — mỗi layer được định danh bằng hash của nội dung.
3. Nếu bạn `docker run` một image ba lần, bạn có ba container hay một? Giải thích bằng ẩn dụ khuôn đúc.
-> Tôi sẽ có 3 container, vì image là bất biến, chạy một lần sẽ tạo ra 1 container độc lập riêng, image giống như khuôn bánh trưng, có thể tạo ra nhiều cái bánh, mỗi cái bánh là độc lập không liên quan đến nhau chỉ giống nhau về hình dáng
-> **AI Review:** Hoàn toàn chính xác! Ba lần `docker run` = ba container riêng biệt, mỗi container có lớp ghi (writable layer) riêng. Ẩn dụ khuôn bánh rất đúng tinh thần bài học. Một điểm nhỏ về từ ngữ: bạn viết "khuôn bánh trưng" — có lẽ ý bạn là "khuôn bánh trung thu"? Nếu vậy thì ẩn dụ rất hay: một khuôn bánh trung thu đúc ra nhiều cái bánh giống hệt nhau về hình dáng, nhưng mỗi cái bánh tồn tại độc lập — ăn (xóa) cái này không ảnh hưởng cái kia, và khuôn (image) vẫn còn nguyên.

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên và gửi cho ChatGPT/Claude. Chỉ mở bài 3 sau khi đã được nhận xét.
