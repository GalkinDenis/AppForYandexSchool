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
    public static ArrayList<String> changeIndexList;
    public static ArrayList<String> dividentList;
    public static Map<String, Integer> stockStack;
    public static Map<String, String> stockNameStack;
    public static ArrayList<String> tickersForFavorites;
    public static String[] tickers;
    public static String[] nameOfCompany;
    public static int[] drawable;

    public static void initializationOfArrays() {
        //Массив для храненя связки - "тикер + эмблема",
        // для более удобного формирования списка избранного.
        stockStack = new HashMap<String, Integer>();

        //Массив для храненя связки - "тикер + имя".
        stockNameStack = new HashMap<String, String>();


        //Массив для хранения перечня избранных тикеров.
        tickersForFavorites = new ArrayList<>();

        //Массивы для кеширования избранного {
        tickerList = new ArrayList<>();
        priceList = new ArrayList<>();
        changeList = new ArrayList<>();
        changeIndexList = new ArrayList<>();
        dividentList = new ArrayList<>();
        //////////////////////////////////// }

        tickers = new String[]{
                "YNDX", "RDS-B", "TSLA", "AAPL", "GE", "AMD", "CSCO",
                "3420.T", "GOOGL", "MCD", "DIS", "MSFT", "NFLX", "NKE",
                "MA", "UA", "V", "ADBE", "AMZN", "HP", "PG", "U", "JNJ"
        };

        nameOfCompany = new String[]{
                "Yandex", "Shell", "Tesla", "Appl", "General Electric",
                "Advanced Micro Devices", "CSCO", "KFC", "Google", "McDonalds",
                "Disney", "Microsoft", "Netflix", "Nike", "Master Card",
                "Under Armor", "Visa", "Adobe", "Amazon", "Hewlett-Packard",
                "Procter & Gamble", "Unity", "Jonson & Jonson"
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

        //Формирование связки - "тикер + имя".
        for (int i = 0; i < tickers.length; i++) {
            stockNameStack.put(tickers[i], nameOfCompany[i]);
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

