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

public class LeakageFactorActivity extends AppCompatActivity {

    double B , kb, KB;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double mm_kb, m_kb, mm_KB, m_KB;
    double temp_feet;
    boolean computed = false;
    String item;
    EditText num_B, num_kb, num_KB;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer,convertTo, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakage_factor);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_leakage_factor);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeakageFactorActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_B = new ArrayList<>();
        categories_B.add(0, "");

        List<String> categories_kb = new ArrayList<>();
        categories_kb.add(0, "Choose");
        categories_kb.add("mm");
        categories_kb.add("m");

        List<String> categories_KB = new ArrayList<>();
        categories_KB.add(0, "Choose");
        categories_KB.add("mm");
        categories_KB.add("m");

        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_B;
        final ArrayAdapter<String> dataAdapter_kb;
        final ArrayAdapter<String> dataAdapter_KB;
        dataAdapter_B = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_B);
        dataAdapter_kb = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_kb);
        dataAdapter_KB = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_KB);

        //Dropdown layout style
        dataAdapter_B.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_kb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_KB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_B);
        spinner.setAdapter(dataAdapter_kb);
        spinner.setAdapter(dataAdapter_KB);

        num_B = findViewById(R.id.edit_text_B_1);
        num_kb = findViewById(R.id.edit_text_kb);
        num_KB = findViewById(R.id.edit_text_KB);
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
                                createPDFFile(Common.getAppPath(LeakageFactorActivity.this)+"test_pdf.pdf");
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

                if(num_B.getText().toString().equals("") && !num_kb.getText().toString().equals("")
                        && !num_KB.getText().toString().equals("")) {

                    computed = true;


                    kb = Double.parseDouble(num_kb.getText().toString());
                    KB = Double.parseDouble(num_KB.getText().toString());

                    B = Math.sqrt(kb) / (KB);
                    missing.setText("The missing variable is B");
                    answer.setText("Which has a value of : \n" + B);

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    convertTo.setText("B is unitless");
                    spinner.setAdapter(dataAdapter_B);

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

                } else if (num_kb.getText().toString().equals("") && !num_B.getText().toString().equals("")
                        && !num_KB.getText().toString().equals("")){

                    computed = true;

                    B = Double.parseDouble(num_B.getText().toString());
                    // h = Double.parseDouble(num_h.getText().toString());
                    KB = Double.parseDouble(num_KB.getText().toString());

                    kb = Math.sqrt(B) * (KB);

                    missing.setText("The missing variable is kb");
                    answer.setText("Which has a value of : " + kb + " cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_kb);

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
                                    mm_kb = kb * 10;
                                    converted.setText(mm_kb + " mm");
                                } else if (item.equals("m")){
                                    m_kb = kb / 100;
                                    converted.setText(m_kb + " m");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if (num_KB.getText().toString().equals("") && !num_B.getText().toString().equals("")
                        && !num_kb.getText().toString().equals("")){

                    computed = true;

                    B = Double.parseDouble(num_B.getText().toString());
                    kb = Double.parseDouble(num_kb.getText().toString());
                    //L = Double.parseDouble(num_L.getText().toString());

                    KB = Math.sqrt(kb) / (B);

                    missing.setText("The missing variable is Kb");
                    answer.setText("Which has a value of : " + KB + "cm");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    convertTo.setText("Convert answer to ");
                    spinner.setAdapter(dataAdapter_KB);

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
                                    mm_KB = KB * 10;
                                    converted.setText(mm_KB + " mm");
                                } else if (item.equals("m")){
                                    m_KB = KB / 100;
                                    converted.setText(m_KB + " m");
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
            addNewItem(document,"Leakage Factor", Element.ALIGN_CENTER,title);

            Font titleB= new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for B", Element.ALIGN_CENTER,titleB);

            //Add more
            Font valueB = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_B.getText().toString(),Element.ALIGN_CENTER,valueB);

            Font title_kb = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for kb", Element.ALIGN_CENTER,title_kb);

            Font value_kb = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_kb.getText().toString(),Element.ALIGN_CENTER,value_kb);

            Font title_KB = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for KB", Element.ALIGN_CENTER,title_KB);

            Font value_KB = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_KB.getText().toString(),Element.ALIGN_CENTER,value_KB);

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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(LeakageFactorActivity.this,Common.getAppPath(LeakageFactorActivity.this)+"test_pdf.pdf");
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
