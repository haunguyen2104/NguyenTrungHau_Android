package com.example.baocao;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final String DATABASE_NAME = "DB_SV.sqlite";
    int id = -1;

    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;


    Button btnLuu, btnHuy, btnadd, btnchonanh, btnchupanh;
    EditText edtTen, edtSdt, edtnamsinh;
    ImageView imgHinhDaiDien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        addControls();
        addEvents();
        initUI();
    }

    private void addControls() {
        btnchonanh = (Button) findViewById(R.id.btnchonanh);
        btnchupanh = (Button) findViewById(R.id.btnchupanh);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);
        edtTen = (EditText) findViewById(R.id.edtten);
        edtSdt = (EditText) findViewById(R.id.edtSdt);
        edtnamsinh = (EditText) findViewById(R.id.edtnamsinh);
        imgHinhDaiDien = (ImageView) findViewById(R.id.imgHinhDaiDien);
    }

    private void addEvents() {

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
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

    private void initUI() {
        Intent intent = getIntent();
        id = intent.getIntExtra("MaSV", -1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM SinhVien WHERE MaSV= ? ", new String[]{id + ""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        String namsinh = cursor.getString(3);
        byte[] HinhAnh = cursor.getBlob(4);

        Bitmap bitmap = BitmapFactory.decodeByteArray(HinhAnh, 0, HinhAnh.length);

        edtSdt.setText(sdt);
        edtTen.setText(ten);
        edtnamsinh.setText(namsinh);
        imgHinhDaiDien.setImageBitmap(bitmap);

    }

    private void update() {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        String namsinh = edtnamsinh.getText().toString();
        byte[] HinhAnh = getByteArrayFromImageView(imgHinhDaiDien);
        if (ten.equals("") || sdt.equals("") || namsinh.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.add_errol, Toast.LENGTH_SHORT).show();
        } else {

            ContentValues contentValues = new ContentValues();
            contentValues.put("HoTen", ten);
            contentValues.put("DienThoai", sdt);
            contentValues.put("NamSinh", namsinh);
            contentValues.put("HinhAnh", HinhAnh);

            SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
            database.update("SinhVien", contentValues, "MaSv = ?", new String[]{id + ""});
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), R.string.update_ok, Toast.LENGTH_SHORT).show();
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

    public byte[] getByteArrayFromImageView(ImageView imgv) {

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}

