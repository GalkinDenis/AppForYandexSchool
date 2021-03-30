package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.app.TabActivity;
import android.content.Intent;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/*
Реализация проектного задания, школы мобильной разработки Яндекса,
на основе библиотеки - "YahooFinanceAPI-3.15.0" , c кешированием избранного.
Извиняюсь за использование устаревшего API, что то посовременнее не успел изучить)
 */

public class MainActivity extends TabActivity {

    TabHost.TabSpec tabsOnStartActivity;
    SharedPreferences savingFavorites;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализация структур данных.
        tickerPlusLabel.initOfArrays();

        //Загрука сохраненного списка избранного.
        loadText();

        tabHost = getTabHost();

        //Добавление первой вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag1");
        tabsOnStartActivity.setIndicator("Trendable stocks");
        tabsOnStartActivity.setContent(new Intent(this, originalList.class));
        tabHost.addTab(tabsOnStartActivity);

        //Добавление второй вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag2");
        tabsOnStartActivity.setIndicator("Search to stocks");
        tabsOnStartActivity.setContent(new Intent(this, searchStock.class));
        tabHost.addTab(tabsOnStartActivity);

        //Добавление третей вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag3");
        tabsOnStartActivity.setIndicator("Favorites stocks");
        tabsOnStartActivity.setContent(new Intent(this, favorites.class));
        tabHost.addTab(tabsOnStartActivity);
    }

    //Обнуление сохраненных данных избранного и последующее новое их сохранение.
    @Override
    protected void onPause(){
        super.onPause();
        savingFavorites.edit().clear().apply();
        saveText();
    }

    //Функция для сохранения данных избранного(кеша) перед закрытием приложения.
    public void saveText() {
        for(int i = 0; i < tickerPlusLabel.tickerList.size(); i++) {
            savingFavorites = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = savingFavorites.edit();
            ed.putString(i+"a", String.valueOf(tickerPlusLabel.tickerList.get(i)));
            ed.putString(i+"b", String.valueOf(tickerPlusLabel.priceList.get(i)));
            ed.putString(i+"c", String.valueOf(tickerPlusLabel.changeList.get(i)));
            ed.putString(i+"d", String.valueOf(tickerPlusLabel.dividentList.get(i)));
            ed.apply();
        }
    }

    //Функция для загрузки данных избранного, после перезапуска приложения.
    public void loadText() {
        int i = 0;
        for(;;) {
            savingFavorites = getPreferences(MODE_PRIVATE);
            String key = savingFavorites.getString(i+"a", "");
            String key2 = savingFavorites.getString(i+"b", "");
            String key3 = savingFavorites.getString(i+"c", "");
            String key4 = savingFavorites.getString(i+"d", "");

            if(!key.equals("")) {
                tickerPlusLabel.tickerList.add(key);
                tickerPlusLabel.priceList.add(key2);
                tickerPlusLabel.changeList.add(key3);
                tickerPlusLabel.dividentList.add(key4);
                i++;
            }else{
                break;
            }
        }
    }
}
