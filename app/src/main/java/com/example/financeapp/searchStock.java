package com.example.financeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import java.io.IOException;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Актитвити закладки поиска тикеров.
public class searchStock extends Activity implements View.OnClickListener {

    Context context;
    EditText enterTicker;
    Button getSearch;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_stock);

        context = this;
        enterTicker = (EditText)findViewById(R.id.enterStock);

        getSearch = (Button)findViewById(R.id.getSearch);
        getSearch.setOnClickListener(this);
    }

    public void onClick(View v) {

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {
                String tickerName = enterTicker.getText().toString();

                Stock stock = null;
                try {

                    //запрос
                    stock = YahooFinance.get(tickerName);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Возвращение к UI потоку для взаимодействия с имеющимися View.
                Stock finalStock1 = stock;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Проверка на пустую строку
                        if (tickerName.length() != 0) {
                            //Проверка запроса на null.
                            if (finalStock1 == null) {
                                Toast.makeText(context, "Wrong request", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }else{
                            Toast.makeText(context, "Field is empty", Toast.LENGTH_LONG).show();
                            return;
                        }

                        //Вывод активити найденной акции.
                        Intent intentFindStock = new Intent("intentFindStock");
                        intentFindStock.putExtra("enterTicker", tickerName);
                        startActivity(intentFindStock);
                    }
                });
            }
        }.start();
    }

}
