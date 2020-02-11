package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book10_Transmissivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book10__transmissivity);

        PDFView book10 = findViewById(R.id.pdf_book10);

        book10.fromAsset("Book10.pdf").load();
    }
}
