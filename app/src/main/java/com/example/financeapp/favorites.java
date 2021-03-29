package com.example.financeapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;


//Актитвити закладки фавориты.
public class favorites extends Activity {

    LayoutInflater ltInflater;
    LinearLayout linLayout;
    ImageView label;
    TextView price;
    TextView changing;
    ImageView upOrDown;
    TextView ticker;
    Button removeFromFavorites;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Создание потока под запрос на Yahoo finance, для получения акции.
        new Thread(){
            @Override
            public void run() {
                Stock stock = null;
                for (int i = 0; i < tickerPlusLabel.tickerList.size(); i++) {

                    int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            linLayout = (LinearLayout) findViewById(R.id.linLayout);

                            ltInflater = getLayoutInflater();
                            View item = ltInflater.inflate(R.layout.item, linLayout, false);

                            label = (ImageView) item.findViewById(R.id.imageTesla);
                            label.setImageResource(tickerPlusLabel.choiceLabel(tickerPlusLabel.tickerList.get(finalI)));

                            ticker = (TextView) item.findViewById(R.id.ticker);
                            ticker.setText(tickerPlusLabel.tickerList.get(finalI));

                            price = (TextView) item.findViewById(R.id.price);
                            price.setText("$ " + tickerPlusLabel.priceList.get(finalI));

                            changing = (TextView) item.findViewById(R.id.changing);
                            changing.setText(tickerPlusLabel.changeList.get(finalI));

                            //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
                            upOrDown = (ImageView) item.findViewById(R.id.upOrDown);
                            double converter = Double.parseDouble(tickerPlusLabel.changeList.get(finalI));
                            if (converter > 0){
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.gr);
                            } else {
                                upOrDown.setScaleY(1);
                                upOrDown.setScaleX(1);
                                upOrDown.setImageResource(R.drawable.r);
                            }

                            //Кнопка удаления позиции из избранного.
                            removeFromFavorites = (Button) item.findViewById(R.id.favorits);
                            removeFromFavorites.setText(R.string.removeFromFavorites);
                            removeFromFavorites.setTag(tickerPlusLabel.tickerList.get(finalI));

                            linLayout.addView(item);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(linLayout != null) {
            //Обнуление списка.
            linLayout.removeAllViews();
        }
    }

    public void onClick(View v){
        //Удаления позиции из избранного(кеша).
            int index = tickerPlusLabel.tickerList.indexOf((String) v.getTag());

            tickerPlusLabel.tickerList.remove(index);
            tickerPlusLabel.priceList.remove(index);
            tickerPlusLabel.changeList.remove(index);
            tickerPlusLabel.dividentList.remove(index);

        if(linLayout != null) {
            //Обнуление списка.
            linLayout.removeAllViews();
        }
        onResume();
    }

}