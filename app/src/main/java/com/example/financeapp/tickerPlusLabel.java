package com.example.financeapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;

public class tickerPlusLabel {

    public static ArrayList<String> tickerList;
    public static ArrayList<String> priceList;
    public static ArrayList<String> changeList;
    public static ArrayList<String> dividentList;
    public static Map<String, Integer> stockStack;
    public static ArrayList<String> tickersForFavorites;
    public static String[] tickers;
    public static String[] tickers2;
    public static int[] drawable;

    public static void initOfArrays() {
        //Массив для храненя связки - "тикер + эмблема",
        // для более удобного формирования списка избранного.
        stockStack = new HashMap<String, Integer>();

        //Массив для хранения перечня избранных тикеров.
        tickersForFavorites = new ArrayList<>();

        //Массивы для кеширования избранного {
        tickerList = new ArrayList<>();
        priceList = new ArrayList<>();
        changeList = new ArrayList<>();
        dividentList = new ArrayList<>();
        //                                 }

        tickers = new String[]{
                "YNDX", "RDS-B", "TSLA", "AAPL", "GE", "AMD", "CSCO",
                "3420.T", "GOOGL", "MCD", "DIS", "MSFT", "NFLX", "NKE",
                "MA", "UA", "V", "ADBE", "AMZN", "HP", "PG", "U", "JNJ"
        };

        tickers2 = new String[]{
                "YNDX Yandex", "RDS-B Shell", "TSLA Tesla", "AAPL Appl", "GE General Electric", "AMD     Advanced Micro Devices", "CSCO",
                "3420.T KFC", "GOOGL Google", "MCD     McDonalds", "DIS Disney", "MSFT     Microsoft", "NFLX Netflix", "NKE Nike",
                "MA Master Card", "UA Under Armor", "V        Visa", "ADBE Adobe", "AMZN Amazon", "HP Hewlett-Packard", "PG Procter & Gamble", "U     Unity", "JNJ Jonson & Jonson"
        };

        drawable = new int[]{
                R.drawable.yandex, R.drawable.shell, R.drawable.tesla, R.drawable.appl,
                R.drawable.ge, R.drawable.amd, R.drawable.csco2, R.drawable.kfc, R.drawable.goo,
                R.drawable.mc, R.drawable.wd, R.drawable.ms, R.drawable.nf, R.drawable.nike,
                R.drawable.mastercard, R.drawable.ua, R.drawable.v, R.drawable.adobe,
                R.drawable.amazon, R.drawable.hp, R.drawable.pg, R.drawable.u, R.drawable.jj
        };

        //Формирование связки - "тикер + эмблема".
        for (int i = 0; i < tickers.length; i++) {
            stockStack.put(tickers[i], drawable[i]);
        }
    }

    //Функция подбора соответствующей эмблемы компании.
    public static int choiceLabel(String str) {
        if (stockStack.get(str) != null) {
            return stockStack.get(str);
        } else {
            return R.drawable.clear;
        }
    }

}


