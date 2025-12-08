package com.ms2.ms2.Service;

import org.springframework.stereotype.Service;

import com.ms2.ms2.Aux.StockCollect;
import com.ms2.ms2.DAO.NewsInterface;
import com.ms2.ms2.DAO.StockInterface;
import com.ms2.ms2.Entity.Stocks;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


// Modificar aqui como o exemplo 1 

@Service
public class StocksService {

    public StockInterface stockInterface;
    public RedisReactive redisService;
    public NewsInterface newsInterface;
    public NewsService newsService;
    public StockCollect stockCollect = new StockCollect();

    @Autowired
    public StocksService(StockInterface stockInterface , NewsInterface newsInterface, RedisReactive redisService, NewsService newsService) {
        this.stockInterface = stockInterface;
        this.newsInterface = newsInterface;
        this.redisService = redisService;
        this.newsService = newsService;
    }


    public Mono<Stocks> findById(int id) {
        return stockInterface.findById(id);
    }
    

    public Mono<Stocks> addStock(Stocks stock) {
        return stockInterface.save(stock);
    }



    public Flux<Stocks> getAllStocks() {
        return stockInterface.findAll();
    }

    public void saveStocksToPostgres(Flux<Stocks> stocksList) {
        stockInterface.saveAll(stocksList).subscribe(); // the method call a stream in the argument, maybe need to change it latter 
    }


    public Flux<Stocks> giveBackStock1(){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */
        System.out.println("\n\n\n Is been here in the giveBackStock1 method of StocksService\n\n\n");

        return redisService.getTop2Request()
            .elementAt(0)
            .doOnNext(stockName -> {
                System.out.println("Top requested stock 1: " + stockName);
            })
            .flatMapMany(stockName -> {
            return stockInterface.findAllByName(stockName);
            });
    }

    public Flux<Stocks> giveBackStock2(){
        /*
         * 1. get the top 2 stocks from the redis
         * 2. get all the data from the database based on a stockName and the stockname is the top of the redis function getTop2Request
         */
        return redisService.getTop2Request()
            .elementAt(1)
            .doOnNext(stockName -> {
                System.out.println("Top requested stock 2: " + stockName);
            })
            .flatMapMany(stockName -> {
            return stockInterface.findAllByName(stockName);
            });
    }

    public Mono<String> giveBackPrompt1(){
        Flux<Stocks> stockList1 = giveBackStock1();
        
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Stock Data for ").append(stockList1).append(":\n\n\n");
        
        Mono<String> noticias1 = newsService.getNewsTop1();

        promptBuilder.append("News Data:\n").append(noticias1).append("\n\n");

        return Mono.just(promptBuilder.toString());

    }

    public Mono<String> giveBackPrompt2(){
        //List<StockEntity> stockList2 = giveBackStock2();
        Flux<Stocks> stockList2 = giveBackStock2();

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("Stock Data for ").append(stockList2).append(":\n\n\n");

        Mono<String> noticias2 = newsService.getNewsTop2();

        promptBuilder.append("News Data:\n").append(noticias2).append("\n\n");

        return Mono.just(promptBuilder.toString());
        
    }

    public Mono<String> createAllStock(String stockName){
        /*
         * 1. collect the data from the API, the API is gonna return a lot of json one for each day
         * 2. the parser is already done in the StockCollect class
         * 3. get the data here and in a for loop create a StockEntity for each day and save it in the database
         * 4. return the list of StockEntity
         */
        
        String body = stockCollect.collectData(stockName);
        List<Stocks> stocks = stockCollect.processingData(body, stockName);

        System.out.println("Collected " + stocks.size() + " stock entries for " + stockName);
        for (int i = 0 ; i < stocks.size() ; i++){
            System.out.println("Saving stock entry: " + stocks.get(i).getDate() + " for " + stocks.get(i).getName());
            stockInterface.save(stocks.get(i));
        }
        System.out.println("Stocks saved to the database.");

        if (stocks.isEmpty()) {
            //return "No stock data found for symbol: " + stockName;
            return Mono.just("No stock data found for symbol: " + stockName);
        } else {
            return Mono.just("Stock data for " + stockName + " has been collected and saved. Total entries: " + stocks.size());
        }

    }

    public Mono<String> stopRedis(){
        System.out.println("\n\n\nIs been here in the stopRedis method of StocksService\n\n\n");

        Flux<String> topStocks = redisService.getTop2Request();

        if(topStocks == null || topStocks.count().block() < 2){
            return Mono.just("Not enough stock requests recorded in Redis.");
        }
        
        System.out.println("Top requested stocks: " + topStocks);

        topStocks.subscribe(stockName -> createAllStock(stockName));

        System.out.println("Data collection and storage completed for top requested stocks.");

        redisService.clearRedis();

        return Mono.just("Data collection and storage completed for top requested stocks.");
    }

}
