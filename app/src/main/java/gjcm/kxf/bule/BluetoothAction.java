package gjcm.kxf.bule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import gjcm.kxf.bluetoothdemo.R;


public class BluetoothAction implements OnClickListener {

    private Button switchBT = null;
    private Button searchDevices = null;
    private Activity activity = null;

    private ListView unbondDevices = null;
    private ListView bondDevices = null;
    private Context context = null;
    private BluetoothService bluetoothService = null;
    private Button openSeeting = null;

    public BluetoothAction(Context context, ListView unbondDevices,
                           ListView bondDevices, Button switchBT, Button searchDevices, Button openSeeting,
                           Activity activity) {
        super();
        this.context = context;
        this.openSeeting = openSeeting;
        this.unbondDevices = unbondDevices;
        this.bondDevices = bondDevices;
        this.switchBT = switchBT;
        this.searchDevices = searchDevices;
        this.activity = activity;
        this.bluetoothService = new BluetoothService(this.context,
                this.unbondDevices, this.bondDevices, this.switchBT,
                this.searchDevices);
    }

    public void setSwitchBT(Button switchBT) {
        this.switchBT = switchBT;
    }

    public void setSearchDevices(Button searchDevices) {
        this.searchDevices = searchDevices;
    }

    public void setUnbondDevices(ListView unbondDevices) {
        this.unbondDevices = unbondDevices;
    }

    /**
     * 初始化界面
     */
    public void initView() {

        if (this.bluetoothService.isOpen()) {
            System.out.println("蓝牙有开!");
            switchBT.setText("关闭蓝牙");
        }
        if (!this.bluetoothService.isOpen()) {
            System.out.println("蓝牙没开!");
            this.searchDevices.setEnabled(false);
        }
    }

    private void searchDevices() {
        bluetoothService.searchDevices();
    }

    /**
     * 各种按钮的监听
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchDevices:
                this.searchDevices();

                break;
            case R.id.return_Bluetooth_btn:
                activity.finish();
                break;
            case R.id.openBluetooth_tb:
                if (!this.bluetoothService.isOpen()) {
                    // 蓝牙关闭的情况
                    System.out.println("蓝牙关闭的情况");
                    this.bluetoothService.openBluetooth(activity);
                } else {
                    // 蓝牙打开的情况
                    System.out.println("蓝牙打开的情况");
                    this.bluetoothService.closeBluetooth();

                }
                break;
            case R.id.bluetooth_open:
                context.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                break;
        }

        if (v.getId() == R.id.searchDevices) {
        } else if (v.getId() == R.id.return_Bluetooth_btn) {

        } else if (v.getId() == R.id.openBluetooth_tb) {


        }
    }

}
