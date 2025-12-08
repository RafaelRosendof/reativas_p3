package com.ms2.ms2.Aux;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.ms2.ms2.Entity.Stocks;


@Service
public class StockCollect {



   
    private String apiKey = "";

    
  

    public String buildApiUrl(String typeOfTime, String symbol, String outputsize) {
        return "https://www.alphavantage.co/query?function=" + typeOfTime + "&symbol=" + symbol + "&outputsize=" + outputsize + "&apikey=" + apiKey + "&datatype=json";
    }

    public String collectData(String symbol) {
        String url = buildApiUrl("TIME_SERIES_DAILY", symbol, "compact");
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("API request failed: " + response.body());
                return null;
            }
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

public List<Stocks> processingData(String body, String symbol) {
        if (body == null || body.isEmpty()) {
            return Collections.emptyList();
        }

        List<Stocks> stockEntityList = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(body, JsonObject.class);

        if (!jsonObject.has("Time Series (Daily)")) {
            System.err.println("Resposta da API não contém 'Time Series (Daily)': " + body);
            return Collections.emptyList();
        }

        JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");

        for (String date : timeSeries.keySet()) {
            JsonObject data = timeSeries.getAsJsonObject(date);

            Stocks entity = new Stocks();
            entity.setName(symbol);
            entity.setDate(date);
            entity.setOpenPrice(data.get("1. open").getAsDouble());
            entity.setHighPrice(data.get("2. high").getAsDouble());
            entity.setClosePrice(data.get("4. close").getAsDouble());

            stockEntityList.add(entity);
        }

        return stockEntityList;
    }

    
}