package com.example.canteen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

public class MainActivity extends AppCompatActivity {

    EditText et_nid, et_pwd;
    Button btn_visibility;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        et_nid = findViewById(R.id.input_nid);
        et_pwd = findViewById(R.id.input_pwd);
        btn_visibility = findViewById(R.id.btn_visibility);

        pb = findViewById(R.id.progress_bar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_login);
        setSupportActionBar(toolbar);

        Preferences pref = new Preferences();


        Log.i("LOG_RESPONSE", pref.getStringPref(this, "n_id"));
        if(!pref.getStringPref(this, "n_id").isEmpty() && !pref.getStringPref(this, "pwd").isEmpty())
            JsonLoginRequest(this, pref.getStringPref(this, "n_id"), pref.getStringPref(this, "pwd"));

    }

    public void openSignUp(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);

    }

    public void login(View view) {

        if(!et_nid.getText().toString().isEmpty() && !et_pwd.getText().toString().isEmpty()) {

            JsonLoginRequest(this, et_nid.getText().toString(), et_pwd.getText().toString());

        }else{

            Toast.makeText(this, "Fill the fields please!", Toast.LENGTH_LONG).show();

        }

    }

    private void JsonLoginRequest(Context context, String n_identificacao, String senha) {

        pb.setVisibility(View.VISIBLE);

        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("n_identificacao", n_identificacao);
            jsonBody.put("senha", senha);
            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/login";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleLoginResponse(n_identificacao, senha, response);
                    pb.setVisibility(View.GONE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    et_pwd.setText("");
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
            pb.setVisibility(View.GONE);
        }
    }

    private void handleLoginResponse(String n_identificacao, String pwd,String json) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        LoginResponse res = gson.fromJson(json, LoginResponse.class);

        if(res.getCode() == 200) {

            Preferences pref = new Preferences();

            pref.editStringPref(this, "n_id", n_identificacao);
            pref.editStringPref(this, "pwd", pwd);
            pref.editStringPref(this, "token", res.getToken());
            nextActivity();
        }

        else

            Toast.makeText(this, res.getErro().toString(), Toast.LENGTH_LONG).show();


    }

    private void nextActivity(){

        Intent intent = new Intent(this, DashboardActivity.class);

        et_nid.requestFocus();
        et_nid.setText("");
        et_pwd.setText("");

        startActivity(intent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("UseCompatLoadingForDrawables")
    public void toogleVisibility(View view) {

        if(et_pwd.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")){

            btn_visibility.setBackground(getResources().getDrawable(R.drawable.ic_visibility_off));
            et_pwd.setTransformationMethod(new SingleLineTransformationMethod());

        }else{

            btn_visibility.setBackground(getDrawable(R.drawable.ic_visibility));
            et_pwd.setTransformationMethod(new PasswordTransformationMethod());

        }

    }
}