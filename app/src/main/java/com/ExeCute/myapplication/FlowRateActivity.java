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

public class FlowRateActivity extends AppCompatActivity {

    double q , K, i, A;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_Q, m_Q, mm_K, m_K, mm_i, m_i, mm_A, m_A;
    boolean computed = false;
    String item;
    EditText num_q, num_K, num_i, num_A;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, convertTo, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_rate);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_flow_rate);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FlowRateActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_Q = new ArrayList<>();
        categories_Q.add(0, "Choose");
        categories_Q.add("cm^3/hr");
        categories_Q.add("cm^3/day");

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("mm/sec");
        categories_K.add("m/sec");

        List<String> categories_I = new ArrayList<>();
        categories_I.add(0, "");

        List<String> categories_A = new ArrayList<>();
        categories_A.add(0, "Choose");
        categories_A.add("mm^2");
        categories_A.add("m^2");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_q;
        final ArrayAdapter<String> dataAdapter_K;
        final ArrayAdapter<String> dataAdapter_i;
        final ArrayAdapter<String> dataAdapter_A;
        dataAdapter_q = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Q);
        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_i = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_I);
        dataAdapter_A = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_A);

        //Dropdown layout style
        dataAdapter_q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_i.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_A.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_q);
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_i);
        spinner.setAdapter(dataAdapter_A);

        num_q = findViewById(R.id.edit_text_q);
        num_K = findViewById(R.id.edit_text_K2);
        num_i = findViewById(R.id.edit_text_i3);
        num_A = findViewById(R.id.edit_text_A);
        btn_compute =  findViewById(R.id.button_compute);
        missing = findViewById(R.id.text_view_missing);
        answer = findViewById(R.id.text_view_answer);
        convertTo = findViewById(R.id.text_view_convert);
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
                                createPDFFile(Common.getAppPath(FlowRateActivity.this)+"test_pdf.pdf");
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

                if(num_q.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_i.getText().toString().equals("") && !num_A.getText().toString().equals("")) {

                    computed = true;


                    K = Double.parseDouble(num_K.getText().toString());
                    i = Double.parseDouble(num_i.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());

                    q = K * i * A;
                    //q = q * q * q;
                    missing.setText("The missing variable is q");
                    answer.setText("Which has a value of : \n" + q  + " cm^3/sec");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_q);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                item = "none";
                                // item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "cm/sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();


                                //Do the calculation here
                                if(item.equals("cm^3/hr")){
                                    mm_Q = q * (3600/q);
                                    converted.setText(mm_Q + " cm^3/hr");
                                } else if (item.equals("cm^3/day")){
                                    m_Q = q * 86400;
                                    converted.setText(m_Q + " cm^3/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (num_K.getText().toString().equals("") && !num_q.getText().toString().equals("")
                        && !num_i.getText().toString().equals("") && !num_A.getText().toString().equals("")){

                    computed = true;

                    q = Double.parseDouble(num_q.getText().toString());
                    // h = Double.parseDouble(num_h.getText().toString());
                    i = Double.parseDouble(num_i.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());

                    K = q / (i * A);

                    missing.setText("The missing variable is K");
                    answer.setText("Which has a value of : " + K + " cm/sec");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_K);

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
                                if(item.equals("mm/sec")){
                                    mm_K = K * 10;
                                    converted.setText(mm_K + " mm/sec");
                                } else if (item.equals("m/sec")){
                                    m_K = K / 100;
                                    converted.setText(m_K + " m/sec");

                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if (num_i.getText().toString().equals("") && !num_q.getText().toString().equals("")
                        && !num_K.getText().toString().equals("") && !num_A.getText().toString().equals("")){

                    computed = true;

                    q = Double.parseDouble(num_q.getText().toString());
                    K = Double.parseDouble(num_K.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());
                    A = Double.parseDouble(num_A.getText().toString());

                    i = q / (K * A);

                    missing.setText("The missing variable is i");
                    answer.setText("Which has a value of : " + i);

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("i is unitless");
                    spinner.setAdapter(dataAdapter_i);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("Choose")){
                                converted.setText(" ");
                                item = "none";
                                //item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "per sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();

                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();

                                //Do the calculation here
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (num_A.getText().toString().equals("") && !num_q.getText().toString().equals("")
                        && !num_K.getText().toString().equals("") && !num_i.getText().toString().equals("")){

                    computed = true;

                    q = Double.parseDouble(num_q.getText().toString());
                    K = Double.parseDouble(num_K.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());
                    i = Double.parseDouble(num_i.getText().toString());

                    A = q / (K * i);

                    missing.setText("The missing variable is A");
                    answer.setText("Which has a value of : " + A + "cm^2");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
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
                                if(item.equals("mm^2")){
                                    mm_A = A * 10;
                                    mm_A = mm_A * mm_A;
                                    converted.setText(mm_A + " mm^2");
                                } else if (item.equals("m^2")){
                                    m_A = A / 100;
                                    m_A = m_A * m_A;
                                    converted.setText(m_A + " m^2");
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
            addNewItem(document,"Flow Rate", Element.ALIGN_CENTER,title);

            Font title_q= new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for q", Element.ALIGN_CENTER,title_q);

            //Add more
            Font value_q = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_q.getText().toString(),Element.ALIGN_CENTER,value_q);

            Font title_K = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_i = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for i", Element.ALIGN_CENTER,title_i);

            Font value_i = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_i.getText().toString(),Element.ALIGN_CENTER,value_i);

            Font title_A = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for A", Element.ALIGN_CENTER,title_A);

            Font value_A = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_A.getText().toString(),Element.ALIGN_CENTER,value_A);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(FlowRateActivity.this,Common.getAppPath(FlowRateActivity.this)+"test_pdf.pdf");
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
