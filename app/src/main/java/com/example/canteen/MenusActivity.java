package com.example.canteen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Date;
import java.util.Locale;

public class MenusActivity extends AppCompatActivity {

    SimpleDateFormat format = new SimpleDateFormat("EEE\ndd-MM", Locale.getDefault());

    private final SimpleDateFormat FullDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    private TabLayout tabLayout1, tabLayout2;

    private RecyclerView recyclerView;

    private RecyclerViewAdapterMenus mAdapter;

    private ProgressBar pb;

    private final Type ExtractType = new TypeToken<ArrayList<MealsResponse>>(){}.getType();
    private ArrayList<MealsResponse> ar;

    private TextView no_menus;

    // below line is for getting
    // the windowmanager service.
    private WindowManager manager;

    // initializing a variable for default display.
    private Display display;

    // creating a variable for point which
    // is to be displayed in QR Code.
    private Point point;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menus);

        pb = findViewById(R.id.progress_bar);

        no_menus = findViewById(R.id.txtview_nomenus);

        Preferences pref = new Preferences();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_meals);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create an instance of the tab layout from the view.
        tabLayout1 = findViewById(R.id.tab_layout1);

        Date currentDate = new Date();

        Calendar c = Calendar.getInstance();

        // below line is for getting
        // the windowmanager service.
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);


        for(int i = 0; i < 7; i++){

            c.setTime(currentDate);
            c.add(Calendar.DAY_OF_YEAR, i);
            tabLayout1.addTab(tabLayout1.newTab().setText(format.format(c.getTime())));

        }



        Context context = this;

        tabLayout1.addOnTabSelectedListener(new
           TabLayout.OnTabSelectedListener() {
               @Override
               public void onTabSelected(TabLayout.Tab tab) {

                   c.setTime(currentDate);
                   c.add(Calendar.DAY_OF_YEAR, tabLayout1.getSelectedTabPosition());
                   JsonRequestMenus(context, FullDateFormat.format(c.getTime()), pref.getStringPref(context, "token"));

               }

               @Override
               public void onTabUnselected(TabLayout.Tab tab) {
               }

               @Override
               public void onTabReselected(TabLayout.Tab tab) {
               }
           });

        // Create an instance of the tab layout from the view.
        tabLayout2 = findViewById(R.id.tab_layout2);

        Resources res = getResources();
        for (String str : res.getStringArray(R.array.meal_option)){

            tabLayout2.addTab(tabLayout2.newTab().setText(str));

        }

        tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);


        tabLayout2.addOnTabSelectedListener(new
            TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {

                    c.setTime(currentDate);
                    c.add(Calendar.DAY_OF_YEAR, tabLayout1.getSelectedTabPosition());
                    JsonRequestMenus(context, FullDateFormat.format(c.getTime()), pref.getStringPref(context, "token"));

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

        recyclerView = findViewById(R.id.recyclerview);

        ar = new ArrayList<>();

        mAdapter = new RecyclerViewAdapterMenus(this, point, ar);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        c.setTime(currentDate);

        JsonRequestMenus(this, FullDateFormat.format(c.getTime()), pref.getStringPref(context, "token"));




    }


    private void JsonRequestMenus(Context context, String data, String token) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("data", data);
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/listar_ementas_compradas_data";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleMenus(response);
                    pb.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
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

    private void handleMenus(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        ar.clear();

        ArrayList<MealsResponse> new_ar;

        new_ar =  gson.fromJson(json, ExtractType);

        for (MealsResponse res : new_ar){

            if(tabLayout2.getSelectedTabPosition() == 0 && res.getTipodeRefeicao().equals("Almoço")){

                ar.add(res);

            }else if(tabLayout2.getSelectedTabPosition() == 1 && res.getTipodeRefeicao().equals("Jantar")){

                ar.add(res);

            }

        }


        if(ar.size() > 0)
            no_menus.setVisibility(View.GONE);
        else
            no_menus.setVisibility(View.VISIBLE);

        mAdapter.notifyDataSetChanged();

    }

}