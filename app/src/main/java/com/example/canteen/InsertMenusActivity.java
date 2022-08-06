package com.example.canteen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class InsertMenusActivity extends AppCompatActivity {



    Spinner spinner_mealsOption, spinner_soup, spinner_main, spinner_dessert;

    EditText et_price;

    TextView tv_mealType;

    ProgressBar pb;

    CalendarView cv;

    ArrayList<SoupResponse> ar_soup;

    ArrayList<MainResponse> ar_main;

    ArrayList<DessertResponse> ar_dessert;

    private final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_menus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.btn_insertmenus);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        spinner_mealsOption = findViewById(R.id.spinner_meal);

        initSpinnerMealOption();

        pb = findViewById(R.id.progress_bar);

        cv = findViewById(R.id.calendar_date);

        tv_mealType = findViewById(R.id.txtview_mealType);

        et_price = findViewById(R.id.input_price);

        spinner_soup = findViewById(R.id.spinner_soup);

        spinner_main = findViewById(R.id.spinner_main);

        spinner_dessert = findViewById(R.id.spinner_dessert);

        Preferences pref = new Preferences();

        JsonRequestMeals(this, pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/listar_sopas");

        JsonRequestMeals(this, pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/listar_pratos");

        JsonRequestMeals(this, pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/listar_sobremesas");

        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if(spinner_main.getSelectedItemPosition() < ar_main.size()){

                    tv_mealType.setText(ar_main.get(spinner_main.getSelectedItemPosition()).getTipo());

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                view.setDate(calendar.getTimeInMillis());
            }
        });

    }

    private void JsonRequestMeals(Context context, String token, String url) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    pb.setVisibility(View.GONE);
                    if(url.contains("sopas"))
                        handleSoups(response);

                    if(url.contains("pratos"))
                        handleMain(response);

                    if(url.contains("sobremesas"))
                        handleDessert(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    error.printStackTrace();
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    finish();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleSoups(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        Type SoupType = new TypeToken<ArrayList<SoupResponse>>(){}.getType();

        ar_soup =  gson.fromJson(json, SoupType);

        if(ar_soup.size() == 0)
            return;

        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i < ar_soup.size(); i++){

            nomes.add(ar_soup.get(i).getNome());

        }

        initSpinnerMeals(spinner_soup,nomes);


    }

    private void handleMain(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        Type MainType = new TypeToken<ArrayList<MainResponse>>(){}.getType();

        ar_main =  gson.fromJson(json, MainType);

        if(ar_main.size() == 0)
            return;

        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i < ar_main.size(); i++){

            nomes.add(ar_main.get(i).getNome());

        }

        initSpinnerMeals(spinner_main,nomes);


    }

    private void handleDessert(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        Type DessertType = new TypeToken<ArrayList<DessertResponse>>(){}.getType();

        ar_dessert =  gson.fromJson(json, DessertType);

        if(ar_dessert.size() == 0)
            return;

        ArrayList<String> nomes = new ArrayList<>();
        for(int i = 0; i < ar_dessert.size(); i++){

            nomes.add(ar_dessert.get(i).getNome());

        }

        initSpinnerMeals(spinner_dessert,nomes);


    }

    private void initSpinnerMealOption(){

        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(this,
                R.array.meal_option, android.R.layout.simple_spinner_item);

        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_mealsOption.setAdapter(sp_adapter);

    }

    private void initSpinnerMeals(Spinner s,ArrayList<String> nomes){

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        s.setAdapter(adapter);

    }

    private void JsonRequestInsertMenu(Context context, String token) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("preco", et_price.getText().toString());
            jsonBody.put("sobremesas_id_sobremesa", ar_dessert.get(spinner_dessert.getSelectedItemPosition()).getId_sobremesa());
            jsonBody.put("sopas_id_sopa", ar_soup.get(spinner_soup.getSelectedItemPosition()).getId_sopa());
            jsonBody.put("pratos_id_prato", ar_main.get(spinner_main.getSelectedItemPosition()).getId_prato());
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/criar_ementa";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    pb.setVisibility(View.GONE);
                    handleInsertMenu(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    error.printStackTrace();
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    finish();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleInsertMenu(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        InsertMenuResponse res = gson.fromJson(json, InsertMenuResponse.class);

        if(res.getId_ementa() == 0){
            Toast.makeText(this, "Erro na operação", Toast.LENGTH_LONG).show();
            return;
        }else{

            Preferences pref = new Preferences();
            JsonRequestRegisterMenu(this, res.getId_ementa(), pref.getStringPref(this, "token"));

        }

    }

    private void JsonRequestRegisterMenu(Context context, int id_ementa, String token) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("data", format.format(cv.getDate()));
            jsonBody.put("ementas_id_ementa", id_ementa);

            if(spinner_mealsOption.getSelectedItemPosition() == 0)

                jsonBody.put("tipo", "Almoço");

            else

                jsonBody.put("tipo", "Jantar");

            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/registar_ementa";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    pb.setVisibility(View.GONE);

                    if(response.contains("200"))
                        Toast.makeText(context, "Sucesso", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, "Erro na operação", Toast.LENGTH_LONG).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    error.printStackTrace();
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    finish();
                }
            }) {

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void resgisterMenu(View view) {

        if(et_price.getText().toString().isEmpty()){

            Toast.makeText(this, "Campos vazios!", Toast.LENGTH_LONG).show();
            return;

        }

        Preferences pref = new Preferences();

        JsonRequestInsertMenu(this, pref.getStringPref(this, "token"));

    }
}