package gjcm.kxf.bule;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import gjcm.kxf.bluetoothdemo.R;

/**
 * Created by kxf on 2016/12/19.
 */
public class MyPrintActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    MyTools tools;
    TextView txtShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_print_layout);

        txtShow = (TextView) findViewById(R.id.my_print_status);


    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("gjcmkxf", Activity.MODE_PRIVATE);
        String dname = sharedPreferences.getString("dName", "");
        Log.i("kxflog", "onStart--------" + dname);
        dname = ("" == dname) ? "无连接" : dname;
        txtShow.setText("检测到：" + dname);
    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        String address = sharedPreferences.getString("dAddress", "");
//        String dname = sharedPreferences.getString("dName", "");
//        Log.i("kxflog", "onRestart--------" + dname);
//        System.out.println("dname------：" + dname + "    " + address);
//        txtShow.setText("   " + dname);
//
//    }

    public void goSetting(View view) {
        startActivity(new Intent(MyPrintActivity.this, EndTest.class));
    }

    //localObject.printBackMoneyOrder(titleText, contentText,moneyText, "", note);
    //localObject.printOrderDetail(titleText,contentText,moneyText, orderNumberStr,note);
    public void printNow(View view) {
        String address = sharedPreferences.getString("dAddress", "");
        String dname = sharedPreferences.getString("dName", "null");
        if ("null" != dname) {
            tools = new MyTools(this, address);
            if (tools.connect()) {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        //(String titleText, String contentText, String orderAm, String orderNumberStr, String note, String payType, String realAm, String youhui,String mendian,String shouyy,String success
                        tools.printRefoundMonery("蓝色的大海","卜桂森","201612200904374338033870","23.63","退款成功","23.63");
//                        tools.printDeatail("266.35", "201612200904374338033870", "不要香菜，不要葱花，不放盐", "支付宝", "236.95", "36.24", "蓝色的大海", "卜桂森", "支付成功");
                    }
                }.start();
            } else {
                Toast.makeText(MyPrintActivity.this, "打印机连接失败，请检查打印机", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(MyPrintActivity.this, "请到打印机界面进行相关设置", Toast.LENGTH_SHORT).show();

    }
}
