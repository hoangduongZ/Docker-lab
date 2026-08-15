# 🏗️ Đây là "khuôn mẫu" để hô biến ra Căn Phòng Ma Thuật của em.
# "java" có phải là một khuôn mẫu còn tồn tại trên Docker Hub không nhỉ? 🤔
# (Gợi ý: thử tìm "java" trên hub.docker.com xem có thấy image chính thức nào không)
FROM openjdk:21-ea-oraclelinux8

# 📍 Bên trong Căn Phòng Ma Thuật, tạo một "căn phòng con" tên /my-app.
# Từ giờ mọi lệnh COPY/RUN bên dưới sẽ diễn ra trong căn phòng con này.
WORKDIR /my-app

# 📄 Mang "danh sách nguyên vật liệu" (pom.xml) vào phòng trước tiên,
# để Docker "nhớ" (cache) bước tải thư viện, không phải tải lại mỗi khi
# code của em thay đổi.
COPY pom.xml .

# 🔧 Mang "cây đũa thần" mvnw (Maven Wrapper) vào phòng.
# Nhưng... mvnw cần một người bạn đồng hành là thư mục .mvn/ nữa đó,
# em có thấy dòng nào COPY .mvn/ chưa? 🤔
COPY .mvn .mvn
COPY mvnw .

# 🏃 Ra lệnh cho "cây đũa thần" chạy để đóng gói ứng dụng.
# Hai câu hỏi nhỏ cho em:
#   1) mvnw đã được cấp quyền "được phép chạy" (chmod +x) chưa?
#   2) -DskipTest — có đúng chính tả cờ lệnh Maven không, hay là -DskipTests? 🤔
RUN chmod +x ./mvnw
COPY . .
RUN ./mvnw clean package -DskipTests

# 🚪 Khoét một "cái lỗ" trên tường phòng ở cổng 8080,
# để bên ngoài có thể "gõ cửa" gọi vào ứng dụng bên trong.
EXPOSE 8080

# ▶️ Khi Căn Phòng Ma Thuật được hô biến ra (docker run),
# đây là lệnh sẽ tự động chạy.
# Nhưng... "mvnw" ở đây có cần thêm "./" ở đầu không?
# Và ứng dụng đã được "đóng gói" (package) ở bước RUN phía trên rồi,
# vậy sao ở đây mình lại chạy "spring-boot:run" (chạy từ mã nguồn) thay vì
# chạy thẳng file .jar đã đóng gói bằng "java -jar ..." nhỉ? 🤔
CMD ["./mvnw", "spring-boot:run"]

# ============================================================
# 🪄 Cách hô biến và vận hành Căn Phòng Ma Thuật này:
# ============================================================
#
# 1) Hô biến "khuôn mẫu" (image) từ công thức Dockerfile ở trên,
#    đặt tên khuôn mẫu là "my-app" (dấu "." nghĩa là: lấy công thức
#    ngay tại thư mục hiện tại).
#
#      docker build -t my-app .
#
# 2) Từ khuôn mẫu đó, dựng thật một Căn Phòng Ma Thuật (container)
#    và sống động lên (chạy):
#    -p 8080:8080  → nối "cái lỗ" cổng 8080 trong phòng với cổng 8080
#                    ở máy thật của em, để em gõ cửa từ bên ngoài được.
#
#      docker run -p 8080:8080 my-app
#
# 🤔 Câu hỏi nhỏ cho em: nếu em Ctrl+C tắt rồi "docker run" lại,
# ứng dụng có phải build lại từ đầu không, hay Docker "nhớ" các lớp
# (layers) đã đóng gói trước đó để chạy nhanh hơn?
