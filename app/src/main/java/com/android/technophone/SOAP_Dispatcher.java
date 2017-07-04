package com.android.technophone;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by User on 27.06.2017.
 */

public class SOAP_Dispatcher extends Thread {

    int timeout;
    String URL;
    String user;
    String pass;
    int ACTION;
    SoapObject soap_Response;
    final String NAMESPACE = "www.URI.com";//"ReturnPhones_XDTO";

    public SOAP_Dispatcher(int soapParam_timeout, String soapParam_URL, String soapParam_user, String soapParam_pass, int SOAP_ACTION){
        timeout = soapParam_timeout;
        URL = soapParam_URL;
        user = soapParam_user;
        pass = soapParam_pass;
        ACTION = SOAP_ACTION;
    }

    @Override
    public void run() {

        /*switch (ACTION) {
            case LogInActivity.ACTION_GetLoginList:
                GetLoginList();
                break;
            case LogInActivity.ACTION_Login:
                Login();
                break;

        }*/
        getPhones();

        if (soap_Response != null) {
            LogInActivity.soapParam_Response = soap_Response;
            LogInActivity.soapHandler.sendEmptyMessage(ACTION);
        } else {
            LogInActivity.soapHandler.sendEmptyMessage(LogInActivity.ACTION_ConnectionError);
        }
    }

    void GetLoginList(){

        String method = "GetLoginList";
        String action = NAMESPACE + "#AcceptingOrders:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);

    }

    void Login(){

        String method = "Login";
        String action = NAMESPACE + "#AcceptingOrders:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        request.addProperty("ID", LogInActivity.wsParam_LoginID);
        request.addProperty("Password", LogInActivity.wsParam_PassHash);
        soap_Response = callWebService(request, action);

    }

    void getPhones() {
        String method = "GetEmployyes";
        //String method = "GetPhones";
        String action = NAMESPACE + "#returnPhones:" + method;
        SoapObject request = new SoapObject(NAMESPACE, method);
        soap_Response = callWebService(request, action);
    }

    private SoapObject callWebService(SoapObject request, String action){

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        HttpTransportSE androidHttpTransport = new HttpTransportBasicAuthSE(URL, user, pass);
        androidHttpTransport.debug = true;

        try {
            androidHttpTransport.call(action, envelope);
            return (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            LogInActivity.responseFault = (SoapFault) envelope.bodyIn;
        }

        return null;
    }

}

