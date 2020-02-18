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

public class TransmissivityActivity extends AppCompatActivity {

    double T , K, b;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double min_T, hr_T, day_T, mm_K, m_K, mm_b, m_b;
    double temp_feet;
    boolean computed = false;
    String item;
    EditText num_T, num_K, num_b;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer,convertTo, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmissivity);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_transmissivity);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransmissivityActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_T = new ArrayList<>();
        categories_T.add(0, "Choose");
        categories_T.add("mm^2/min");
        categories_T.add("mm^2/hr");
        categories_T.add("mm^2/day");

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("mm");
        categories_K.add("m");

        List<String> categories_b = new ArrayList<>();
        categories_b.add(0, "Choose");
        categories_b.add("mm");
        categories_b.add("m");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_T;
        final ArrayAdapter<String> dataAdapter_K;
        final ArrayAdapter<String> dataAdapter_b;
        dataAdapter_T = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_T);
        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_b = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_b);

        //Dropdown layout style
        dataAdapter_T.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_T);
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_b);

        num_T = findViewById(R.id.edit_text_T_1);
        num_K = findViewById(R.id.edit_text_K_2);
        num_b = findViewById(R.id.edit_text_b);
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
                                createPDFFile(Common.getAppPath(TransmissivityActivity.this)+"test_pdf.pdf");
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

                if(num_T.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_b.getText().toString().equals("")) {

                    computed = true;


                    K = Double.parseDouble(num_K.getText().toString());
                    b = Double.parseDouble(num_b.getText().toString());

                    T = (K * b);
                    missing.setText("The missing variable is T");
                    answer.setText("Which has a value of : \n" + T + " m^2/sec");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_T);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("")){
                                converted.setText(" ");
                                item = " ";
                                // item = parent.getItemAtPosition(position).toString();
                                //Toast.makeText(parent.getContext(), "cm/sec", Toast.LENGTH_SHORT).show();
                            } else {
                                //on selecting a spinner item
                                item = parent.getItemAtPosition(position).toString();
                                //show selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected " + item, Toast.LENGTH_SHORT).show();


                                //Do the calculation here
                                if(item.equals("mm^2/min")){
                                    min_T = T * 600000000;
                                    converted.setText(min_T + " mm^2/min");
                                } else if (item.equals("mm^2/hr")){
                                    hr_T = T / (10000 * 360000);
                                    converted.setText(hr_T + " mm^2/hr");
                                } else if (item.equals("mm^2/day")){
                                    day_T = T / 86400;
                                    converted.setText(hr_T + " mm^2/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (num_K.getText().toString().equals("") && !num_T.getText().toString().equals("")
                        && !num_b.getText().toString().equals("")){

                    computed = true;

                    T = Double.parseDouble(num_T.getText().toString());
                    // h = Double.parseDouble(num_h.getText().toString());
                    b = Double.parseDouble(num_b.getText().toString());

                    K = T / b;

                    missing.setText("The missing variable is K");
                    answer.setText("Which has a value of : " + K + " cm");

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
                                if(item.equals("mm")){
                                    mm_K = K * 10;
                                    converted.setText(mm_K + " mm");
                                } else if (item.equals("m")){
                                    m_K = K / 100;
                                    converted.setText(m_K + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if (num_b.getText().toString().equals("") && !num_T.getText().toString().equals("")
                        && !num_K.getText().toString().equals("")){

                    computed = true;

                    T = Double.parseDouble(num_T.getText().toString());
                    K = Double.parseDouble(num_K.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());

                    b = T / K;

                    missing.setText("The missing variable is b");
                    answer.setText("Which has a value of : " + b + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_b);

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
                                    mm_b = b * 10;
                                    converted.setText(mm_b + " mm");
                                } else if (item.equals("m")){
                                    m_b = b / 100;
                                    converted.setText(m_b + " m");
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
            addNewItem(document,"Transmissivity", Element.ALIGN_CENTER,title);

            Font titleT= new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for T", Element.ALIGN_CENTER,titleT);

            //Add more
            Font valueT = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_T.getText().toString(),Element.ALIGN_CENTER,valueT);

            Font title_K = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_b = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for b", Element.ALIGN_CENTER,title_b);

            Font value_b = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_b.getText().toString(),Element.ALIGN_CENTER,value_b);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(TransmissivityActivity.this,Common.getAppPath(TransmissivityActivity.this)+"test_pdf.pdf");
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
