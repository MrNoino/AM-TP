package com.example.canteen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SupportActivity extends AppCompatActivity {

    Spinner sp;

    EditText et_report_suggestion;

    ProgressBar pb;

    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_support);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        sp = findViewById(R.id.spinner_reports_suggestions);

        et_report_suggestion = findViewById(R.id.input_suggestions);

        pb = findViewById(R.id.progress_bar);

        sw = findViewById(R.id.toogle_anonimo);

        initSpinner();


    }


    public void openDial(View view) {

        TextView tv = (TextView) view;

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+tv.getText().toString()));
        startActivity(intent);

    }

    public void openEmail(View view) {

        TextView tv = (TextView) view;

        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:"+tv.getText().toString()));

        startActivity(intent);

    }

    private void initSpinner(){

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(this,
                R.array.reports_suggestions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sp.setAdapter(sp_adapter);

    }

    private void JsonRequestSendReportSuggestion(Context context, String info, String token) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            if(sp.getSelectedItemPosition() == 0)

                jsonBody.put("assunto", "Reporte de Bug");

            else

                jsonBody.put("assunto", "Sugestões");

            jsonBody.put("mensagem", et_report_suggestion.getText().toString());
            jsonBody.put("info", info);
            if(sw.isChecked())

                jsonBody.put("anonimo", "True");

            else

                jsonBody.put("anonimo", "False");

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/enviar_report";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    if(response.contains("200")) {

                        Toast.makeText(context, "Sucesso", Toast.LENGTH_LONG).show();
                        et_report_suggestion.setText("");

                    }else

                        Toast.makeText(context, "Erro na operação", Toast.LENGTH_LONG).show();

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

    public void sendReportSuggestion(View view) {

        if(et_report_suggestion.getText().toString().isEmpty()){

            Toast.makeText(this, "Campos Vazios", Toast.LENGTH_LONG).show();
            return;

        }

        String brand = Build.BRAND;
        String model = Build.MODEL;
        String versionRelease = Build.VERSION.RELEASE;

        Preferences pref = new Preferences();

        JsonRequestSendReportSuggestion(this, "Marca: " +brand.toUpperCase() +" | Modelo: "+ model.toUpperCase() +" | Versão SO: Android "+ versionRelease, pref.getStringPref(this, "token"));

    }
}