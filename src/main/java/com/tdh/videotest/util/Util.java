package com.tdh.videotest.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.stream.Collectors;

public class Util {

    private static final String CREDENTIALS = "credentials.json";

    private static String readResourceFileToString(String filename){
        try {
            InputStream stream = new ClassPathResource(filename).getInputStream();
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                return (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
            } else {
                throw new RuntimeException("resource not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readCredentialsFile(){
        return readResourceFileToString(CREDENTIALS);
    }

    public static JsonObject getYouTubeTokenJson (){
        String data = readCredentialsFile();
        return new JsonObject(data);
    }

    public static void writeJsonToCredsFile(JsonObject object){
        String jsonToWrite = Json.encodePrettily(object);
//        String data = Json.encode(object);
        writeStringToResourceFile(jsonToWrite, "./src/main/resources/credentials.json");
    }

    public static void writeStringToResourceFile(String data, String path){
        try {
            FileWriter writer = new FileWriter(path);
            for(int i = 0; i < data.length(); i++){
                writer.write(data.charAt(i));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getYouTubeAccessToken() {
        return getYouTubeTokenJson().getString("access_token");
    }
}
