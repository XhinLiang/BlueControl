package com.xhin.xdu.bluecontrol.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.activity.base.BaseActivity;

import java.util.Set;

import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by xhinliang on 15-9-24.
 * xhinliang@gmail.com
 */

public class DeviceListActivity extends BaseActivity {

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;

    @Override
    protected String setTitle() {
        return null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        //默认Result为canceled
        setResult(Activity.RESULT_CANCELED);
        initView();

        //注册发现设备的Receiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        //注册查找设备完成的Receiver
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        //获取本地蓝牙适配器
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        //获取已经适配的蓝牙设备
        pairedDevices = mBtAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + getString(R.string.lf) + device.getAddress());
            }
        } else {
            String noDevices = getString(R.string.no_device_found);
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    private void initView() {
        FloatingActionButton scanButton = (FloatingActionButton) findViewById(R.id.fab_search);
        ListView pairedListView = (ListView) findViewById(R.id.list_devices);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
            }
        });
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
    }

    protected void onDestroy() {
        super.onDestroy();
        //取消搜索设备
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        unregisterReceiver(mReceiver);
        finish();
    }

    private void doDiscovery() {
        mPairedDevicesArrayAdapter.clear();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            mPairedDevicesArrayAdapter.add(getString(R.string.no_device_found));
        }
        setTitle(getString(R.string.scanning));
        //如果正在搜索则停止
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        //开始搜索
        mBtAdapter.startDiscovery();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            //一旦选择设备则停止搜索
            if (mBtAdapter.isDiscovering())
                mBtAdapter.cancelDiscovery();

            String strNoFound = getString(R.string.no_device_found);
            //如果选择的Item不是"未找到"
            if (!((TextView) v).getText().toString().equals(strNoFound)) {
                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                //设置好Result
                Intent intent = new Intent();
                intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //找到一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String strNoFound = getString(R.string.no_device_found);
                    if (mPairedDevicesArrayAdapter.getItem(0).equals(strNoFound)) {
                        mPairedDevicesArrayAdapter.remove(strNoFound);
                    }
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                String strSelectDevice = getString(R.string.select_device);
                setTitle(strSelectDevice);
            }
        }
    };

}
