package com.example.baocao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "DB_SV.sqlite";
    SQLiteDatabase database;
    ListView listView;
    ArrayList<SinhVien> list;
    AdapterSinhVien adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        readData();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_option,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_add) {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }
        if (id == R.id.menu_cancel) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } 
        if (id == R.id.menu_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), R.string.logout_ok, Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    private void addControls() {
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new AdapterSinhVien(this, list);
        listView.setAdapter(adapter);

    }
    private void readData(){
        database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM SinhVien",null);
        list.clear();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            int id = cursor.getInt(0);
            String hoten = cursor.getString(1);
            int namsinh = cursor.getInt(3);
            String dienthoai = cursor.getString(2);
            byte[] hinhanh = cursor.getBlob(4);

            list.add(new SinhVien(id, hoten, namsinh, dienthoai, hinhanh));
        }
        adapter.notifyDataSetChanged();
    }

}