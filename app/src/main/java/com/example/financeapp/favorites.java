package com.example.financeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    TextView name;
    TextView index;
    TextView price;
    TextView ticker;
    ImageView label;
    TextView changing;
    Button showDetails;
    ImageView upOrDown;
    Animation animAlpha;
    LinearLayout linLayout;
    LayoutInflater ltInflater;
    Button removeFromFavorites;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Вывод содержимого избранного(кеша) на экран.
        for (int i = 0; i < tickerPlusLabel.tickerList.size(); i++) {
            linLayout = (LinearLayout) findViewById(R.id.linLayout);

            ltInflater = getLayoutInflater();
            View item = ltInflater.inflate(R.layout.item, linLayout, false);

            label = (ImageView) item.findViewById(R.id.imageTesla);
            label.setImageResource(tickerPlusLabel.choiceLabel(tickerPlusLabel.tickerList.get(i)));

            ticker = (TextView) item.findViewById(R.id.ticker);
            ticker.setText("(" + tickerPlusLabel.tickerList.get(i) + ")");

            name = (TextView) item.findViewById(R.id.name);
            name.setText(tickerPlusLabel.stockNameStack.get(tickerPlusLabel.tickerList.get(i)));

            price = (TextView) item.findViewById(R.id.price);
            price.setText("$ " + tickerPlusLabel.priceList.get(i));

            index = (TextView) item.findViewById(R.id.index);
            index.setText(tickerPlusLabel.changeIndexList.get(i));

            changing = (TextView) item.findViewById(R.id.changing);
            changing.setText("(" + tickerPlusLabel.changeList.get(i) + "%)");


            //Вывод зеленой\красной стрелки, в зависимости от знака перед значением.
            upOrDown = (ImageView) item.findViewById(R.id.upOrDown);
            double converter = Double.parseDouble(tickerPlusLabel.changeList.get(i));
            if (converter > 0) {
                upOrDown.setScaleY(1);
                upOrDown.setScaleX(1);
                upOrDown.setImageResource(R.drawable.gr);
            } else {
                upOrDown.setScaleY(1);
                upOrDown.setScaleX(1);
                upOrDown.setImageResource(R.drawable.r);
            }

            showDetails = (Button) item.findViewById(R.id.details);
            showDetails.setText(R.string.showDetails);
            showDetails.setTag(tickerPlusLabel.tickerList.get(i));

            //Кнопка удаления позиции из избранного.
            removeFromFavorites = (Button) item.findViewById(R.id.favorits);
            removeFromFavorites.setText(R.string.removeFromFavorites);
            removeFromFavorites.setTag(tickerPlusLabel.tickerList.get(i));

            //Добавление элемента в список.
            linLayout.addView(item);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(linLayout != null) {
            //Обнуление списка.
            linLayout.removeAllViews();
        }
    }

    //Вывод активити с деталями найденной акции.
    public void onClickShowDetails(View v){
        v.startAnimation(animAlpha);

        int index = tickerPlusLabel.tickerList.indexOf((String) v.getTag());

        //Вывод активити с деталями найденной акции.
        Intent intentFindStock = new Intent("showDetailsFromCache");
        intentFindStock.putExtra("ticker", tickerPlusLabel.tickerList.get(index));
        intentFindStock.putExtra("price", tickerPlusLabel.priceList.get(index));
        intentFindStock.putExtra("change", tickerPlusLabel.changeList.get(index));
        intentFindStock.putExtra("changeIndex", tickerPlusLabel.changeIndexList.get(index));
        intentFindStock.putExtra("divident", tickerPlusLabel.dividentList.get(index));
        startActivity(intentFindStock);
    }

    //Удаление из избанного(кеша).
    public void onClickAddOrRemoveFromFavorites(View v){
        v.startAnimation(animAlpha);

        //Удаления позиции из избранного(кеша).
        int index = tickerPlusLabel.tickerList.indexOf((String) v.getTag());

        tickerPlusLabel.tickerList.remove(index);
        tickerPlusLabel.priceList.remove(index);
        tickerPlusLabel.changeList.remove(index);
        tickerPlusLabel.changeIndexList.remove(index);
        tickerPlusLabel.dividentList.remove(index);

        if(linLayout != null) {
            //Обнуление списка.
            linLayout.removeAllViews();
        }

        //Перерисовка обновленного списка.
        onResume();
    }

}
