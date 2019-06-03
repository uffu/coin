package com.ufu.coin;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.flexbox.FlexboxLayout;

import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private final BigDecimal COIN_VALUE = new BigDecimal("4.40");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_total = findViewById(R.id.editText_total);
        layout_coin_images = findViewById(R.id.layout_coin_images);
        label_coins_needed = findViewById(R.id.label_coins_needed);
        label_change = findViewById(R.id.label_change);
        label_change_example = findViewById(R.id.label_change_example);

        editText_total.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count)
            { calculate(); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }
        });
    }

    private EditText editText_total;
    private FlexboxLayout layout_coin_images;
    private TextView label_coins_needed;
    private TextView label_change;
    private TextView label_change_example;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu, menu);
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void drawCoins(int count)
    {
        layout_coin_images.removeAllViews();

        for(int i=0; i<count; i++)
        {
            int screen_w = getResources().getDisplayMetrics().widthPixels;

            ImageView img = new ImageView(this);
            img.setImageResource(R.drawable.coin);
            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            img.setMaxWidth(screen_w/3);
            img.setAdjustViewBounds(true);

            layout_coin_images.addView(img);

            if(i>=7)
            {
                TextView t = new TextView(this);
                t.setText("...");
                t.setTextSize(64);
                layout_coin_images.addView(t);
                break;
            }
        }
    }

    private void clear()
    {
        layout_coin_images.removeAllViews();
        label_coins_needed.setText("");
        label_change.setText("");
        label_change_example.setText("");
    }
    private void calculate()
    {
        if(editText_total.getText().length() == 0)
        {
            clear();
        }
        else {
            try
            {
                Resources res = getResources();
                BigDecimal total = new BigDecimal(editText_total.getText().toString());
                if(total.compareTo(BigDecimal.valueOf(999))>0)
                {
                    clear();
                    label_coins_needed.setText(R.string.total_too_large);
                    return;
                }
                int numOfCoins = total.divide(COIN_VALUE,0, RoundingMode.CEILING).intValue();
                BigDecimal change = COIN_VALUE.multiply(BigDecimal.valueOf(numOfCoins)).subtract(total);

                drawCoins(numOfCoins);

                label_coins_needed.setText(res.getQuantityString(R.plurals.numOfCoinsNeeded, numOfCoins, numOfCoins));
                label_change.setText(res.getString(R.string.change, change));
                label_change_example.setText(res.getString(R.string.change_example,
                        GetChange.getLeastNumOfCoins(change),
                        GetChange.getNumOfOptions(change),
                        GetChange.getAllOptions_str(change)));

            } catch (Exception ignored) { }
        }
    }

    public void onClick_CalcBtn(View v)
    {
        calculate();
    }
}

