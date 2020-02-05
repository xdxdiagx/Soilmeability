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

public class SeePageVelocityActivity extends AppCompatActivity {

    double Vs , V, n;
    double mm_Vs, m_Vs, mm_V, m_V;
    boolean computed = false;
    String item;
    EditText num_Vs, num_V, num_n;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer,convertTo, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_page_velocity);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_SPV);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SeePageVelocityActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_Vs = new ArrayList<>();
        categories_Vs.add(0, "Choose");
        categories_Vs.add("mm^3");
        categories_Vs.add("m^3");

        List<String> categories_V = new ArrayList<>();
        categories_V.add(0, "Choose");
        categories_V.add("mm^3");
        categories_V.add("m^3");

        List<String> categories_N = new ArrayList<>();
        categories_N.add(0, "");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_Vs;
        final ArrayAdapter<String> dataAdapter_V;
        final ArrayAdapter<String> dataAdapter_n;
        dataAdapter_Vs = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Vs);
        dataAdapter_V = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_V);
        dataAdapter_n = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_N);

        //Dropdown layout style
        dataAdapter_Vs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_V.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_n.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_Vs);
        spinner.setAdapter(dataAdapter_V);
        spinner.setAdapter(dataAdapter_n);

        num_Vs = findViewById(R.id.edit_text_Vs);
        num_V = findViewById(R.id.edit_text_V2);
        num_n = findViewById(R.id.edit_text_n);
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
                                createPDFFile(Common.getAppPath(SeePageVelocityActivity.this)+"test_pdf.pdf");
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

                if(num_Vs.getText().toString().equals("") && !num_V.getText().toString().equals("")
                        && !num_n.getText().toString().equals("")) {

                    computed = true;


                    V = Double.parseDouble(num_V.getText().toString());
                    n = Double.parseDouble(num_n.getText().toString());

                    Vs = V / n;
                    missing.setText("The missing variable is Vs");
                    answer.setText("Which has a value of : \n" + Vs  + " cm^3");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_Vs);

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
                                if(item.equals("mm^3")){
                                    mm_Vs = Vs * (3600/Vs);
                                    converted.setText(mm_Vs + " mm^3");
                                } else if (item.equals("m^3")){
                                    mm_Vs = Vs * 86400;
                                    converted.setText(m_Vs + " m^3");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if (num_V.getText().toString().equals("") && !num_Vs.getText().toString().equals("")
                        && !num_n.getText().toString().equals("")){

                    computed = true;

                    Vs = Double.parseDouble(num_Vs.getText().toString());
                    // h = Double.parseDouble(num_h.getText().toString());
                    n = Double.parseDouble(num_n.getText().toString());

                    V = Vs * n;

                    missing.setText("The missing variable is V");
                    answer.setText("Which has a value of : " + V + " cm^3");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_V);

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
                                if(item.equals("mm^3")){
                                    mm_V = V * 10;
                                    converted.setText(mm_V + " mm^3");
                                } else if (item.equals("m^3")){
                                    m_V = V / 100;
                                    converted.setText(m_V + " m^3");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if (num_n.getText().toString().equals("") && !num_Vs.getText().toString().equals("")
                        && !num_V.getText().toString().equals("")){

                    computed = true;

                    Vs = Double.parseDouble(num_Vs.getText().toString());
                    V = Double.parseDouble(num_V.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());
                    n = V / Vs;

                    missing.setText("The missing variable is n");
                    answer.setText("Which has a value of : " + n );

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("n is unitless");
                    spinner.setAdapter(dataAdapter_n);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).equals("")){
                                converted.setText(" ");
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
            addNewItem(document,"See Page Velocity", Element.ALIGN_CENTER,title);

            Font title_Vs= new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Vs", Element.ALIGN_CENTER,title_Vs);

            //Add more
            Font value_Vs = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Vs.getText().toString(),Element.ALIGN_CENTER,value_Vs);

            Font title_V = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for V", Element.ALIGN_CENTER,title_V);

            Font value_V = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_V.getText().toString(),Element.ALIGN_CENTER,value_V);

            Font title_n = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for n", Element.ALIGN_CENTER,title_n);

            Font value_n = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_n.getText().toString(),Element.ALIGN_CENTER,value_n);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(SeePageVelocityActivity.this,Common.getAppPath(SeePageVelocityActivity.this)+"test_pdf.pdf");
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
