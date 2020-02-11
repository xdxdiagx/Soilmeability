package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class Book8_Field_Determination_of_Permiability extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book8__field__determination_of__permiability);

        PDFView book8 = findViewById(R.id.pdf_book8);

        book8.fromAsset("Book8.pdf").load();
    }
}
