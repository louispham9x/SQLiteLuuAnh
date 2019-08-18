package com.example.sqliteluuanh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button butThem;
    ListView listDV;
   public static HinhAdapter adapter;
    public static ArrayList<Hinh> list;
    public static Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butThem = (Button) findViewById(R.id.buttonThem);
        listDV = (ListView) findViewById(R.id.listDoVat);
        list = new ArrayList<>();
        adapter = new HinhAdapter(MainActivity.this, R.layout.dong_anh, list);
        listDV.setAdapter(adapter);
        butThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThemActivity.class);
                startActivity(intent);
            }
        });
        database = new Database(MainActivity.this, "LuuAnh.sqlite", null, 1);
        database.TruyVan("CREATE TABLE IF NOT EXISTS DoVat ( Id INTEGER PRIMARY KEY AUTOINCREMENT, Ten VARCHAR(200), MoTa VARCHAR(200), Hinh BLOB )");
        XuLy();
    }

    public static void XuLy() {
        Cursor cursor = database.LayData("SELECT * FROM DoVat");
        list.clear();
        while (cursor.moveToNext())
        {
            String ten = cursor.getString(1);
            String mota = cursor.getString(2);
            int id = cursor.getInt(0);
            byte[] hinh = cursor.getBlob(3);
            list.add(new Hinh(id, ten, mota, hinh));
        }
        adapter.notifyDataSetChanged();
    }
}
