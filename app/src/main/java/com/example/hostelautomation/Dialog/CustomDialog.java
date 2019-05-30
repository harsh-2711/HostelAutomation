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
import android.widget.Toast;

import com.example.hostelautomation.R;

import static android.content.Context.MODE_PRIVATE;

public class CustomDialog extends Dialog {

    private static final String PREFS_NAME = "FanSpeed";
    private static final String ROOM_NAME = "Room";
    private static final String SEPERATOR = "/";

    private static final String LIVING_ROOM_1 = "LR_IP1";
    private static final String LIVING_ROOM_2 = "LR_IP2";
    private static final String MASTER_BEDROOM = "MB_IP1";
    private static final String BEDROOM = "B_IP1";
    private static final String KITCHEN = "K_IP1";

    public Activity c;
    public Dialog d;
    SeekBar seekBar;
    TextView ok, speedText;
    WebView webView;

    String ip = "255.255.255.0";

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

        seekBar = (SeekBar) findViewById(R.id.speedSlider);
        speedText = (TextView) findViewById(R.id.speedText);
        ok = (TextView) findViewById(R.id.OK);

        webView = (WebView) findViewById(R.id.dialogWeb);

        SharedPreferences preferences10 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
        String room = preferences10.getString("room_name", "Living Room");
        String code = "";

        switch (room) {
            case "Living Room":
                code = "hfs";
                break;
            case "Kitchen":
                code = "kfs";
                break;
            case "Master Bedroom":
                code = "r1fs";
                break;
            case "Bedroom":
                code = "r2fs";
                break;
            default:
                break;
        }

        SharedPreferences preferences11 = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String progress = preferences11.getString("fan_speed" + code, "0");
        seekBar.setProgress(Integer.valueOf(progress));
        speedText.setText(progress);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences preferences1 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
                String room = preferences1.getString("room_name", "Living Room");
                String code = "";
                switch (room) {
                    case "Living Room":
                        code = "hfs";
                        SharedPreferences preferences2 = c.getSharedPreferences(LIVING_ROOM_1, MODE_PRIVATE);
                        ip = preferences1.getString("lr_ip1", "255.255.0");
                        break;
                    case "Kitchen":
                        code = "kfs";
                        SharedPreferences preferences3 = c.getSharedPreferences(KITCHEN, MODE_PRIVATE);
                        ip = preferences3.getString("k_ip1", "255.255.0");
                        break;
                    case "Master Bedroom":
                        code = "r1fs";
                        SharedPreferences preferences4 = c.getSharedPreferences(MASTER_BEDROOM, MODE_PRIVATE);
                        ip = preferences4.getString("mb_ip1", "255.255.0");
                        break;
                    case "Bedroom":
                        code = "r2fs";
                        SharedPreferences preferences5 = c.getSharedPreferences(BEDROOM, MODE_PRIVATE);
                        ip = preferences5.getString("b_ip1", "255.255.0");
                        break;
                    default:
                        break;
                }

                SharedPreferences preferences = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String progress = preferences.getString("fan_speed" + code, "0");
                webView.loadUrl("http://" + ip + SEPERATOR + code + progress);
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

                SharedPreferences preferences1 = c.getSharedPreferences(ROOM_NAME, MODE_PRIVATE);
                String room = preferences1.getString("room_name", "Living Room");
                String code = "";
                switch (room) {
                    case "Living Room":
                        code = "hfs";
                        break;
                    case "Kitchen":
                        code = "kfs";
                        break;
                    case "Master Bedroom":
                        code = "r1fs";
                        break;
                    case "Bedroom":
                        code = "r2fs";
                        break;
                    default:
                        break;
                }
                SharedPreferences.Editor editor = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("fan_speed" + code, String.valueOf(progress));
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
