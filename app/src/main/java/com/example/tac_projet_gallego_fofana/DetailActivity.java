package com.example.tac_projet_gallego_fofana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_layout_detail);
        Toast.makeText(this, "on create detail", Toast.LENGTH_SHORT).show();

        TextView textView = findViewById(R.id.itemDetailMovieTitle);

        Intent intent = getIntent();
        textView.setText(intent.getStringExtra("MOVIE_TITLE"));
    }

    public void navMainLayout(View view) {
        //Goto mainLayout
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
