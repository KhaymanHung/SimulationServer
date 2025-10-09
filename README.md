# SimulationServer - 老虎機遊戲模擬 API

這是一個基於 Spring Boot 的老虎機遊戲模擬 API 服務，專門處理「Hop Panda」主題的遊戲邏輯。提供完整的遊戲模擬功能，包含多輪消除機制、特殊圖標處理和精確的數值計算。

## 主要功能

- **老虎機遊戲模擬**: 支援 3x3 網格的遊戲盤面
- **多輪消除機制**: 連續遊戲輪次，包含圖標消除和掉落補充
- **特殊圖標系統**: 百搭(0)、免費遊戲(1)、炸彈(2)、一般圖標(3-12)
- **倍數獎勵計算**: 特定圖標組合獲得倍數獎勵
- **精確數值計算**: 使用 BigDecimal 確保遊戲數值精確度
- **MySQL 資料庫整合**: 支援遊戲資料持久化
- **SSL 安全連接**: 預設啟用 HTTPS 安全連接

## 建置與執行

### 系統需求
- JDK 17 或以上版本
- Maven 3.6+
- MySQL 資料庫（可選，用於資料持久化）

### 快速啟動（Windows）

1. 進入專案資料夾
```cmd
cd "c:\Project File\SimulationServer"
```

2. 編譯與打包
```cmd
mvn clean package -DskipTests
```

3. 執行應用程式
```cmd
mvn spring-boot:run
```

### 其他執行方式

#### 使用 VS Code 任務
- **Maven: clean package**: 建置專案
- **Maven: spring-boot:run**: 啟動服務

#### 直接執行 JAR
```cmd
java -jar target\SimulationServer.jar
```

## API 說明

### 主要端點
- `POST /spine`: 執行老虎機遊戲旋轉
  - 支援參數: `gamble` (賭注金額), `gambleLv` (賭注等級), `lineCount` (線數)
  - 回傳完整的遊戲結果，包含多輪消除資料

### 回應格式
```json
{
  "code": "0",
  "data": "[遊戲結果JSON]",
  "msg": "success",
  "status": "true"
}
```

## 專案結構

```
src/
├── main/
│   ├── java/com/
│   │   ├── server/
│   │   │   ├── GameMain.java          # Spring Boot 主應用程式
│   │   │   └── APISlotHitHopPanda.java # 主要遊戲 API 控制器
│   │   └── utli/                      # 自訂工具類別
│   │       ├── Logger.java            # 日誌工具（台北時區）
│   │       ├── MysqlHelper.java       # MySQL 資料庫操作工具
│   │       └── BigDecimalUtil.java    # 精確數值計算工具
│   └── resources/
│       ├── application.properties     # 應用程式配置
│       └── keystore.p12              # SSL 金鑰庫
└── test/
    └── java/                          # 測試程式碼
```

## 配置說明

### 應用程式配置 (application.properties)
- **伺服器端口**: 8080
- **SSL 設定**: 預設啟用，使用 PKCS12 金鑰庫
- **CORS 設定**: 允許所有來源的跨域請求

### 資料庫配置
專案使用自訂的 `MysqlHelper` 工具類別進行資料庫操作：
```java
MysqlHelper db = new MysqlHelper(host, port, db, user, password);
```

## 開發指南

### 程式碼慣例
- 使用自訂工具類別而非標準庫
- 日誌記錄統一使用 `Logger` 類別（包含台北時區時間戳記）
- 數值計算統一使用 `BigDecimalUtil` 確保精確度
- API 回應統一使用 `LinkedHashMap` 保持欄位順序

### 遊戲邏輯說明
- **盤面結構**: 3x3 網格，共 9 個位置
- **消除規則**: 連續相同圖標可觸發消除
- **掉落機制**: 消除後上方圖標自動掉落補充
- **多輪遊戲**: 支援連續多輪直到沒有可消除圖標

## 技術棧

- **框架**: Spring Boot 3.2.12
- **Java 版本**: 17
- **建置工具**: Maven
- **資料庫**: MySQL (JDBC)
- **JSON 處理**: Jackson
- **安全**: SSL/TLS (PKCS12)

## 注意事項

- 遊戲邏輯複雜，修改前請充分理解消除和掉落機制
- 所有數值計算使用 BigDecimalUtil 確保精確度
- SSL 預設啟用，如需停用請修改 application.properties
- 建議在開發環境中使用 `-DskipTests` 加快建置速度