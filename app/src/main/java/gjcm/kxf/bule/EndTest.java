package gjcm.kxf.bule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import gjcm.kxf.bluetoothdemo.R;

/**
 * Created by kxf on 2016/12/19.
 */
public class EndTest extends AppCompatActivity implements View.OnClickListener {
    private ListView listWPD;//未配对
    private ListView listYPD;//已配对
    private TextView txtYBD;//已绑定
    private LinearLayout txtLine;//jump setting
    private LinearLayout txtSerach;//搜索
    private SharedPreferences preferences;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private ArrayList<BluetoothDevice> unbondDevices = null; // 用于存放未配对蓝牙设备
    private ArrayList<BluetoothDevice> bondDevices = null;// 用于存放已配对蓝牙设备
    private ProgressBar progressbar;
    private TextView txtOpenClose;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_seeting);
        initviewevent();
        initIntentFilter();

    }


    private void initviewevent() {
        preferences = getSharedPreferences("gjcmkxf", Activity.MODE_PRIVATE);
        progressbar = (ProgressBar) findViewById(R.id.blue_seeting_progress);
        listWPD = (ListView) findViewById(R.id.blue_seeting_wl);
        listYPD = (ListView) findViewById(R.id.blue_seeting_yp);
        txtLine = (LinearLayout) findViewById(R.id.blue_seeting_linerlin);
        txtSerach = (LinearLayout) findViewById(R.id.blue_seeting_sousuo);
        txtYBD = (TextView) findViewById(R.id.blue_seeting_yb);
        txtOpenClose = (TextView) findViewById(R.id.blue_seeting_openclose);
        txtLine.setOnClickListener(this);
        txtSerach.setOnClickListener(this);
        txtOpenClose.setOnClickListener(this);
        unbondDevices = new ArrayList<>();
        bondDevices = new ArrayList<>();
        String dname = preferences.getString("dName", "");
        txtYBD.setText("   " + dname);
        if (isOpen())
            txtOpenClose.setText("已打开蓝牙");
        else
            txtOpenClose.setText("已关闭蓝牙，");


    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void breakReturn(View v) {
        this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blue_seeting_linerlin:
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                break;
            case R.id.blue_seeting_sousuo:
                searchDevices();
                progressbar.setVisibility(View.VISIBLE);
                break;
            case R.id.blue_seeting_openclose:
                if (!isOpen()) {
                    // 蓝牙关闭的情况
                    System.out.println("蓝牙关闭的情况");
                    openBluetooth(this);
                } else {
                    // 蓝牙打开的情况
                    System.out.println("蓝牙打开的情况");
                    closeBluetooth();
                }
                break;
        }

    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private void initIntentFilter() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            this.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加未绑定蓝牙设备到ListView
     */
    private void addUnbondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = this.unbondDevices.size();
        System.out.println("未绑定设备数量：" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", this.unbondDevices.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = {"deviceName"};
        int[] to = {R.id.undevice_name};
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data,
                R.layout.unbonddevice_item, from, to);

        // 把适配器装载到listView中
        listWPD.setAdapter(simpleAdapter);

        // 为每个item绑定监听，用于设备间的配对
        listWPD
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        try {
                            Method createBondMethod = BluetoothDevice.class
                                    .getMethod("createBond");
                            createBondMethod
                                    .invoke(unbondDevices.get(arg2));
                            // 将绑定好的设备添加的已绑定list集合
                            bondDevices.add(unbondDevices.get(arg2));
                            // 将绑定好的设备从未绑定list集合中移除
                            unbondDevices.remove(arg2);
                            addBondDevicesToListView();
                            addUnbondDevicesToListView();
                        } catch (Exception e) {
                            Toast.makeText(EndTest.this, "配对失败！", Toast.LENGTH_SHORT)
                                    .show();
                        }

                    }
                });
    }

    /**
     * 添加已绑定蓝牙设备到ListView
     */
    private void addBondDevicesToListView() {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        int count = this.bondDevices.size();
        System.out.println("已绑定设备数量：" + count);
        for (int i = 0; i < count; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deviceName", this.bondDevices.get(i).getName());
            data.add(map);// 把item项的数据加到data中
        }
        String[] from = {"deviceName"};
        int[] to = {R.id.device_name};
        SimpleAdapter simpleAdapter = new SimpleAdapter(EndTest.this, data,
                R.layout.bonddevice_item, from, to);
        // 把适配器装载到listView中
        listYPD.setAdapter(simpleAdapter);
        listYPD
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong) {
                        BluetoothDevice device = bondDevices.get(paramAnonymousInt);
                        SharedPreferences.Editor edit = preferences.edit();
                        String address = device.getAddress();
                        String name = device.getName();
                        edit.putString("dAddress", address);
                        edit.putString("dName", name);
                        txtYBD.setText("  " + name);
                        edit.commit();
                        address = preferences.getString("dAddress", "");
                        name = preferences.getString("dName", "");
                        Log.i("kxflog", "address------->" + address + "-------" + name);
//                        Intent intent = new Intent();
//                        intent.setClassName(EndTest.this,
//                                "gjcm.kxf.bule.PrintDataActivity");
//                        intent.putExtra("deviceAddress", device.getAddress());
//                        startActivity(intent);
                    }
                });

    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(
                BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 1);

    }

    /**
     * 关闭蓝牙
     */
    public void closeBluetooth() {
        this.bluetoothAdapter.disable();
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return boolean
     */
    public boolean isOpen() {
        return this.bluetoothAdapter.isEnabled();

    }

    /**
     * 搜索蓝牙设备
     */
    public void searchDevices() {
        this.bondDevices.clear();
        this.unbondDevices.clear();
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        this.bluetoothAdapter.startDiscovery();
    }

    /**
     * 添加未绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addUnbondDevices(BluetoothDevice device) {
        System.out.println("未绑定设备名称：" + device.getName());
        if (!this.unbondDevices.contains(device)) {
            this.unbondDevices.add(device);
        }
    }

    /**
     * 添加已绑定蓝牙设备到list集合
     *
     * @param device
     */
    public void addBandDevices(BluetoothDevice device) {
        System.out.println("已绑定设备名称：" + device.getName());
        if (!this.bondDevices.contains(device)) {
            this.bondDevices.add(device);
        }
    }

    public void closePrint() {
//        if ((!isOpen()) && (isPrinterOpenCurrent))
//        {
//            this.switchViewPrint.setSwitchStatus(false);
//            this.editor.putBoolean("isPrinterOpenCurrent", false).commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        closePrint();

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        ProgressDialog progressDialog = null;


        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    addBandDevices(device);
                } else {
                    addUnbondDevices(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
//                progressDialog = ProgressDialog.show(context, "请稍等...",
//                        "搜索蓝牙设备中...", true);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                System.out.println("设备搜索完毕");
//                progressDialog.dismiss();
                progressbar.setVisibility(View.INVISIBLE);
                addUnbondDevicesToListView();
                addBondDevicesToListView();
                context.unregisterReceiver(receiver);
                // bluetoothAdapter.cancelDiscovery();
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
                    System.out.println("--------打开蓝牙-----------");
                    txtOpenClose.setText("关闭蓝牙");
                    txtSerach.setEnabled(true);
                    listYPD.setEnabled(true);
                    listWPD.setEnabled(true);
                } else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {//10
                    System.out.println("--------关闭蓝牙-----------");
                    txtOpenClose.setText("打开蓝牙");
                    txtSerach.setEnabled(false);
                    listYPD.setEnabled(false);
                    listWPD.setEnabled(false);
                }
            }

        }

    };


}
