# BÀI 1 — CONTAINER LÀ GÌ VÀ VÌ SAO CẦN DOCKER

## 1. Vấn đề trước khi có Docker

"Trên máy tôi chạy được mà" — câu than thở kinh điển của dev. Máy dev có Node 18, máy server có Node 16; máy này thiếu một thư viện hệ thống, máy kia lại thừa. Ứng dụng chạy tốt ở một nơi, lỗi tơi bời ở nơi khác.

Nguyên nhân: ứng dụng không chỉ cần code, nó cần cả một **môi trường** xung quanh — ngôn ngữ đúng phiên bản, thư viện, biến cấu hình, hệ điều hành phù hợp.

Docker giải quyết vấn đề này bằng cách đóng gói ứng dụng CÙNG với toàn bộ môi trường nó cần thành một **kiện hàng chuẩn hóa**, chạy giống hệt nhau ở bất kỳ đâu: máy dev, máy đồng nghiệp, hay server thật.

## 2. Ẩn dụ trung tâm: Cảng container hàng hải

Trước khi có container hàng hải chuẩn hóa (thùng kim loại kích thước cố định), việc bốc dỡ hàng hóa lên tàu rất thủ công: mỗi kiện hàng một hình dạng, phải xếp tay, tốn thời gian, mỗi cảng lại xếp một kiểu khác.

Khi thùng container ra đời: hàng hóa được đóng vào các thùng kích thước chuẩn. Cần cẩu ở bất kỳ cảng nào trên thế giới cũng nâng được, tàu nào cũng chở được, xe tải nào cũng kéo được. Bên trong thùng chứa gì không quan trọng với cần cẩu — nó chỉ quan tâm thùng có đúng chuẩn hay không.

Docker làm điều tương tự với phần mềm:

- Ứng dụng + môi trường của nó = hàng hóa được đóng vào một **container chuẩn hóa**.
- Docker Engine (cài trên bất kỳ máy nào) = **cần cẩu/bến cảng** biết cách nâng, chạy loại container này, không cần quan tâm bên trong là Node.js, Python hay Go.
- Vì vậy container chạy giống hệt nhau dù ở laptop bạn, máy đồng nghiệp, hay server công ty.

## 3. Container so với Máy ảo (VM) — vì sao container nhẹ hơn

Trước Docker, cách cô lập ứng dụng phổ biến là dùng **máy ảo (VM)**.

Ẩn dụ: hãy tưởng tượng cả hệ thống là một khu đất.

- **VM giống xây nhiều căn nhà riêng biệt.** Mỗi nhà có nền móng riêng, hệ thống điện nước riêng, tường bao riêng — tức mỗi VM có một **hệ điều hành (kernel) đầy đủ riêng**. Rất an toàn, cách ly tuyệt đối, nhưng xây chậm, tốn đất, tốn vật liệu (tốn RAM/CPU/dung lượng ổ đĩa).
- **Container giống nhiều căn hộ trong cùng một tòa chung cư.** Tất cả căn hộ dùng chung nền móng và hệ thống điện nước chính của tòa nhà — tức các container **dùng chung kernel của hệ điều hành host**. Mỗi căn hộ vẫn có cửa riêng, khóa riêng, đồ đạc riêng (cô lập về tiến trình, file hệ thống, network), nhưng dựng lên nhanh hơn nhiều và tiết kiệm tài nguyên hơn nhiều so với xây nhà riêng.

Nối lại với ẩn dụ cảng: VM giống mỗi lô hàng đi trên một con tàu riêng, kèm động cơ và thủy thủ đoàn riêng — an toàn tuyệt đối nhưng cực tốn kém. Container giống nhiều kiện hàng chuẩn hóa cùng đi chung một con tàu lớn (dùng chung "động cơ" là kernel hệ điều hành), mỗi kiện vẫn niêm phong kín, không lẫn hàng, nhưng tiết kiệm hơn nhiều.

Hệ quả thực tế: một máy chủ có thể chạy được hàng chục container nhẹ, nhưng chỉ chạy nổi vài VM đầy đủ.

## 4. Bảng so sánh nhanh

| | Máy ảo (VM) | Container (Docker) |
| --- | --- | --- |
| Hệ điều hành | Mỗi VM có kernel riêng | Dùng chung kernel của host |
| Tốc độ khởi động | Chậm (giây đến phút, boot cả OS) | Rất nhanh (thường dưới 1 giây) |
| Kích thước | Nặng (hàng GB, gồm cả OS) | Nhẹ (thường vài chục đến vài trăm MB) |
| Mật độ trên 1 máy | Ít | Nhiều |
| Mức cô lập | Rất mạnh (OS riêng hoàn toàn) | Mạnh, nhưng chia sẻ kernel host |

Không cần nhớ số liệu cụ thể — cần nhớ **nguyên lý**: container nhẹ hơn vì dùng chung kernel, VM nặng hơn vì mỗi cái có cả một OS riêng.

## 5. Docker không thay thế VM hoàn toàn

Hai công cụ có mục đích chồng lấn nhưng không phải lúc nào cũng thay thế nhau. VM vẫn cần khi bạn thực sự cần một hệ điều hành khác hoàn toàn (ví dụ chạy Windows trên máy Linux) hoặc mức cô lập bảo mật cực mạnh. Docker phù hợp nhất khi bạn muốn đóng gói và chạy nhanh, nhiều ứng dụng, dùng chung một kernel Linux.

Trên thực tế, rất nhiều server production là: một VM (hoặc máy vật lý) chạy Docker, và bên trong VM đó chạy hàng chục container. Hai lớp cô lập chồng lên nhau, không loại trừ nhau.

## 6. Hiểu lầm thường gặp

- "Container là máy ảo thu nhỏ" — **sai**. Container không có kernel riêng; nó là tiến trình chạy trên kernel của máy host, chỉ được cô lập bằng các cơ chế của hệ điều hành (bạn chưa cần nhớ tên kỹ thuật, chỉ cần hiểu ý tưởng "cô lập nhưng dùng chung nền").
- "Cài Docker là xong, không cần biết ứng dụng chạy trên hệ điều hành nào" — **sai**. Container Linux cần chạy trên kernel Linux. Trên Windows/macOS, Docker Desktop âm thầm chạy một máy ảo Linux nhỏ phía sau để làm việc này.
- "Docker luôn nhanh hơn và tốt hơn VM trong mọi trường hợp" — **sai**, xem mục 5.

## Kiểm tra nhanh

1. Vì sao nhiều container có thể chạy nhẹ nhàng trên một máy, trong khi máy đó chỉ chạy nổi vài VM?
-> Vì docker dùng chung kernel của host nên không độc lập OS, nhẹ có thể chạy được nhiều container, còn VM tạo ra OS riêng có kernel riêng, nên riêng việc tạo OS đã tốn bộ nhớ và kernel riêng tốn hiệu năng rồi
-> **AI Review:** Trả lời rất chính xác và hiểu rõ bản chất. Điểm mấu chốt là sự khác biệt giữa việc dùng chung kernel (container) và cấp phát OS/kernel riêng (VM).
2. Trong ẩn dụ tòa chung cư, "nền móng và hệ thống điện nước chung" tương ứng với khái niệm kỹ thuật nào?
-> Tương đương với khái niệm các container dùng chung kernel của host
-> **AI Review:** Chính xác. "Nền móng và hệ thống điện nước chung" chính là Kernel của hệ điều hành Host.
3. Docker có hoàn toàn thay thế được VM không? Cho một tình huống vẫn cần VM.
-> Docker không hoàn toàn thay thế được VM, nếu server cần chạy trên window hoặc khác hệ điều hành của host, hoặc muốn độc lập và bảo mật cao VM là lựa chọn hoàn hảo, hoặc có thể kết hợp cả 2, dùng docker trong VM
-> **AI Review:** Rất tốt. Bạn đã nêu đúng các điểm quan trọng: (1) Cần chạy hệ điều hành khác loại, (2) Cần mức độ bảo mật/cô lập cấp cao nhất, và (3) Thực tế người ta thường kết hợp cả hai bằng cách chạy Docker bên trong VM.

## DỪNG TẠI ĐÂY

Hãy trả lời ba câu trên bằng lời của bạn và gửi cho ChatGPT/Claude. Chỉ mở bài 2 sau khi đã được nhận xét.
