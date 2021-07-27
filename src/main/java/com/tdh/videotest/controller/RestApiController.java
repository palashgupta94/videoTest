package com.tdh.videotest.controller;

import com.tdh.videotest.apiAuth.AuthHelper;
import com.tdh.videotest.util.Util;
import com.tdh.videotest.youtubeDataModel.VideoItem;
import com.tdh.videotest.youtubeDataModel.VideoSnippet;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import jdk.jshell.Snippet;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restApi")
public class RestApiController {

    Map<String , String> map = null;
    public static final String CREDENTIAL_FILE="C:\\Users\\HOME\\Downloads\\videotest\\videotest\\src\\main\\resources\\static\\images\\credFiles\\credentials.json";
    public static  final String API_KEY ="AIzaSyD_nsPtLhllJpazm9IcvhZtapdY0xHo9jw";
    public static final String UNAUTHORIZED_STRING = "unauthorized";
    @GetMapping("/code")
    public String getCode(@RequestParam("code")String code){
        System.out.println("Code = "+ code);

        String urlParameters = "redirect_uri=http://localhost:8080/restApi/code&";
        urlParameters += "code="+code+"&";
        urlParameters += "grant_type=authorization_code&";
        urlParameters += "client_id=154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com&";
        urlParameters += "client_secret=tzE7ldNiSlKSgXLX6kIZwV38";

        try {

            byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "https://oauth2.googleapis.com/token";
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write( postData );

            InputStream is = null;
            if (conn.getResponseCode() != 200) {
                is = conn.getErrorStream();
            } else {
                is = conn.getInputStream();
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = in.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            String responseString = sb.toString();

            AuthHelper.updateFile(responseString , CREDENTIAL_FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "code";
    }

    @GetMapping("/refresh")
    private String getNewAccessToken() {
        System.out.println("invoked getNewAccessToken");
        String responseString="";
        String newParameters = "grant_type=refresh_token&";
        newParameters += "refresh_token="+"1//0gWdr0tuXdTM6CgYIARAAGBASNwF-L9Ir40NO5JNdc3W1Apm9_jR4mi5wWobFVEvP27sVomkTW_Wk00DtusUawYr7b67IH2NVNvo"+"&";
        newParameters += "client_id=154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com&";
        newParameters += "client_secret=tzE7ldNiSlKSgXLX6kIZwV38";

        try{

            byte[] postData = newParameters.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;
            String request = "https://oauth2.googleapis.com/token";
            URL url = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
//            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write( postData );

            InputStream is = null;
            boolean success = true;
            if (conn.getResponseCode() != 200) {
                is = conn.getErrorStream();
                success = false;
                System.out.println("got error while getting new access token");
            } else {
                is = conn.getInputStream();
            }
            StringBuilder sb = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = in.readLine()) != null) {
                System.out.println(line);
                sb.append(line);
            }
            responseString = sb.toString();
            System.out.println(responseString);
            if (success){
                JsonObject accessObj = new JsonObject(responseString);
                String token = accessObj.getString("access_token");
                AuthHelper.updateCredsFileFromJson(accessObj);
                return token;
            }
//            map = AuthHelper.updateAccessCredentialsMap(responseString);

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/getData")
    public List<VideoItem> getYoutubeApiData(){
        List<VideoItem>list = null;
        try {
            String access_token = Util.getYouTubeAccessToken();
            list = getYoutubeApiData(access_token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (VideoItem item: list){
            System.out.println("item: " + list);
            System.out.println("\n");
        }
        return list;
    }

    public List<VideoItem> getYoutubeApiData(String accessToken) throws IOException {
       int retryMax = 5;
       int retryCount = 0;
        String uploads="";
        String videoCount="";
        try {
            String response = fireYoutubeDataApi(accessToken);
            if (response!=null) {
                if (response.equalsIgnoreCase(UNAUTHORIZED_STRING)) {
                    while (retryCount++ < retryMax){

                        accessToken = getNewAccessToken();
                        response = fireYoutubeDataApi(accessToken);
                        if (!response.equals(UNAUTHORIZED_STRING)) {
                            break;
                        }
                    }
                }
            }
            JsonObject object = new JsonObject(response);
            if(object != null){
                JsonArray nodeArr = object.getJsonArray("items");
                JsonObject item = null;
                for (int i = 0; i < nodeArr.size(); i++){
                    item = nodeArr.getJsonObject(i);
                }
//                System.out.println("items = " + item);
                uploads = item.getJsonObject("contentDetails").getJsonObject("relatedPlaylists").getString("uploads");
//                System.out.println("uploads = " + uploads);
//
//                System.out.println("videoCount = " + videoCount);
            }
            return getYoutubePlaylistData(accessToken , API_KEY , uploads);

//            if(response.getStatusCodeValue() >= 400){
//
//            HttpHeaders headers1 = response.getHeaders();
//            List<String> headerResponse = headers1.get("WWW-Authenticate");
//            if(headerResponse!=null && headerResponse.get(0).contains("invalid_token")){
//
//                String newAccessToken = getNewAccessToken();
//                AuthHelper.updateFile(newAccessToken , CREDENTIAL_FILE);
//
////                newAccessToken = AuthHelper.getDataFromFile(CREDENTIAL_FILE);
//                JsonObject jso = AuthHelper.getJsonObjectFromFile(CREDENTIAL_FILE);
//                newAccessToken = jso.getString("access_token");
//                getYoutubeApiData(newAccessToken);
//            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private String  fireYoutubeDataApi(String accessToken) throws  Exception{

        try {
            Map<String , String> valueMap = new LinkedHashMap<>();

            RestTemplate template = new RestTemplate();
            String channelId ="UCoIXuO8Aco0M3-4MFSyZ_DA";
            String apiKey = "AIzaSyD_nsPtLhllJpazm9IcvhZtapdY0xHo9jw";
            String url = "https://www.googleapis.com/youtube/v3/channels";
            String parts = "contentDetails,statistics";
            String getUrl = url+"?part="+parts+"&id="+channelId+"&key="+apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization" , "Bearer "+accessToken);
            headers.set("Accept" , "application/json");

            HttpEntity request = new HttpEntity(headers);

            ResponseEntity<String> response = template.exchange(getUrl , HttpMethod.GET , request , String.class);
            System.out.println("response = "+response);
            if (response!=null && response.getStatusCodeValue() == 200){
                return response.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
            if (e instanceof HttpClientErrorException.Unauthorized){
                return UNAUTHORIZED_STRING;
            }
        }
        return null;
    }

    private List<VideoItem> getYoutubePlaylistData(String accessToken, String apiKey, String uploads) {

        List<VideoItem>list = null;
        String hostUrl = "https://www.googleapis.com/youtube/v3/playlistItems";
        String parts = "snippet,contentDetails";

        String getUrl = hostUrl+"?part="+parts+"&playlistId="+uploads+"&key="+apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization" , "Bearer "+accessToken);
        headers.set("Accept" , "application/json");

        RestTemplate template = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(getUrl , HttpMethod.GET , httpEntity , String.class);
//        System.out.println("Youtube data response = " + response);

        if (response.getStatusCodeValue() == 200) {
            list = new ArrayList<>();
            JsonObject responseObj = new JsonObject(response.getBody());
            JsonArray arr = responseObj.getJsonArray("items");
            for (int i=0; i < arr.size(); i++){
                JsonObject item = arr.getJsonObject(i);
                VideoItem videoItem = Json.decodeValue(Json.encode(item), VideoItem.class);
                System.out.println("Standard Url = " + videoItem.getSnippet().getThumbnails().get("standard"));

                if(videoItem.getSnippet().getThumbnails().get("Standard") != null){
                    videoItem.getSnippet().setStandardThumbnailUrl(videoItem.getSnippet().getThumbnails().get("standard").getUrl());
                }else{
                    videoItem.getSnippet().setStandardThumbnailUrl(videoItem.getSnippet().getThumbnails().get("high").getUrl());

                }

                if (videoItem!=null){
                    list.add(videoItem);
                }
            }
        }
        return list;
    }

    @GetMapping("/getAccessToken")
    public String getAccessToken(){

        new AuthHelper().getAuthCredentials();
        return "getAccessToken";

    }

    @GetMapping("/start")
    public void getStart(){

        File jsonFile = new File( CREDENTIAL_FILE);
        if(jsonFile.exists()){

//            ObjectMapper mapper = new ObjectMapper();
            try {
//                String credString = mapper.readValue(new File(CREDENTIAL_FILE), String.class);
                String token = Util.getYouTubeAccessToken();
                if (token !=null && !token.isBlank()){
                    System.out.println("auth = " + token);
                    List<VideoItem> list = getYoutubeApiData(token);
                }
                else{
                    getAuthToken();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            getAccessToken();
        }

    }

    @GetMapping("/testRead")
    public void getAuthToken(){
        JsonObject authObj = Util.getYouTubeTokenJson();
        System.out.println("authObj: " + authObj);
    }

    @GetMapping("/testWrite")
    public void testWriteToResourceFile(){
        String dummy = "dummyData";
        Util.writeStringToResourceFile(dummy, "./src/main/resources/credentials.json");

    }




}
