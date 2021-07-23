package com.tdh.videotest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdh.videotest.apiAuth.AuthHelper;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restApi")
public class RestApiController {

    Map<String , String> map = null;
    public static final String CREDENTIAL_FILE="C:\\Users\\HOME\\Downloads\\videotest\\videotest\\credentials.json";

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

            map = new AuthHelper().getAccessCredentials(responseString);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(CREDENTIAL_FILE) , responseString);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "code";
    }

    @GetMapping("/refresh")
    private String getNewAccessToken() {

        String responseString="";

//        String newParameters = "grant_type=refresh_token&";
//        newParameters += "refresh_token="+map.get("refresh_token")+"&";
//        newParameters += "client_id=154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com&";
//        newParameters += "client_secret=tzE7ldNiSlKSgXLX6kIZwV38";
        String newParameters = "grant_type=refresh_token&";
        newParameters += "refresh_token="+"1//0g_DHzaL_6VB-CgYIARAAGBASNwF-L9IrDjMxCr-b9AwHA8Ini4jfevU0RMDwDuO3MkQ7pjJBYBJjHiPtKJEbdTvEbqmGLANVnTQ"+"&";
        newParameters += "client_id=154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com&";
        newParameters += "client_secret=tzE7ldNiSlKSgXLX6kIZwV38";

        try{

            byte[] postData = newParameters.getBytes( StandardCharsets.UTF_8 );
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
            responseString = sb.toString();
            AuthHelper gac = new AuthHelper();
            map = gac.getAccessCredentials(responseString);

        }catch(Exception e){
            e.printStackTrace();
        }
        return responseString;

    }

    @GetMapping("/getData")
    public Map<String , String> getYoutubeApiData(String accessToken) throws IOException {

//        System.out.println("Global map = "+map.toString());
        String uploads="";
        String videoCount="";

        Map<String , String> valueMap = new LinkedHashMap<>();

        RestTemplate template = new RestTemplate();
        String channelId ="UCoIXuO8Aco0M3-4MFSyZ_DA";
        String apiKey = "AIzaSyD_nsPtLhllJpazm9IcvhZtapdY0xHo9jw";
        String url = "https://www.googleapis.com/youtube/v3/channels";
        String parts = "contentDetails,statistics";
        String getUrl = url+"?part="+parts+"&id="+channelId+"&key="+apiKey;

        HttpHeaders headers = new HttpHeaders();
//        System.out.println("access token = "+map.get("access_token"));
        headers.set("Authorization" , "Bearer "+accessToken);
        headers.set("Accept" , "application/json");

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = template.exchange(getUrl , HttpMethod.GET , request , String.class);

        if(response.getStatusCodeValue() >= 400){

            HttpHeaders headers1 = response.getHeaders();
            List<String> headerResponse = headers1.get("WWW-Authenticate");
            if(headerResponse!=null && headerResponse.get(0).contains("invalid_token")){

                String newAccessToken = getNewAccessToken();

                JsonObject object = new JsonObject(newAccessToken);
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue((new File()) , newAccessToken);
                String value = mapper.readValue((new File(CREDENTIAL_FILE)), String.class);
                JsonObject jso = new JsonObject(value);

                newAccessToken = jso.getString("access_token");
                getYoutubeApiData(newAccessToken);
            }

        }

//        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonObject object = new JsonObject(response.getBody().toString());

            if(object != null){
                JsonArray nodeArr = object.getJsonArray("items");
                JsonObject item = null;
                for (int i = 0; i < nodeArr.size(); i++){
                    item = nodeArr.getJsonObject(i);
                }
                System.out.println("items = " + item);
                uploads = item.getJsonObject("contentDetails").getJsonObject("relatedPlaylists").getString("uploads");
                System.out.println("uploads = " + uploads);

//                videoCount = item.getJsonObject("statistics").getString("videoCount");
                System.out.println("videoCount = " + videoCount);
            }

//            JsonNode node = mapper.readTree(response.getBody());
//            System.out.println("node = " + node);
        } catch ( Exception e) {
            e.printStackTrace();
        }

        return getYoutubePlaylistData(accessToken , apiKey , uploads);
    }

    private Map<String, String> getYoutubePlaylistData(String accessToken, String apiKey, String uploads) {

        String hostUrl = "https://www.googleapis.com/youtube/v3/playlistItems";
        String parts = "snippet,contentDetails";

        String getUrl = hostUrl+"?part="+parts+"&playlistId="+uploads+"&key="+apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization" , "Bearer "+accessToken);
        headers.set("Accept" , "application/json");

        RestTemplate template = new RestTemplate();
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = template.exchange(getUrl , HttpMethod.GET , httpEntity , String.class);
        System.out.println("Youtube data response = " + response);


        return null;
    }

    @GetMapping("/getAccessToken")
    public String getAccessToken(){

        new AuthHelper().getAuthCredentials();
        return "getAccessToken";

    }

    @GetMapping("/start")
    public void getStart(){

        File jsonFile = new File("C:\\Users\\HOME\\Downloads\\videotest\\videotest\\credentials.json");
        if(jsonFile.exists()){

            ObjectMapper mapper = new ObjectMapper();
            try {
                String credString = mapper.readValue(new File("C:\\Users\\HOME\\Downloads\\videotest\\videotest\\credentials.json"), String.class);
                if(credString!= null && !credString.equalsIgnoreCase("")) {

                    JsonObject credObject = new JsonObject(credString);
                    String authToken = credObject.getString("access_token");
                    System.out.println("auth = " + authToken);
                    Map<String, String> youtubeApiData = getYoutubeApiData(authToken);
//                    System.out.println("dataMap = " + youtubeApiData.toString());
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

    private String getAuthToken(){


        return null;

    }




}
