package com.ExeCute.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.interfaces.PdfDocumentActions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EquivalentHorizontal extends AppCompatActivity {

    double Kh, T, A, B, C, H1, H2, H3;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_T, m_T, mm_A, m_A, mm_B, m_B, mm_C, m_C, mm_H1, m_H1, mm_H2, m_H2, mm_H3, m_H3;
    boolean computed = false;
    String item;
    EditText num_Kh, num_T, num_A,num_B, num_C, num_H1, num_H2, num_H3;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equivalent_horizontal);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_equivalent_vertical);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquivalentHorizontal.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_Kh = new ArrayList<>();
        categories_Kh.add(0, "Choose");
        categories_Kh.add("cm/hr");
        categories_Kh.add("cm/day");
        categories_Kh.add("m/sec");
        categories_Kh.add("m/hr");
        categories_Kh.add("m/day");

        List<String> categories_T = new ArrayList<>();
        categories_T.add(0, "Choose");
        categories_T.add("mm");
        categories_T.add("m");

        List<String> categories_A = new ArrayList<>();
        categories_A.add(0, "Choose");
        categories_A.add("mm");
        categories_A.add("m");

        List<String> categories_B = new ArrayList<>();
        categories_B.add(0, "Choose");
        categories_B.add("mm");
        categories_B.add("m");

        List<String> categories_C = new ArrayList<>();
        categories_C.add(0, "Choose");
        categories_C.add("mm");
        categories_C.add("m");

        List<String> categories_H1 = new ArrayList<>();
        categories_H1.add(0, "Choose");
        categories_H1.add("mm");
        categories_H1.add("m");

        List<String> categories_H2 = new ArrayList<>();
        categories_H2.add(0, "Choose");
        categories_H2.add("mm");
        categories_H2.add("m");

        List<String> categories_H3 = new ArrayList<>();
        categories_H3.add(0, "Choose");
        categories_H3.add("mm");
        categories_H3.add("m");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_Kh;
        final ArrayAdapter<String> dataAdapter_T;
        final ArrayAdapter<String> dataAdapter_A;
        final ArrayAdapter<String> dataAdapter_B;
        final ArrayAdapter<String> dataAdapter_C;
        final ArrayAdapter<String> dataAdapter_H1;
        final ArrayAdapter<String> dataAdapter_H2;
        final ArrayAdapter<String> dataAdapter_H3;

        dataAdapter_Kh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Kh);
        dataAdapter_T = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_T);
        dataAdapter_A = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_A);
        dataAdapter_B = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_B);
        dataAdapter_C = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_C);
        dataAdapter_H1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H1);
        dataAdapter_H2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H2);
        dataAdapter_H3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H3);

        //Dropdown layout style
        dataAdapter_Kh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_T.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_A.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_B.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_C.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_Kh);
        spinner.setAdapter(dataAdapter_T);
        spinner.setAdapter(dataAdapter_A);
        spinner.setAdapter(dataAdapter_B);
        spinner.setAdapter(dataAdapter_C);
        spinner.setAdapter(dataAdapter_H1);
        spinner.setAdapter(dataAdapter_H2);
        spinner.setAdapter(dataAdapter_H3);

        num_Kh = findViewById(R.id.edit_text_Kh);
        num_T = findViewById(R.id.edit_text_T4);
        num_A = findViewById(R.id.edit_text_A5);
        num_B = findViewById(R.id.edit_text_B2);
        num_C = findViewById(R.id.edit_text_C2);
        num_H1 = findViewById(R.id.edit_text_H1_2);
        num_H2 = findViewById(R.id.edit_text_H2_2);
        num_H3 = findViewById(R.id.edit_text_H3_2);

        btn_compute =  findViewById(R.id.button_compute);
        missing = findViewById(R.id.text_view_missing);
        answer = findViewById(R.id.text_view_answer);
        converted = findViewById(R.id.text_view_converted_answer);
        //btn_clear =  findViewById(R.id.button_clear);
        btn_print =  findViewById(R.id.button_print);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_print.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(View v) {
                                createPDFFile(Common.getAppPath(EquivalentHorizontal.this)+"test_pdf.pdf");
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();


        btn_compute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                converted.setText(" ");

                if(num_Kh.getText().toString().equals("") && !num_T.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_B.getText().toString().equals("")
                        && !num_C.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")) {

                    computed = true;


                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());


                    Kh = ((A * H1) + (B * H2) + (C * H3)) / (T);
                    missing.setText("The missing variable is Kh");
                    answer.setText("Which has a value of : \n" + Kh  + " cm/sec");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_Kh);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                // item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "cm/sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("cm/hr")){
                                    cm_hr = Kh * (3600/Kh);
                                    converted.setText(cm_hr + " cm/hr");
                                } else if (item.equals("cm/day")){
                                    cm_day = Kh * 86400;
                                    converted.setText(cm_day + " cm/day");
                                } else if (item.equals("m/sec")){
                                    m_sec = Kh * (0.01/Kh);
                                    converted.setText(m_sec + " m/sec");
                                } else if (item.equals("m/hr")){
                                    m_hr = Kh * (36/Kh);
                                    converted.setText(m_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    m_day = Kh * 864;
                                    converted.setText(m_day + " m/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_T.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_B.getText().toString().equals("")
                        && !num_C.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    T = ((A * T) - (B * H2) + (C * H3)) / Kh;
                    T = Math.round(T);

                    missing.setText("The missing variable is T");
                    answer.setText("Which has a value of : " + T + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_T);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                //item = parent.getItemAtPosition(position).toString();
                                // Toast.makeText(parent.getContext(), "centimeter", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_T = T * 10;
                                    converted.setText(mm_T + " mm");
                                } else if (item.equals("m")){
                                    m_T = T / 100;
                                    converted.setText(m_T + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_A.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_B.getText().toString().equals("")
                        && !num_C.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    A = ((Kh * T) - (B * H2) - (C * H3)) / H1;
                    A = Math.round (A);
                    missing.setText("The missing variable is A");
                    answer.setText("Which has a value of : " + A + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_A);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_A = A * 10;
                                    converted.setText(mm_A + " mm");
                                } else if (item.equals("m")){
                                    m_A = A / 100;
                                    converted.setText(m_A + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_B.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_C.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    B = ((Kh * T - A * H1 -C * H3) / (H2));
                    B = Math.round (B);
                    missing.setText("The missing variable is B");
                    answer.setText("Which has a value of : " + B + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_B);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_B = B * 10;
                                    converted.setText(mm_B + " mm");
                                } else if (item.equals("m")){
                                    m_B = B / 100;
                                    converted.setText(m_B + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_C.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_B.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    C = ((Kh * T) - (A * H1) - (B * H2)) / H3;
                    C = Math.round (C);
                    missing.setText("The missing variable is C");
                    answer.setText("Which has a value of : " + C + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_C);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_C = C * 10;
                                    converted.setText(mm_C + " mm");
                                } else if (item.equals("m")){
                                    m_C = C / 100;
                                    converted.setText(m_C + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_H1.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_B.getText().toString().equals("") && !num_C.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    H1 = ((Kh * T) - (B * H2) - (C * H3)) / (A);
                    H1 = Math.pow(10, H1);
                    missing.setText("The missing variable is H1");
                    answer.setText("Which has a value of : " + H1 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_H1);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_H1 = H1 * 10;
                                    converted.setText(mm_H1 + " mm");
                                } else if (item.equals("m")){
                                    m_H1 = H1 / 100;
                                    converted.setText(m_H1 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_H2.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_B.getText().toString().equals("") && !num_C.getText().toString().equals("")
                        && !num_H1.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    H2 = ((Kh * T) - (A * H1)- (C * H3)) / (B);
                    H2 = Math.pow(10, H2);
                    missing.setText("The missing variable is H2");
                    answer.setText("Which has a value of : " + H2 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_H2);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_H2 = H2 * 10;
                                    converted.setText(mm_H2 + " mm");
                                } else if (item.equals("m")){
                                    m_H2 = H2 / 100;
                                    converted.setText(m_H2 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_H3.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_T.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_B.getText().toString().equals("") && !num_C.getText().toString().equals("")
                        && !num_H1.getText().toString().equals("") && !num_H2.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    T = Double.parseDouble(num_T.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    B = Double.parseDouble(num_B.getText().toString());
                    C = Double.parseDouble(num_C.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());

                    H3 = ((Kh * T) - (A * H1) + (B * H2)) / (C);
                    H3 = Math.pow( 10, H3);
                    missing.setText("The missing variable is H3");
                    answer.setText("Which has a value of : " + H3 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_H3);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_H3 = H3 * 10;
                                    converted.setText(mm_H3 + " mm");
                                } else if (item.equals("m")){
                                    m_H3 = H3 / 100;
                                    converted.setText(m_H3 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                }
                else {
                    computed = false;
                    missing.setText("Error! ");
                    answer.setText("Don't leave all the fields empty / Don't fill up all fields, just leave 1 empty field");
                    btn_print.setEnabled(computed);
                }
            }
        });

       /* btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computed = false;
                //num_i.setText(" ");
                num_i.setHint("Enter the value for i");
                //num_h.setText(" ");
                num_h.setHint("Enter the value for h");
                //num_L.setText(" ");
                num_L.setHint("Enter the value for L");
                missing.setText(" ");
                answer.setText(" ");

                btn_print.setEnabled(computed);
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPDFFile(String path) {
        if(new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            //Save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            //Open to write
            document.open();

            //Setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Duke Jimenez");
            document.addCreator("Carl Jason Lignes");

            //Font Setting
            BaseColor colorAccent = new BaseColor(0, 153, 204, 255);
            float fontSize = 26.0f;
            //float valueFontSize = 26.0f;

            //Custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8",BaseFont.EMBEDDED);

            //Create Title of Document
            Font title = new Font(fontName,36.6f,Font.NORMAL   ,BaseColor.BLACK);
            addNewItem(document,"Equivalent Horizontal", Element.ALIGN_CENTER,title);

            Font title_Kv = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Kv", Element.ALIGN_CENTER,title_Kv);

            //Add more
            Font value_Kv = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Kh.getText().toString(),Element.ALIGN_CENTER,value_Kv);

            Font title_T = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for T", Element.ALIGN_CENTER,title_T);

            Font value_T = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_T.getText().toString(),Element.ALIGN_CENTER,value_T);

            Font title_A = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for A", Element.ALIGN_CENTER,title_A);

            Font value_A = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_A.getText().toString(),Element.ALIGN_CENTER,value_A);

            Font title_B = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for B", Element.ALIGN_CENTER,title_B);

            Font value_B = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_B.getText().toString(),Element.ALIGN_CENTER,value_B);

            Font title_C = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for C", Element.ALIGN_CENTER,title_C);

            Font value_C = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_C.getText().toString(),Element.ALIGN_CENTER,value_C);

            Font title_H1 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H1", Element.ALIGN_CENTER,title_H1);

            Font value_H1 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H1.getText().toString(),Element.ALIGN_CENTER,value_H1);

            Font title_H2 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H2", Element.ALIGN_CENTER,title_H2);

            Font value_H2 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H2.getText().toString(),Element.ALIGN_CENTER,value_H2);

            Font title_H3 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H3", Element.ALIGN_CENTER,title_H3);

            Font value_H3 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H3.getText().toString(),Element.ALIGN_CENTER,value_H3);

            Font titleMissing = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,missing.getText().toString(),Element.ALIGN_CENTER,titleMissing);

            Font titleAnswer = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,answer.getText().toString(),Element.ALIGN_CENTER,titleAnswer);

            Font titleConverted = new Font(fontName,36.6f,Font.NORMAL   ,BaseColor.BLACK);
            addNewItem(document,"Converted to " + item, Element.ALIGN_CENTER,titleConverted);

            Font titleConvert = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,converted.getText().toString(),Element.ALIGN_CENTER,titleConvert);

            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

            printPDF();




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printPDF() {
        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(EquivalentHorizontal.this,Common.getAppPath(EquivalentHorizontal.this)+"test_pdf.pdf");
            printManager.print("Document",printDocumentAdapter,new PrintAttributes.Builder().build());

        } catch (Exception ex){
            Log.e("Duke",""+ex.getMessage());
        }

    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);

    }
}
