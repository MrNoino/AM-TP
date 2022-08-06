package com.example.canteen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

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
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class RecyclerViewAdapterMenus extends RecyclerView.Adapter<RecyclerViewAdapterMenus.RecyclerViewHolder>{

    private ArrayList<MealsResponse> mMenus;
    private LayoutInflater mInflater;
    private Point mPoint;

    private DecimalFormat df = new DecimalFormat("0.00");

    public RecyclerViewAdapterMenus(Context context, Point point, ArrayList<MealsResponse> arr) {
        mPoint = point;
        mInflater = LayoutInflater.from(context);
        this.mMenus = arr;

    }

    @Override
    public RecyclerViewAdapterMenus.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.menu_item, parent, false);
        return new RecyclerViewHolder(mItemView, this);

    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterMenus.RecyclerViewHolder holder, int position) {

        if(mMenus.get(position).getSopa().isEmpty())
            holder.mSoup.setText(R.string.label_empty);
        else
            holder.mSoup.setText(mMenus.get(position).getSopa());

        if(mMenus.get(position).getPrato().isEmpty())
            holder.mMain.setText(R.string.label_empty);
        else
            holder.mMain.setText(mMenus.get(position).getPrato());

        if(mMenus.get(position).getTipo().equals("Vegetariano")){

            holder.mMain_label.setText(R.string.label_vegOption);
            holder.mMain_label.setTextColor(Color.parseColor("#228B22"));

        }else if(mMenus.get(position).getTipo().equals("Carne")){

            holder.mMain_label.setText(R.string.label_meatOption);
            holder.mMain_label.setTextColor(Color.parseColor("#B22222"));

        }else if(mMenus.get(position).getTipo().equals("Peixe")){

            holder.mMain_label.setText(R.string.label_fishOption);
            holder.mMain_label.setTextColor(Color.parseColor("#48D1CC"));

        }

        if(mMenus.get(position).getSobremesa().isEmpty())
            holder.mDessert.setText(R.string.label_empty);
        else
            holder.mDessert.setText(mMenus.get(position).getSobremesa());


        holder.mPrice.setText(String.valueOf(df.format(mMenus.get(position).getPreco()) + "€"));

        holder.mBtnCancel.setEnabled(true);
        holder.mBtnCancel.setBackgroundColor(Color.parseColor("#DAA520"));

        holder.mBtnBuy.setEnabled(true);
        holder.mBtnBuy.setBackgroundColor(Color.parseColor("#228B22"));

        holder.mQrCode_imgview.setImageBitmap(null);

        if(mMenus.get(position).isComprado()){

            holder.mBtnBuy.setEnabled(false);
            holder.mBtnBuy.setBackgroundColor(Color.parseColor("#A9A9A9"));
            generateQrCode(holder.itemView.getContext(), mMenus.get(position).getId_reserva(), holder.mQrCode_imgview);

        } else{

            holder.mBtnCancel.setEnabled(false);
            holder.mBtnCancel.setBackgroundColor(Color.parseColor("#A9A9A9"));
        }

        holder.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mMenus.get(position).isComprado()){

                    Toast.makeText(v.getContext(), "Ementa ainda não foi reservada.", Toast.LENGTH_LONG).show();
                    return;
                }

                Preferences pref = new Preferences();

                JsonRequestCancelMenu(v.getContext(), mMenus.get(position).getId_reserva(), pref.getStringPref(v.getContext(), "token"), position, holder.mBtnCancel, holder.mBtnBuy, holder.mQrCode_imgview);

                mMenus.get(position).setId_reserva(0);



            }

        });

        holder.mBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mMenus.get(position).isComprado()){

                    Toast.makeText(v.getContext(), "Ementa já comprada.", Toast.LENGTH_LONG).show();
                    return;
                }


                Preferences pref = new Preferences();

                JsonRequestBuyMenu(v.getContext(), mMenus.get(position).getId(), pref.getStringPref(v.getContext(), "token"), position, holder.mBtnCancel, holder.mBtnBuy, holder.mQrCode_imgview);

            }

        });


    }

    @Override
    public int getItemCount() {

        return mMenus.size();

    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        RecyclerViewAdapterMenus mAdapter;
        public TextView mSoup;
        public TextView mMain;
        public TextView mMain_label;
        public TextView mDessert;
        public TextView mPrice;
        public Button mBtnCancel;
        public Button mBtnBuy;
        public ImageView mQrCode_imgview;


        public RecyclerViewHolder(View itemView, RecyclerViewAdapterMenus adapter) {

            super(itemView);

            this.mAdapter = adapter;
            mSoup = itemView.findViewById(R.id.txtview_soup);
            mMain = itemView.findViewById(R.id.txtview_main);
            mMain_label = itemView.findViewById(R.id.txtview_main_label);
            mDessert = itemView.findViewById(R.id.txtview_dessert);
            mPrice = itemView.findViewById(R.id.txtview_price);
            mBtnCancel = itemView.findViewById(R.id.btn_cancel);
            mBtnBuy = itemView.findViewById(R.id.btn_buy);
            mQrCode_imgview = itemView.findViewById(R.id.imgview_qrcode);

        }

    }

    private void JsonRequestBuyMenu(Context context, int registo_ementas_id_registo, String token, int position, Button cancel, Button buy, ImageView qrcode_imgview) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("registo_ementas_id_registo", registo_ementas_id_registo);
            jsonBody.put("token", token);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/comprar_ementa";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleBuy(context, response, position, cancel, buy, qrcode_imgview);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();

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

    private void handleBuy(Context context, String json, int position, Button cancel, Button buy , ImageView qrcode_imgview) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        BuyResponse res = gson.fromJson(json, BuyResponse.class);

        if(res.getCode() != 200){
            Toast.makeText(context, "Erro na operação", Toast.LENGTH_LONG).show();
            return;
        }else {

            Toast.makeText(context, "Sucesso", Toast.LENGTH_LONG).show();
            mMenus.get(position).setId_reserva(res.getId_reserva());
            mMenus.get(position).setComprado(true);
            buy.setEnabled(false);
            buy.setBackgroundColor(Color.parseColor("#A9A9A9"));
            cancel.setEnabled(true);
            cancel.setBackgroundColor(Color.parseColor("#DAA520"));

            generateQrCode(context, res.getId_reserva(), qrcode_imgview);

        }

    }

    private void JsonRequestCancelMenu(Context context, int id_reserva, String token, int position, Button cancel, Button buy, ImageView qrcode_imgview) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("token", token);
            jsonBody.put("id_reserva", id_reserva);

            final String mRequestBody = jsonBody.toString();

            String url = "https://cantina-postgres.herokuapp.com/anular_ementa_registada";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);
                    handleCancel(context, response, position, cancel, buy, qrcode_imgview);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());
                    Toast.makeText(context, "Erro na conexão.", Toast.LENGTH_LONG).show();

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

    private void handleCancel(Context context, String json, int position, Button cancel, Button buy, ImageView qrcode_imgview) {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        CancelResponse res = gson.fromJson(json, CancelResponse.class);

        if(res.getCode() == 200) {

            Toast.makeText(context, "Sucesso", Toast.LENGTH_LONG).show();
            cancel.setEnabled(false);
            cancel.setBackgroundColor(Color.parseColor("#A9A9A9"));
            mMenus.get(position).setId_reserva(0);
            mMenus.get(position).setComprado(false);
            buy.setEnabled(true);
            buy.setBackgroundColor(Color.parseColor("#228B22"));
            qrcode_imgview.setImageBitmap(null);

        }
        else
            Toast.makeText(context, res.getErro(), Toast.LENGTH_LONG).show();

    }

    private void generateQrCode(Context context, int id, ImageView qrcode_imgview){

        // getting width and
        // height of a point
        int width = mPoint.x;
        int height = mPoint.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 2 / 5;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        QRGEncoder qrgEncoder = new QRGEncoder(String.valueOf(id), null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrcode_imgview.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

    }
}
