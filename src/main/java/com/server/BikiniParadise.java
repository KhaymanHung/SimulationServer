package com.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.io.BufferedReader;
// import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;

import com.utli.BigDecimalUtil;
import com.utli.Logger;

@CrossOrigin(origins = "*")
@RestController
public class BikiniParadise {
    double userMoney = 0;
    long sid = 1;
    private static final Logger LOGGER = new Logger();
    private final int[][] linkList = new int[][] {
        {0,4,8,12,16}, {1,5,9,13,17}, {2,6,10,14,18}, {3,7,11,15,19}, {0,5,8,13,16},
        {1,6,9,14,17}, {2,7,10,15,18}, {1,4,9,12,17}, {2,5,10,13,18}, {3,6,11,14,19},
        {0,4,9,12,16}, {1,5,10,13,17}, {1,5,8,13,17}, {2,6,9,14,18}, {3,7,10,15,19},
        {0,5,9,13,16}, {1,6,10,14,17}, {2,7,11,15,18}, {1,4,8,12,17}, {2,5,9,13,18},
        {3,6,10,14,19}, {0,5,10,13,16}, {1,6,11,14,17}, {2,5,8,13,18}, {3,6,9,14,19}
    };
    private final int[][] multiple = new int[][] {
        {0}, {0}, {0},                                              // 0 百搭，1 免費遊戲，2 空白
        {20,25,30}, {15,20,25}, {15,20,25}, {10,15,20}, {10,15,20}, // 3-7 一般圖標
        {5,10,14}, {5,10,14}, {5,10,14}, {5,10,14}, {5,10,14}       // 8-12 一般圖標
    };
    private final int[] freegameNumber = new int[] {0,0,0,8,12,20}; // 0-2 無免費遊戲，3-5 分別對應免費遊戲次數
    
    @CrossOrigin(origins = "*")
    @org.springframework.web.bind.annotation.PostMapping(
        value = "/api/BikiniParadise/EnterGame",
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enterGame(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            // ignore
        }
        String request = sb.toString();
        LOGGER.log("==================================== enter game start ====================================");
        LOGGER.log("enterGame, request: " + request);
        String token = "MTAwMDA0ODY0fDE3NTkzODgxNTZ8MHw4ODY2MDIwfDEwMTAwMDMz";
        this.userMoney = 500000.00;
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("tableid", 10100033);
        data.put("token", token);
        Map<String, Object> innerData = new LinkedHashMap<>();
        innerData.put("userid", 100004864);
        innerData.put("NickName", "2948739");
        innerData.put("gold", this.userMoney);
        innerData.put("betScores", Arrays.asList(02,12,80));
        innerData.put("lJackPotScores", new ArrayList<>());
        innerData.put("GlodMultiple", 100);
        innerData.put("wicon", "");
        data.put("data", innerData);
        data.put("gameid", 8866020);
        data.put("levelid", 88660200);
        data.put("Lv", Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        data.put("Currency", "BRL");
        data.put("totalLineCount", 25);
        data.put("minBetScore", 10);
                
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("iTotalFree", 0);
        context.put("currTotalFree", 0);
        context.put("betScore", 0);
        context.put("bSelectFree", false);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String contextJson = objectMapper.writeValueAsString(context);
            data.put("Context", contextJson);
        } catch (JsonProcessingException e) {
            data.put("Context", "{}");
        }
        
        data.put("otherGameID", 0);
        data.put("result", 1);
        data.put("msg", null);

        // 回傳結果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("status", true);
        result.put("msg", "Success");
        result.put("data", data);

        LOGGER.log("==================================== enter game end ====================================");

        return ResponseEntity.ok(result);
    }

    @org.springframework.web.bind.annotation.PostMapping(
        value = "/api/BikiniParadise/Gamble",
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, String>> gamble(@org.springframework.web.bind.annotation.RequestBody(required = false) String request) {
        LOGGER.log("==================================== spine start ====================================");
        LOGGER.log("Gamble, request:" + request);
        double gambleValue = 10;
        int gambleLv = 1;
        double lineCountValue = 20;
        if (request != null && !request.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> reqMap = objectMapper.readValue(request, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                Object gambleObj = reqMap.get("gamble");
                if (gambleObj != null) {
                    gambleValue = Double.parseDouble(gambleObj.toString());
                }
                Object gambleLvObj = reqMap.get("lv");
                if (gambleLvObj != null) {
                    gambleLv = Integer.parseInt(gambleLvObj.toString());
                }
                Object lineCountObj = reqMap.get("lineCount");
                if (lineCountObj != null) {
                    lineCountValue = Double.parseDouble(lineCountObj.toString());
                }
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                LOGGER.log("Parse request error (JsonProcessingException): " + e.getMessage());
            }
        }
        // double gamble = (gambleValue * lineCountValue);
        // double gamble = BigDecimalUtil.multiply(gambleValue, lineCountValue);

        List<Object> gambleResult = new ArrayList<>();
        // 產生新一輪結果
        Map<String, Object> gambleItem = createSpinResult(gambleValue, gambleLv, lineCountValue);
        if (gambleItem != null) {
            gambleResult.add(gambleItem);
        }

        // this.userMoney = BigDecimalUtil.subtract(this.userMoney, gamble);
        // LOGGER.log("userMoney after gamble: " + this.userMoney + ", gamble: " + gamble + ", gambleValue: " + gambleValue + ", lineCountValue: " + lineCountValue);
        
        // 回傳結果
        Map<String, String> result = new LinkedHashMap<>();
        result.put("code", "0");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String gambleResultJson = objectMapper.writeValueAsString(gambleResult);
            result.put("data", gambleResultJson);
        } catch (JsonProcessingException e) {
            result.put("data", "[]");
        }
        result.put("msg", "success");
        result.put("status", "true");

        LOGGER.log("====================================  spine end  ====================================");

        return ResponseEntity.ok(result);
    }

    private Map<String, Object> createSpinResult(double gambleValue, int gambleLv, double lineCountValue) {
        if (gambleValue < 1 || gambleLv < 1 || lineCountValue < 1) {
            LOGGER.log("gambleValue:" + gambleValue + ", gambleLv:" + gambleLv + ", lineCountValue:" + lineCountValue);
            return null;
        }

        List<Integer> rl = new LinkedList<>();      // 縱4橫5盤面
        
        // 產生隨機盤面，範圍為0-12，0 百搭，1 免費遊戲，2 空白，3-12 一般圖標
        for (int i = 0; i < 20; i++) {
            rl.add((int)Math.floor((Math.random() * 10) + 3)); // 產生3~12之間的圖標
        }

        // for (int i = 0; i < 2; i++) {
        //     // 百搭、免費遊戲各有1%機率出現，如果隨機到的位置是其它特殊圖標，則不放
        //     // 測試暫不出現免費遊戲圖標
        //     if (i == 1) {
        //         continue;
        //     }

        //     // 測試暫時提高機率為10%
        //     if ((int)Math.floor(Math.random() * 100) < 10) {
        //         int wildPos = (int)Math.floor(Math.random() * 9); // 隨機產生0~8之間的位置
        //         if (rl.get(wildPos) != 0 && rl.get(wildPos) != 1) {
        //             rl.set(wildPos, i);
        //         }
        //     }
        // }

        LOGGER.log("First round, rl: " + rl.toString());

        String spinSid = this.getSid();
        String psid = spinSid;
        double cs = BigDecimalUtil.divide(gambleValue, 100, 2); // 每線押注金額
        double gamble = BigDecimalUtil.multiply(gambleValue, gambleLv);
        gamble = BigDecimalUtil.multiply(gamble, lineCountValue);
        double tb = BigDecimalUtil.divide(gamble, 100, 2);  // 此輪押注金額，免費遊戲僅第一輪有
        double tbb = tb;

        Map<String, Object> wp = null;  // 此輪中獎圖標位置

        Map<String, Object> checkWinList = checkWinList(rl);
        if (checkWinList != null && !checkWinList.isEmpty()) {
            wp = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : checkWinList.entrySet()) {
                wp.put(entry.getKey(), entry.getValue());
            }
        }
        // LOGGER.log("[debug] checkWinList, index: " + index + ", rl: " + rl.toString() + ", ws: " + ((ws != null) ? ws.toString() : "null") + ", wp: " + ((wp != null) ? wp.toString() : "null"));
        
        Map<String, Object> gambleItem = new LinkedHashMap<>();
        Map<String, Object> dt = new LinkedHashMap<>();
        Map<String, Object> lw = null;      // 乘倍前每線得獎金額
        Map<String, Object> rwsp = null;    // 各線得獎倍數
        double wabm = 0;                    // 乘倍前總得獎金額
        double ctw = 0;                     // 乘倍後總得獎金額
        double wm = 1;                      // 總乘倍數
        List<Integer> rwm = null;           // 中乘倍的column位置

        // 百搭的出現位置，免費遊戲中顯示為變動後的位置，沒有百搭時顯示為空list而非null
        List<List<Integer>> wppr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            wppr.add(new ArrayList<>());
        }
        for (int i = 0; i < rl.size(); i += 4) {
            if (rl.get(i) == 0) {
                wppr.get((int)(i / 4)).add(i % 4);
            }
        }
        for (int i = 0; i < wppr.size(); i++) {
            if (wppr.get(i).size() == 4) {
                if (rwm == null) {
                    rwm = new ArrayList<>();
                }
                int multi = i + 1;
                
                // 記錄中乘倍的column位置
                rwm.add(multi);

                // 計算總乘倍數
                wm = BigDecimalUtil.multiply(wm, multi);
            }
        }

        // 在rwsp中加入得獎倍數，在lw中加入得獎金額，並計算乘倍前後的總得獎金額
        if (wp != null) {
            for (Map.Entry<String, Object> entry : wp.entrySet()) {
                @SuppressWarnings("unchecked")
                List<Integer> link = (List<Integer>) entry.getValue();
                int linkCount = link.size();
                int winSymbols = rl.get(link.get(0));
                if (winSymbols == 0) {
                    // 如果第一個位置是百搭，則找下一個非百搭圖標作為檢查圖標
                    for (int k = 1; k < link.size(); k++) {
                        if (rl.get(link.get(k)) != 0) {
                            winSymbols = rl.get(link.get(k));
                            break;
                        }
                    }
                }

                // 計算得獎倍數
                int multipleValue = multiple[winSymbols][linkCount - 3];
                if (rwsp == null) {
                    rwsp = new LinkedHashMap<>();
                }
                rwsp.put(entry.getKey(), multipleValue);

                // 計算乘倍前實際得獎金額
                double lineWinAmount = BigDecimalUtil.multiply(cs, multipleValue);
                if (lw == null) {
                    lw = new LinkedHashMap<>();
                }
                lw.put(entry.getKey(), lineWinAmount);

                // 累計乘倍前總得獎金額
                wabm = BigDecimalUtil.add(wabm, lineWinAmount);
            }

            ctw = BigDecimalUtil.multiply(wabm, wm); // 乘倍後總得獎金額
        }
                
        Map<String, Object> fstc = null;        // 免費遊戲中會有"2"，值為免費遊戲回合數

                 
        double tw = ctw;                  // 此輪總得獎金額
        double np = BigDecimalUtil.subtract(tw, tb);                    // 此輪淨利潤(得獎-押注)，可能為負數
        double blb = BigDecimalUtil.divide(this.userMoney, 100, 2);   // 押注前餘額
        double blab = BigDecimalUtil.subtract(blb, tb);                 // 押注後餘額
        double bl = BigDecimalUtil.add(blb, np);                        // 得獎後餘額
        
        double aw = tw;     // 累計得獎金額
        
        int cwc = 0;        // 連續得獎回合數
        int pcwc = 0;       // 同cwc

        Map<String, Object> fs = null;
        if (rl.contains(1)) {
            // 前一輪沒有免費遊戲，此輪沒有消除及炸彈，盤面有免費遊戲圖標，表示進入免費遊戲
            fs = new LinkedHashMap<>();
            int fsCount = 0;
            for (int i = 0; i < rl.size(); i++) {
                if (rl.get(i) == 1) {
                    fsCount++;
                }
            }
            fsCount = freegameNumber[fsCount];  // 免費遊戲數量
            fs.put("s", fsCount);           // 免費遊戲未執行回合量
            fs.put("ts", fsCount);          // 免費遊戲總回合數
            fs.put("aw", 0.0);              // 免費遊戲總得獎金額
            fs.put("nosa", fsCount);        // 免費遊戲增加回合數
            fs.put("wpbn", null);           // 免費遊戲中百搭圖標變動前位置
        }
        LOGGER.log("fs: " + ((fs != null) ? fs.toString() : "null"));


        int st = 1;     // 前一回合的狀態，第一回合為1
        int nst = 1;        // 本輪狀態，一般遊戲及免費遊戲最後一回合為1，進入免費遊戲當回合及免費遊戲中為2

        this.userMoney = BigDecimalUtil.multiply(bl, 100);
        LOGGER.log("userMoney after gamble, start money:" + blb + ", gamble: " + tb
                + ", tw:" + tw + ", aw: " + aw + ", profit: " + np + ", end money: " + bl);
        LOGGER.log("round:" + 1
                            + ", rl: " + rl.toString()
                            + ", lw: " + (lw != null ? lw.toString() : "null")
                            + ", wp: " + (wp != null ? wp.toString() : "null")
                            + ", fstc: " + (fstc != null ? fstc.toString() : "null"));
        // wp = null; // 先將消除圖標位置清空，避免影響後續邏輯判斷
        // lw = null; // 先將乘倍後每線消除得獎金額清空，避免影響後續邏輯判斷

        Map<String, Object> si = new LinkedHashMap<>();
        si.put("wp", wp);                                       // 此輪得獎圖標位置
        si.put("lw", lw);                                       // 乘倍後每線得獎金額
        si.put("orl", null);                            // 免費遊戲時百搭未變動前盤面，一般遊戲皆為null
        si.put("wm", wm);                                       // 總乘倍數
        si.put("rwm", rwm);                                     // 中乘倍的column位置
        si.put("wabm", wabm);                                   // 乘倍前總得獎金額
        si.put("fs", fs);                                       // 免費遊戲相關參數
        si.put("wppr", wppr);                                 //  百搭的出現位置，免費遊戲中顯示為變動後的位置
        si.put("gwt", -1);
        si.put("pmt", null);
        si.put("ab", null);
        si.put("ml", gambleLv);                                 // 押注倍數
        si.put("cs", cs);                                       // 每線押注金額
        si.put("rl", rl);                                       // 最終盤面，免費遊戲時顯示百搭變動後盤面
        si.put("ctw", ctw);                                     // 此回合得獎金額
        si.put("cwc", cwc);                                     // 連續得獎次數，同pcwc
        si.put("fstc", fstc);                                   // 免費遊戲時時2為已執行回合數，一般時皆為null
        si.put("pcwc", pcwc);                                   // 連續得獎次數，同cwc
        si.put("rwsp", rwsp);                                   // 各線得獎倍數
        si.put("hashr", null);                                  // 用途不明，免費遊戲時會有值
        si.put("fb", null);
        si.put("sid", spinSid);                                 // 此輪局號
        si.put("psid", psid);                                   // 連消及免費遊戲時的總局號(及第一輪局號)
        si.put("st", st);                                       // 前一回合狀態，第一回合為1
        si.put("nst", nst);                                     // 本輪狀態，一般遊戲及免費遊戲最後一回合為1，進入免費遊戲當回合及免費遊戲中為2
        si.put("pf", 1);
        si.put("aw", aw);
        si.put("wid", 0);
        si.put("wt", "C");
        si.put("wk", "0_C");
        si.put("wbn", null);
        si.put("wfg", null);
        si.put("blb", blb);                                     // 押注前玩家餘額
        si.put("blab", blab);                                   // 押注後玩家餘額
        si.put("bl", bl);                                       // 得獎後玩家餘額
        si.put("tb", tb);                                       // 此輪押注金額(僅第一輪有)
        si.put("tbb", tbb);                                     // 總押注金額
        si.put("tw", tw);                                       // 總得獎金額
        si.put("np", np);                                       // 扣除押注金額後的贏分
        si.put("ocr", null);
        si.put("mr", null);
        si.put("ge", Arrays.asList(1, 11));
        dt.put("si", si);
        gambleItem.put("dt", dt);
        gambleItem.put("err", null);

        return gambleItem;
    }

    private String getSid() {
        long base = new Date().getTime() * 1000000;
        String sidStr = String.valueOf(this.sid);
        while (sidStr.length() < 7) {
            sidStr = "0" + sidStr;
        }
        this.sid++;
        return base + sidStr;
    }

    private Map<String, Object> checkWinList(List<Integer> rl) {
        Map<String, Object> winList = new LinkedHashMap<>();

        for (int i = 0; i < 4; i++) {
            int checkSym = rl.get(i);
            if (checkSym == 1) {
                continue;
            }
            for (int j = 0; j < linkList.length; j++) {
                int[] link = linkList[j];
                if (link[0] == i) {
                    int newCheckSym = checkSym;
                    if (checkSym == 0) {
                        // 如果第一個位置是百搭，則找下一個非百搭圖標作為檢查圖標
                        for (int k = 1; k < link.length; k++) {
                            if (rl.get(link[k]) != 0) {
                                newCheckSym = rl.get(link[k]);
                                break;
                            }
                        }
                    }

                    if (newCheckSym == 0) {
                        // 全部都是百搭，跳過不算
                        continue;
                    }

                    int linkCount = 1;
                    for (int k = 1; k < link.length; k++) {
                        if (rl.get(link[k]) != newCheckSym && rl.get(link[k]) != 0) {
                            break;
                        }
                        linkCount++;
                    }

                    // 至少3個連線才算
                    if (linkCount < 3) {
                        continue;
                    }

                    List<Integer> linkPos = new ArrayList<>();
                    for (int k = 0; k < linkCount; k++) {
                        linkPos.add(link[k]);
                    }

                    // 把 linkPos 加入 winList
                    winList.put((j + 1) + "", linkPos);
                }
            }
        }

        return winList;
    }

    // private void MysqlHelperTest() {
    //     // example (任意位置呼叫)
    //     try (MysqlHelper db = new MysqlHelper("localhost", 3306, "testdb", "user", "pass")) {
    //         // 查詢多筆
    //         List<Map<String,Object>> rows = db.queryList("SELECT id,name FROM users WHERE status = ?", "active");
    //         // 單筆
    //         Map<String,Object> one = db.queryOne("SELECT * FROM users WHERE id = ?", 123);
    //         // 新增並取得 id
    //         long id = db.insertAndGetId("INSERT INTO users(name,status) VALUES(?,?)", "bob", "active");
    //         // 更新
    //         int updated = db.executeUpdate("UPDATE users SET status = ? WHERE id = ?", "inactive", id);
    //         // 切換資料庫
    //         db.changeDatabase("otherdb");
    //     } catch (SQLException e) {
    //         LOGGER.log("Database error: " + e.getMessage());
    //     }
    // }
}
