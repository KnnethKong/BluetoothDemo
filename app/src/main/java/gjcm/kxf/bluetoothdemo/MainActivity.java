package gjcm.kxf.bluetoothdemo;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shangmi_main);
		findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// 1: Get BluetoothAdapter
				BluetoothAdapter btAdapter = BluetoothUtil.getBTAdapter();
				if (btAdapter == null) {
					Toast.makeText(getBaseContext(), "Please Open Bluetooth!", Toast.LENGTH_LONG).show();
					return;
				}
				// 2: Get Sunmi's InnerPrinter BluetoothDevice
				BluetoothDevice device = BluetoothUtil.getDevice(btAdapter);
				if (device == null) {
					Toast.makeText(getBaseContext(), "Please Make Sure Bluetooth have InnterPrinter!",
							Toast.LENGTH_LONG).show();
					return;
				}
				// 3: Generate a order data
				byte[] data = ESCUtil.generateMockData();
				// 4: Using InnerPrinter print data
				BluetoothSocket socket = null;
				try {
					socket = BluetoothUtil.getSocket(device);
					BluetoothUtil.sendData(data, socket);
				} catch (IOException e) {
					if (socket != null) {
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
	}

}
