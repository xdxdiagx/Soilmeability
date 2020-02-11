package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book4_Discharge_Velocity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book4__discharge__velocity);

        PDFView book4 = findViewById(R.id.pdf_book4);

        book4.fromAsset("Book4.pdf").load();
    }
}
