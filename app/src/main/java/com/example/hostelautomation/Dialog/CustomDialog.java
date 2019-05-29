package com.example.hostelautomation.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.hostelautomation.R;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialog extends Dialog {

    private static final String PREFS_NAME = "FanSpeed";
    private static final String SEPERATOR = "/";

    public Activity c;
    public Dialog d;
    SeekBar seekBar;
    TextView ok, speedText;
    WebView webView;

    public CustomDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);

        final String ip = "255.255.255.0";

        seekBar = (SeekBar) findViewById(R.id.speedSlider);
        speedText = (TextView) findViewById(R.id.speedText);
        ok = (TextView) findViewById(R.id.OK);

        webView = (WebView) findViewById(R.id.dialogWeb);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        SharedPreferences preferences = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        final String speed = preferences.getString("fan_speed", "0");

        speedText.setText(speed);
        seekBar.setProgress(Integer.valueOf(speed));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedText.setText(String.valueOf(progress));
                SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("fan_speed", String.valueOf(progress));
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
