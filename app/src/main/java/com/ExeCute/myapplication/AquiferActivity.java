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

public class AquiferActivity extends AppCompatActivity {

    double K, Q, logr1, r2, t, h1, h2;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_K, m_K, mm_Q, m_Q, mm_logr1, m_logr1, mm_r2, m_r2, mm_t, m_t, mm_h1, m_h1, mm_h2, m_h2;
    boolean computed = false;
    String item;
    EditText num_K, num_Q, num_logr1,num_r2, num_t, num_h1, num_h2;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquifer);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_aquifer);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AquiferActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("cm/hr");
        categories_K.add("cm/day");
        categories_K.add("m/sec");
        categories_K.add("m/hr");
        categories_K.add("m/day");

        List<String> categories_Q = new ArrayList<>();
        categories_Q.add(0, "Choose");
        categories_Q.add("mm");
        categories_Q.add("m");


        List<String> categories_logr1 = new ArrayList<>();
        categories_logr1.add(0, "Choose");
        categories_logr1.add("mm");
        categories_logr1.add("m");

        List<String> categories_r2 = new ArrayList<>();
        categories_r2.add(0, "Choose");
        categories_r2.add("mm");
        categories_r2.add("m");

        List<String> categories_t = new ArrayList<>();
        categories_t.add(0, "Choose");
        categories_t.add("mm");
        categories_t.add("m");

        List<String> categories_h1 = new ArrayList<>();
        categories_h1.add(0, "Choose");
        categories_h1.add("mm");
        categories_h1.add("m");

        List<String> categories_h2 = new ArrayList<>();
        categories_h2.add(0, "Choose");
        categories_h2.add("mm");
        categories_h2.add("m");


        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_K;
        final ArrayAdapter<String> dataAdapter_Q;
        final ArrayAdapter<String> dataAdapter_logr1;
        final ArrayAdapter<String> dataAdapter_r2;
        final ArrayAdapter<String> dataAdapter_t;
        final ArrayAdapter<String> dataAdapter_h1;
        final ArrayAdapter<String> dataAdapter_h2;

        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_Q = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Q);
        dataAdapter_logr1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_logr1);
        dataAdapter_r2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_r2);
        dataAdapter_t = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_t);
        dataAdapter_h1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_h1);
        dataAdapter_h2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_h2);

        //Dropdown layout style
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_Q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_logr1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_r2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_t.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_Q);
        spinner.setAdapter(dataAdapter_logr1);
        spinner.setAdapter(dataAdapter_r2);
        spinner.setAdapter(dataAdapter_t);
        spinner.setAdapter(dataAdapter_h1);
        spinner.setAdapter(dataAdapter_h2);

        num_K = findViewById(R.id.edit_text_K6);
        num_Q = findViewById(R.id.edit_text_Q);
        num_logr1 = findViewById(R.id.edit_text_logr1);
        num_r2 = findViewById(R.id.edit_text_r2);
        num_t = findViewById(R.id.edit_text_t3);
        num_h1 = findViewById(R.id.edit_text_h1_2);
        num_h2 = findViewById(R.id.edit_text_h2_2);

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
                                createPDFFile(Common.getAppPath(AquiferActivity.this)+"test_pdf.pdf");
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

                if(num_K.getText().toString().equals("") && !num_Q.getText().toString().equals("")
                        && !num_logr1.getText().toString().equals("") && !num_r2.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")) {

                    computed = true;


                    Q = Double.parseDouble(num_Q.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());


                    K = (Q) * Math.log10(logr1 / r2) / (3.14 * t) * (h1 - h2);
                    missing.setText("The missing variable is K");
                    answer.setText("Which has a value of : \n" + K  + " cm/sec");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_K);

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
                                    cm_hr = K * (3600/K);
                                    converted.setText(cm_hr + " cm/hr");
                                } else if (item.equals("cm/day")){
                                    cm_day = K * 86400;
                                    converted.setText(cm_day + " cm/day");
                                } else if (item.equals("m/sec")){
                                    m_sec = K * (0.01/K);
                                    converted.setText(m_sec + " m/sec");
                                } else if (item.equals("m/hr")){
                                    m_hr = K * (36/K);
                                    converted.setText(m_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    m_day = K * 864;
                                    converted.setText(m_day + " m/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_Q.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_logr1.getText().toString().equals("") && !num_r2.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    Q = (K) * (3.14 * t) * (h1 - h2) / Math.log10(logr1 / r2);

                    missing.setText("The missing variable is Q");
                    answer.setText("Which has a value of : " + Q + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_Q);

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
                                    mm_Q = Q * 10;
                                    converted.setText(mm_Q + " mm");
                                } else if (item.equals("m")){
                                    m_Q = Q / 100;
                                    converted.setText(m_Q + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_logr1.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_r2.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    logr1 = Math.pow(r2, (K) * (3.14 * t) * (h1 - h2) / Q);    //Little bit confuse of the formula
                    missing.setText("The missing variable is logr1");
                    answer.setText("Which has a value of : " + logr1 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_logr1);

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
                                    mm_logr1 = logr1 * 10;
                                    converted.setText(mm_logr1 + " mm");
                                } else if (item.equals("m")){
                                    m_logr1 = logr1 / 100;
                                    converted.setText(m_logr1 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_r2.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_logr1.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    r2 =  (logr1) / Math.pow(1, (K) * (3.14 * t) * (h1 - h2) / Q);     //Little bit confuse of the formula
                    missing.setText("The missing variable is r2");
                    answer.setText("Which has a value of : " + r2 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_r2);

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
                                    mm_r2 = r2 * 10;
                                    converted.setText(mm_r2 + " mm");
                                } else if (item.equals("m")){
                                    m_r2 = r2 / 100;
                                    converted.setText(m_r2 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_t.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_logr1.getText().toString().equals("")
                        && !num_r2.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    t = (Q) * Math.log10(logr1 / r2) / (3.14 * K) * (h1 - h2);
                    missing.setText("The missing variable is t");
                    answer.setText("Which has a value of : " + t + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_t);

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
                                    mm_t = t * 10;
                                    converted.setText(mm_t + " mm");
                                } else if (item.equals("m")){
                                    m_t = t / 100;
                                    converted.setText(m_t + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_h1.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_logr1.getText().toString().equals("")
                        && !num_r2.getText().toString().equals("") && !num_t.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    h1 = (h2) + (Q * Math.log10(logr1 / r2) / (K) * (2 * 3.14 * t));
                    missing.setText("The missing variable is h1");
                    answer.setText("Which has a value of : " + h1 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_h1);

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
                                    mm_h1 = h1 * 10;
                                    converted.setText(mm_h1 + " mm");
                                } else if (item.equals("m")){
                                    m_h1 = h1 / 100;
                                    converted.setText(m_h1 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_h2.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_logr1.getText().toString().equals("")
                        && !num_r2.getText().toString().equals("") && !num_t.getText().toString().equals("")
                        && !num_h1.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    logr1 = Double.parseDouble(num_logr1.getText().toString());
                    r2 = Double.parseDouble(num_r2.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());

                    h2 = (K / Q * logr1) * Math.log10(h1) / (r2 * t);
                    missing.setText("The missing variable is h2");
                    answer.setText("Which has a value of : " + h2 + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_h2);

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
                                    mm_h2 = h2 * 10;
                                    converted.setText(mm_h2 + " mm");
                                } else if (item.equals("m")){
                                    m_h2 = h2 / 100;
                                    converted.setText(m_h2 + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else {
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
            addNewItem(document,"Aquifer", Element.ALIGN_CENTER,title);

            Font title_K = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            //Add more
            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_Q = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Q", Element.ALIGN_CENTER,title_Q);

            Font value_Q = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Q.getText().toString(),Element.ALIGN_CENTER,value_Q);

            Font title_logr1 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for logr1", Element.ALIGN_CENTER,title_logr1);

            Font value_logr1 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_logr1.getText().toString(),Element.ALIGN_CENTER,value_logr1);

            Font title_r2 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for r2", Element.ALIGN_CENTER,title_r2);

            Font value_r2 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_r2.getText().toString(),Element.ALIGN_CENTER,value_r2);

            Font title_t = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for t", Element.ALIGN_CENTER,title_t);

            Font value_t = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_t.getText().toString(),Element.ALIGN_CENTER,value_t);

            Font title_h1 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for h1", Element.ALIGN_CENTER,title_h1);

            Font value_h1 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_h1.getText().toString(),Element.ALIGN_CENTER,value_h1);

            Font title_h2 = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for h2", Element.ALIGN_CENTER,title_h2);

            Font value_h2 = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_h2.getText().toString(),Element.ALIGN_CENTER,value_h2);


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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(AquiferActivity.this,Common.getAppPath(AquiferActivity.this)+"test_pdf.pdf");
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