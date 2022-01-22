package com.example.kalkulatorwalut;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView result;
    private RequestQueue http;
    private EditText input;
    private String currency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        http= Volley.newRequestQueue(this);
        input=findViewById(R.id.input);
        input.setFilters(new InputFilter[] { filter });
        result=findViewById(R.id.result);
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.currancies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(input.getText().toString().equals("")){
                    fetch(0);
                }
                else{
                    fetch(Double.parseDouble(input.getText().toString()));
                }

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currency =adapterView.getItemAtPosition(i).toString();
        if(input.getText().toString().equals("")){
            fetch(0);
        }
        else{
            fetch(Double.parseDouble(input.getText().toString()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    private void fetch(double value){
        String url="http://api.nbp.pl/api/exchangerates/rates/a/"+ currency +"/?format=json";
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray= response.getJSONArray("rates");
                        JSONObject rates= jsonArray.getJSONObject(0);
                        double mid=rates.getDouble("mid");
                        result.setText(String.format("%.2f%n", mid*value));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        http.add(request);

    }
    private final InputFilter filter = (source, start, end, dest, dstart, dend) -> {

        String blockCharacterSet = ",- ";
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };
}