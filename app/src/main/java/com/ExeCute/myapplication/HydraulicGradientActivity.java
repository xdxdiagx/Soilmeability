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

public class HydraulicGradientActivity extends AppCompatActivity {

    double i , h, L;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_h, m_h, mm_L, m_L;
    double temp_feet;
    boolean computed = false;
    String item, unit, converted_unit;
    EditText num_i, num_h, num_L;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer,convertTo, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydraulic_gradient);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_HG);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HydraulicGradientActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_I = new ArrayList<>();
        categories_I.add(0, "");

        List<String> categories_H = new ArrayList<>();
        categories_H.add(0, "Choose");
        categories_H.add("mm");
        categories_H.add("m");

        List<String> categories_L = new ArrayList<>();
        categories_L.add(0, "Choose");
        categories_L.add("mm");
        categories_L.add("m");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_i;
        final ArrayAdapter<String> dataAdapter_h;
        final ArrayAdapter<String> dataAdapter_L;
        dataAdapter_i = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_I);
        dataAdapter_h = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_H);
        dataAdapter_L = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_L);

        //Dropdown layout style
        dataAdapter_i.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_h.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_L.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_i);
        spinner.setAdapter(dataAdapter_h);
        spinner.setAdapter(dataAdapter_L);

        num_i = findViewById(R.id.edit_text_i);
        num_h = findViewById(R.id.edit_text_h);
        num_L = findViewById(R.id.edit_text_L);
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
                                createPDFFile(Common.getAppPath(HydraulicGradientActivity.this)+"test_pdf.pdf");
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

                if(num_i.getText().toString().equals("") && !num_h.getText().toString().equals("")
                        && !num_L.getText().toString().equals("")) {

                    computed = true;


                    h = Double.parseDouble(num_h.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());

                    i = h / L;
                    missing.setText("The missing variable is i");
                    answer.setText("Which has a value of : \n" + i);
                    unit = "";

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    convertTo.setText("i is unitless");
                    converted_unit = "";
                    converted_unit = "";
                    spinner.setAdapter(dataAdapter_i);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("")){
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
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (num_h.getText().toString().equals("") && !num_i.getText().toString().equals("")
                        && !num_L.getText().toString().equals("")){

                    computed = true;

                    i = Double.parseDouble(num_i.getText().toString());
                    // h = Double.parseDouble(num_h.getText().toString());
                    L = Double.parseDouble(num_L.getText().toString());

                    h = i * L;

                    missing.setText("The missing variable is h");
                    answer.setText("Which has a value of : " + h + " cm");
                    //unit = "cm";

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_h);

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
                                    mm_h = h * 10;
                                    converted.setText(mm_h + " mm");
                                   // converted_unit = "mm";
                                } else if (item.equals("m")){
                                    m_h = h / 100;
                                    converted.setText(m_h + " m");
                                    //converted_unit = "m";
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if (num_L.getText().toString().equals("") && !num_i.getText().toString().equals("")
                        && !num_h.getText().toString().equals("")){

                    computed = true;

                    i = Double.parseDouble(num_i.getText().toString());
                    h = Double.parseDouble(num_h.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());

                    L = h / i;

                    missing.setText("The missing variable is L");
                    answer.setText("Which has a value of : " + L + " cm");
                    //unit = "cm";

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
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
                                    //converted_unit = "mm";
                                } else if (item.equals("m")){
                                    m_L = L / 100;
                                    converted.setText(m_L + " m");
                                    //converted_unit = "m";
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
            addNewItem(document,"Hydraulic Gradient", Element.ALIGN_CENTER,title);

            Font titleI= new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for i", Element.ALIGN_CENTER,titleI);

            //Add more
            Font valueI = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_i.getText().toString(),Element.ALIGN_CENTER,valueI);

            Font titleH = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for h", Element.ALIGN_CENTER,titleH);

            Font valueH = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_h.getText().toString(),Element.ALIGN_CENTER,valueH);

            Font titleL = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for L", Element.ALIGN_CENTER,titleL);

            Font valueL = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_L.getText().toString(),Element.ALIGN_CENTER,valueL);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(HydraulicGradientActivity.this,Common.getAppPath(HydraulicGradientActivity.this)+"test_pdf.pdf");
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
