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

public class AbsolutePermeabilityActivity extends AppCompatActivity {

    double k , K, N, Y;
    double cm_hr, cm_day, m_sec, m_hr, m_day;
    double K_min, K_hr, K_day, k_min,k_hr, k_day, N_cm_min, N_cm_hr, N_mm_day, Y_m, Y_mm, Y_km;
    boolean computed = false;

    String item;
    EditText num_k, num_K, num_N,num_Y;
    Button btn_compute, btn_clear, btn_print;
    TextView missing, answer, converted;

    private Spinner spinner;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absolute_permeability);


        spinner = findViewById(R.id.spinner);

        btn_back = findViewById(R.id.button_back_absolute_permeability);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AbsolutePermeabilityActivity.this, FormulaActivity.class);
                startActivity(i);
            }
        });

        List<String> categories_k = new ArrayList<>();
        categories_k.add(0, "Choose");
        categories_k.add("m/min");
        categories_k.add("m/hr");
        categories_k.add("m/day");

        List<String> categories_K = new ArrayList<>();
        categories_K.add(0, "Choose");
        categories_K.add("m/min");
        categories_K.add("m/hr");
        categories_K.add("m/day");

        List<String> categories_N = new ArrayList<>();
        categories_N.add(0, "Choose");
        categories_N.add("cm^2/min");
        categories_N.add("cm^2/hr");
        categories_N.add("mm^2/day");

        List<String> categories_Y = new ArrayList<>();
        categories_Y.add(0, "Choose");
        categories_Y.add("kN/m^3");
        categories_Y.add("kN/mm^3");
        categories_Y.add("kN/km^3");



        //Style and populate the spinner
        final ArrayAdapter<String> dataAdapter_k;
        final ArrayAdapter<String> dataAdapter_K;
        final ArrayAdapter<String> dataAdapter_N;
        final ArrayAdapter<String> dataAdapter_Y;

        dataAdapter_k = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_k);
        dataAdapter_K = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_K);
        dataAdapter_N = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_N);
        dataAdapter_Y = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories_Y);



        //Dropdown layout style
        dataAdapter_k.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_K.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_N.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter_Y.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //attaching data adapter to spinner
        spinner.setAdapter(dataAdapter_k);
        spinner.setAdapter(dataAdapter_K);
        spinner.setAdapter(dataAdapter_N);
        spinner.setAdapter(dataAdapter_Y);



        num_k = findViewById(R.id.edit_text_k_1);
        num_K = findViewById(R.id.edit_text_K5);
        num_N = findViewById(R.id.edit_text_N);
        num_Y = findViewById(R.id.edit_text_Y);

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
                                createPDFFile(Common.getAppPath(AbsolutePermeabilityActivity.this)+"test_pdf.pdf");
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

                if(num_k.getText().toString().equals("") && !num_K.getText().toString().equals("")
                        && !num_N.getText().toString().equals("") && !num_Y.getText().toString().equals("")) {

                    computed = true;


                    K = Double.parseDouble(num_K.getText().toString());
                    N = Double.parseDouble(num_N.getText().toString());
                    Y = Double.parseDouble(num_Y.getText().toString());



                    k = (K * N) / Y;
                    missing.setText("The missing variable is k");
                    answer.setText("Which has a value of : \n" + k  + " cm/sec");

                    btn_print.setEnabled(computed);
                    //missing.setText("i is missing which has a value of: ");
                    // answer.setText(toString().valueOf(i));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_k);

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
                                if(item.equals("m/min")){
                                    k_min= k * 0.6;
                                    converted.setText(k_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    k_hr = k * 3600;
                                    converted.setText(k_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    k_day = k * 864;
                                    converted.setText(k_day + " m/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_K.getText().toString().equals("") && !num_k.getText().toString().equals("")
                        && !num_N.getText().toString().equals("") && !num_Y.getText().toString().equals("")){

                    computed = true;

                    k = Double.parseDouble(num_k.getText().toString());
                    N = Double.parseDouble(num_N.getText().toString());
                    Y = Double.parseDouble(num_Y.getText().toString());


                    K = (k * Y) / N;

                    missing.setText("The missing variable is K");
                    answer.setText("Which has a value of : " + K + " cm/sec");

                    btn_print.setEnabled(computed);

                    //missing.setText("h is missing which has a value of: ");
                    //answer.setText(toString().valueOf(h));

                    //attaching data adapter to spinner
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
                                if(item.equals("m/min")){
                                    K_min= K * 0.6;
                                    converted.setText(K_min + " m/min");
                                } else if (item.equals("m/hr")){
                                    K_hr = K * 3600;
                                    converted.setText(K_hr + " m/hr");
                                } else if (item.equals("m/day")){
                                    K_day = K * 864;
                                    converted.setText(K_day + " m/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });


                } else if(num_N.getText().toString().equals("") && !num_k.getText().toString().equals("")
                        && !num_K.getText().toString().equals("") && !num_Y.getText().toString().equals("")){

                    computed = true;

                    k = Double.parseDouble(num_k.getText().toString());
                    K = Double.parseDouble(num_K.getText().toString());
                    Y = Double.parseDouble(num_Y.getText().toString());


                    N = (k * Y) / K;
                    missing.setText("The missing variable is N");
                    answer.setText("Which has a value of : " + N + "m^2/sec");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_N);

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
                                if(item.equals("cm^2/min")){
                                    N_cm_min= N * 36000000;
                                    converted.setText(k_min + " cm^2/min");
                                } else if (item.equals("cm^2/hr")){
                                    N_cm_hr = N * 600000;
                                    converted.setText(k_hr + " cm^2/hr");
                                } else if (item.equals("mm^2/day")){
                                    N_mm_day = N * (864000000 * 1000);
                                    converted.setText(k_hr + " mm^2/day");
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                } else if(num_Y.getText().toString().equals("") && !num_k.getText().toString().equals("")
                        && !num_K.getText().toString().equals("") && !num_N.getText().toString().equals("")){

                    computed = true;

                    k = Double.parseDouble(num_k.getText().toString());
                    K = Double.parseDouble(num_K.getText().toString());
                    N = Double.parseDouble(num_N.getText().toString());


                    Y = (k * N) / K;
                    missing.setText("The missing variable is Y");
                    answer.setText("Which has a value of : " + Y + "kn/cm^3");

                    btn_print.setEnabled(computed);

                    //missing.setText("L is missing which has a value of: ");
                    //answer.setText(toString().valueOf(L));

                    //attaching data adapter to spinner
                    spinner.setAdapter(dataAdapter_Y);

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
                                if(item.equals("kN/m^3")){
                                    Y_m = Y * 1000000;
                                    converted.setText(Y_m + " kN/m^3");
                                } else if (item.equals("kN/m^3")){
                                    Y_mm = Y * 0.0001;
                                    converted.setText(Y_mm + " kN/m^3");
                                } else if (item.equals("kN/km^3")){
                                    Y_km = Y / (1000000 * 1000000);
                                    converted.setText(Y_km + " kN/km^3");
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
            addNewItem(document,"Absolute Permeability", Element.ALIGN_CENTER,title);

            Font title_k = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for k", Element.ALIGN_CENTER,title_k);

            //Add more
            Font value_k = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_k.getText().toString(),Element.ALIGN_CENTER,value_k);

            Font title_K = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for K", Element.ALIGN_CENTER,title_K);

            Font value_K = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_K.getText().toString(),Element.ALIGN_CENTER,value_K);

            Font title_N = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for N", Element.ALIGN_CENTER,title_N);

            Font value_N = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_N.getText().toString(),Element.ALIGN_CENTER,value_N);

            Font title_Y = new Font(fontName,36.6f,Font.NORMAL,BaseColor.BLACK);
            addNewItem(document,"Value for Y", Element.ALIGN_CENTER,title_Y);

            Font value_Y = new Font(fontName,fontSize,Font.NORMAL,colorAccent);
            addNewItem(document,num_Y.getText().toString(),Element.ALIGN_CENTER,value_Y);




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
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(AbsolutePermeabilityActivity.this,Common.getAppPath(AbsolutePermeabilityActivity.this)+"test_pdf.pdf");
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
