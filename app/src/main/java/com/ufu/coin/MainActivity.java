package com.ufu.coin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;

import android.widget.ImageView;
import com.google.android.flexbox.FlexboxLayout;

import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

public class MainActivity extends AppCompatActivity {

    private final double COIN_VALUE = 4.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_total = findViewById(R.id.editText_total);
        layout_coins = findViewById(R.id.layout_coins);
        label_info = findViewById(R.id.label_info);
        label_change = findViewById(R.id.label_change);

        editText_total.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            { calculate(); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }

    private EditText editText_total;
    private FlexboxLayout layout_coins;
    private TextView label_info;
    private TextView label_change;

    private double getValue()
    {
        String str = editText_total.getText().toString();
        return Double.parseDouble(str);
    }

    private void drawCoins(int count)
    {
        layout_coins.removeAllViews();

        for(int i=0; i<count; i++)
        {
            int screen_w = getResources().getDisplayMetrics().widthPixels;

            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.coin);
            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            img.setMaxWidth(screen_w/3);
            img.setAdjustViewBounds(true);
            //img.setLayoutParams(new LinearLayout.LayoutParams(512, 512));

            layout_coins.addView(img);
        }
    }


    private void calculate()
    {
        if(editText_total.getText().length() == 0)
        {
            layout_coins.removeAllViews();
            label_info.setText("");
            label_change.setText("");
            return;
        }

        try
        {
            double total = getValue();
            int numOfCoins = (int) Math.ceil(total / COIN_VALUE);
            double change = COIN_VALUE * numOfCoins - total;

            drawCoins(numOfCoins);

            String info_str = "";
            info_str += numOfCoins + " coin(s)\n";
            info_str += String.format("%.2f", change) + "€ change\n";
            label_info.setText(info_str);
            label_change.setText("Change: " + GetChange.getLeastNumOfCoins(change));
        }
        catch (Exception ex) {}
    }

    public void onClick_CalcBtn(View v)
    {
        calculate();
    }
}

