package gjcm.kxf.bule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;


import gjcm.kxf.bluetoothdemo.R;

public class BluetoothActivity extends Activity {

    private Context context = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        setTitle("蓝牙打印");
        setContentView(R.layout.bluetooth_layout);
        this.initListener();
        Log.i("kxflog", "onCreate------>");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("kxflog", "onRestart------>");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("kxflog", "onRestart------>");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("kxflog", "onResume------>");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("kxflog", "onResume------>");
    }
    private void initListener() {
        ListView unbondDevices = (ListView) this
                .findViewById(R.id.unbondDevices);
        ListView bondDevices = (ListView) this.findViewById(R.id.bondDevices);
        Button switchBT = (Button) this.findViewById(R.id.openBluetooth_tb);
        Button searchDevices = (Button) this.findViewById(R.id.searchDevices);
        Button openSetting = (Button) this.findViewById(R.id.bluetooth_open);
        BluetoothAction bluetoothAction = new BluetoothAction(this.context,
                unbondDevices, bondDevices, switchBT, searchDevices, openSetting,
                BluetoothActivity.this);

        Button returnButton = (Button) this
                .findViewById(R.id.return_Bluetooth_btn);
        bluetoothAction.setSearchDevices(searchDevices);
        bluetoothAction.initView();

        switchBT.setOnClickListener(bluetoothAction);
        searchDevices.setOnClickListener(bluetoothAction);
        returnButton.setOnClickListener(bluetoothAction);
        openSetting.setOnClickListener(bluetoothAction);
    }

    //屏蔽返回键的代码:
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
