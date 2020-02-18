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

public class EquivalentHorizontalActivity extends AppCompatActivity {

    double Kh, T, A, B, C, H1, H2, H3;
    double Kh_min,Kh_hr, Kh_day,A_min, A_hr, A_day, B_min, B_hr, B_day, C_min, C_hr, C_day, T_m, T_mm, T_km, H1_m, H1_mm, H1_km,  H2_m, H2_mm, H2_km,  H3_m, H3_mm, H3_km;
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
        setContentView(R.layout.activity_equivalent_horizontal2);

        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_equivalent_horizontal);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquivalentHorizontalActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_Kh = new ArrayList<>();
        categories_Kh.add(0, "Choose");
        categories_Kh.add("m/min");
        categories_Kh.add("m/hr");
        categories_Kh.add("m/day");

        List<String> categories_T = new ArrayList<>();
        categories_T.add(0, "Choose");
        categories_T.add("m");
        categories_T.add("mm");
        categories_T.add("km");

        List<String> categories_A = new ArrayList<>();
        categories_A.add(0, "Choose");
        categories_A.add("m/min");
        categories_A.add("m/hr");
        categories_A.add("m/day");

        List<String> categories_B = new ArrayList<>();
        categories_B.add(0, "Choose");
        categories_B.add("m/min");
        categories_B.add("m/hr");
        categories_B.add("m/day");

        List<String> categories_C = new ArrayList<>();
        categories_C.add(0, "Choose");
        categories_C.add("m/min");
        categories_C.add("m/hr");
        categories_C.add("m/day");

        List<String> categories_H1 = new ArrayList<>();
        categories_H1.add(0, "Choose");
        categories_H1.add("m");
        categories_H1.add("mm");
        categories_H1.add("km");

        List<String> categories_H2 = new ArrayList<>();
        categories_H2.add(0, "Choose");
        categories_H2.add("m");
        categories_H2.add("mm");
        categories_H2.add("km");

        List<String> categories_H3 = new ArrayList<>();
        categories_H3.add(0, "Choose");
        categories_H3.add("m");
        categories_H3.add("mm");
        categories_H3.add("km");

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
                                createPDFFile(Common.getAppPath(EquivalentHorizontalActivity.this)+"test_pdf.pdf");
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
                                if(item.equals("m/min")){
                                    Kh_min = Kh * 0.6;
                                    converted.setText(Kh_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    Kh_hr = Kh * 3600;
                                    converted.setText(Kh_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    Kh_day = Kh * 864;
                                    converted.setText(Kh_day + " m/day");
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

                    T = ((A * H1) + (B * H2) + (C * H3)) / Kh;

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
                                if(item.equals("m")){
                                    T_m = T * 100;
                                    converted.setText(T_m + " m");
                                } else if (item.equals("mm")){
                                    T_mm = T * 10;
                                    converted.setText(T_mm + " mm");
                                } else if (item.equals("km")){
                                    T_km = T * 100000;
                                    converted.setText(T_km + " km");
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
                    missing.setText("The missing variable is A");
                    answer.setText("Which has a value of : " + A + " cm/sec");


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
                                if(item.equals("m/min")){
                                    A_min = A * 0.6;
                                    converted.setText(A_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    A_hr = A * 3600;
                                    converted.setText(A_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    A_day = A * 864;
                                    converted.setText(A_day + " m/day");
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

                    B = ((Kh * T - A * H1 - C * H3) / (H2));
                    missing.setText("The missing variable is B");
                    answer.setText("Which has a value of : " + B + " cm/sec");

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
                                if(item.equals("m/min")){
                                    B_min = B * 0.6;
                                    converted.setText(B_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    B_hr = B * 3600;
                                    converted.setText(B_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    B_day = B * 864;
                                    converted.setText(B_day + " m/day");
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
                    missing.setText("The missing variable is C");
                    answer.setText("Which has a value of : " + C + " cm/sec");

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
                                if(item.equals("m/min")){
                                    C_min = C * 0.6;
                                    converted.setText(C_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    C_hr = C * 3600;
                                    converted.setText(C_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    C_day = C * 864;
                                    converted.setText(C_day + " m/day");
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
                    missing.setText("The missing variable is H1");
                    answer.setText("Which has a value of : " + H1 + " cm");

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
                                if(item.equals("m")){
                                    H1_m = H1 * 100;
                                    converted.setText(H1_m + " mm");
                                } else if (item.equals("mm")){
                                    H1_mm = H1 * 10;
                                    converted.setText(H1_mm + " m");
                                } else if (item.equals("km")){
                                    H1_km = H1 * 100000;
                                    converted.setText(H1_km + " m");
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
                    missing.setText("The missing variable is H2");
                    answer.setText("Which has a value of : " + H2 + " cm");

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
                                if(item.equals("m")){
                                    H2_m = H2 * 100;
                                    converted.setText(H2_m + " mm");
                                } else if (item.equals("mm")){
                                    H2_mm = H2 * 10;
                                    converted.setText(H2_mm + " m");
                                } else if (item.equals("km")){
                                    H2_km = H2 * 100000;
                                    converted.setText(H2_km + " m");
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

                    H3 = ((Kh * T) - (A * H1) - (B * H2)) / (C);
                    missing.setText("The missing variable is H3");
                    answer.setText("Which has a value of : " + H3 + " cm");

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
                                if(item.equals("m")){
                                    H3_m = H3 * 100;
                                    converted.setText(H3_m + " mm");
                                } else if (item.equals("mm")){
                                    H3_mm = H3 * 10;
                                    converted.setText(H3_mm + " m");
                                } else if (item.equals("km")){
                                    H3_km = H3 * 100000;
                                    converted.setText(H3_km + " m");
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

            Font title_Kv = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Kv", Element.ALIGN_CENTER,title_Kv);

            //Add more
            Font value_Kv = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Kh.getText().toString(),Element.ALIGN_CENTER,value_Kv);

            Font title_T = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for T", Element.ALIGN_CENTER,title_T);

            Font value_T = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_T.getText().toString(),Element.ALIGN_CENTER,value_T);

            Font title_A = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for A", Element.ALIGN_CENTER,title_A);

            Font value_A = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_A.getText().toString(),Element.ALIGN_CENTER,value_A);

            Font title_B = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for B", Element.ALIGN_CENTER,title_B);

            Font value_B = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_B.getText().toString(),Element.ALIGN_CENTER,value_B);

            Font title_C = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for C", Element.ALIGN_CENTER,title_C);

            Font value_C = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_C.getText().toString(),Element.ALIGN_CENTER,value_C);

            Font title_H1 = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H1", Element.ALIGN_CENTER,title_H1);

            Font value_H1 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H1.getText().toString(),Element.ALIGN_CENTER,value_H1);

            Font title_H2 = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H2", Element.ALIGN_CENTER,title_H2);

            Font value_H2 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H2.getText().toString(),Element.ALIGN_CENTER,value_H2);

            Font title_H3 = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H3", Element.ALIGN_CENTER,title_H3);

            Font value_H3 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H3.getText().toString(),Element.ALIGN_CENTER,value_H3);

            Font titleMissing = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,missing.getText().toString(),Element.ALIGN_CENTER,titleMissing);

            Font titleAnswer = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,answer.getText().toString(),Element.ALIGN_CENTER,titleAnswer);

            Font titleConverted = new Font(fontName,30.0f,Font.NORMAL   ,BaseColor.BLACK);
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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(EquivalentHorizontalActivity.this,Common.getAppPath(EquivalentHorizontalActivity.this)+"test_pdf.pdf");
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
