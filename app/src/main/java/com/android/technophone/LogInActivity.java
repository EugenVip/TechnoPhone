package com.android.technophone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
    private static String soapParam_URL ;

    public static final int ACTION_ConnectionError = 0;
    public static final int ACTION_GetLoginList = 1;
    public static final int ACTION_Login = 2;
    public static SharedPreferences preferences;
    public static UIManager uiManager;
    public static Handler soapHandler;
    public ArrayList<String> loginIDList;
    public static SoapObject soapParam_Response;
    public static SoapFault responseFault;
    public static String wsParam_LoginID;
    public static String wsParam_PassHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        soapParam_URL = getString(R.string.soapServer);

        // Инициализируем вспомогательный класс
        uiManager = new UIManager(this);
        // Инициализируем менеджер настроек
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Читаем идентификатор последнего выбранного пользователя из настроек
        wsParam_LoginID = preferences.getString("LoginID", "");
        // Читаем настройки подключения
        initiateConnectionSettings();
        // Инициализируем обработчик ответа от сервиса
        soapHandler = new incomingHandler(this);

        Log.d("LogInOnCreate", ""+soapParam_URL);
        if (soapParam_URL.equals("")) {
            // Первый запуск, открываем настройки
            //openSettings();
        }
        else {
            // Выводим на экран форму авторизации
            setContentView(R.layout.activity_log_in);
            // Запрашиваем список пользователей
            startExchange(ACTION_GetLoginList);
        }
    }

    public void onClickStart(View v){
        Intent intentMain = createIntent();
        startActivity(intentMain);
    }

    protected Intent createIntent(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        return mainIntent;
    }

    private ArrayList<Employee> initialiseEmployeeData()
    {
        ArrayList<Employee> employeeArrayList = new ArrayList<>();

        int count = soapParam_Response.getPropertyCount();

        for (int i = 0; i < count; i++) {
            SoapObject property = (SoapObject) soapParam_Response.getProperty(i);
            //Log.i("initialiseEmploy", ""+property.toString());
            if (property instanceof SoapObject) {
                SoapObject info = (SoapObject) property;
                String name = info.getProperty("FullName").toString();
                String phone = info.getProperty("PhoneNumber").toString();
                Employee mEmployee = new Employee(name, phone);
                employeeArrayList.add(mEmployee);
            }
        }
        return employeeArrayList;
    }

    protected void initiateLoginList(){

        ArrayList<String> loginList = new ArrayList<>();
        loginIDList = new ArrayList<>();

        int count = soapParam_Response.getPropertyCount();
        int position = 0;

        for (int i = 0; i < count; i++) {
            SoapObject login = (SoapObject) soapParam_Response.getProperty(i);
            String name = (String) login.getProperty("Description");
            String id = (String) login.getProperty("ID");
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
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveUserID(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void saveUserID(int pos){

    }

    public void checkLoginResult(){

        Boolean isLoginSuccess = Boolean.parseBoolean((String) soapParam_Response.getProperty("Result"));

        if (isLoginSuccess){
            soapParam_user = (String) soapParam_Response.getProperty("Name");
            EditText wsParam_Password = (EditText) findViewById(R.id.wsParam_Password);
            soapParam_pass = wsParam_Password.getText().toString();
            //setActivityTaskList();
        }
        else

            uiManager.showToast("Ошибка! Неверно введен пароль");

    }

    protected void startExchange(int ACTION){

        Log.d("LogInOnCreate", ""+soapParam_URL+ ","+ soapParam_user+ ","+ soapParam_pass);
        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(soapParam_timeout, soapParam_URL, soapParam_user, soapParam_pass, ACTION);
        dispatcher.start();

    }

    private static String getSoapErrorMessage () {

        String errorMessage;

        if (responseFault == null)
            errorMessage = "Отсутствует соединение с сервером.";
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

    /*public static String getPassHash(String text){

        return base64string(AeSimpleSHA1.SHA1(text)) + "," + base64string(sha1(text.toUpperCase()));

    }*/

    class incomingHandler extends Handler {

        private final WeakReference<LogInActivity> mTarget;

        // Конструктор
        public incomingHandler(LogInActivity context){
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage", "true Load data"+msg.toString());
            LogInActivity target = mTarget.get();
            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast("Ошибка" + getSoapErrorMessage());
                    break;
                /*case ACTION_GetLoginList:
                    target.initiateLoginList();
                    break;
                case ACTION_Login:
                    target.checkLoginResult();
                    break;*/
                default:
                    Log.d("LoadData", "true Load data");
                    DBHelper dbHelper = new DBHelper(getBaseContext());
                    //dbHelper.onCreate(dbHelper.getWritableDatabase(), initialiseEmployeeData());
                    dbHelper.createArrayForDB(dbHelper.getWritableDatabase(), initialiseEmployeeData());
            }

        }
    }
}

