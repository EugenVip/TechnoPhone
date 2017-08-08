package com.android.technophone;

/**
 * Created by User on 29.06.2017.
 */

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.android.technophone.LogInActivity.wsParam_PassHash;

public class AeSimpleSHA1 {
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }

        return buf.toString();
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("UTF-8");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        //return convertToHex(sha1hash);
        String result = Base64.encodeToString(sha1hash, Base64.DEFAULT);
        result = result.substring(0, result.length()-1);
        return result;
    }

    public static String getPassHash(String text){

        try {
            String textPass = AeSimpleSHA1.SHA1(text);
            //Log.i("SHA1 - ", textPass);

            String textPassUpper = AeSimpleSHA1.SHA1(text.toUpperCase());
            //Log.i("SHA1UPPER - ", base64string(text));

            //String textPass = sha1Hash(text);
            wsParam_PassHash = textPass + "," + textPassUpper;

        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return wsParam_PassHash;

    }
}