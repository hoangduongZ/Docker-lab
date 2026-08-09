# DOCKER CƠ BẢN — BẮT ĐẦU Ở ĐÂY

Chào bạn! Bộ tài liệu này dạy Docker theo phương pháp Feynman: giải thích bằng ngôn ngữ đơn giản nhất, dùng ẩn dụ đời thực, chủ động chỉ ra hiểu lầm phổ biến, và bắt bạn tự diễn giải lại trước khi đi tiếp.

Đây là phần **core** — nền tảng Docker dành cho junior backend dev. Sau khi hoàn thành, bạn sẽ có đủ vốn để học tiếp phần **medium** (trung cấp/nâng cao, hướng tới middle backend dev) ở thư mục `docker/medium`.

## Ẩn dụ xuyên suốt: Cảng container hàng hải

Tên "Docker" và biểu tượng con cá voi chở container không phải ngẫu nhiên. Toàn bộ khóa học dùng một ẩn dụ duy nhất, cứ nhầm là quay lại đây đọc lại:

- Ứng dụng của bạn = **hàng hóa** cần vận chuyển.
- Image = **bản thiết kế/khuôn đúc** kiện hàng: hàng gì, đóng gói ra sao.
- Container = **kiện hàng thật** đã đóng gói xong, niêm phong, chạy được ở bất kỳ cảng nào.
- Dockerfile = **tờ hướng dẫn đóng gói** viết ra từng bước.
- Docker Hub/registry = **kho bãi trung tâm** lưu và chia sẻ các mẫu kiện hàng.
- Volume = **kho ngoài bãi**, tách khỏi kiện hàng — hàng không mất dù kiện hàng bị dỡ bỏ.
- Network (port, bridge) = **hệ thống cửa và đường nội bộ** trong cảng để kiện hàng giao tiếp.
- Docker Compose = **bản kế hoạch điều phối** nhiều kiện hàng cùng vận hành một dây chuyền.

## Overview để import XMind

Trước khi bắt đầu và sau khi học xong, mở `OVERVIEW_XMIND.md` và import vào XMind để xem toàn cảnh/ôn tập. File này không thay thế việc học chi tiết từng bài bên dưới.

## Cách học

Học đúng thứ tự dưới đây. Mỗi lần chỉ đọc một bài:

1. `01_CONTAINER_VA_VM.md`
2. `02_IMAGE_VA_CONTAINER.md`
3. `03_DOCKERFILE_CONG_THUC_DONG_GOI.md`
4. `04_CAC_LENH_CLI_CO_BAN.md`
5. `05_VOLUME_LUU_TRU_DU_LIEU.md`
6. `06_NETWORKING_CO_BAN.md`
7. `07_DOCKER_COMPOSE_NHAP_MON.md`
8. `08_GHEP_TOAN_BO_FLOW.md`
9. `09_THUC_HANH_AN_TOAN.md`
10. `10_BAI_KIEM_TRA_TONG_HOP.md`
11. `11_GOI_Y_DAP_AN.md` — chỉ mở sau khi đã tự trả lời.

## Quy tắc học quan trọng

Cuối mỗi bài có mục **Kiểm tra nhanh**. Khi gặp dòng **DỪNG TẠI ĐÂY**:

1. Không mở bài tiếp theo.
2. Trả lời bằng lời của chính bạn, không cần dùng thuật ngữ hoàn hảo.
3. Gửi câu trả lời cho ChatGPT/Claude để được nhận xét (có thể dùng mẫu trong `prompt-review-cau-hoi-kiem-tra.md`).
4. Chỉ học bài tiếp theo sau khi đã sửa phần chưa hiểu.

## Mục tiêu cuối khóa

Sau khi học xong, bạn có thể tự kể lại câu chuyện sau:

> Tôi viết code và một Dockerfile mô tả cách đóng gói nó. `docker build` đọc Dockerfile và tạo ra một image — một bản thiết kế bất biến. `docker run` khởi động một container từ image đó — một tiến trình cô lập, đóng gói đủ mọi thứ ứng dụng cần, không phụ thuộc máy tôi cài gì. Muốn giữ dữ liệu qua lần chạy sau, tôi gắn volume. Muốn máy ngoài truy cập được, tôi map port. Khi ứng dụng cần nhiều container phối hợp (ví dụ app + database), tôi mô tả tất cả trong một file Docker Compose và chạy bằng một lệnh duy nhất.

Không cần học thuộc ngay. Chúng ta sẽ xây dựng câu chuyện này từng mảnh nhỏ.
