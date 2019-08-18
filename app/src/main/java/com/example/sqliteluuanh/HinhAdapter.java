package com.example.sqliteluuanh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HinhAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Hinh> list;

    public HinhAdapter(Context context, int layout, ArrayList<Hinh> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }
    private class Holder
    {
        ImageView imgHinh;
        TextView txtTen, txtMoTa;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder = new Holder();
            holder.imgHinh = view.findViewById(R.id.imageViewHinh);
            holder.txtTen = view.findViewById(R.id.TenHinh);
            holder.txtMoTa = view.findViewById(R.id.MoTaHinh);
            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }
        Hinh hinh = list.get(i);
        holder.txtTen.setText(hinh.getTen());
        holder.txtMoTa.setText(hinh.getMota());
        //chuyển byte[] -> bitmap
        byte[] hinhanh = hinh.getHinh();
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
        holder.imgHinh.setImageBitmap(bitmap);
        return view;
    }
}
