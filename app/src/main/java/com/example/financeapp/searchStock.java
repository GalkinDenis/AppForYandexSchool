package com.example.financeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import java.io.IOException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Актитвити закладки поиска тикеров.
public class searchStock extends Activity implements View.OnClickListener {

    Context context;
    Button getSearch;
    Animation animAlpha;
    EditText enterTicker;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_stock);

        context = this;
        getSearch = (Button)findViewById(R.id.getSearch);
        getSearch.setOnClickListener(this);
        enterTicker = (EditText)findViewById(R.id.enterStock);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
    }

    public void onClick(View v) {
        v.startAnimation(animAlpha);

        //Создание потока под запрос на Yahoo finance, для проверки, существует ли запрашиваемая акция.
        new Thread(){
            @Override
            public void run() {
                String tickerName = enterTicker.getText().toString();

                //Запрос.
                Stock stock = null;
                try {
                    stock = YahooFinance.get(tickerName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Возвращение к UI потоку для взаимодействия с имеющимися View.
                Stock finalStock = stock;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Проверка на пустую строку
                        if (tickerName.length() != 0) {
                            //Проверка запроса на null.
                            if (finalStock == null) {
                                Toast.makeText(context, "Wrong request", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }else{
                            Toast.makeText(context, "Field is empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Вывод активити найденной акции.
                        Intent intentFindStock = new Intent("intentFindStock");
                        intentFindStock.putExtra("enter", tickerName);
                        startActivity(intentFindStock);
                    }
                });
            }
        }.start();
    }
}
