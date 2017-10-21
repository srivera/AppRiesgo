package activities.riesgo.yacare.com.ec.appriesgo.chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import activities.riesgo.yacare.com.ec.appriesgo.R;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.ContactoArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.adapter.MensajeArrayAdapter;
import activities.riesgo.yacare.com.ec.appriesgo.app.DatosAplicacion;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.EnviarMensajeAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.GuardarContactoAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerContactosAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerPersonaAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.asynctask.ObtenerReferenciasAsyncTask;
import activities.riesgo.yacare.com.ec.appriesgo.chat.threads.ProcesarMensajeOffScheduledTask;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.ContactoDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.datasource.local.MensajeOffLineDataSource;
import activities.riesgo.yacare.com.ec.appriesgo.dto.Contacto;
import activities.riesgo.yacare.com.ec.appriesgo.dto.MensajeOffLine;
import activities.riesgo.yacare.com.ec.appriesgo.evacuacion.EvacuacionFragment;
import activities.riesgo.yacare.com.ec.appriesgo.general.PrincipalActivity;
import activities.riesgo.yacare.com.ec.appriesgo.util.BaseContainerFragment;

public class ChatBlueToothActivity extends Fragment {


    private Button btnRegresarChat;

    //Cabecera
    private TextView tituloChatEmergencia;
    private Button btnContactos;
    private Button btnChat;

    private Boolean tabContactoSeleccionado = true;

    private Typeface fontRegular;
    private Typeface fontBold;

    private LinearLayout layoutMensajes;
    private LinearLayout layoutContacto;

    private EditText editCedulaContacto;
    private Button btnBuscarContacto;

    private EditText editMensajeOffLine;
    private Button btnEnviarChatSolidario;

    private String numeroDocumentoBusqueda;
    private String idContactoAgregar;
    private String nombreContactoAgregar;
    private String mensaje;


    private ListView contactosConfianza;

    private ArrayList<Contacto> contactosPrincipales;
    private ArrayList<Contacto> contactosCompletos;

    private ArrayList<MensajeOffLine> mensajesPropios;

    private static final String TAG = "ChatBlueToothActivity";
    private static final boolean D = true;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;

    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;


    // Layout Views
    private ListView mConversationView;

    //COnversacion ofline
    // Local Bluetooth adapter
    public BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    public BluetoothChatService mChatService = null;

    public ArrayAdapter<String> mNewDevicesArrayAdapter;

    private DatosAplicacion aplicacion;

    public ListView newDevicesListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_chat_bluetooth, container, false);

        aplicacion = (DatosAplicacion) getActivity().getApplicationContext();

        aplicacion.setChatBlueToothActivity(ChatBlueToothActivity.this);

        mNewDevicesArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.device_name);

        fontRegular = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Regular.ttf");
        fontBold = Typeface.createFromAsset(getActivity().getAssets(), "Lato-Bold.ttf");

        btnContactos = (Button) rootView.findViewById(R.id.btnContactos);
        btnChat = (Button) rootView.findViewById(R.id.btnChat);
        tituloChatEmergencia = (TextView) rootView.findViewById(R.id.tituloChatEmergencia);

        layoutMensajes = (LinearLayout) rootView.findViewById(R.id.layoutMensajes);
        layoutContacto = (LinearLayout) rootView.findViewById(R.id.layoutContacto);


        btnContactos.setTypeface(fontBold);
        btnChat.setTypeface(fontRegular);
        tituloChatEmergencia.setTypeface(fontRegular);
        layoutContacto.setVisibility(View.VISIBLE);
        layoutMensajes.setVisibility(View.GONE);

        btnContactos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnContactos.setTypeface(fontBold);
                btnChat.setTypeface(fontRegular);
                tabContactoSeleccionado = true;
                layoutContacto.setVisibility(View.VISIBLE);
                layoutMensajes.setVisibility(View.GONE);
                ContactoArrayAdapter contactoArrayAdapter = new ContactoArrayAdapter(getActivity().getApplicationContext(), contactosPrincipales, ChatBlueToothActivity.this);
                contactosConfianza.setAdapter(contactoArrayAdapter);
            }
        });

        btnChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChat.setTypeface(fontBold);
                btnContactos.setTypeface(fontRegular);
                tabContactoSeleccionado = false;
                layoutContacto.setVisibility(View.GONE);
                layoutMensajes.setVisibility(View.VISIBLE);
                editMensajeOffLine.setEnabled(true);
                btnEnviarChatSolidario.setEnabled(true);
                btnEnviarChatSolidario.setFocusable(true);
            }
        });


        btnRegresarChat = (Button) rootView.findViewById(R.id.btnRegresarChat);
        btnRegresarChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                EvacuacionFragment fragment = new EvacuacionFragment();
                ((BaseContainerFragment) getParentFragment()).replaceFragment(fragment, true);

            }
        });


        btnEnviarChatSolidario = (Button) rootView.findViewById(R.id.btnEnviarChatSolidario);
        btnEnviarChatSolidario.setEnabled(true);

        editMensajeOffLine = (EditText) rootView.findViewById(R.id.editMensajeOffLine);

        btnBuscarContacto = (Button) rootView.findViewById(R.id.btnBuscarContacto);
        editCedulaContacto = (EditText) rootView.findViewById(R.id.editCedulaContacto);

        btnBuscarContacto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactosPrincipales.size() < 5) {
                    if (editCedulaContacto.getText() != null && !editCedulaContacto.getText().toString().equals("")
                            && !editCedulaContacto.getText().toString().equals(aplicacion.getCuenta().getNumeroDocumento())) {
                        //Buscar contacto
                        numeroDocumentoBusqueda = editCedulaContacto.getText().toString();

                        ObtenerPersonaAsyncTask obtenerPersonaAsyncTask = new ObtenerPersonaAsyncTask(getActivity().getApplicationContext(), ChatBlueToothActivity.this);
                        obtenerPersonaAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    } else {
                        editCedulaContacto.setError("Ingrese el # de Identificación de su contacto");
                    }
                }else{

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder.setTitle("INFORMACIÓM")
                            .setMessage("Sólo está permitido 5 contactos de emergencia")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        contactosConfianza = (ListView) rootView.findViewById(R.id.contactosConfianza);
        ContactoDataSource contactoDataSource = new ContactoDataSource(getActivity().getApplicationContext());
        contactoDataSource.open();
        contactosPrincipales = contactoDataSource.getAllContactoByTipo("P");
        contactosCompletos = contactoDataSource.getAllContacto();
        ContactoArrayAdapter contactoArrayAdapter = new ContactoArrayAdapter(getActivity().getApplicationContext(), contactosPrincipales, ChatBlueToothActivity.this);
        contactoDataSource.close();
        contactosConfianza.setAdapter(contactoArrayAdapter);

        ObtenerContactosAsyncTask obtenerContactosAsyncTask = new ObtenerContactosAsyncTask(getActivity().getApplicationContext(), ChatBlueToothActivity.this);
        obtenerContactosAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        ObtenerReferenciasAsyncTask obtenerReferenciasAsyncTask = new ObtenerReferenciasAsyncTask(getActivity().getApplicationContext(), ChatBlueToothActivity.this);
        obtenerReferenciasAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        mConversationView = (ListView) rootView.findViewById(R.id.mensajesSolidarios);
        MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(getActivity().getApplicationContext());
        mensajeOffLineDataSource.open();
        mensajesPropios = mensajeOffLineDataSource.getAllMensajes();
        MensajeArrayAdapter mensajeArrayAdapter = new MensajeArrayAdapter(getActivity().getApplicationContext(), mensajesPropios, ChatBlueToothActivity.this);
        mensajeOffLineDataSource.close();
        mConversationView.setAdapter(mensajeArrayAdapter);

        newDevicesListView = (ListView) rootView.findViewById(R.id.new_devices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        try {
            // Register for broadcasts when a device is discovered
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getActivity().registerReceiver(mReceiver, filter);

            // Register for broadcasts when discovery has finished
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            getActivity().registerReceiver(mReceiver, filter);

            doDiscovery();
        } catch (Exception e) {
            e.printStackTrace();
        }

         newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    public void doDiscovery() {

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }


    private void setupChat() {
        Log.d(TAG, "setupChat()");
        btnEnviarChatSolidario.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                mensaje = editMensajeOffLine.getText().toString();
                sendMessage(mensaje);


            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity().getApplicationContext(), mHandler);
        mChatService.start();
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        ensureDiscoverable();

        Timer time = new Timer();
        ProcesarMensajeOffScheduledTask st = new ProcesarMensajeOffScheduledTask(ChatBlueToothActivity.this);
        time.schedule(st, 0, 20000);

    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        if (message.length() > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            MensajeOffLine mensajeOffLine = new MensajeOffLine();
            mensajeOffLine.setId(UUID.randomUUID().toString());
            mensajeOffLine.setAddressBlueTooth(null);
            mensajeOffLine.setEstado("PEN");
            mensajeOffLine.setFecha(dateFormat.format(date));
            mensajeOffLine.setIdContacto(aplicacion.getCuenta().getId());
            mensajeOffLine.setMensaje(aplicacion.getCuenta().getPrimerNombre() + " " + aplicacion.getCuenta().getPrimerApellido() + ": " + message);
            MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(getActivity());
            mensajeOffLineDataSource.open();
            mensajeOffLineDataSource.createMensajeOffLineNew(mensajeOffLine);
            mensajesPropios = mensajeOffLineDataSource.getAllMensajes();
            MensajeArrayAdapter mensajeArrayAdapter = new MensajeArrayAdapter(getActivity().getApplicationContext(), mensajesPropios, ChatBlueToothActivity.this);
            mensajeOffLineDataSource.close();
            mConversationView.setAdapter(mensajeArrayAdapter);

            editMensajeOffLine.setText("");

            ArrayList<MensajeOffLine> mensajes = new ArrayList<MensajeOffLine>();
            mensajes.add(mensajeOffLine);
            EnviarMensajeAsyncTask enviarMensajeAsyncTask = new EnviarMensajeAsyncTask(ChatBlueToothActivity.this, mensajes, (PrincipalActivity)getActivity() );
            enviarMensajeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            btnEnviarChatSolidario.setEnabled(true);
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
                    if (device.getName() != null && ((PrincipalActivity) getActivity()) != null && mNewDevicesArrayAdapter != null) {
                        mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
                if (((PrincipalActivity) getActivity()) != null) {
                    newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//	                if (principalActivity.mNewDevicesArrayAdapter.getCount() == 0) {
//	                    String noDevices = principalActivity.getResources().getText(R.string.none_found).toString();
//	                    principalActivity.mNewDevicesArrayAdapter.add(noDevices);
//	                }
            }
        }
    };


    private void ensureDiscoverable() {
        if (D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(discoverableIntent);
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatService.MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
//	                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//	                    mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
//	                    setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
//	                    setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case BluetoothChatService.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
//	                mConversationArrayAdapter.add("Yo:  " + writeMessage);
                    break;
                case BluetoothChatService.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                   procesarMensaje(readMessage);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    // Get instance of Vibrator from current Context
//                    Vibrator mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
//                    mVibrator.vibrate(1000);
                    break;
                case BluetoothChatService.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothChatService.DEVICE_NAME);
                    Toast.makeText(getActivity().getApplicationContext(), "Conectado a  "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatService.MESSAGE_TOAST:
                    Toast.makeText(getActivity().getApplicationContext(), msg.getData().getString(BluetoothChatService.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void procesarMensaje(String readMessage) {
        ArrayList<MensajeOffLine> mensajesLista = new ArrayList<MensajeOffLine>();
        String[] mensajes = readMessage.split("!");
        for (int i = 0; i < mensajes.length; i++) {
            String[] mensaje = mensajes[i].split(";");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            MensajeOffLine mensajeOffLine = new MensajeOffLine();
            mensajeOffLine.setId(UUID.randomUUID().toString());
            mensajeOffLine.setEstado("PEN");
            mensajeOffLine.setFecha(dateFormat.format(date));
            mensajeOffLine.setIdContacto(mensaje[0]);
            mensajeOffLine.setMensaje(mensaje[1]);

            Boolean esPropio =false;

           for (Contacto contacto : contactosCompletos) {
                if (contacto.getId().trim().equals(mensajeOffLine.getIdContacto()) && !aplicacion.getCuenta().getId().trim().equals(mensajeOffLine.getIdContacto())) {
                    esPropio = true;
                    break;
                }

            }

            if(!esPropio){
                mensajeOffLine.setEstado("PET");
                MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(getActivity());
                mensajeOffLineDataSource.open();
                mensajeOffLineDataSource.createMensajeOffLineNew(mensajeOffLine);
                mensajeOffLineDataSource.close();
            }else{
                MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(getActivity());
                mensajeOffLineDataSource.open();
                mensajeOffLineDataSource.createMensajeOffLineNew(mensajeOffLine);
                mensajesPropios = mensajeOffLineDataSource.getAllMensajes();
                MensajeArrayAdapter mensajeArrayAdapter = new MensajeArrayAdapter(getActivity().getApplicationContext(), mensajesPropios, ChatBlueToothActivity.this);
                mensajeOffLineDataSource.close();
                mConversationView.setAdapter(mensajeArrayAdapter);
            }


            mensajesLista.add(mensajeOffLine);
        }

        EnviarMensajeAsyncTask enviarMensajeAsyncTask = new EnviarMensajeAsyncTask(ChatBlueToothActivity.this, mensajesLista, (PrincipalActivity) getActivity());
        enviarMensajeAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void verificarObtenerPersona(String respuesta) {
        if (!respuesta.equals("ERR")) {
            Boolean statusFlag = null;
            try {
                JSONObject respuestaJSON = new JSONObject(respuesta);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (statusFlag != null && statusFlag) {
                String idCuenta = null;
                try {
                    JSONObject respuestaJSON = new JSONObject(respuesta);
                    JSONObject respuestaId = new JSONObject(respuestaJSON.getString("resultado"));
                    idContactoAgregar = respuestaId.getString("id");
                    Boolean agregarContacto = true;
                    for (Contacto contacto : contactosPrincipales) {
                        if (contacto.getId().equals(idContactoAgregar)) {
                            agregarContacto = false;
                            break;
                        }
                    }

                    if (agregarContacto) {
                        nombreContactoAgregar = respuestaId.getString("nombre") + " " + respuestaId.getString("apellidopaterno");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("CONFIRMACIÓN")
                                .setMessage("Desea agregar a  " + respuestaId.getString("nombre") + " " + respuestaId.getString("apellidopaterno") + " a sus contactos?")
                                .setCancelable(false)
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {


                                    }
                                })
                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        GuardarContactoAsyncTask guardarContactoAsyncTask = new GuardarContactoAsyncTask(ChatBlueToothActivity.this, "A");
                                        guardarContactoAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        editCedulaContacto.setText("");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getActivity());
                        alertDialogBuilder.setTitle("INFORMACIÓN")
                                .setMessage(respuestaId.getString("nombre") + " " + respuestaId.getString("apellidopaterno") + " ya pertenece a su lista de contactos")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("ERROR")
                        .setMessage("No existe una persona registrada con esa identificación")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ;
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            //Error general
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder.setTitle("ERROR")
                    .setMessage("Vuelva a intentar en un momento")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    public void verificarAgregarContacto(String respuesta, String accion) {
        if (!respuesta.equals("ERR")) {
            Boolean statusFlag = null;
            try {
                JSONObject respuestaJSON = new JSONObject(respuesta);
                statusFlag = respuestaJSON.getBoolean("statusFlag");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (statusFlag != null && statusFlag) {
                if (accion.equals("A")) {
                    Contacto contacto = new Contacto();
                    contacto.setId(idContactoAgregar);
                    contacto.setNombre(nombreContactoAgregar);
                    contacto.setTipo("P");
                    ContactoDataSource contactoDataSource = new ContactoDataSource(getActivity());
                    contactoDataSource.open();
                    contactoDataSource.createContacto(contacto);
                    contactosPrincipales = contactoDataSource.getAllContactoByTipo("P");
                    contactosCompletos = contactoDataSource.getAllContacto();
                    ContactoArrayAdapter contactoArrayAdapter = new ContactoArrayAdapter(getActivity().getApplicationContext(), contactosPrincipales, ChatBlueToothActivity.this);
                    contactosConfianza.setAdapter(contactoArrayAdapter);
                    contactoDataSource.close();
                    editCedulaContacto.setText("");


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder.setTitle("INFORMACIÓN")
                            .setMessage("Su contacto ha sido agregado")
                            .setCancelable(false)
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    ContactoDataSource contactoDataSource = new ContactoDataSource(getActivity());
                    contactoDataSource.open();
                    contactoDataSource.deleteContacto(idContactoAgregar);
                    contactosPrincipales = contactoDataSource.getAllContactoByTipo("P");
                    contactosCompletos = contactoDataSource.getAllContacto();
                    ContactoArrayAdapter contactoArrayAdapter = new ContactoArrayAdapter(getActivity().getApplicationContext(), contactosPrincipales, ChatBlueToothActivity.this);
                    contactosConfianza.setAdapter(contactoArrayAdapter);
                    contactoDataSource.close();
                    editCedulaContacto.setText("");

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder.setTitle("INFORMACIÓN")
                            .setMessage("Su contacto ha sido eliminado")
                            .setCancelable(false)
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }


            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getActivity());
                alertDialogBuilder.setTitle("ERROR")
                        .setMessage("Vuelva a intentar en un momento, por favor")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                ;
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        } else {
            //Error general
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getActivity());
            alertDialogBuilder.setTitle("ERROR")
                    .setMessage("Vuelva a intentar en un momento")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    public void verificarEnviarMensaje(String respuesta) {
        if (respuesta.equals("ERR")) {
            ProcesarMensajeOffScheduledTask st = new ProcesarMensajeOffScheduledTask(ChatBlueToothActivity.this);
            st.run();
        }
    }

    public String getNumeroDocumentoBusqueda() {
        return numeroDocumentoBusqueda;
    }

    public void setNumeroDocumentoBusqueda(String numeroDocumentoBusqueda) {
        this.numeroDocumentoBusqueda = numeroDocumentoBusqueda;
    }

    public String getIdContactoAgregar() {
        return idContactoAgregar;
    }

    public void setIdContactoAgregar(String idContactoAgregar) {
        this.idContactoAgregar = idContactoAgregar;
    }

    public void verificarContactos() {
        if (layoutContacto.getVisibility() == View.VISIBLE) {
            ContactoDataSource contactoDataSource = new ContactoDataSource(getActivity());
            contactoDataSource.open();
            contactoDataSource.deleteContacto(idContactoAgregar);
            contactosPrincipales = contactoDataSource.getAllContactoByTipo("P");
            contactosCompletos = contactoDataSource.getAllContacto();
            ContactoArrayAdapter contactoArrayAdapter = new ContactoArrayAdapter(getActivity().getApplicationContext(), contactosPrincipales, ChatBlueToothActivity.this);
            contactosConfianza.setAdapter(contactoArrayAdapter);
            contactoDataSource.close();
        }
    }


    public void verificarMensajes() {
        if (layoutMensajes.getVisibility() == View.VISIBLE) {
            MensajeOffLineDataSource mensajeOffLineDataSource = new MensajeOffLineDataSource(getActivity().getApplicationContext());
            mensajeOffLineDataSource.open();
            mensajesPropios = mensajeOffLineDataSource.getAllMensajes();
            MensajeArrayAdapter mensajeArrayAdapter = new MensajeArrayAdapter(getActivity().getApplicationContext(), mensajesPropios, ChatBlueToothActivity.this);
            mensajeOffLineDataSource.close();
            mConversationView.setAdapter(mensajeArrayAdapter);
        }
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    @Override
    public void onPause() {
        super.onPause();
        BluetoothAdapter  myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter.isEnabled() ) {
            mChatService.stop();
            try {
                getActivity().unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BluetoothAdapter  myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter.isEnabled() ) {
            mChatService.stop();
            try {
                getActivity().unregisterReceiver(mReceiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BluetoothAdapter  myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (myBluetoothAdapter.isEnabled() ) {

            mChatService = new BluetoothChatService(getActivity().getApplicationContext(), mHandler);
            mChatService.start();
            try {
                // Register for broadcasts when a device is discovered
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                getActivity().registerReceiver(mReceiver, filter);

                // Register for broadcasts when discovery has finished
                filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                getActivity().registerReceiver(mReceiver, filter);

                doDiscovery();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}