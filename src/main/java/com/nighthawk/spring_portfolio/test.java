package com.nighthawk.spring_portfolio;

import okhttp3.*;

import java.io.IOException;

public class test {
    private static final String OPENAI_API_KEY = "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";
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/engines/gpt-3.5-turbo-instruct/completions";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{ \"prompt\": \"Translate the following English text to French: '{}'\", \"max_tokens\": 60 }");
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
