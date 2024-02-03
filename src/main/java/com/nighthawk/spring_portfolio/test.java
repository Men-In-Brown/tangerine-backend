package com.nighthawk.spring_portfolio;

import okhttp3.*;

import java.io.IOException;

public class test {
    OkHttpClient client = new OkHttpClient();

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer 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")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static void main(String[] args) throws IOException {
        test example = new test();
        String json = "{\"inputs\":\"<s> [INST] How to make an api request? [/INST] Model answer</s>\"}";
        String response = example.post("https://api-inference.huggingface.co/models/mistralai/Mixtral-8x7B-Instruct-v0.1", json);
        System.out.println(response);
    }
}
