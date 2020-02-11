package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book12_Seepage_and_Soil_Permeability extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book12__seepage_and__soil__permeability);

        PDFView book12 = findViewById(R.id.pdf_book12);

        book12.fromAsset("Book12.pdf").load();
    }
}
