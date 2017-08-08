package com.android.technophone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {

    public static final Integer soapParam_timeout = R.integer.soapTimeOut;
    public static String soapParam_pass;
    public static String soapParam_user;
    public static String soapParam_URL ;

    public static final int ACTION_ConnectionError = 0;
    public static final int ACTION_GetLoginList = 1;
    public static final int ACTION_Login = 2;
    public static final int ACTION_GetPhones = 3;
    public static SharedPreferences preferences;
    public static UIManager uiManager;
    public static Handler soapHandler;
    public ArrayList<String> loginIDList;
    public ArrayList<String> loginList;
    public static SoapObject soapParam_Response;
    public static SoapFault responseFault;
    public static String wsParam_LoginID;
    public static String wsParam_LoginName;
    public static String wsParam_PassHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        soapParam_URL = getString(R.string.soapServer);

        // Инициализируем вспомогательный класс
        uiManager = new UIManager(this);

        // Инициализируем менеджер настроек
        preferences = getPreferences(MODE_PRIVATE);

        // Читаем идентификатор последнего выбранного пользователя из настроек
        wsParam_LoginID = preferences.getString("LoginID", "");

        // Читаем настройки подключения
        initiateConnectionSettings();

        // Инициализируем обработчик ответа от сервиса
        soapHandler = new incomingHandler(this);

        //Log.d("LogInOnCreate", ""+soapParam_URL);
        if (wsParam_LoginID.equals("")) {
            // Первый запуск, открываем настройки
            startExchange(ACTION_GetLoginList);
        }
        else {
            openMainIntent();

        }
    }

    private void readPreferences(){
        preferences.getString("LoginID", wsParam_LoginID);
        preferences.getString("LoginName", wsParam_LoginName);
    }

    public void onClickStart(View v){
        EditText wsParam_Password = (EditText) findViewById(R.id.wsParam_Password);
        wsParam_PassHash = AeSimpleSHA1.getPassHash(wsParam_Password.getText().toString());

        startExchange(ACTION_Login);
    }

    private void openMainIntent(){
        Intent intentMain = createIntent();
        startActivity(intentMain);
        this.finish();
    }

    protected Intent createIntent(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        return mainIntent;
    }

    protected void initiateLoginList(){

        loginList = new ArrayList<>();
        loginIDList = new ArrayList<>();

        int count = soapParam_Response.getPropertyCount();
        int position = 0;

        for (int i = 0; i < count; i++) {
            SoapObject login = (SoapObject) soapParam_Response.getProperty(i);

            String name = login.getProperty("Description").toString();
            String id = login.getProperty("ID").toString();
            loginList.add(name);
            loginIDList.add(id);

            if (wsParam_LoginID.equals(id)){
                position = i;
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, loginList);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("Выберите пользователя");
        spinner.setAdapter(adapter);
        //spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                saveUserID(position);

                wsParam_LoginID = loginIDList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void saveUserID(int pos)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LoginID", loginIDList.get(pos).toString());
        editor.putString("LoginName", loginList.get(pos).toString());
        editor.commit();

    }

    public void checkLoginResult(){

        Boolean isLoginSuccess = Boolean.parseBoolean(soapParam_Response.getProperty("Result").toString());

        if (isLoginSuccess){
            uiManager.showToast(getString(R.string.textPassTrue));

            soapParam_user = soapParam_Response.getProperty("Name").toString();
            EditText wsParam_Password = (EditText) findViewById(R.id.wsParam_Password);
            soapParam_pass = wsParam_Password.getText().toString();

            startExchange(ACTION_GetPhones);

            openMainIntent();

        }
        else
            uiManager.showToast(getString(R.string.textPassFalse));

    }

    protected void startExchange(int ACTION){

        //Log.d("LogInOnCreate", ""+soapParam_URL+ ","+ soapParam_user+ ","+ soapParam_pass);
        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(soapParam_timeout, soapParam_URL, soapParam_user, soapParam_pass, ACTION);
        dispatcher.start();

    }

    private String getSoapErrorMessage () {

        String errorMessage;

        if (responseFault == null)
            errorMessage = getString(R.string.textNoInternet);
        else{
            try {
                errorMessage =  responseFault.faultstring;
            }
            catch (Exception e) {
                e.printStackTrace();
                errorMessage = "Неизвестная ошибка.";
            }
        }
        return errorMessage;
    }

    private void initiateConnectionSettings()
    {
        soapParam_user = preferences.getString("SoapUser", "wsUser");
        soapParam_pass = preferences.getString("SoapPass", "wsUser");
    }



    class incomingHandler extends Handler {

        private final WeakReference<LogInActivity> mTarget;

        // Конструктор
        public incomingHandler(LogInActivity context){
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {

            LogInActivity target = mTarget.get();
            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast("Ошибка: " + getSoapErrorMessage());
                    break;
                case ACTION_GetLoginList:
                    target.initiateLoginList();
                    break;
                case ACTION_Login:
                    target.checkLoginResult();
                    break;
                case ACTION_GetPhones:
                    Log.d("LoadData", "true Load data");
                    DBHelper dbHelper = new DBHelper(getBaseContext());
                    //dbHelper.onCreate(dbHelper.getWritableDatabase(), initialiseEmployeeData());
                    dbHelper.createArrayForDB(dbHelper.getWritableDatabase(), dbHelper.initialiseEmployeeData());
            }

        }
    }
}

