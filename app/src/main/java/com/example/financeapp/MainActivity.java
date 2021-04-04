package com.example.financeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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
на основе библиотеки - "Quotes API for Yahoo Finance-3.15.0"
https://financequotes-api.com/ ,
c кешированием избранного.

Программирую меньше года, очень хочу изучить все современные технологии, но к сожалению сделать этого на данный момент не успел,
поэтому заранее извиняюсь за использование устаревшего API).
Основная работа выполнялась в период с 27.03.2021 по 29.03.2021, в дополнительный срок производились косметические изменеия, а именно:
изменение компоновки элементов LinearLayout и небольшая доработка кеша.
 */

public class MainActivity extends TabActivity {

    TabHost.TabSpec tabsOnStartActivity;
    SharedPreferences savingFavorites;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Инициализация списков.
        tickerPlusLabel.initializationOfArrays();

        //Загрука сохраненного списка избранного.
        loadCache();

        tabHost = getTabHost();

        //Добавление первой вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag1");
        tabsOnStartActivity.setIndicator("Trending");
        tabsOnStartActivity.setContent(new Intent(this, originalList.class));
        tabHost.addTab(tabsOnStartActivity);

        //Добавление второй вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag2");
        tabsOnStartActivity.setIndicator("Search");
        tabsOnStartActivity.setContent(new Intent(this, searchStock.class));
        tabHost.addTab(tabsOnStartActivity);

        //Добавление третей вкладки.
        tabsOnStartActivity = tabHost.newTabSpec("tag3");
        tabsOnStartActivity.setIndicator("Favorites");
        tabsOnStartActivity.setContent(new Intent(this, favorites.class));
        tabHost.addTab(tabsOnStartActivity);
    }

    //Обнуление сохраненных данных избранного и последующее новое их сохранение.
    @Override
    protected void onPause(){
        super.onPause();
        savingFavorites.edit().clear().apply();
        saveCache();
    }

    //Функция для сохранения данных избранного(кеша) перед закрытием приложения.
    public void saveCache() {
        for(int i = 0; i < tickerPlusLabel.tickerList.size(); i++) {
            savingFavorites = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor ed = savingFavorites.edit();
            ed.putString(i+"a", String.valueOf(tickerPlusLabel.tickerList.get(i)));
            ed.putString(i+"b", String.valueOf(tickerPlusLabel.priceList.get(i)));
            ed.putString(i+"c", String.valueOf(tickerPlusLabel.changeList.get(i)));
            ed.putString(i+"d", String.valueOf(tickerPlusLabel.changeIndexList.get(i)));
            ed.putString(i+"e", String.valueOf(tickerPlusLabel.dividentList.get(i)));
            ed.apply();
        }
    }

    //Функция для загрузки данных избранного, после перезапуска приложения.
    public void loadCache() {
        int i = 0;
        for(;;) {
            savingFavorites = getPreferences(MODE_PRIVATE);
            String loadKey = savingFavorites.getString(i+"a", "");
            String loadKey2 = savingFavorites.getString(i+"b", "");
            String loadKey3 = savingFavorites.getString(i+"c", "");
            String loadKey4 = savingFavorites.getString(i+"d", "");
            String loadKey5 = savingFavorites.getString(i+"e", "");

            if(!loadKey.equals("")) {
                tickerPlusLabel.tickerList.add(loadKey);
                tickerPlusLabel.priceList.add(loadKey2);
                tickerPlusLabel.changeList.add(loadKey3);
                tickerPlusLabel.changeIndexList.add(loadKey4);
                tickerPlusLabel.dividentList.add(loadKey5);
                i++;
            }else{
                break;
            }
        }
    }
}
