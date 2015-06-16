package com.amoure.mycalc;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private boolean doClear = false;
    private Button latestOperation = null;
    private int latestNumber = 0;
    private String currentDisplay = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //registrasi GCM
        new GcmRegistrationAsyncTask(this).execute();

        Button btnAC = (Button) findViewById(R.id.btnAC);
        btnAC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allClear();
            }
        });

        Button btnSum = (Button) findViewById(R.id.btnSum);
        btnSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOperation(currentDisplay+"=");
                calculate();
                setOperation(currentDisplay);

                latestOperation = null;
                latestNumber = 0;
            }
        });
    }

    public void btnOperation_Click(View v){
        //tambahkan kedalam operation value yang ada didalam display + operation yang sedang ditekan
        String tmpCurrentDisplay = currentDisplay;
        if(latestOperation==null){
            allClear();
            currentDisplay = tmpCurrentDisplay;
        }
        setOperation(currentDisplay);

        String newOperation = ((Button) v).getText().toString();
        setOperation(newOperation);

        calculate();

        latestOperation = ((Button) v);
        latestOperation.setActivated(true);
        latestNumber = Integer.parseInt(currentDisplay.replace(",", ""));
        doClear = true;
    }

    public void btnNumber_Click(View v){
        //ambil angka yang ada diproperty text
        String angka = ((Button) v).getText().toString();

        if(doClear) clearDisplay();

        if (currentDisplay.length() < 15) {
            currentDisplay = currentDisplay.replace(",", "");
            //jika display menunjukan angka 0 maka clear terlebih dahulu display
            if (currentDisplay.charAt(0) == '0') {
                currentDisplay = "";
            }

            currentDisplay = currentDisplay + angka;
            setDisplay(currentDisplay);
        }

    }

    private void calculate(){
        //hitung operasi yang sebelumnya pernah ditekan
        if(latestOperation!=null){
            String oldOperation = latestOperation.getText().toString();
            int newNumber = Integer.parseInt(currentDisplay.replace(",",""));

            switch (oldOperation.charAt(0)){
                case '+':
                    setDisplay(String.valueOf(latestNumber + newNumber));
                    break;
                case '-':
                    setDisplay(String.valueOf(latestNumber - newNumber));
                    break;
                case 'x':
                    setDisplay(String.valueOf(latestNumber * newNumber));
                    break;
                case '/':
                    setDisplay(String.valueOf(latestNumber * newNumber));
                    break;
            }

            //hilangkan status activated untuk operasi yang ditekan sebelumnya
            //kemudian ganti tombol operasi yang lama dengan yang baru
            latestOperation.setActivated(false);
        }
    }

    private void setOperation(String content){
        TextView operation = (TextView) findViewById(R.id.operation);
        operation.setText(operation.getText() + content);
    }

    //mengeset display dengan content yang ditentukan
    private void setDisplay(String content) {
        TextView display = (TextView) findViewById(R.id.display);

        //formatiing display menjadi format ribuah
        int panjangContent = content.length();
        String tmp = "";
        boolean isComma = false;
        for(int a=panjangContent-1; a>=0;a--){
            tmp = content.charAt(a)+ tmp;
            if(!isComma) isComma = (content.charAt(0)=='.'?true:false);
            if((panjangContent-a) % 3 == 0 && a!=0 && !isComma){
                tmp = "," + tmp;
            }
        }
        display.setText(tmp);
        currentDisplay = display.getText().toString();
    }

    private void allClear(){
        clearDisplay();

        TextView operation = (TextView) findViewById(R.id.operation);
        operation.setText("");
        latestNumber=0;
        latestOperation=null;
    }

    private void clearDisplay() {
        TextView display = (TextView) findViewById(R.id.display);
        display.setText("0");
        currentDisplay="0";
        doClear = false;
    }
}
