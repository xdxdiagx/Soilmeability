package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book7_Aquifers_and_Groundwater extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book7__aquifers_and__groundwater);

        PDFView book7 = findViewById(R.id.pdf_book7);

        book7.fromAsset("Book7.pdf").load();
    }
}
