package com.example.canteen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUpActivity extends AppCompatActivity {

    EditText et_nid, et_name, et_email, et_pwd, et_cfm_pwd;
    CheckBox cb;

    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_signup);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_nid = findViewById(R.id.input_nid);
        et_name = findViewById(R.id.input_name);
        et_email = findViewById(R.id.input_email);
        et_pwd = findViewById(R.id.input_pwd);
        et_cfm_pwd = findViewById(R.id.input_confirm_pwd);
        cb = findViewById(R.id.checkbox_terms);

        pb = findViewById(R.id.progress_bar);

    }

    public void openTerms(View view) {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.estgoh.ipc.pt/estudantes/gabinete-informatica"));
        startActivity(browserIntent);

    }

    public void SignUp(View view) {

        String pass = et_pwd.getText().toString(), cfm = et_cfm_pwd.getText().toString();

        if(!pass.equals(cfm)){

            Toast.makeText(this, "Palavra Passe não é a mesma que a sua confirmação.\nPass: " + et_pwd.getText() + "\nCfm: "+ et_cfm_pwd.getText(), Toast.LENGTH_LONG).show();
            et_pwd.setText("");
            et_cfm_pwd.setText("");
            return;

        }

        if(!et_nid.getText().toString().isEmpty() && !et_name.getText().toString().isEmpty() && !et_email.getText().toString().isEmpty() && !et_pwd.getText().toString().isEmpty() && cb.isChecked()) {

            JsonSignUpRequest(this, et_nid.getText().toString(), et_name.getText().toString(), et_pwd.getText().toString(), et_email.getText().toString());
            return;

        }

        Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();

    }

    private void JsonSignUpRequest(Context context, String n_identificacao, String nome, String senha, String email) {

        pb.setVisibility(View.VISIBLE);

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("n_identificacao", n_identificacao);
            jsonBody.put("nome", nome);
            jsonBody.put("senha", senha);
            jsonBody.put("cargo", "aluno");
            jsonBody.put("email", email);
            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/registar_utilizador";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleSignUpResponse(response, n_identificacao, senha);
                    pb.setVisibility(View.GONE);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();
                    et_pwd.setText("");
                    cb.setChecked(false);
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

    private void handleSignUpResponse(String json, String n_id, String pwd) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        SignUpResponse res = gson.fromJson(json, SignUpResponse.class);

        if(res.getCode() == 200) {

            Toast.makeText(this, "Sucesso", Toast.LENGTH_LONG).show();
            Preferences pref = new Preferences();

            pref.editStringPref(this, "n_id", n_id);

            pref.editStringPref(this, "pwd", pwd);

            this.finish();

        }else{

            Toast.makeText(this, "Erro ao registar.", Toast.LENGTH_LONG).show();
            et_pwd.setText("");
            cb.setChecked(false);

        }
    }
}