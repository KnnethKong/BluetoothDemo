package com.inuker.bluetooth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import gjcm.kxf.bluetoothdemo.R;
/**
 * Created by dingjikerbo on 2016/9/7.
 */
public class TestActivity extends Activity implements View.OnClickListener {

    private static final String MAC1 = "80:EA:CA:00:00:72";
    private static final String MAC2 = "CF:3B:1E:11:8E:21";

    private Button mBtn1;
    private Button mBtn2;
    private Button mBtn3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);

        mBtn1.setOnClickListener(this);
        mBtn2.setOnClickListener(this);
        mBtn3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
//                ZenTone.getInstance().generate(40000, 10000, 100, new ToneStoppedListener() {
//                    @Override
//                    public void onToneStopped() {
//                        // Do something when the tone has stopped playing
//                        Toast.makeText(TestActivity.this, "stopped", Toast.LENGTH_SHORT).show();
//                    }
//                });
                break;

            case R.id.btn2:
//                ZenTone.getInstance().stop();
                break;

            case R.id.btn3:

                break;
        }
    }
}
