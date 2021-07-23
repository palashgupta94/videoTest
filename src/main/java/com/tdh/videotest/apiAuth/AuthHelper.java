package com.tdh.videotest.apiAuth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public Map<String , String> getAccessCredentials(String authCode){

        Map<String , String>map = new LinkedHashMap<>();

        JsonObject object = new JsonObject(authCode);

        map.put("access_token" , object.getString("access_token"));
        map.put("expires_in" , object.getString("expires_in"));
        map.put("refresh_token" , object.getString("refresh_token"));
        map.put("scope" , object.getString("scope"));
        map.put("token_type" , object.getString("token_type"));


        System.out.println(map.toString());

        return map;
    }

    public static  String getNewAccessToken(){

    return  null;

    }

    public static void writeToFile(String data , String filePath){

        try {
            FileWriter file = new FileWriter(filePath);

            for(int i = 0; i < data.length(); i++){
                file.write(data.charAt(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
