package com.example.financeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

//Активити стартового списка трендовых акций.
public class originalList extends Activity {

    View item;
    TextView index;
    ImageView label;
    TextView showName;
    TextView showPrice;
    ImageView upOrDown;
    Button showDetails;
    Animation animAlpha;
    TextView showTicker;
    TextView showChanging;
    Button addToFavorites;
    LinearLayout linLayout;
    LayoutInflater ltInflater;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < tickerPlusLabel.tickers.length; i++) {

                    //Запрос.
                    Stock stock = null;
                    try {
                        stock = YahooFinance.get(tickerPlusLabel.tickers[i]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Получение цены.
                    BigDecimal price;
                    price = stock.getQuote().getPrice();

                    //Получение изменения за сутки.
                    BigDecimal change;
                    change = stock.getQuote().getChangeInPercent();

                    //Получение изменения за сутки.
                    BigDecimal changeIndex;
                    changeIndex = stock.getQuote().getChange();

                    //Возвращение к UI потоку для взаимодействия с имеющимися View.
                    int I = i;
                    BigDecimal finalPrice = price;
                    BigDecimal finalChangeIndex = changeIndex;
                    BigDecimal finalChange = change;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linLayout = (LinearLayout) findViewById(R.id.linLayout);
                            ltInflater = getLayoutInflater();
                            item = ltInflater.inflate(R.layout.item, linLayout, false);

                            label = (ImageView) item.findViewById(R.id.imageTesla);
                            label.setImageResource(tickerPlusLabel.drawable[I]);

                            showTicker = (TextView) item.findViewById(R.id.ticker);
                            showTicker.setText("(" + tickerPlusLabel.tickers[I] + ")");

                            showName = (TextView) item.findViewById(R.id.name);
                            showName.setText(tickerPlusLabel.stockNameStack.get(tickerPlusLabel.tickers[I]));

                            showPrice = (TextView) item.findViewById(R.id.price);
                            showPrice.setText("$ " + String.valueOf(finalPrice));

                            index = (TextView) item.findViewById(R.id.index);
                            index.setText(String.valueOf(finalChangeIndex));

                            showChanging = (TextView) item.findViewById(R.id.changing);
                            showChanging.setText("(" + String.valueOf(finalChange) + "%)");

                            //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
                            upOrDown = (ImageView) item.findViewById(R.id.upOrDown);
                            double converter =  Double.parseDouble(String.valueOf(finalChange));
                            if(converter > 0) {
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.gr);
                            }else{
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.r);
                            }

                            //Кнопка вызова экрана детализации.
                            showDetails = (Button) item.findViewById(R.id.details);
                            showDetails.setText(R.string.showDetails);
                            showDetails.setTag(tickerPlusLabel.tickers[I]);

                            //Кнопка добавления в избранное.
                            addToFavorites = (Button) item.findViewById(R.id.favorits);
                            addToFavorites.setText(R.string.addToFavorites);
                            addToFavorites.setTag(tickerPlusLabel.tickers[I]);

                            //Добавление элемента в список.
                            linLayout.addView(item);
                        }
                    });
                }
            }
        }.start();
    }


    //Вывод активити с деталями найденной акции.
    public void onClickShowDetails(View v){
        v.startAnimation(animAlpha);

        Intent intentFindStock = new Intent("intentFindStock");
        intentFindStock.putExtra("enter", (String) v.getTag());
        startActivity(intentFindStock);
    }

    //Добавление в избранное(кеш).
    public void onClickAddOrRemoveFromFavorites(View v){
        v.startAnimation(animAlpha);

        new Thread(){
            @Override
            public void run() {

                Stock stock2 = null;
                try {
                    stock2 = YahooFinance.get((String) v.getTag());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigDecimal price2;
                price2 = stock2.getQuote().getPrice();

                BigDecimal change2;
                change2 = stock2.getQuote().getChangeInPercent();

                BigDecimal dividends2;
                dividends2 = stock2.getDividend().getAnnualYieldPercent();

                BigDecimal changeIndex2;
                changeIndex2 = stock2.getQuote().getChange();

                BigDecimal finalPrice2 = price2;
                BigDecimal finalChange2 = change2;
                BigDecimal finalDividends2 = dividends2;
                BigDecimal finalChangeIndex2 = changeIndex2;
                //Возвращение к UI потоку для взаимодействия с имеющимися View.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Добавление в кеш.
                        if(!tickerPlusLabel.tickerList.contains((String) v.getTag())){
                            tickerPlusLabel.tickerList.add((String) v.getTag());
                            tickerPlusLabel.priceList.add(String.valueOf((finalPrice2)));
                            tickerPlusLabel.changeList.add(String.valueOf((finalChange2)));
                            tickerPlusLabel.changeIndexList.add(String.valueOf((finalChangeIndex2)));
                            if(finalDividends2 == null) {
                                tickerPlusLabel.dividentList.add("-");
                            }else{
                                tickerPlusLabel.dividentList.add(String.valueOf((finalDividends2)));
                            }
                        }
                    }
                });
            }
        }.start();

    }
}
