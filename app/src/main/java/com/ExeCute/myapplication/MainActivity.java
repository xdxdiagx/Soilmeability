package com.ExeCute.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GridLayout mainGrid = findViewById(R.id.gridlayout_main);

        setSingleEvent(mainGrid);

        // Button startDiscussion = findViewById(R.id.button_discussion);

       /* startDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDiscussion();
            }
        });*/

    }

    private void setSingleEvent(GridLayout mainGrid) {
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI == 0) {
                        startDiscussion();
                    } else if (finalI == 1){
                        formulas();
                    } else if (finalI == 2){
                        //Toast.makeText(MainActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                        about();
                    } else if (finalI == 3){
                        exitApp();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please set activity for this card item", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void startDiscussion() {
        Intent intent = new Intent(MainActivity.this, DiscussionActivity.class);
        startActivity(intent);
    }

    private void formulas() {
        Intent intent = new Intent(MainActivity.this, FormulaActivity.class);
        startActivity(intent);
    }

    private void exitApp(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to do this ?");
        builder.setCancelable(true);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void about(){
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }
}
