package com.xhin.xdu.bluecontrol.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
@SuppressLint("NewApi")
public class DeviceListActivity extends BaseActivity {
    // Debugging
    private static final String TAG = "BluetoothSPP";
    private static final boolean D = true;

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private FloatingActionButton scanButton;

    @Override
    protected String setTitle() {
        return null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//         Setup the window
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        int listId = getIntent().getIntExtra("layout_list", R.layout.activity_device_list);
        setContentView(listId);

        String strBluetoothDevices = getIntent().getStringExtra("bluetooth_devices");
        if(strBluetoothDevices == null)
            strBluetoothDevices = "Bluetooth Devices";
        setTitle(strBluetoothDevices);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        scanButton = (FloatingActionButton) findViewById(app.akexorcist.bluetotohspp.library.R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
            }
        });

        // Initialize array adapters. One for already paired devices
        // and one for newly discovered devices
        int layout_text = getIntent().getIntExtra("layout_text", app.akexorcist.bluetotohspp.library.R.layout.device_name);
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, layout_text);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(app.akexorcist.bluetotohspp.library.R.id.list_devices);

        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "No devices found";
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
        this.finish();
    }

    // Start device discover with the BluetoothAdapter
    private void doDiscovery() {
        if (D) Log.d(TAG, "doDiscovery()");

        // Remove all element from the list
        mPairedDevicesArrayAdapter.clear();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String strNoFound = getIntent().getStringExtra("no_devices_found");
            if(strNoFound == null)
                strNoFound = "No devices found";
            mPairedDevicesArrayAdapter.add(strNoFound);
        }

        // Indicate scanning in the title
        String strScanning = getIntent().getStringExtra("scanning");
        if(strScanning == null)
            strScanning = "Scanning for devices...";
        setProgressBarIndeterminateVisibility(true);
        setTitle(strScanning);

        // Turn on sub-title for new devices
        // findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            if(mBtAdapter.isDiscovering())
                mBtAdapter.cancelDiscovery();

            String strNoFound = getIntent().getStringExtra("no_devices_found");
            if(strNoFound == null)
                strNoFound = "No devices found";
            if(!((TextView) v).getText().toString().equals(strNoFound)) {
                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                // Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    String strNoFound = getIntent().getStringExtra("no_devices_found");
                    if(strNoFound == null)
                        strNoFound = "No devices found";

                    if(mPairedDevicesArrayAdapter.getItem(0).equals(strNoFound)) {
                        mPairedDevicesArrayAdapter.remove(strNoFound);
                    }
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }

                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                String strSelectDevice = getIntent().getStringExtra("select_device");
                if(strSelectDevice == null)
                    strSelectDevice = "Select a device to connect";
                setTitle(strSelectDevice);
            }
        }
    };

}
