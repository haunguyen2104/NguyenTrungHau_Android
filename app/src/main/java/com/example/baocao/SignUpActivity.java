package com.example.baocao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    final String DATABASE_NAME = "Login.sqlite";
    SQLiteDatabase database;
    Button btnCancel, btnSignUp;
    EditText edtMasv, edtUsername, edtPassword, edtxnPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        database = Database.initDatabase(this, DATABASE_NAME);

        btnCancel = findViewById(R.id.btnCancel);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtMasv = findViewById(R.id.edtMaSv);
        edtUsername = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPass);
        edtxnPassword = findViewById(R.id.edtxnPass);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masv = edtMasv.getText().toString();
                String user = edtUsername.getText().toString();
                String pass = edtPassword.getText().toString();
                String repass = edtxnPassword.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")||masv.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Hãy điền đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pass.equals(repass)) {
                        Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor cursor = database.rawQuery("select * from UserName where User = ?", new String[]{user});
                        if (cursor.getCount() > 0) {
                            Toast.makeText(SignUpActivity.this, "Tên tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            Cursor sv = database.rawQuery("SELECT * FROM UserName WHERE Ma = ?", new String[]{masv});
                            if (sv.getCount() > 0) {
                                Toast.makeText(SignUpActivity.this, "Sinh viên đã tồn tại.", Toast.LENGTH_LONG).show();
                            } else {
                                ContentValues row = new ContentValues();
                                row.put("User", user);
                                row.put("Password", pass);
                                long r = database.insert("UserName", null, row);
                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        });
    }


}