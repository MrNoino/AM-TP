package com.example.canteen;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class RecyclerViewAdapterExtract extends RecyclerView.Adapter<RecyclerViewAdapterExtract.ViewHolder> {

    private final ArrayList<ExtractResponse> mMeals;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat parse = new SimpleDateFormat("dd-MM-yyyy\nHH:mm:ss", Locale.getDefault());


    public RecyclerViewAdapterExtract(ArrayList<ExtractResponse> ar) {

        this.mMeals = ar;

    }

    @Override
    public RecyclerViewAdapterExtract.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.extract_framelayout, parent, false);

        RecyclerViewAdapterExtract.ViewHolder holder = new RecyclerViewAdapterExtract.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterExtract.ViewHolder holder, int position) {

        String operation = "Comprou" + " " + mMeals.get(position).getTipodeRefeicao();
        holder.mOperation.setText(operation);
        DecimalFormat df = new DecimalFormat("0.00");
        String cost = df.format(mMeals.get(position).getPreco()) + "€";
        holder.mCost.setText(cost);

        Date date = null;
        try {
            date = format.parse(mMeals.get(position).getDataCompra());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        holder.mDate.setText(parse.format(date));

    }

    @Override
    public int getItemCount() {
        return mMeals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        FrameLayout extract_layout;
        TextView mOperation;
        TextView mCost;
        TextView mDate;

        public ViewHolder(View itemView) {
            super(itemView);
            extract_layout = itemView.findViewById(R.id.extract_layout);
            mOperation = itemView.findViewById(R.id.txtview_operation);
            mCost = itemView.findViewById(R.id.txtview_cost);
            mDate = itemView.findViewById(R.id.txtview_date);

        }
    }
}

