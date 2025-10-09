# SimulationServer - AI Agent 開發指南

## 專案架構概覽

這是一個基於 Spring Boot 的老虎機遊戲模擬 API 服務，主要處理「Hit Hop Panda」主題的遊戲邏輯。

### 核心元件
- **GameMain.java**: Spring Boot 主應用程式，配置 CORS 允許所有來源
- **APISlotHitHopPanda.java**: 主要 API 控制器，實作複雜的遊戲邏輯
- **工具類別** (`com.utli` 套件):
  - `Logger.java`: 自訂日誌工具，使用台北時區格式
  - `MysqlHelper.java`: JDBC MySQL 資料庫操作工具
  - `BigDecimalUtil.java`: BigDecimal 精確計算工具

### 遊戲邏輯特點
- **多輪消除機制**: 支援連續多輪遊戲，每輪可能觸發消除和掉落
- **特殊圖標**: 百搭(0)、免費遊戲(1)、炸彈(2)、一般圖標(3-12)
- **動態盤面**: 3x3 網格，消除後上方圖標掉落補充
- **倍數計算**: 特定圖標組合可獲得倍數獎勵

## 開發工作流程

### 建置與執行
```bash
# 使用 Maven 任務 (推薦)
mvn clean package -DskipTests  # 建置
mvn spring-boot:run           # 執行

# 或使用 VS Code 任務
# "Maven: clean package" - 建置專案
# "Maven: spring-boot:run" - 啟動服務
```

### 專案慣例

#### 1. 工具類別使用模式
```java
// 日誌記錄 - 使用自訂 Logger
private static final Logger LOGGER = new Logger();
LOGGER.log("訊息內容");

// 資料庫操作 - 使用 MysqlHelper
MysqlHelper db = new MysqlHelper(host, port, db, user, password);
List<Map<String, Object>> results = db.queryList("SELECT * FROM table");

// 精確計算 - 使用 BigDecimalUtil
double result = BigDecimalUtil.multiply(a, b);
```

#### 2. API 回應格式
```java
Map<String, String> result = new LinkedHashMap<>();
result.put("code", "0");
result.put("data", jsonData);
result.put("msg", "success");
result.put("status", "true");
return ResponseEntity.ok(result);
```

#### 3. JSON 處理
```java
// 使用 Jackson ObjectMapper
ObjectMapper objectMapper = new ObjectMapper();
String json = objectMapper.writeValueAsString(data);
```

#### 4. SSL 配置
- 預設啟用 SSL (server.ssl.enabled=true)
- 使用 PKCS12 格式金鑰庫 (classpath:keystore.p12)
- 預設密碼: changeit

### 程式碼模式

#### 錯誤處理
```java
try {
    // 業務邏輯
} catch (JsonProcessingException e) {
    LOGGER.log("Parse error: " + e.getMessage());
}
```

#### 遊戲狀態管理
- 使用 Map<String, Object> 儲存遊戲狀態
- 巢狀結構: dt(資料) -> si(遊戲資訊) -> rs(結果)
- 支援多輪連續遊戲直到 roundEnd = true

### 關鍵檔案參考
- `src/main/java/com/server/APISlotHitHopPanda.java`: 完整遊戲邏輯實作
- `src/main/java/com/utli/`: 工具類別集合
- `src/main/resources/application.properties`: SSL 和伺服器配置
- `pom.xml`: Spring Boot 3.2.12 + Java 17 配置

### 開發注意事項
- 所有數值計算使用 BigDecimalUtil 確保精確度
- 日誌記錄包含台北時區時間戳記
- API 統一使用 LinkedHashMap 保持欄位順序
- 遊戲邏輯複雜，修改前請充分理解消除和掉落機制