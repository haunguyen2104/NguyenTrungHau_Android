package com.example.baocao;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {
    final String DATABASE_NAME = "DB_SV.sqlite";
    int id = -1;

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    Button btnThem, btnHuy, btnchonanh, btnchupanh;
    EditText edtTen, edtSdt, edtId, edtold;
    ImageView imgHinhDaiDien;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnchonanh = (Button) findViewById(R.id.btnchonanh);
        btnchupanh = (Button) findViewById(R.id.btnchupanh);
        btnThem = (Button) findViewById(R.id.btnThemm);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtTen = (EditText) findViewById(R.id.edtTen);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtId = (EditText) findViewById(R.id.edtid);
        edtold = (EditText) findViewById(R.id.edtOld);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);
    }

    private void addEvents() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        btnchonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        btnchupanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    private void insert() {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        String id = edtId.getText().toString();
        String Old = edtold.getText().toString();
        byte[] HinhAnh = getByteArrayFromImageView(imgHinhDaiDien);

        if (ten.isEmpty() || sdt.isEmpty() || id.isEmpty() || Old.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.add_errol, Toast.LENGTH_LONG).show();

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("MASV", id);
            contentValues.put("HoTen", ten);
            contentValues.put("NamSinh", Old);
            contentValues.put("DienThoai", sdt);
            contentValues.put("HinhAnh", HinhAnh);

            SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);

            database.insert("SinhVien", null, contentValues);
            Log.e("Contact Entered", "DATABASE");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), R.string.add_ok, Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    public byte[] getByteArrayFromImageView(ImageView imgv) {

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {


                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgHinhDaiDien.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinhDaiDien.setImageBitmap(bitmap);

            }
        }
    }

}