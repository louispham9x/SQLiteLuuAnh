package com.example.sqliteluuanh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ThemActivity extends AppCompatActivity {
    ImageView imgAnh;
    EditText edtTen, edtMoTa;
    ImageButton butCamera, butFloder;
    Button butThem, butHuy;
    final int REQUEST_CODE_CAM = 123;
    final int REQUEST_CODE_FLODER = 234;
    final  int REQUEST_CODE_PER_CAM = 345;
    final  int REQUEST_CODE_PER_FLODER = 456;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        XuLy();
    }

    private void XuLy() {
        imgAnh = (ImageView) findViewById(R.id.imgHinh);
        edtTen = (EditText) findViewById(R.id.editTextTen);
        butCamera = (ImageButton) findViewById(R.id.imageButtonCamera);
        butFloder =(ImageButton) findViewById(R.id.imageButtonFloder);
        edtMoTa = (EditText) findViewById(R.id.editTextMoTa);
        butThem = (Button) findViewById(R.id.buttonThemAnh);
        butHuy = (Button) findViewById(R.id.buttonHuy);
        butHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        butThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten = edtTen.getText().toString().trim();
                String mota = edtMoTa.getText().toString().trim();
                if (ten.equals("") || mota.equals("")){
                    Toast.makeText(ThemActivity.this, "Bạn vui lòng nhập đủ tên và mô tả", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Đổi ảnh thành byte[]
                    BitmapDrawable drawable = (BitmapDrawable) imgAnh.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] hinh = byteArrayOutputStream.toByteArray();
                    MainActivity.database.ThemAnh(ten, mota, hinh);
                    Toast.makeText(ThemActivity.this, "Đã thêm đồ vật", Toast.LENGTH_SHORT).show();
                    finish();
                    MainActivity.XuLy();
                }
            }
        });
        butCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ActivityCompat.requestPermissions(ThemActivity.this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAM);
            }
        });
        butFloder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ThemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PER_FLODER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAM && resultCode == RESULT_OK && data!= null)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgAnh.setImageBitmap(bitmap);
        }
        if (requestCode == REQUEST_CODE_FLODER && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAnh.setImageBitmap(bitmap);
                Log.d("AAA", "OK");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d("AAA", e.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PER_FLODER && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE_FLODER);
        }
        else
        {
            Toast.makeText(this, "Bạn không cho mở thư mục chọn ảnh", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_CODE_CAM && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CODE_CAM);
        }
        else
        {
            Toast.makeText(this, "Bạn không cho mở camera", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
