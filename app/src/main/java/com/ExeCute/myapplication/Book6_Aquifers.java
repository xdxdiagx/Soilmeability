package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book6_Aquifers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book6__aquifers);

        PDFView book6 = findViewById(R.id.pdf_book6);

        book6.fromAsset("Book5.pdf").load();
    }
}
