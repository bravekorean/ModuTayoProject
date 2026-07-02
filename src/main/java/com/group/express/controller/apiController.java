package com.group.express.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/publicApi")
public class apiController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${publicAPIurl}")
    private String publicAPIurl;

    @Value("${publicAPIkey}")
    private String publicAPIkey;

    String baseUrl = "http://apis.data.go.kr/1613000/TrainInfoService/getCtyCodeList";
    String busTerminalUrl = "https://apis.data.go.kr/1613000/ExpBusArrInfoService/getExpBusTmnList";


    //    String apiUrl = baseUrl + "?serviceKey=" + publicAPIkey + "&_type=json";
    @Async
    @GetMapping("/getCityCodeList")
    public ResponseEntity<List<Map<String, Object>>> getCityCodeList() throws URISyntaxException {

        String apiUrl = baseUrl + "?serviceKey=" + publicAPIkey + "&_type=json";
        URI uri = new URI(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                List<Map<String, Object>> itemListToReturn = new ArrayList<>();
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    Long cityCode = Long.parseLong(itemMap.get("citycode").toString());
                    String cityName = (String) itemMap.get("cityname");
                    Map<String, Object> cityInfo = new HashMap<>();
                    cityInfo.put("cityCode", cityCode);
                    cityInfo.put("cityName", cityName);
                    itemListToReturn.add(cityInfo);
                }
                System.out.println("지역코드(기차) 호출완료");
                return ResponseEntity.ok(itemListToReturn);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }
    @Async
    @GetMapping("/getCitySttnList")
    public CompletableFuture<ResponseEntity<List<Map<String, Object>>>> getCitySttnList(@RequestParam("cityCode") String cityCode) throws URISyntaxException {
        String apiUrl = publicAPIurl + "/1613000/TrainInfoService/getCtyAcctoTrainSttnList";

        String requestUrl = apiUrl + "?serviceKey=" + publicAPIkey + "&numOfRows=30&pageNo=1&_type=json&cityCode=" + cityCode;
        URI uri = new URI(requestUrl);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                List<Map<String, Object>> itemListToReturn = new ArrayList<>();
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    String nodeId = (String) itemMap.get("nodeid");
                    String nodeName = (String) itemMap.get("nodename");
                    Map<String, Object> stationInfo = new HashMap<>();
                    stationInfo.put("nodeId", nodeId);
                    stationInfo.put("nodeName", nodeName);
                    itemListToReturn.add(stationInfo);
                }
                System.out.println("기차역 정보 호출완료.");
                return CompletableFuture.completedFuture(ResponseEntity.ok(itemListToReturn));
            } catch (ParseException e) {
                e.printStackTrace();
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return CompletableFuture.completedFuture(ResponseEntity.status(response.getStatusCode()).build());
        }
    }
    @Async
    @GetMapping("/getBusList")
    public ResponseEntity<List<Map<String, Object>>> getBusTerminalList() throws URISyntaxException {

        String apiUrl = busTerminalUrl + "?serviceKey=" + publicAPIkey + "&numOfRows=240&pageNo=1&_type=json";
        URI uri = new URI(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, org.springframework.http.HttpMethod.GET, entity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();

            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                // 각 지역에 해당하는 터미널 정보를 저장할 리스트
                List<Map<String, Object>> regionList = new ArrayList<>();

                // 터미널 정보를 처리하고 지역별로 저장
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    String terminalId = String.valueOf(itemMap.get("tmnCd"));
                    String regionKey = classifyRegionFromTerminalId(terminalId);
//                    int areaCode = Integer.parseInt(classifyRegionFromTerminalId(terminalId)); //

                    // 터미널 정보를 해당 지역의 맵에 추가
                    String terminalName = (String) itemMap.get("tmnNm");

                    // 각 지역에 해당하는 터미널 정보를 하나의 맵에 저장
//                    Map<String, Object> regionTerminal = new HashMap<>();
                    Map<String, Object> regionTerminal = new LinkedHashMap<>(); //
                    regionTerminal.put("regionKey", regionKey);
                    regionTerminal.put("terminalId", terminalId);
                    regionTerminal.put("terminalName", terminalName);
//                    regionTerminal.put("areaCode",areaCode); //

                    regionList.add(regionTerminal);
                }
                System.out.println("버스터미널 정보 호출완료.");
                return ResponseEntity.ok(regionList);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }


    // 터미널 ID에서 NAEK 뒤의 숫자를 추출하고 지역을 분류하는 메서드
    private String classifyRegionFromTerminalId(String terminalId) {
        try {
            // NAEK 뒤의 숫자를 추출
//            String numberPart = terminalId.replace("NAEK", "");
//            int number = Integer.parseInt(numberPart);
            int number = Integer.parseInt(terminalId);

            // 지역을 분류
            if (number >= 0 && number <= 99) {
                return "서울"; // 서울
            } else if (number >= 100 && number <= 199) {
                return "인천/경기"; // 인천/경기
            } else if (number >= 200 && number <= 299) {
                return "강원"; // 강원도
            } else if (number >= 300 && number <= 399) {
                return "대전/충남"; // 대전/충남
            } else if (number >= 400 && number <= 499) {
                return "충북"; // 충북
            } else if (number >= 500 && number <= 599) {
                return "광주/전남"; // 광주/전남
            } else if (number >= 600 && number <= 699) {
                return "전북"; // 전북

            } else if (number >= 700 && number <= 799) {
                return "부산/경남"; // 부산/경남
            } else if (number >= 800 && number <= 899) {
                return "경북"; // 경북
            } else {
                return "기타"; // 알 수 없는 지역
            }
        } catch (NumberFormatException e) {
            return "오류"; // 숫자로 변환할 수 없는 경우 처리
        }
    }



    @Async
    @GetMapping("/getTrainroundInfoList")
    public ResponseEntity<List<Map<String, Object>>>  getTrainroundInfoList(@RequestParam String depPlaceId, @RequestParam String arrPlaceId, @RequestParam String depPlaceTime) throws URISyntaxException {

        String apiUrl = publicAPIurl + "/1613000/TrainInfoService/getStrtpntAlocFndTrainInfo" +
                "?serviceKey=" + publicAPIkey + "&pageNo=1" + "&numOfRows=100" + "&_type=json" +
                "&depPlaceId=" + depPlaceId + "&arrPlaceId=" + arrPlaceId + "&depPlandTime=" + depPlaceTime;
            URI uri = new URI(apiUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                List<Map<String, Object>> itemListToReturn = new ArrayList<>();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                int index = 1;
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    Map<String, Object> TrainInfo = new HashMap<>();
                    TrainInfo.put("roundFare", itemMap.get("adultcharge")); // 운임
                    TrainInfo.put("roundarrPlaceName", itemMap.get("arrplacename")); // 도착지이름
                    try {
                        Date arrPlandTime = inputFormat.parse(itemMap.get("arrplandtime").toString());
                        String formattedArrPlandTime = outputFormat.format(arrPlandTime);
                        TrainInfo.put("roundarrPlandTime", formattedArrPlandTime); // 도착시간
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    TrainInfo.put("rounddepPlaceName", itemMap.get("depplacename")); // 출발지 이름
                    try {
                        Date depPlandTime = inputFormat.parse(itemMap.get("depplandtime").toString());
                        String formattedDepPlandTime = outputFormat.format(depPlandTime);
                        TrainInfo.put("rounddepPlandTime", formattedDepPlandTime); // 출발시간
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
//                        throw  new RuntimeException("실패");
                    }
                    TrainInfo.put("roundtrainGradeName", itemMap.get("traingradename")); // 기차 종류
                    TrainInfo.put("roundtrainNum", itemMap.get("trainno")); // 기차 번호
                    TrainInfo.put("Infoindex", index);
                    itemListToReturn.add(TrainInfo);
                    index++;
                }
                System.out.println("기차 정보 호출완료");
                return ResponseEntity.ok(itemListToReturn);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }




    }

    @Async
    @GetMapping("/getTrainInfoList")
    public ResponseEntity<List<Map<String, Object>>>  getTrainInfoList(@RequestParam String depPlaceId, @RequestParam String arrPlaceId, @RequestParam String depPlaceTime) throws URISyntaxException {

        String apiUrl = publicAPIurl + "/1613000/TrainInfoService/getStrtpntAlocFndTrainInfo" +
                "?serviceKey=" + publicAPIkey + "&pageNo=1" + "&numOfRows=100" + "&_type=json" +
                "&depPlaceId=" + depPlaceId + "&arrPlaceId=" + arrPlaceId + "&depPlandTime=" + depPlaceTime;
        URI uri = new URI(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                List<Map<String, Object>> itemListToReturn = new ArrayList<>();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                int index = 1; // 순서를 추적할 변수
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    Map<String, Object> TrainInfo = new HashMap<>();
                    TrainInfo.put("Fare", itemMap.get("adultcharge")); // 운임
                    TrainInfo.put("arrPlaceName", itemMap.get("arrplacename")); // 도착지이름
                    try {
                        Date arrPlandTime = inputFormat.parse(itemMap.get("arrplandtime").toString());
                        String formattedArrPlandTime = outputFormat.format(arrPlandTime);
                        TrainInfo.put("arrPlandTime", formattedArrPlandTime); // 도착시간
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }
                    TrainInfo.put("depPlaceName", itemMap.get("depplacename")); // 출발지 이름
                    try {
                        Date depPlandTime = inputFormat.parse(itemMap.get("depplandtime").toString());
                        String formattedDepPlandTime = outputFormat.format(depPlandTime);
                        TrainInfo.put("depPlandTime", formattedDepPlandTime); // 출발시간
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
//                        throw  new RuntimeException("실패");
                    }
                    TrainInfo.put("trainGradeName", itemMap.get("traingradename")); // 기차 종류
                    TrainInfo.put("trainNum", itemMap.get("trainno")); // 기차 번호
                    TrainInfo.put("Infoindex", index); // 순서를 할당
                    itemListToReturn.add(TrainInfo);
                    index++;
                }
                System.out.println("기차 정보 호출완료");
                return ResponseEntity.ok(itemListToReturn);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }




    }

    @Async
    @GetMapping("/getArrBusList")
    public ResponseEntity<List<Map<String, Object>>> getArrBusTerminalList(String tmnCd) throws URISyntaxException {

        String apiUrl = publicAPIurl + "/1613000/ExpBusArrInfoService/getArrTmnFromDepTmn?serviceKey=" + publicAPIkey + "&numOfRows=240&pageNo=1&_type=json&depTmnCd=" + tmnCd;
        URI uri = new URI(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(uri, org.springframework.http.HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            JSONParser jsonParser = new JSONParser();
            System.out.println(jsonResponse);
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                // 각 지역에 해당하는 터미널 정보를 저장할 리스트
                List<Map<String, Object>> regionList = new ArrayList<>();

                // 터미널 정보를 처리하고 지역별로 저장
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    String arrTerminalId = String.valueOf(itemMap.get("arrTmnCd"));
                    String regionKey = classifyRegionFromTerminalId(arrTerminalId);
//                    int areaCode = Integer.parseInt(classifyRegionFromTerminalId(terminalId)); //

                    // 터미널 정보를 해당 지역의 맵에 추가
                    String arrTerminalName = (String) itemMap.get("arrTmnNm");

                    // 각 지역에 해당하는 터미널 정보를 하나의 맵에 저장
//                    Map<String, Object> regionTerminal = new HashMap<>();
                    Map<String, Object> regionTerminal = new LinkedHashMap<>(); //
                    regionTerminal.put("regionKey", regionKey);
                    regionTerminal.put("terminalId", arrTerminalId);
                    regionTerminal.put("terminalName", arrTerminalName);
//                    regionTerminal.put("areaCode",areaCode); //

                    regionList.add(regionTerminal);
                }
                System.out.println("버스터미널 도착정보 호출완료.");
                return ResponseEntity.ok(regionList);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @Async
    @GetMapping("/BusInfo")
    public ResponseEntity<List<Map<String, Object>>> BusInfo(@RequestParam String depId,@RequestParam String arrId,@RequestParam String depTime) throws URISyntaxException {
        String apiUrl = publicAPIurl + "/1613000/ExpBusInfoService/getStrtpntAlocFndExpbusInfo?serviceKey=" + publicAPIkey + "&pageNo=1&numOfRows=240&_type=json&depTerminalId=" + depId + "&arrTerminalId=" + arrId + "&depPlandTime=" + depTime;
        URI uri = new URI(apiUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = response.getBody();
            System.out.println("jsonResponse : " + jsonResponse);
            JSONParser jsonParser = new JSONParser();
            try {
                JSONObject responseBody = (JSONObject) jsonParser.parse(jsonResponse);
                JSONObject responseMap = (JSONObject) responseBody.get("response");
                JSONObject responseBodyMap = (JSONObject) responseMap.get("body");
                JSONObject itemsMap = (JSONObject) responseBodyMap.get("items");
                JSONArray itemList = (JSONArray) itemsMap.get("item");

                List<Map<String, Object>> BusInfoList = new ArrayList<>();

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmm");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                for (Object item : itemList) {
                    JSONObject itemMap = (JSONObject) item;
                    Map<String, Object> BusInfo = new HashMap<>();
                    BusInfo.put("Fare", itemMap.get("charge")); // 운임
                    BusInfo.put("arrPlaceName", itemMap.get("arrPlaceNm")); // 도착지이름
                    try {
                        Date arrPlandTime = inputFormat.parse(itemMap.get("arrPlandTime").toString());
                        String formattedArrPlandTime = outputFormat.format(arrPlandTime);
                        BusInfo.put("arrPlandTime", formattedArrPlandTime); // 도착시간
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BusInfo.put("depPlaceName", itemMap.get("depPlaceNm")); // 출발지 이름
                    try {
                        Date depPlandTime = inputFormat.parse(itemMap.get("depPlandTime").toString());
                        String formattedDepPlandTime = outputFormat.format(depPlandTime);
                        BusInfo.put("depPlandTime", formattedDepPlandTime); // 출발시간
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BusInfo.put("BusGradeName", itemMap.get("gradeNm")); // 버스 등급
                    BusInfo.put("routeId", itemMap.get("routeId")); // routeId?
                    BusInfoList.add(BusInfo);
                }
                System.out.println("버스 정보 호출완료");
                return ResponseEntity.ok(BusInfoList);
            } catch (ParseException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("에러 코드: " + response.getStatusCodeValue());
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

}