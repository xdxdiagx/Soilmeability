package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book9_Permeability extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book9__permeability);

        PDFView book9 = findViewById(R.id.pdf_book9);

        book9.fromAsset("Book9.pdf").load();
    }
}
