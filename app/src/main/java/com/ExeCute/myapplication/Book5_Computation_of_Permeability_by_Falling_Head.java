package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book5_Computation_of_Permeability_by_Falling_Head extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book5__computation_of__permeability_by__falling__head);

        PDFView book5 = findViewById(R.id.pdf_book5);

        book5.fromAsset("Book5.pdf").load();
    }
}
