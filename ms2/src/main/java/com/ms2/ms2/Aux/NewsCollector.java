package com.ms2.ms2.Aux;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import reactor.core.publisher.Mono;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class NewsCollector {

    private String API_KEY = "";

    @Autowired
    public NewsCollector() {
        this.API_KEY = API_KEY; 
    }

    public String buildApiUrl(String symbol) {
        return "https://www.alphavantage.co/query?function=NEWS_SENTIMENT" +
               "&tickers=" + symbol +
               "&apikey=" + API_KEY;
    }

    public String fetchNewsData(String symbol) {
        if (API_KEY.equals("SUA_CHAVE_AQUI")) {
            System.err.println("ERRO: Por favor, substitua 'SUA_CHAVE_AQUI' pela sua chave da API da Alpha Vantage.");
            return null;
        }



        String url = buildApiUrl(symbol);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() != 200) {
                System.err.println("Falha na requisição à API: " + response.statusCode());
                System.err.println("Corpo da resposta: " + response.body());
                return null;
            }

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String processNewsData(String stockSymbol){
        String jsonData = fetchNewsData(stockSymbol);
        String jsonFinal = parseAndPrintNews(jsonData);
        return jsonFinal;
    }


    public String parseAndPrintNews(String jsonData) {
        if (jsonData == null || jsonData.isEmpty()) {
            System.out.println("Nenhum dado para processar.");
            return null;
        }

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);


        JsonArray feed = jsonObject.getAsJsonArray("feed");
        if (feed == null) {
            System.out.println("Nenhuma notícia encontrada no feed da API.");
            System.out.println("Resposta completa: " + jsonData);
            return null;
        }

        System.out.println("--- Notícias Encontradas para AAPL ---");
        

        StringBuilder newsBuilder = new StringBuilder();
        int newsToDisplay = Math.min(5, feed.size());
        for (int i = 0; i < newsToDisplay; i++) {
            JsonObject newsArticle = feed.get(i).getAsJsonObject();

            String title = newsArticle.get("title").getAsString();
            String summary = newsArticle.get("summary").getAsString();
            String sentimentLabel = newsArticle.get("overall_sentiment_label").getAsString();

            newsBuilder.append("\nNotícia ").append(i + 1).append(":")
                       .append("\n  Título: ").append(title)
                       .append("\n  Resumo: ").append(summary)
                       .append("\n  Sentimento: ").append(sentimentLabel)
                       .append("\n");
        }
        return newsBuilder.toString();
    }


}
