
package com.example.canteen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class DashboardActivity extends AppCompatActivity {

    private String mToken;

    ProgressBar pb;

    TextView mBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_dashboard);
        setSupportActionBar(toolbar);

        mBalance = findViewById(R.id.txtview_balance);

        pb = findViewById(R.id.progress_bar);

        Preferences pref = new Preferences();

        boolean admin = false;

        mToken = pref.getStringPref(this, "token");

        if(!mToken.isEmpty()){

            JsonRequest(this, mToken);
            JsonBalanceRequest(this, pref.getStringPref(this, "token"));

        }

    }

    private void JsonRequest(Context context, String token) {

        pb.setVisibility(View.VISIBLE);

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/isAdmin";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    Log.i("LOG_RESPONSE", response);

                    if(response.contains("true"))
                        addButtons(response);

                    pb.setVisibility(View.GONE);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
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

    public void addButtons(String response){

        Button btn1 = findViewById(R.id.btn_increasebalance);

        btn1.setVisibility(View.VISIBLE);
        btn1.setEnabled(true);

        btn1 = findViewById(R.id.btn_registerMeals);

        btn1.setVisibility(View.VISIBLE);
        btn1.setEnabled(true);

        btn1 = findViewById(R.id.btn_insertmenus);

        btn1.setVisibility(View.VISIBLE);
        btn1.setEnabled(true);

        btn1 = findViewById(R.id.btn_scanqrcode);

        btn1.setVisibility(View.VISIBLE);
        btn1.setEnabled(true);

    }

    private void JsonBalanceRequest(Context context, String token) {

        pb.setVisibility(View.VISIBLE);

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/consultar_saldo";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleBalanceResponse(response);
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

    private void handleBalanceResponse(String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        BalanceResponse res = gson.fromJson(json, BalanceResponse.class);

        DecimalFormat df = new DecimalFormat("0.00");
        String balance = df.format(res.getSaldo()) + "€";
        mBalance.setText(balance);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                Preferences pref = new Preferences();
                pref.clearPref(this);
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show();
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void openSupportActivity(View view) {

        Intent intent = new Intent(this, SupportActivity.class);
        startActivity(intent);

    }

    public void openExtractActivity(View view) {

        Intent intent = new Intent(this, ExtractActivity.class);
        startActivity(intent);

    }

    public void openIncreaseBalance(View view) {

        Intent intent = new Intent(this, IncreaseBalanceActivity.class);
        startActivity(intent);

    }

    public void openInsertMenus(View view) {

        Intent intent = new Intent(this, InsertMenusActivity.class);
        startActivity(intent);

    }

    public void openProfileActivity(View view) {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);

    }

    public void openRegisterMeals(View view) {

        Intent intent = new Intent(this, RegisterMealsActivity.class);
        startActivity(intent);

    }

    public void openMenusActivity(View view) {

        Intent intent = new Intent(this, MenusActivity.class);
        startActivity(intent);

    }

    public void openScanQrCode(View view) {

        Intent intent = new Intent(this, ScanQrCode.class);
        startActivity(intent);

    }
}