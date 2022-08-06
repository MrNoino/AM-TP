package com.example.canteen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class IncreaseBalanceActivity extends AppCompatActivity {

    NumberPicker np;

    ProgressBar pb;

    EditText et_nid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_balance);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.btn_addbalance);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        np = findViewById(R.id.input_amount);
        np.setMinValue(5);
        np.setMaxValue(100);
        np.setValue(10);
        et_nid = findViewById(R.id.input_nid);
        pb = findViewById(R.id.progress_bar);

    }
    private void JsonRequestIncreaseBalanceArray(Context context, String nid, int saldo, String token) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("saldo", saldo);
            jsonBody.put("n_identificacao", nid);
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/carregar_saldo";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    pb.setVisibility(View.GONE);
                    if(response.contains("200")){

                        Toast.makeText(context, "Sucesso.", Toast.LENGTH_LONG).show();

                    }else{

                        Toast.makeText(context, "Erro na operação.", Toast.LENGTH_LONG).show();

                    }
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

    public void add(View view) {

        Preferences pref = new Preferences();
        JsonRequestIncreaseBalanceArray(this, et_nid.getText().toString(), np.getValue(), pref.getStringPref(this, "token"));

    }


}