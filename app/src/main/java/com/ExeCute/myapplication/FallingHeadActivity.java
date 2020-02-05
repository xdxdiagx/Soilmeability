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

public class FallingHeadActivity extends AppCompatActivity {

    double K , a, L, A, t, h1, h2;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_K, m_K, mm_a, m_a, mm_L, m_L,mm_A, m_A, mm_t, m_t, mm_h1, m_h1, mm_h2, m_h2;
    boolean computed = false;
    String item;
    EditText num_K, num_a, num_L,num_A, num_t, num_h1, num_h2;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falling_head);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_falling_head);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FallingHeadActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("mm^3/sec");
        categories_K.add("m^3/sec");

        List<String> categories_a = new ArrayList<>();
        categories_a.add(0, "Choose");
        categories_a.add("mm^2");
        categories_a.add("m^2");

        List<String> categories_L = new ArrayList<>();
        categories_L.add(0, "Choose");
        categories_L.add("mm");
        categories_L.add("m");

        List<String> categories_A = new ArrayList<>();
        categories_A.add(0, "Choose");
        categories_A.add("mm");
        categories_A.add("m");

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
        final ArrayAdapter<String> dataAdapter_a;
        final ArrayAdapter<String> dataAdapter_L;
        final ArrayAdapter<String> dataAdapter_A;
        final ArrayAdapter<String> dataAdapter_t;
        final ArrayAdapter<String> dataAdapter_h1;
        final ArrayAdapter<String> dataAdapter_h2;
        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_a);
        dataAdapter_L = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_L);
        dataAdapter_A = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_A);
        dataAdapter_t = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_t);
        dataAdapter_h1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_h1);
        dataAdapter_h2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_h2);

        //Dropdown layout style
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_L.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_A.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_t.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_a);
        spinner.setAdapter(dataAdapter_L);
        spinner.setAdapter(dataAdapter_A);
        spinner.setAdapter(dataAdapter_t);
        spinner.setAdapter(dataAdapter_h1);
        spinner.setAdapter(dataAdapter_h2);

        num_K = findViewById(R.id.edit_text_K3);
        num_a = findViewById(R.id.edit_text_a);
        num_L = findViewById(R.id.edit_text_L2);
        num_A = findViewById(R.id.edit_text_A2);
        num_t = findViewById(R.id.edit_text_t);
        num_h1 = findViewById(R.id.edit_text_h1);
        num_h2 = findViewById(R.id.edit_text_h2);
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
                                createPDFFile(Common.getAppPath(FallingHeadActivity.this)+"test_pdf.pdf");
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

                if(num_K.getText().toString().equals("") && !num_a.getText().toString().equals("")
                        && !num_L.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")) {

                    computed = true;


                    a = Double.parseDouble(num_a.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //K = (2.3 * a * L * Math.log10(h1 / h2)) / (A * t);
                    K = (a * L)/(A * t) * Math.log10(h1 / h2);
                    //K = Math.round(K);
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
                                converted.setText(" ");
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

                } else if(num_a.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_L.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //a = (K * A * t) / (2.3 * a * Math.log10(h1 / h2));
                    a = (K * A * t) * Math.log10(h1 / h2) / (L);
                    //a = Math.round(a);

                    missing.setText("The missing variable is a");
                    answer.setText("Which has a value of : " + a + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_a);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                //item = parent.getItemAtPosition(position).toString();
                                // Toast.makeText(parent.getContext(), "centimeter", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                                if(item.equals("mm")){
                                    mm_a = a * 10;
                                    converted.setText(mm_a + " mm");
                                } else if (item.equals("m")){
                                    m_a = a / 100;
                                    converted.setText(m_a + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_L.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_a.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    a = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //L = (K * A * t) / (2.3 * a * Math.log10(h1 / h2));
                    L = (K * A * t) * Math.log10(h1 / h2) / a;
                    //L = Math.round (L);
                    missing.setText("The missing variable is L");
                    answer.setText("Which has a value of : " + L + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_L);

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
                                    mm_L = L * 10;
                                    converted.setText(mm_L + " mm");
                                } else if (item.equals("m")){
                                    m_L = L / 100;
                                    converted.setText(m_L + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_A.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_a.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    a = Double.parseDouble(num_L.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //A = (2.3 * a * L * Math.log10(h1 / h2)) / (K * t);
                    A = (a * L) * Math.log10(h1 / h2) / (K * t);
                    //A = Math.round(A);
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
                                    m_L = A / 100;
                                    converted.setText(m_A + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_t.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_a.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_h1.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    a = Double.parseDouble(num_L.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //t = (2.3 * a * L * Math.log10(h1 / h2)) / (K * A);
                    t = (a * L) * Math.log10(h1 / h2) / (K * A);
                    //t = Math.round (L);
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
                        && !num_a.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_t.getText().toString().equals("")
                        && !num_h2.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    a = Double.parseDouble(num_L.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_h1.getText().toString());
                    h2 = Double.parseDouble(num_h2.getText().toString());

                    //h1 = ((K * A * t) / (2.3 * a * L)) + Math.log10(h2);
                    h1 = (K / a * L) * Math.log(h2) / (A * t);
                    //h1 = Math.pow(10, h1);
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
                        && !num_a.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_t.getText().toString().equals("")
                        && !num_h1.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    a = Double.parseDouble(num_L.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h1 = Double.parseDouble(num_h1.getText().toString());

                    //h2 = ((K * A * t) / (2.3 * a * L)) + Math.log10(h1);
                    h2 = (K * a * L) * Math.log10(h1) / (A * t);
                    //h2 = Math.pow(10, h2);
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
            addNewItem(document,"Falling Head Test", Element.ALIGN_CENTER,title);

            Font title_K = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            //Add more
            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_a = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for a", Element.ALIGN_CENTER,title_a);

            Font value_a = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_a.getText().toString(),Element.ALIGN_CENTER,value_a);

            Font title_L = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for L", Element.ALIGN_CENTER,title_L);

            Font value_L = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_L.getText().toString(),Element.ALIGN_CENTER,value_L);

            Font title_A = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for A", Element.ALIGN_CENTER,title_A);

            Font value_A = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_A.getText().toString(),Element.ALIGN_CENTER,value_A);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(FallingHeadActivity.this,Common.getAppPath(FallingHeadActivity.this)+"test_pdf.pdf");
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
