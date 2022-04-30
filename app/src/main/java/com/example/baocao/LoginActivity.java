package com.example.baocao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    final String DATABASE_NAME = "Login.sqlite";

    int id;
    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button Login = (Button) findViewById(R.id.login);
        EditText edtuser = (EditText) findViewById(R.id.username);
        EditText edtpass = (EditText) findViewById(R.id.password);
        database = Database.initDatabase(this, DATABASE_NAME);
        Button btnSingUp= (Button) findViewById(R.id.btnSign_Up);
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        Login.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Cursor cursor = database.rawQuery("SELECT * FROM UserName", null);
                String user;
                String pass;

                String User = edtuser.getText().toString();
                String Pass = edtpass.getText().toString();

                for(int i = 0; i < cursor.getCount(); i++){
                    cursor.moveToPosition(i);
                    user=cursor.getString(1);
                    pass=cursor.getString(2);
                    if (User.equalsIgnoreCase(user) && Pass.equalsIgnoreCase(pass)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), R.string.login_ok, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(i==cursor.getCount()-1){
                        TextView btnerr = (TextView) findViewById(R.id.err);
                        btnerr.setText(R.string.login_errol);

                    }


                }
            }
        });
    }

    public void userOnclick(View view) {
        TextView btnerr = (TextView) findViewById(R.id.err);
        btnerr.setText("");
    }

    public void passwordOnclick(View view) {
        TextView btnerr = (TextView) findViewById(R.id.err);
        btnerr.setText("");
    }
}

