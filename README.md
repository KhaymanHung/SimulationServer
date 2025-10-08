# Java Spring Boot Web API

這是一個使用 Spring Boot 建立的 Java Web API 範例，包含一個 /hello endpoint。

## 建置與執行

1. 安裝 JDK 17 或以上版本。
2. 於專案根目錄執行：
   ```
   ./mvnw spring-boot:run
   ```
   或
   ```
   mvnw.cmd spring-boot:run
   ```
   （Windows 系統建議使用 mvnw.cmd）

3. API 測試：
   - 訪問 http://localhost:8080/hello 會回傳 `Hello World`。

## 專案結構
- src/main/java/... 主要程式碼
- src/test/java/... 測試程式碼
- pom.xml Maven 設定檔

---

# SimulationServer

快速建置與執行（Windows PowerShell / cmd）：

1. 進入專案資料夾
```powershell
cd "c:\Project File\Reverse\Panda\SimulationServer"
```

2. 編譯與打包（使用 Maven）
```powershell
mvn clean package -DskipTests
```

3. (可選) 指定輸出 jar 名稱
```powershell
mvn clean package -Dproject.build.finalName=SimulationServer -DskipTests
```

4. 執行可執行 jar（若為 Spring Boot fat jar）
```powershell
java -jar "target\SimulationServer.jar"
# 或
java -jar "target\SimulationServer.jar"
```

備註：
- 若是 thin JAR，請確保相依 jars 在 classpath 中。
- 若要在開發時直接啟動：`mvn spring-boot:run`

本專案為快速啟動範例，可依需求擴充。