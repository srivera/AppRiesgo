package activities.riesgo.yacare.com.ec.appriesgo.chat.threads;

import java.util.ArrayList;
import java.util.TimerTask;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ArrayAdapter;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.chat.ChatBlueToothActivity;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.MensajeOffLineDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;
import activities.riesgo.yacare.com.ec.appriesgo.util.VariablesUtil;


public class ProcesarMensajeOffScheduledTask extends TimerTask {



	private ChatBlueToothActivity chatBlueToothActivity;


	public ProcesarMensajeOffScheduledTask(ChatBlueToothActivity chatBlueToothActivity) {
		super();
		this.chatBlueToothActivity = chatBlueToothActivity;
	}


	public void run() {
		if(!VariablesUtil.tareaMensajeOffLineActiva) {
			VariablesUtil.tareaMensajeOffLineActiva = true;

			if (chatBlueToothActivity != null && chatBlueToothActivity.getActivity() != null) {
				chatBlueToothActivity.mNewDevicesArrayAdapter = new ArrayAdapter<String>(chatBlueToothActivity.getActivity(), R.layout.device_name);
				chatBlueToothActivity.doDiscovery();

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (chatBlueToothActivity != null && chatBlueToothActivity.getActivity() != null) {
					MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(chatBlueToothActivity.getActivity().getApplicationContext());
					mensajeOffLineDataSource.open();
					//mensajeOffLineDataSource.deleteAllMensajeOffLine();
					ArrayList<MensajeOffLine> mensajes = mensajeOffLineDataSource.getMensajeOffLinePendiente();
					mensajeOffLineDataSource.close();

					if (mensajes != null && mensajes.size() > 0) {
						for (int i = 0; i < chatBlueToothActivity.mNewDevicesArrayAdapter.getCount(); i++) {
							// Get instance of Vibrator from current Context
//						Vibrator mVibrator = (Vibrator) chatBlueToothActivity.getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//						mVibrator.vibrate(1000);


							String info = chatBlueToothActivity.mNewDevicesArrayAdapter.getItem(i);
							Log.d("ENVIANDO", info);
							String address = info.substring(info.length() - 17);
							BluetoothDevice device = chatBlueToothActivity.mBluetoothAdapter.getRemoteDevice(address);
							chatBlueToothActivity.mChatService.connect(device, false, mensajes, mensajeOffLineDataSource, chatBlueToothActivity.mNewDevicesArrayAdapter);

							VariablesUtil.procesandoChat = true;
							do {


							} while (VariablesUtil.procesandoChat);
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			}
			VariablesUtil.tareaMensajeOffLineActiva = false;

		}
	}

	
	// The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                	if(device.getName() != null){
						chatBlueToothActivity.mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                	}
                }
				chatBlueToothActivity.newDevicesListView.setAdapter(chatBlueToothActivity.mNewDevicesArrayAdapter);
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                if (principalActivity.mNewDevicesArrayAdapter.getCount() == 0) {
//                    String noDevices = principalActivity.getResources().getText(R.string.none_found).toString();
//                    principalActivity.mNewDevicesArrayAdapter.add(noDevices);
//                }
            }
        }
    };
	
}