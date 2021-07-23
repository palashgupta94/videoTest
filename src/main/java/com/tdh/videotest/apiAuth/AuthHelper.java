package com.tdh.videotest.apiAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.gson.JsonParser.*;

public class AuthHelper {

    public void getAuthCredentials(){

        ResponseEntity<String> responseEntity = null;

        RestTemplate template = new RestTemplate();
        String credentials = "154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com:tzE7ldNiSlKSgXLX6kIZwV38";
        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization" , "Basic "+encodedCredentials);


        HttpEntity<String> request = new  HttpEntity<String>(headers);

        String accessTokenUrl = "https://accounts.google.com/o/oauth2/auth?";
        accessTokenUrl += "redirect_uri=http://localhost:8080/restApi/code&";
        accessTokenUrl += "response_type=code&";
        accessTokenUrl += "scope=https://www.googleapis.com/auth/youtube&";
        accessTokenUrl += "client_id=154618135621-f1iuf522m78khr2r8q63nce69jrv6dk4.apps.googleusercontent.com&";
        accessTokenUrl += "include_granted_scopes=true&";
        accessTokenUrl += "state=state_parameter_passthrough_value&";
        accessTokenUrl += "access_type=online&";
        accessTokenUrl += "prompt=consent";

        responseEntity = template.exchange(accessTokenUrl , HttpMethod.POST , request , String.class);

        System.out.println("AAccess token =: " + responseEntity);
    }

    public static Map<String , String> updateAccessCredentialsMap(String authCode){

        Map<String , String>map = new LinkedHashMap<>();

        JsonObject object = new JsonObject(authCode);

        if(object.containsKey("access_token")){
            map.put("access_token" , object.getString("access_token"));
        }

        if(object.containsKey("expires_in")){
            map.put("expires_in" , object.getString("expires_in"));
        }

        if(object.containsKey("refresh_token")){
            map.put("refresh_token" , object.getString("refresh_token"));
        }

        if(object.containsKey("scope")){
            map.put("scope" , object.getString("scope"));
        }

        if(object.containsKey("token_type")){
            map.put("token_type" , object.getString("token_type"));
        }


        System.out.println(map.toString());
        return map;
    }

    public static  String getNewAccessToken(){

    return  null;

    }

    public static void updateFile(String data , String filePath){

        try {
            JsonObject object = new JsonObject(data);
            System.out.println("data = "+object);
            JsonObject jso = getJsonObjectFromFile(filePath);
            jso.put("access_token" , object.getString("access_token"));
            jso.put("expires_in" , object.getString("expires_in"));
            jso.put("scope" , object.getString("scope"));
            jso.put("token_type" , object.getString("token_type"));
            if(object.containsKey("refresh_token")){
                jso.put("refresh_token" , object.getString("refresh_token"));
            }
            String jsonToWrite = Json.encodePrettily(jso);
            FileWriter file = new FileWriter(filePath);

            for(int i = 0; i < jsonToWrite.length(); i++){
                file.write(jsonToWrite.charAt(i));
            }
            file.close();



        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static String getDataFromFile(String fileName){

        String fileString="";

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder sb = new StringBuilder();
            String str = "";
            while((str=br.readLine()) != null){
                sb.append(str +System.lineSeparator());
            }
            fileString = sb.toString();
            return fileString;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("from file = "+fileString );
        return fileString;

    }
    public static JsonObject getJsonObjectFromFile(String filePath){

        String fileData = getDataFromFile(filePath);
        JsonObject object = new JsonObject(fileData);
        return object;

    }

    public static Map<String , String> getMapFromJsonObject(String filePath1 , String filePath2){

        JsonObject obj1 = getJsonObjectFromFile(filePath1);
        JsonObject obj2 = getJsonObjectFromFile(filePath2);

        Map<String , String> credsMap = new LinkedHashMap<>();
        credsMap.put("access_token" , obj1.getString("access_token"));
        credsMap.put("expires_in" , obj1.getString("expires_in"));
        credsMap.put("scope" , obj1.getString("scope"));
        credsMap.put("token_type" , obj1.getString("token_type"));
        credsMap.put("client_id" , obj2.getString("client_id"));
        credsMap.put("client_secret" , obj2.getString("client_secret"));

        return credsMap;

    }



}
