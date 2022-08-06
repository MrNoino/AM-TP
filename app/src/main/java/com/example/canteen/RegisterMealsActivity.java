package com.example.canteen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class RegisterMealsActivity extends AppCompatActivity {

    ProgressBar pb;

    Spinner mealsTypeSpinner;

    EditText et_soup, et_main, et_dessert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_meals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.btn_registerMeals);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        pb = findViewById(R.id.progress_bar);

        mealsTypeSpinner = findViewById(R.id.spinner_meal_type);

        initSpinner(mealsTypeSpinner);

        et_soup = findViewById(R.id.input_soup);

        et_main = findViewById(R.id.input_main);

        et_dessert = findViewById(R.id.input_dessert);


    }

    private void JsonRequestRegisterMeals(Context context, String nome, String tipo, String token, String url) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            if(!tipo.equals(""))
                jsonBody.put("tipo", tipo);
            jsonBody.put("nome", nome);
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    pb.setVisibility(View.GONE);
                    if(response.contains("200"))

                        Toast.makeText(context, "Sucesso.", Toast.LENGTH_LONG).show();

                    else

                        Toast.makeText(context, "Erro na operação.", Toast.LENGTH_LONG).show();

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

    private void initSpinner(View v){


        ArrayAdapter<CharSequence> sp_adapter = ArrayAdapter.createFromResource(v.getContext(),
                R.array.meal_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        sp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mealsTypeSpinner.setAdapter(sp_adapter);

    }

    public void addSoup(View view) {

        if(et_soup.getText().toString().isEmpty()){

            Toast.makeText(this, "Campo vazio.", Toast.LENGTH_LONG).show();
            return;

        }

        Preferences pref = new Preferences();
        JsonRequestRegisterMeals(this, et_soup.getText().toString(), "", pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/registar_sopa");

        et_soup.setText("");
    }

    public void addMain(View view) {

        if(et_main.getText().toString().isEmpty()){

            Toast.makeText(this, "Campo vazio.", Toast.LENGTH_LONG).show();
            return;

        }

        Preferences pref = new Preferences();

        if(mealsTypeSpinner.getSelectedItemPosition() == 0)
            JsonRequestRegisterMeals(this, et_main.getText().toString(), "Peixe", pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/registar_prato");

        if(mealsTypeSpinner.getSelectedItemPosition() == 1)
            JsonRequestRegisterMeals(this, et_main.getText().toString(), "Carne", pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/registar_prato");


        if(mealsTypeSpinner.getSelectedItemPosition() == 2)
            JsonRequestRegisterMeals(this, et_main.getText().toString(), "Vegetariano", pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/registar_prato");


        et_main.setText("");
    }

    public void addDessert(View view) {

        if(et_dessert.getText().toString().isEmpty()){

            Toast.makeText(this, "Campo vazio.", Toast.LENGTH_LONG).show();
            return;

        }

        Preferences pref = new Preferences();
        JsonRequestRegisterMeals(this, et_dessert.getText().toString(), "", pref.getStringPref(this, "token"), "https://cantina-postgres.herokuapp.com/registar_sobremesa");

        et_dessert.setText("");

    }


}