package com.eugene21.asmusic;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class NetworkUtils {

    public static String sendPostRequest(String urlString, String postData) throws IOException, ExecutionException, InterruptedException {
        Callable connectTask = new Callable() {
            @Override
            public String call() throws IOException {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Установка параметров запроса
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setConnectTimeout(1500);

                // Отправка данных на сервер
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    throw new IOException("Ошибка при выполнении запроса. Код ответа: " + responseCode);
                }
            }
        };
        FutureTask<String> future = new FutureTask<>(connectTask);
        Thread connectThread = new Thread(future);
        connectThread.start();
        return future.get();
    }

    public static String sendGetRequest(String urlString) throws IOException, ExecutionException, InterruptedException {
        Callable connectTask = new Callable() {
            @Override
            public String call() throws IOException {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Установка параметров запроса
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setConnectTimeout(1500);

                // Получение ответа от сервера
                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    return response.toString();
                } else {
                    throw new IOException("Ошибка при выполнении запроса. Код ответа: " + responseCode);
                }
            }
        };
        FutureTask<String> future = new FutureTask<>(connectTask);
        Thread connectThread = new Thread(future);
        connectThread.start();
        return future.get();
    }
}