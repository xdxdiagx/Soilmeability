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

public class DiscussionActivity extends AppCompatActivity {

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        GridLayout mainGrid = findViewById(R.id.gridlayout_discussion);

        setSingleEvent(mainGrid);

        btn_back = findViewById(R.id.button_back_discussion);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DiscussionActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

       // Button btn_book1 = findViewById(R.id.button_book_1);
       // Button btn_book2 = findViewById(R.id.button_book_2);
       // Button btn_book3 = findViewById(R.id.button_book_3);

       /* btn_book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DiscussionActivity.this, Book1.class);
                startActivity(i);
            }
        });

        btn_book2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(DiscussionActivity.this, Book2.class);
                startActivity(i2);
            }
        });

        btn_book3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(DiscussionActivity.this, Book3.class);
                startActivity(i3);
            }
        }); */
    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        Intent i = new Intent(DiscussionActivity.this, Book1.class);
                        startActivity(i);
                    } else if (finalI == 1){
                        Intent i = new Intent(DiscussionActivity.this, Book2.class);
                        startActivity(i);
                    } else if (finalI == 2){
                        Intent i = new Intent(DiscussionActivity.this, Book3.class);
                        startActivity(i);
                    } else if (finalI == 3){
                        Intent i = new Intent(DiscussionActivity.this, Book4_Discharge_Velocity.class);
                        startActivity(i);
                    } else if (finalI == 4){
                        Intent i = new Intent(DiscussionActivity.this, Book5_Computation_of_Permeability_by_Falling_Head.class);
                        startActivity(i);
                    } else if (finalI == 5){
                        Intent i = new Intent(DiscussionActivity.this, Book6_Aquifers.class);
                        startActivity(i);
                    } else if (finalI == 6){
                        Intent i = new Intent(DiscussionActivity.this, Book7_Aquifers_and_Groundwater.class);
                        startActivity(i);
                    } else if (finalI == 7){
                        Intent i = new Intent(DiscussionActivity.this, Book8_Field_Determination_of_Permiability.class);
                        startActivity(i);
                    } else if (finalI == 8){
                        Intent i = new Intent(DiscussionActivity.this, Book9_Permeability.class);
                        startActivity(i);
                    } else if (finalI == 9){
                        Intent i = new Intent(DiscussionActivity.this, Book10_Transmissivity.class);
                        startActivity(i);
                    } else if (finalI == 10){
                        Intent i = new Intent(DiscussionActivity.this, Book11_Terms.class);
                        startActivity(i);
                    } else if (finalI == 11){
                        Intent i = new Intent(DiscussionActivity.this, Book12_Seepage_and_Soil_Permeability.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(DiscussionActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
