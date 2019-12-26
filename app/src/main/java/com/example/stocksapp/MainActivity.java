package com.example.stocksapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutosuggestAdaptor autoSuggestAdapter;
    private String selected_stock;
    private TextView symbol;
    private TextView open;
    private TextView high;
    private TextView low;
    private TextView price;
    private TextView volume;
   // private TextView latest_trading_day;
    //private TextView previous_close;
    //private TextView change;
    //private TextView change_percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        @SuppressLint("WrongViewCast") final AppCompatAutoCompleteTextView autoCompleteTextView =
                findViewById(R.id.auto_complete_edit_text);
       symbol = findViewById(R.id.symbol);
         open = findViewById(R.id.open);
        high = findViewById(R.id.high);
        low = findViewById(R.id.low);
        price = findViewById(R.id.price);
       // latest_trading_day = findViewById(R.id.latest_trading_day);
//        //previous_close = findViewById(R.id.previous_close);
//        //change = findViewById(R.id.change);
                 volume = findViewById(R.id.volume);
        //change_percent = findViewById(R.id.change_percent);


        autoSuggestAdapter = new AutosuggestAdaptor(this,android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selected_stock=autoSuggestAdapter.getObject(position);
                        selected_stock=autoSuggestAdapter.getDisplayObject(position);
                        //symbol.setText(autoSuggestAdapter.getDisplayObject(position));
                        callApiToDisplaySelectedStockDetails(selected_stock);
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    }

    private void makeApiCall(String text) {
        ApiCall.make(this, "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&keywords=" + text
                + "&apikey=OUHJRQ3O14C8QJ84", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                List<String> stock_name = new ArrayList<>();
                List<String> stock_symbol = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("bestMatches");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stock_name.add(row.getString("2. name"));
                        stock_symbol.add(row.getString("1. symbol"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                autoSuggestAdapter.setData(stock_name,stock_symbol);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void callApiToDisplaySelectedStockDetails(String stock_selected) {
        ApiCall.make(this, "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stock_selected + "&apikey=OUHJRQ3O14C8QJ84", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject obj = new JSONObject(response);

                    String symbol_str = obj.getJSONObject("Global Quote").getString("01. symbol");


                    String color = obj.getJSONObject("Global Quote").getString("02. open");
                    String color1 = obj.getJSONObject("Global Quote").getString("03. high");
                    String color2 = obj.getJSONObject("Global Quote").getString("04. low");
                    String color3 = obj.getJSONObject("Global Quote").getString("05. price");
                    String color4 = obj.getJSONObject("Global Quote").getString("06. volume");


                    String data = "Open: " + color;
                    String data1 = "High: " + color1;
                    String data2 = "Low: " + color2;
                    String data3 = "Price: " + color3;
                    String data4 = "Volume: " + color4;
                    String data5 = "Symbol: " + symbol_str;

                    symbol.setText(data5);
                    open.setText(data);
                    high.setText(data1);
                    low.setText(data2);
                    price.setText(data3);
                    volume.setText(data4);
                   // open.setText(obj.getJSONObject("Global Quote").getString("02. open"));
                   // high.setText(obj.getJSONObject("Global Quote").getString("03. high"));
                    //low.setText(obj.getJSONObject("Global Quote").getString("04. low"));
                    //price.setText(obj.getJSONObject("Global Quote").getString("05. price"));
                    //volume.setText(obj.getJSONObject("Global Quote").getString("06. volume"));
                    //latest_trading_day.setText(responseObject.getJSONObject("Global Quote").getString("07. latest trading day"));
                   // previous_close.setText(responseObject.getJSONObject("Global Quote").getString("08. previous close"));
                   // change.setText(responseObject.getJSONObject("Global Quote").getString("09. change"));
                   // change_percent.setText(responseObject.getJSONObject("Global Quote").getString("10. change percent"));

                  //  String color = obj.getString("02. open");
                    //String desc = obj.getString("03. high");

                    // Adds strings from object to the "data" string
                    //String data = "Open: " + color;

                    // Adds the data string to the TextView "results"
                    //open.setText(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }




}