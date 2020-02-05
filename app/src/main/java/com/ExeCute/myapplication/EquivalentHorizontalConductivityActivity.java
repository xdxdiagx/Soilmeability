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

public class EquivalentHorizontalConductivityActivity extends AppCompatActivity {

    double Kh, H, K1, K2, K3, H1, H2, H3;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_H, m_H, mm_K1, m_K1, mm_K2, m_K2, mm_K3, m_K3, mm_H1, m_H1, mm_H2, m_H2, mm_H3, m_H3;
    boolean computed = false;
    String item;
    EditText num_Kh, num_H, num_K1,num_K2, num_K3, num_H1, num_H2, num_H3;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equivalent_horizontal_conductivity);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_equivalent_horizontal_conductivity);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EquivalentHorizontalConductivityActivity.this, FormulaActivity.class);
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

        List<String> categories_H = new ArrayList<>();
        categories_H.add(0, "Choose");
        categories_H.add("mm");
        categories_H.add("m");

        List<String> categories_K1 = new ArrayList<>();
        categories_K1.add(0, "Choose");
        categories_K1.add("mm");
        categories_K1.add("m");

        List<String> categories_K2 = new ArrayList<>();
        categories_K2.add(0, "Choose");
        categories_K2.add("mm");
        categories_K2.add("m");

        List<String> categories_K3 = new ArrayList<>();
        categories_K3.add(0, "Choose");
        categories_K3.add("mm");
        categories_K3.add("m");

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
        final ArrayAdapter<String> dataAdapter_H;
        final ArrayAdapter<String> dataAdapter_K1;
        final ArrayAdapter<String> dataAdapter_K2;
        final ArrayAdapter<String> dataAdapter_K3;
        final ArrayAdapter<String> dataAdapter_H1;
        final ArrayAdapter<String> dataAdapter_H2;
        final ArrayAdapter<String> dataAdapter_H3;

        dataAdapter_Kh = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Kh);
        dataAdapter_H = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H);
        dataAdapter_K1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K1);
        dataAdapter_K2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K2);
        dataAdapter_K3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K3);
        dataAdapter_H1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H1);
        dataAdapter_H2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H2);
        dataAdapter_H3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H3);

        //Dropdown layout style
        dataAdapter_Kh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_H3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_Kh);
        spinner.setAdapter(dataAdapter_H);
        spinner.setAdapter(dataAdapter_K1);
        spinner.setAdapter(dataAdapter_K2);
        spinner.setAdapter(dataAdapter_K3);
        spinner.setAdapter(dataAdapter_H1);
        spinner.setAdapter(dataAdapter_H2);
        spinner.setAdapter(dataAdapter_H3);

        num_Kh = findViewById(R.id.edit_text_Kh_2);
        num_H = findViewById(R.id.edit_text_H_2);
        num_K1 = findViewById(R.id.edit_text_K1_2);
        num_K2 = findViewById(R.id.edit_text_K2_2);
        num_K3 = findViewById(R.id.edit_text_K3_2);
        num_H1 = findViewById(R.id.edit_text_H1_3);
        num_H2 = findViewById(R.id.edit_text_H2_3);
        num_H3 = findViewById(R.id.edit_text_H3_3);

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
                                createPDFFile(Common.getAppPath(EquivalentHorizontalConductivityActivity.this)+"test_pdf.pdf");
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

                if(num_Kh.getText().toString().equals("") && !num_H.getText().toString().equals("")
                        && !num_K1.getText().toString().equals("") && !num_K2.getText().toString().equals("")
                        && !num_K3.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")) {

                    computed = true;


                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());


                    Kh = ((K1 * H1) + (K2 * H2) + (K3 * H3)) / (H);

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

                } else if(num_H.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_K1.getText().toString().equals("") && !num_K2.getText().toString().equals("")
                        && !num_K3.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    H = ((K1 * H1) + (K2 * H2) + (K3 * H3)) / (Kh);
                    H = Math.round(H);

                    missing.setText("The missing variable is H");
                    answer.setText("Which has a value of : " + H + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_H);

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
                                    mm_H = H * 10;
                                    converted.setText(mm_H + " mm");
                                } else if (item.equals("m")){
                                    m_H = H / 100;
                                    converted.setText(m_H + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_K1.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_H.getText().toString().equals("") && !num_K2.getText().toString().equals("")
                        && !num_K3.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    K1 = ((Kh * H) - (K2 * H2) - (K3 * H3)) / (H1);
                    K1 = Math.round (K1);
                    missing.setText("The missing variable is K1");
                    answer.setText("Which has a value of : " + K1 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_K1);

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
                                    mm_K1 = K1 * 10;
                                    converted.setText(mm_K1 + " mm");
                                } else if (item.equals("m")){
                                    m_K1 = K1 / 100;
                                    converted.setText(m_K1 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_K2.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_H.getText().toString().equals("") && !num_K1.getText().toString().equals("")
                        && !num_K3.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    K2 = (Kh * - H - K1 * H1 - K3 * H3) / (H2);
                    K2 = Math.round (K2);
                    missing.setText("The missing variable is K2");
                    answer.setText("Which has a value of : " + K2 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_K2);

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
                                    mm_K2 = K2 * 10;
                                    converted.setText(mm_K2 + " mm");
                                } else if (item.equals("m")){
                                    m_K2 = K2 / 100;
                                    converted.setText(m_K2 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_K3.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_H.getText().toString().equals("") && !num_K1.getText().toString().equals("")
                        && !num_K2.getText().toString().equals("") && !num_H1.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    K3 = ((Kh * H) - (K1 * H1) - (K2 * H2)) / (H3);
                    K3 = Math.round (K3);
                    missing.setText("The missing variable is K3");
                    answer.setText("Which has a value of : " + K3 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_K3);

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
                                    mm_K3 = K3 * 10;
                                    converted.setText(mm_K3 + " mm");
                                } else if (item.equals("m")){
                                    m_K3 = K3 / 100;
                                    converted.setText(m_K3 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_H1.getText().toString().equals("") && !num_Kh.getText().toString().equals("")
                        && !num_H.getText().toString().equals("") && !num_K1.getText().toString().equals("")
                        && !num_K2.getText().toString().equals("") && !num_K3.getText().toString().equals("")
                        && !num_H2.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    H1 = ((Kh * H) - (K2 * H2) - (K3 * H3)) / (K1);
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
                        && !num_H.getText().toString().equals("") && !num_K1.getText().toString().equals("")
                        && !num_K2.getText().toString().equals("") && !num_K3.getText().toString().equals("")
                        && !num_H1.getText().toString().equals("") && !num_H3.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H3 = Double.parseDouble(num_H3.getText().toString());

                    H2 = ((Kh * H) - (K1 * H1) - (K3 * H3)) / (K2);
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
                        && !num_H.getText().toString().equals("") && !num_K1.getText().toString().equals("")
                        && !num_K2.getText().toString().equals("") && !num_K3.getText().toString().equals("")
                        && !num_H1.getText().toString().equals("") && !num_H2.getText().toString().equals("")){

                    computed = true;

                    Kh = Double.parseDouble(num_Kh.getText().toString());
                    H = Double.parseDouble(num_H.getText().toString());
                    K1 = Double.parseDouble(num_K1.getText().toString());
                    K2 = Double.parseDouble(num_K2.getText().toString());
                    K3 = Double.parseDouble(num_K3.getText().toString());
                    H1 = Double.parseDouble(num_H1.getText().toString());
                    H2 = Double.parseDouble(num_H2.getText().toString());

                    H3 = ((Kh * H) - (K1 * H1) - (K2 * H2)) / (K3);
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
            addNewItem(document,"Equivalent Horizontal Conductivity", Element.ALIGN_CENTER,title);

            Font title_Kh = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Kh", Element.ALIGN_CENTER,title_Kh);

            //Add more
            Font value_Kh = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Kh.getText().toString(),Element.ALIGN_CENTER,value_Kh);

            Font title_H = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for H", Element.ALIGN_CENTER,title_H);

            Font value_H = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_H.getText().toString(),Element.ALIGN_CENTER,value_H);

            Font title_K1 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K1", Element.ALIGN_CENTER,title_K1);

            Font value_K1 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K1.getText().toString(),Element.ALIGN_CENTER,value_K1);

            Font title_K2 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K2", Element.ALIGN_CENTER,title_K2);

            Font value_K2 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K2.getText().toString(),Element.ALIGN_CENTER,value_K2);

            Font title_K3 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K3", Element.ALIGN_CENTER,title_K3);

            Font value_K3 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K3.getText().toString(),Element.ALIGN_CENTER,value_K3);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(EquivalentHorizontalConductivityActivity.this,Common.getAppPath(EquivalentHorizontalConductivityActivity.this)+"test_pdf.pdf");
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
