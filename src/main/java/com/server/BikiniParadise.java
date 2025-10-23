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
@SuppressWarnings("unchecked")
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

        LOGGER.log("====================================  enter game end  ====================================");

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
        boolean roundEnd;
        do {
            roundEnd = true;
            // 取得前一輪結果
            Map<String, Object> lastResult = null;
            if (!gambleResult.isEmpty()) {
                lastResult = (Map<String, Object>)gambleResult.get(gambleResult.size() - 1);
            }

            // 產生新一輪結果
            Map<String, Object> gambleItem = createSpinResult(gambleResult.size(), lastResult, gambleValue, gambleLv, lineCountValue);
            if (gambleItem != null) {
                Map<String, Object> dt = (Map<String, Object>)gambleItem.get("dt");
                if (dt != null) {
                    Map<String, Object> si = (Map<String, Object>)dt.get("si");
                    if (si != null) {
                        // 判斷是否進入免費遊戲，如果有免費遊戲且不是最後一輪則繼續下一輪
                        Map<String, Object> fs = (Map<String, Object>)si.get("fs");
                        if (fs != null && (Integer)fs.get("s") > 0) {
                            roundEnd = false;
                            LOGGER.log("roundEnd:" + roundEnd + ", fs: " + fs.toString());
                        } else {
                            if (fs == null) {
                                LOGGER.log("roundEnd:" + roundEnd + ", fs is null");
                            } else {
                                LOGGER.log("roundEnd:" + roundEnd + ", free game ended");
                            }
                        }

                    }
                }
                gambleResult.add(new LinkedHashMap<>(gambleItem));
            }
        } while (!roundEnd);

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

    private Map<String, Object> createSpinResult(int index, Map<String, Object> previousResult, double gambleValue, int gambleLv, double lineCountValue) {
        if (index < 0 || (index > 0 && previousResult == null)) {
            LOGGER.log("index:" + index + ", previousResult:" + previousResult);
            return null;
        }
        if (gambleValue < 1 || gambleLv < 1 || lineCountValue < 1) {
            LOGGER.log("gambleValue:" + gambleValue + ", gambleLv:" + gambleLv + ", lineCountValue:" + lineCountValue);
            return null;
        }

        List<Integer> rl = new LinkedList<>();      // 縱4橫5盤面
        List<Integer> orl = null;                   // 免費遊戲時百搭未變動前盤面，一般遊戲皆為null

        // 前一輪參數資料
        Map<String, Object> previousDt;             // 前一輪dt
        Map<String, Object> previousSi = null;      // 前一輪si
        Map<String, Object> previousFs = null;      // 前一輪fs
        
        if (index != 0) {
            previousDt = (Map<String, Object>)previousResult.get("dt");
            if (previousDt != null) {
                previousSi = (Map<String, Object>)previousDt.get("si");
                if (previousSi != null) {
                    previousFs = (Map<String, Object>)previousSi.get("fs");
                }
            }
        }
        
        // 產生隨機盤面，範圍為0-12，0 百搭，1 免費遊戲，2 空白，3-12 一般圖標
        for (int i = 0; i < 20; i++) {
            rl.add((int)Math.floor((Math.random() * 10) + 3)); // 產生3~12之間的圖標
        }

        for (int i = 0; i < 2; i++) {
            // 百搭、免費遊戲各有0.1%機率出現，如果隨機到的位置是其它特殊圖標，則不放
            // 測試暫時提高機率為10%
            // if (i == 1) {
            //     // 測試暫不出現免費遊戲圖標
            //     continue;
            // }

            for (int j = 0; j < 5; j++) {
                // 每column最多只能有一個百搭或免費遊戲圖標，每column的出現機率相等
                int randomNum = (int)Math.floor(Math.random() * 100);
                if (i == 0) {                    
                    if ((previousFs == null && randomNum < 15) || (previousFs != null && randomNum < 10)) {
                        int wildPos = (int)Math.floor(Math.random() * 7) - 3; // 隨機產生-3~3之間的位置
                        int startPos = wildPos < 0 ? 0 : wildPos;
                        int endPos = wildPos + 3 > 3 ? 3 : wildPos + 3;
                        // int startPos = 0;
                        // int endPos = 3;
                        LOGGER.log("[debug] column: " + j + ", wildPos: " + wildPos + ", startPos: " + startPos + ", endPos: " + endPos);
                        for (int k = startPos; k <= endPos; k++) {
                            int pos = j * 4 + k;
                            if (rl.get(pos) != 0 && rl.get(pos) != 1) {
                                rl.set(pos, 0);
                            }
                        }
                    }
                } else if (i == 1) {
                    if ((previousFs == null && randomNum < 20) || (previousFs != null && randomNum < 10)) {
                        int scatterPos = (int)Math.floor(Math.random() * 4); // 隨機產生0~3之間的位置
                        int pos = j * 4 + scatterPos;
                        if (pos >= rl.size() || rl.get(pos) == 0) {
                            continue;
                        }
                        LOGGER.log("[debug] column: " + j + ", scatter pos: " + pos);
                        rl.set(pos, 1);
                    }
                }
            }
        }

        // 百搭的出現位置，先取得變動前的位置，沒有百搭時顯示為空list而非null
        List<List<Integer>> wppr = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            wppr.add(new ArrayList<>());
        }
        for (int i = 0; i < rl.size(); i ++) {
            if (rl.get(i) == 0) {
                wppr.get((int)(i / 4)).add(i % 4);
            }
        }

        int sc = 0;         // 免費遊戲圖標數目
        if (rl.contains(1)) {
            int fsCount = 0;
            for (int i = 0; i < rl.size(); i++) {
                if (rl.get(i) == 1) {
                    fsCount++;
                }
            }

            sc = fsCount;
        }

        Map<String, Object> fs = null;  // 免費遊戲相關參數
        if (previousFs == null && sc >= 3) {                
            // 前一輪沒有免費遊戲，盤面有3個以上免費遊戲圖標，表示進入免費遊戲
            fs = new LinkedHashMap<>();
            int fsCount = freegameNumber[sc];  // 免費遊戲數量
            fs.put("s", fsCount);           // 免費遊戲未執行回合量
            fs.put("ts", fsCount);          // 免費遊戲總回合數
            fs.put("aw", 0.0);              // 免費遊戲總得獎金額
            fs.put("nosa", fsCount);        // 免費遊戲增加回合數
            fs.put("wpbn", null);           // 免費遊戲中百搭圖標變動前位置，進入免費遊戲當回合為null
        } else if (previousFs != null) {
            // 前一輪已有免費遊戲
            fs = new LinkedHashMap<>(previousFs);
            int currentFreeSpin = (Integer)fs.get("s") - 1;
            int totalFreeSpin = (Integer)fs.get("ts");
            int addedFreeSpin = freegameNumber[sc];
            currentFreeSpin += addedFreeSpin;
            totalFreeSpin += addedFreeSpin;
            fs.put("s", currentFreeSpin);
            fs.put("ts", totalFreeSpin);
            fs.put("aw", (Double)fs.get("aw"));
            fs.put("nosa", addedFreeSpin);
            fs.put("wpbn", new ArrayList<>(wppr)); // 免費遊戲中百搭圖標變動前位置，即使沒出現百搭也會是空list而非null

            // 免費遊戲時記錄百搭變動前盤面
            orl = new LinkedList<>(rl);

            for (int i = 0; i < wppr.size(); i++) {
                List<Integer> wildPosList = wppr.get(i);
                if (!wildPosList.isEmpty()) {
                    // 把該column全設為百搭
                    for (int j = i * 4; j < (i + 1) * 4; j++) {
                        rl.set(j, 0);
                    }
                    wppr.set(i, Arrays.asList(0,1,2,3)); // 該column的百搭位置設為全滿
                }
            }
        }

        LOGGER.log("round " + index + ", rl: " + rl.toString() + ", sc: " + sc + ", wppr: " + wppr.toString()
                    + ", orl: " + orl + ", fs: " + ((fs != null) ? fs.toString() : "null"));

        String spinSid = this.getSid();
        String psid = spinSid;
        double cs = BigDecimalUtil.divide(gambleValue, 100, 2); // 每線押注金額
        double gamble = BigDecimalUtil.multiply(gambleValue, gambleLv);
        gamble = BigDecimalUtil.multiply(gamble, lineCountValue);
        double tb = BigDecimalUtil.divide(gamble, 100, 2);  // 此輪押注金額，免費遊戲僅第一輪有
        double tbb = tb;
        if (previousFs != null) {
            tb = 0;
        }

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

        int wmCount = 0;
        for (int i = 0; i < wppr.size(); i++) {
            if (wppr.get(i).size() == 4) {
                // 先判斷有沒有該百搭有沒有在得獎線上
                if (i > 2) {
                    // 只有第4個及第5個column有可能不在得獎線上
                    if (wp != null) {
                        boolean inWinLine = false;
                        for (Map.Entry<String, Object> entry : wp.entrySet()) {
                            List<Integer> link = (List<Integer>) entry.getValue();
                            if (link.size() > i) {
                                inWinLine = true;
                                break;
                            }
                        }
                        if (inWinLine == false) {
                            continue;
                        }
                    }
                }
                
                if (rwm == null) {
                    rwm = new ArrayList<>();
                }
                int multi = i + 1;
                
                // 記錄中乘倍的column位置
                rwm.add(multi);

                // 計算總乘倍數
                wm = BigDecimalUtil.multiply(wm, multi);

                wmCount++;
            }
        }
        if (wmCount == 5) {
            wm = 5000; // 全盤百搭，總乘倍數為5000倍
        }

        // 在rwsp中加入得獎倍數，在lw中加入得獎金額，並計算乘倍前後的總得獎金額
        if (wp != null) {
            for (Map.Entry<String, Object> entry : wp.entrySet()) {
                List<Integer> link = (List<Integer>) entry.getValue();
                int linkCount = link.size();
                int winSymbols = rl.get(link.get(0));
                if (winSymbols == 0 || winSymbols == 1) {
                    // 如果第一個位置是百搭或scatter，則找下一個一般圖標作為檢查圖標
                    for (int k = 1; k < link.size(); k++) {
                        if (rl.get(link.get(k)) != 0 && rl.get(link.get(k)) != 1) {
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
        if (fs != null && (int)fs.get("ts") != (int)fs.get("s")) {
            fstc = new LinkedHashMap<>();
            fstc.put("2", (Integer)fs.get("ts") - (Integer)fs.get("s"));
        }

                 
        double tw = ctw;                  // 此輪總得獎金額
        double np = BigDecimalUtil.subtract(tw, tb);                    // 此輪淨利潤(得獎-押注)，可能為負數
        double blb = BigDecimalUtil.divide(this.userMoney, 100, 2);   // 押注前餘額
        double blab = BigDecimalUtil.subtract(blb, tb);                 // 押注後餘額
        double bl = BigDecimalUtil.add(blb, np);                        // 得獎後餘額
        
        double aw = tw;     // 累計得獎金額
        if (previousSi != null) {
            double previousAw = (Double)previousSi.get("aw");
            aw = BigDecimalUtil.add(previousAw, tw);
        }
        
        if (fs != null && !((Integer)fs.get("ts")).equals((Integer)fs.get("s"))) {
            // 更新免費遊戲累計得獎金額
            double previousAw = (Double)fs.get("aw");
            fs.replace("aw", BigDecimalUtil.add(previousAw, tw));
        }
        
        int cwc = 0;        // 連續得獎回合數
        if (wp != null) {
            cwc = 1;
            if (previousSi != null) {
                Map<String, Object> previousWp = (Map<String, Object>)previousSi.get("wp");
                if (previousWp != null) {
                    int previousCwc = (Integer)previousSi.get("cwc");
                    cwc = previousCwc + 1;
                }
            }
        }
        int pcwc = 0;       // 一直為0
        int st = 1;         // 前一回合的狀態，第一回合為1，前一輪是免費遊戲則為2
        if (previousFs != null) {
            // 前一輪也是免費遊戲
            st = 2;
        }
        int nst = 1;        // 本輪狀態，一般遊戲及免費遊戲最後一回合為1，進入免費遊戲當回合及免費遊戲中為2
        if (fs != null && (Integer)fs.get("s") > 0) {
            // 免費遊戲中
            nst = 2;
        }

        // 如果有免費遊戲且還有回合數(包括進入免費遊戲當回合)，第一個值為2
        // 如果沒有免費遊戲，則看有沒有得到乘倍，若有則為3，沒有則為1，如果有免費遊戲，則此值放到第二個位置
        // 第三個值固定為11
        List<Integer> ge = new ArrayList<>();
        if (fs != null) {
            if ((Integer)fs.get("s") > 0) {
                ge.add(2);
            }
        }
        if (rwm != null && !rwm.isEmpty()) {
            ge.add(3);
        } else {
            ge.add(1);
        }
        ge.add(11);

        this.userMoney = BigDecimalUtil.multiply(bl, 100);
        LOGGER.log("userMoney after gamble, start money:" + blb + ", gamble: " + tb
                + ", tw:" + tw + ", aw: " + aw + ", profit: " + np + ", end money: " + bl);
        LOGGER.log(" lw: " + (lw != null ? lw.toString() : "null")
                + ", wp: " + (wp != null ? wp.toString() : "null")
                + ", fstc: " + (fstc != null ? fstc.toString() : "null"));

        Map<String, Object> si = new LinkedHashMap<>();
        si.put("wp", wp);                           // 此輪得獎圖標位置
        si.put("lw", lw);                           // 乘倍後每線得獎金額
        si.put("orl", orl);                         // 免費遊戲時百搭未變動前盤面，一般遊戲皆為null
        si.put("wm", wm);                           // 總乘倍數
        si.put("rwm", rwm);                         // 中乘倍的column位置
        si.put("wabm", wabm);                       // 乘倍前總得獎金額
        si.put("fs", fs);                           // 免費遊戲相關參數
        si.put("sc", sc);                           // 免費遊戲圖標數目
        si.put("wppr", wppr);                       //  百搭的出現位置，免費遊戲中顯示為變動後的位置
        si.put("gwt", -1);
        si.put("pmt", null);
        si.put("ab", null);
        si.put("ml", gambleLv);                     // 押注倍數
        si.put("cs", cs);                           // 每線押注金額
        si.put("rl", rl);                           // 最終盤面，免費遊戲時顯示百搭變動後盤面
        si.put("ctw", ctw);                         // 此回合得獎金額
        si.put("cwc", cwc);                         // 連續得獎次數，同pcwc
        si.put("fstc", fstc);                       // 免費遊戲時時2為已執行回合數，一般時皆為null
        si.put("pcwc", pcwc);                       // 連續得獎次數，同cwc
        si.put("rwsp", rwsp);                       // 各線得獎倍數
        si.put("hashr", null);                      // 用途不明，免費遊戲時會有值
        si.put("fb", null);
        si.put("sid", spinSid);                     // 此輪局號
        si.put("psid", psid);                       // 連消及免費遊戲時的總局號(及第一輪局號)
        si.put("st", st);                           // 前一回合狀態，第一回合為1
        si.put("nst", nst);                         // 本輪狀態，一般遊戲及免費遊戲最後一回合為1，進入免費遊戲當回合及免費遊戲中為2
        si.put("pf", 1);
        si.put("aw", aw);                           // 累計得獎金額
        si.put("wid", 0);
        si.put("wt", "C");
        si.put("wk", "0_C");
        si.put("wbn", null);
        si.put("wfg", null);
        si.put("blb", blb);                         // 押注前玩家餘額
        si.put("blab", blab);                       // 押注後玩家餘額
        si.put("bl", bl);                           // 得獎後玩家餘額
        si.put("tb", tb);                           // 此輪押注金額(僅第一輪有)
        si.put("tbb", tbb);                         // 總押注金額
        si.put("tw", tw);                           // 總得獎金額
        si.put("np", np);                           // 扣除押注金額後的贏分
        si.put("ocr", null);
        si.put("mr", null);
        si.put("ge", ge);
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
                    if (checkSym == 0 || checkSym == 1) {
                        // 如果第一個位置是百搭或scatter，則找下一個一般圖標作為檢查圖標
                        for (int k = 1; k < link.length; k++) {
                            if (rl.get(link[k]) != 0 && rl.get(link[k]) != 1) {
                                newCheckSym = rl.get(link[k]);
                                break;
                            }
                        }
                    }

                    if (newCheckSym == 0 || newCheckSym == 1) {
                        // 全部都是百搭或scatter，跳過不算
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
