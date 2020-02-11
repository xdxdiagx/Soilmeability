package com.ExeCute.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;


public class FormulaActivity extends AppCompatActivity {

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);

        GridLayout mainGrid = findViewById(R.id.gridlayout_formulas);

        setSingleEvent(mainGrid);

        btn_back = findViewById(R.id.button_back_formula);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FormulaActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                      //  Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, HydraulicGradientActivity.class);
                        startActivity(i);
                    } else if (finalI == 1){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, DischargeVelocityActivity.class);
                        startActivity(i);
                    } else if (finalI == 2){
                       // Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, FlowRateActivity.class);
                        startActivity(i);
                    } else if (finalI == 3){
                        // Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, SeePageVelocityActivity.class);
                        startActivity(i);
                    } else if (finalI == 4){
                        // Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, ConstantHeadActivity.class);
                        startActivity(i);
                    } else if (finalI == 5){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, FallingHeadActivity.class);
                        startActivity(i);
                    } else if (finalI == 6){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, EquivalentVerticalActivity.class);
                        startActivity(i);
                    } else if (finalI == 7){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, EquivalentHorizontalActivity.class);
                        startActivity(i);
                    } else if (finalI == 8){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, AbsolutePermeabilityActivity.class);
                        startActivity(i);
                    } else if (finalI == 9){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, AquiferActivity.class);
                        startActivity(i);
                    } else if (finalI == 10){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, EquivalentHorizontalConductivityActivity.class);
                        startActivity(i);
                    } else if (finalI == 11){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, EquivalentVerticalConductivityActivity.class);
                        startActivity(i);
                    } else if (finalI == 12){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, LeakageFactorActivity.class);
                        startActivity(i);
                    } else if (finalI == 13){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, TransmissivityActivity.class);
                        startActivity(i);
                    } else if (finalI == 14){
                        //Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormulaActivity.this, UnconfinedAquiferActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(FormulaActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
