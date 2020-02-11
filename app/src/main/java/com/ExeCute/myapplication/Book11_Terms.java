package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book11_Terms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book11__terms);

        PDFView book11 = findViewById(R.id.pdf_book11);

        book11.fromAsset("Book11.pdf").load();
    }
}
