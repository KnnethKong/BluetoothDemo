package gjcm.kxf.bule;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import gjcm.kxf.bluetoothdemo.ESCUtil;

/**
 * Created by kxf on 2016/12/19.
 */
public class MyTools {
    private Context context = null;
    private String deviceAddress = null;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter
            .getDefaultAdapter();
    private BluetoothDevice device = null;
    private static BluetoothSocket bluetoothSocket = null;
    private static OutputStream outputStream = null;
    private static final UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private boolean isConnection = false;
    final String[] items = {"复位打印机", "标准ASCII字体", "压缩ASCII字体", "字体不放大",
            "宽高加倍", "取消加粗模式", "选择加粗模式", "取消倒置打印", "选择倒置打印", "取消黑白反显", "选择黑白反显",
            "取消顺时针旋转90°", "选择顺时针旋转90°"};
    final byte[][] byteCommands = {{0x1b, 0x40},// 复位打印机
            {0x1b, 0x4d, 0x00},// 标准ASCII字体
            {0x1b, 0x4d, 0x01},// 压缩ASCII字体
            {0x1d, 0x21, 0x00},// 字体不放大
            {0x1d, 0x21, 0x11},// 宽高加倍
            {0x1b, 0x45, 0x00},// 取消加粗模式
            {0x1b, 0x45, 0x01},// 选择加粗模式
            {0x1b, 0x7b, 0x00},// 取消倒置打印
            {0x1b, 0x7b, 0x01},// 选择倒置打印
            {0x1d, 0x42, 0x00},// 取消黑白反显
            {0x1d, 0x42, 0x01},// 选择黑白反显
            {0x1b, 0x56, 0x00},// 取消顺时针旋转90°
            {0x1b, 0x56, 0x01},// 选择顺时针旋转90°
    };

    public MyTools(Context context, String deviceAddress) {
        this.context = context;
        this.deviceAddress = deviceAddress;
        this.device = this.bluetoothAdapter.getRemoteDevice(this.deviceAddress);

    }

    /**
     * 获取设备名称
     *
     * @return String
     */
    public String getDeviceName() {
        return this.device.getName();
    }

    /**
     * 连接蓝牙设备
     */
    public boolean connect() {
        if (!this.isConnection) {
            try {
                bluetoothSocket = this.device
                        .createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                this.isConnection = true;
                if (this.bluetoothAdapter.isDiscovering()) {
                    System.out.println("关闭适配器！");
                    this.bluetoothAdapter.isDiscovering();
                }
            } catch (Exception e) {
                Toast.makeText(this.context, "连接失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
            Toast.makeText(this.context, this.device.getName() + "连接成功！",
                    Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return true;
        }
    }

    /**
     * 断开蓝牙设备连接
     */
    public static void disconnect() {
        System.out.println("断开蓝牙设备连接");
        try {
            bluetoothSocket.close();
            if (outputStream != null)
                outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 选择指令
     */
    public void selectCommand() {
        new AlertDialog.Builder(context).setTitle("请选择指令")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isConnection) {
                            try {
                                outputStream.write(byteCommands[which]);

                            } catch (IOException e) {
                                Toast.makeText(context, "设置指令失败！",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "设备未连接，请重新连接！",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).create().show();
    }

    /**
     * 发送数据
     */
    public void send(String sendData) {
        if (this.isConnection) {
            System.out.println("开始打印！！");
            try {
                byte[] data = sendData.getBytes("gbk");
                outputStream.write(data, 0, data.length);
                outputStream.flush();
            } catch (IOException e) {
                Toast.makeText(this.context, "发送失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(this.context, "设备未连接，请重新连接！", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //String paramString1, String paramString2, String paramString3, String paramString4, String paramString5
    public void printRefoundMonery(String mendian, String shouyy, String orderNumberStr, String orderAm, String success, String ztuikuan) {
        if (this.isConnection) {
            try {
                byte[] left = ESCUtil.alignLeft();
                outputStream.write(left);
                ztuikuan = "总退款：" + ztuikuan + "\n\n";
                orderNumberStr = "订单编号：\n" + orderNumberStr + "\n";
                orderAm = "订单金额：" + orderAm + "\n";
                mendian = "门店名：" + mendian + "\n";
                String fuk = "********** 退款凭证 **********\n\n";
                shouyy = "收银员：" + shouyy + "\n";
                success = "退款状态：" + success + "\n";
                outputStream.write(fuk.getBytes("gbk"));
                outputStream.write(mendian.getBytes("gbk"));
                outputStream.write(shouyy.getBytes("gbk"));
                outputStream.write(orderNumberStr.getBytes("gbk"));
                outputStream.write(success.getBytes("gbk"));
                outputStream.write(orderAm.getBytes("gbk"));
                outputStream.write(ztuikuan.getBytes("gbk"));
                String note = "********** 结束 **********\n\n\n\n";
                outputStream.write(note.getBytes("gbk"));
            } catch (Exception paramString1) {
                Toast.makeText(this.context, "发送失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this.context, "蓝牙未连接，请重新连接", Toast.LENGTH_SHORT).show();
    }

    public void printDeatail(String orderAm, String orderNumberStr, String note, String payType, String realAm, String youhui, String mendian, String shouyy, String success) {
        if (this.isConnection) {
            try {
                byte[] left = ESCUtil.alignLeft();
                outputStream.write(left);
                payType = "支付方式：" + payType + "\n";
                realAm = "实际支付：" + realAm + "\n\n";
                orderNumberStr = "订单编号：\n" + orderNumberStr + "\n";
                orderAm = "订单金额：" + orderAm + "\n";
                youhui = "优惠金额：" + youhui + "\n";
                mendian = "门店名：" + mendian + "\n";
                String fuk = "********** 付款凭证 **********\n\n";
                shouyy = "收银员：" + shouyy + "\n";
                success = "支付状态：" + success + "\n";
                note = "备注：" + note + "\n\n";
                outputStream.write(fuk.getBytes("gbk"));
                outputStream.write(mendian.getBytes("gbk"));
                outputStream.write(shouyy.getBytes("gbk"));
                outputStream.write(orderNumberStr.getBytes("gbk"));
                outputStream.write(success.getBytes("gbk"));
                outputStream.write(payType.getBytes("gbk"));
                outputStream.write(orderAm.getBytes("gbk"));
                outputStream.write(youhui.getBytes("gbk"));
                outputStream.write(realAm.getBytes("gbk"));
                outputStream.write(note.getBytes("gbk"));
                note = "********** 结束 **********\n\n\n";
                outputStream.write(note.getBytes("gbk"));
            } catch (Exception paramString1) {
                Toast.makeText(this.context, "发送失败", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this.context, "蓝牙未连接，请重新连接", Toast.LENGTH_SHORT).show();
    }
}