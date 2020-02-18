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

public class ConstantHeadActivity extends AppCompatActivity {

    double K , Q, L, A, h, t;
    double K_mm_sec,K_m_min,K_cm_hr,K_m_day, Q_m3, Q_mm3,Q_km3, L_m, L_mm, L_km, A_m2, A_mm2, A_km2, h_m, h_mm, h_km, t_min, t_hr, t_day;
    boolean computed = false;
    String item;
    EditText num_K, num_Q, num_L,num_A, num_h, num_t;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constant_head);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_constant_head);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ConstantHeadActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("mm/sec");
        categories_K.add("m/min");
        categories_K.add("cm/hr");
        categories_K.add("m/day");

        List<String> categories_Q = new ArrayList<>();
        categories_Q.add(0, "Choose");
        categories_Q.add("m^3");
        categories_Q.add("mm^3");
        categories_Q.add("km^3");

        List<String> categories_L = new ArrayList<>();
        categories_L.add(0, "Choose");
        categories_L.add("m");
        categories_L.add("mm");
        categories_L.add("km");

        List<String> categories_A = new ArrayList<>();
        categories_A.add(0, "Choose");
        categories_A.add("m^2");
        categories_A.add("mm^2");
        categories_A.add("km^2");

        List<String> categories_h = new ArrayList<>();
        categories_h.add(0, "Choose");
        categories_h.add("m");
        categories_h.add("mm");
        categories_h.add("km");

        List<String> categories_t = new ArrayList<>();
        categories_t.add(0, "Choose");
        categories_t.add("min");
        categories_t.add("hr");
        categories_t.add("day");



        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_K;
        final ArrayAdapter<String> dataAdapter_Q;
        final ArrayAdapter<String> dataAdapter_L;
        final ArrayAdapter<String> dataAdapter_A;
        final ArrayAdapter<String> dataAdapter_h;
        final ArrayAdapter<String> dataAdapter_t;

        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_Q = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Q);
        dataAdapter_L = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_L);
        dataAdapter_A = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_A);
        dataAdapter_h = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_h);
        dataAdapter_t = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_t);



        //Dropdown layout style
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_Q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_L.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_A.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_t.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_Q);
        spinner.setAdapter(dataAdapter_L);
        spinner.setAdapter(dataAdapter_A);
        spinner.setAdapter(dataAdapter_h);
        spinner.setAdapter(dataAdapter_t);



        num_K = findViewById(R.id.edit_text_K4);
        num_Q = findViewById(R.id.edit_text_Q);
        num_L = findViewById(R.id.edit_text_L_3);
        num_A = findViewById(R.id.edit_text_A3);
        num_h = findViewById(R.id.edit_text_h3);
        num_t = findViewById(R.id.edit_text_t2);

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
                                createPDFFile(Common.getAppPath(ConstantHeadActivity.this)+"test_pdf.pdf");
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
                        && !num_L.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h.getText().toString().equals("")) {

                    computed = true;


                    Q = Double.parseDouble(num_Q.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());



                    K = (Q * L) / (A * h * t);
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
                                if(item.equals("mm/sec")){
                                    K_mm_sec = K * 10;
                                    converted.setText(K_mm_sec + " mm/sec");
                                } else if (item.equals("m/min")){
                                    K_m_min = K / 1.667;
                                    converted.setText(K_m_min + " m/min");
                                } else if (item.equals("cm/hr")){
                                    K_cm_hr = K * 3600;
                                    converted.setText(K_cm_hr + " cm/hr");
                                } else if (item.equals("m/day")){
                                    K_m_day = K * 864;
                                    converted.setText(K_m_day + " m/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_Q.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_L.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());


                    Q = (K * A * h * t) / L;

                    missing.setText("The missing variable is Q");
                    answer.setText("Which has a value of : " + Q + " cm^3");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_Q);

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
                                if(item.equals("m^3")){
                                    Q_m3 = Q / 1000000;
                                    converted.setText(Q_m3 + " m^3");
                                } else if (item.equals("mm^3")){
                                    Q_mm3 = Q * 1000;
                                    converted.setText(Q_mm3 + " mm^3");
                                } else if (item.equals("km^3")){
                                    Q_km3 = Q / (1000000000 * 1000000);
                                    converted.setText(Q_km3 + " km^3");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_L.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_A.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());


                    L = (K * A * h * t) / Q;
                    missing.setText("The missing variable is L");
                    answer.setText("Which has a value of : " + L + " cm");

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
                                if(item.equals("m")){
                                    L_m = L / 100;
                                    converted.setText(L_m + " m");
                                } else if (item.equals("mm")){
                                    L_mm = L * 10;
                                    converted.setText(L_mm + " m");
                                } else if (item.equals("km")){
                                    L_km = L / 100000;
                                    converted.setText(L_km + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_A.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_t.getText().toString().equals("") && !num_h.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_L.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());


                    A = (Q * L) / (K * h * t);
                    //A = A * A;
                    missing.setText("The missing variable is A");
                    answer.setText("Which has a value of : " + A + "cm^2");

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
                                if(item.equals("m^2")){
                                    A_m2= A / 10000;
                                    converted.setText(A_m2 + " mm^2");
                                } else if (item.equals("mm^2")){
                                    A_mm2 = A * 100;
                                    converted.setText(A_mm2 + " mm^2");
                                } else if (item.equals("km^2")){
                                    A_km2 = A / (1000000 * 10000);
                                    converted.setText(A_km2 + " km^2");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_h.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_t.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    t = Double.parseDouble(num_t.getText().toString());

                    h = (Q * L) / (K * A * t);
                    missing.setText("The missing variable is h");
                    answer.setText("Which has a value of : " + h + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_h);

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
                                    h_m = h / 100;
                                    converted.setText(h_m + " m");
                                } else if (item.equals("mm")){
                                    h_mm = h * 10;
                                    converted.setText(h_mm + " m");
                                } else if (item.equals("km")){
                                    h_km = h / 100000;
                                    converted.setText(h_km + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_t.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_Q.getText().toString().equals("") && !num_L.getText().toString().equals("")
                        && !num_A.getText().toString().equals("") && !num_h.getText().toString().equals("")){

                    computed = true;

                    K = Double.parseDouble(num_K.getText().toString());
                    Q = Double.parseDouble(num_Q.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());

                    t = (Q * L) / (K * A * h);
                    missing.setText("The missing variable is t");
                    answer.setText("Which has a value of : " + t + " sec");

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
                                if(item.equals("min")){
                                    t_min = t / 60;
                                    converted.setText(t_min + " min");
                                } else if (item.equals("hr")){
                                    t_hr = t / 3600;
                                    converted.setText(t_hr + " hr");
                                } else if (item.equals("day")){
                                    t_day = t / 86400;
                                    converted.setText(t_day + " day");
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
            addNewItem(document,"Constant Head Test", Element.ALIGN_CENTER,title);

            Font title_K = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            //Add more
            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_Q = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Q", Element.ALIGN_CENTER,title_Q);

            Font value_Q = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Q.getText().toString(),Element.ALIGN_CENTER,value_Q);

            Font title_L = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for L", Element.ALIGN_CENTER,title_L);

            Font value_L = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_L.getText().toString(),Element.ALIGN_CENTER,value_L);

            Font title_A = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for A", Element.ALIGN_CENTER,title_A);

            Font value_A = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_A.getText().toString(),Element.ALIGN_CENTER,value_A);

            Font title_h = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for h", Element.ALIGN_CENTER,title_h);

            Font value_h = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_h.getText().toString(),Element.ALIGN_CENTER,value_h);

            Font title_t = new Font(fontName,30.0f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for t", Element.ALIGN_CENTER,title_t);

            Font value_t = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_t.getText().toString(),Element.ALIGN_CENTER,value_t);




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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(ConstantHeadActivity.this,Common.getAppPath(ConstantHeadActivity.this)+"test_pdf.pdf");
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
