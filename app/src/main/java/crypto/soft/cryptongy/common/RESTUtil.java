package crypto.soft.cryptongy.common;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by noni on 26/10/2017.
 */

public class RESTUtil {
    public String callREST(String url)
    {
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject(url, String.class);
////                System.out.println("response " +marketSummariesStr);
        String response = null;
        try {

            URL myURL = new URL(url);
            HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();
//            myURLConnection.setUseCaches(false);
//            myURLConnection.setDoInput(true);
//            myURLConnection.setDoOutput(true);
            myURLConnection.setRequestMethod("GET");
//            myURLConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = myURLConnection.getResponseCode();
            Log.i("responseCode " , ""+ responseCode);
            if (responseCode == 200) {
                // Read response
                BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                StringBuffer jsonString = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                myURLConnection.disconnect();
                response = jsonString.toString();
                Log.i("response " , ""+ response);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        return response;
    }



    public String callRestHttp(String baseURL , String key, String secret)
    {
        String response = null;
        try {
            String nonce = generateNonce();//Calendar.getInstance().getTime().toString(); //UUID.randomUUID().toString();
            String serviceURL = baseURL + "?apikey=" + key + "&nonce=" + nonce;
            URL myURL = new URL(serviceURL);
            HttpsURLConnection myURLConnection = (HttpsURLConnection)myURL.openConnection();
            myURLConnection.setUseCaches(false);
            myURLConnection.setDoInput(true);
            myURLConnection.setDoOutput(true);
//            myURLConnection.setRequestMethod("GET");

//            myURLConnection.setRequestProperty("Content-Type", "application/json");
            String hash = generateHMAC(secret,serviceURL);
//            Log.i("hash " , ""+ hash);
            myURLConnection.addRequestProperty ("apisign", hash);
//            Log.i("header " , myURLConnection.getHeaderField("apisign") +" "+ myURLConnection.getRequestProperty("apisign")+ " "+ myURLConnection.getHeaderFields().keySet() +" "+  myURLConnection.getHeaderFields().values());
            int responseCode = myURLConnection.getResponseCode();
//            Log.i("responseCode " , ""+ responseCode);
            if (responseCode == 200) {
                // Read response
                BufferedReader br = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                StringBuffer jsonString = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                myURLConnection.disconnect();
                response = jsonString.toString();
//                Log.i("response " , ""+ response);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
        e.printStackTrace();
        }
    return response;

    }

    public String callRestHttpClient(String baseURL , String key, String secret){
        String response = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            String nonce = UUID.randomUUID().toString();
            String serviceURL = baseURL + "?apikey=" + key + "&nonce=" + nonce;
            HttpGet request = new HttpGet(serviceURL);
            String hash = generateHMAC(secret,serviceURL);
//            Log.i("hash " , ""+ hash);
            request.addHeader("apisign", hash); // Attaches signature as a header

            HttpResponse httpResponse = httpclient.execute(request);
            StringBuffer resultBuffer = getResponse(httpResponse);

            response = resultBuffer.toString();
//            Log.i("response " , ""+ response);
        } catch (UnknownHostException | SocketException  e ) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return  response;

        }

    public String callRestHttpClient(String baseURL , String key, String secret, Map<String, String> paramters){
        String response = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            String nonce = UUID.randomUUID().toString();
            String serviceURL = baseURL + "?apikey=" + key + "&nonce=" + nonce;
            for (String pkey: paramters.keySet()) {
                serviceURL += "&"+ pkey +"=" + paramters.get(pkey);
            }
            HttpGet request = new HttpGet(serviceURL);
            String hash = generateHMAC(secret,serviceURL);
//            Log.i("hash " , ""+ hash);
            request.addHeader("apisign", hash); // Attaches signature as a header

            HttpResponse httpResponse = httpclient.execute(request);
            StringBuffer resultBuffer = getResponse(httpResponse);

            response = resultBuffer.toString();
//            Log.i("response " , ""+ response);
        } catch (UnknownHostException | SocketException  e ) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;

    }



    public String callRestHttpClient(String baseURL , String key, String secret, Map<String, String> paramters, String algorithm, String method){
        String response = null;
         key = "lagDESAI7BOvlaBaPCMbi8vftz3VJ74sdZKEh1XHCMF2GbAm6XMdGQMumTRGwumP";
         secret = "pHeeK3C5tJc9SruZGvhZ1P3KRNiIz5Vte8tbgBgM5NYRg216C9s7higBeZ38uizI";
        if (method == null) method = "GET";
        try {

            long timestamp = System.currentTimeMillis();
            String queryString = "timestamp=" + timestamp ;

            if(paramters != null) {
                for (String pkey : paramters.keySet()) {
                    queryString += "&" + pkey + "=" + paramters.get(pkey);
                }
            }
            String hash = generateHMAC(secret,queryString, algorithm);
            queryString += "&signature=" + hash;
            String serviceURL = baseURL +"?"+ queryString ;

            Log.i("serviceURL " , ""+ serviceURL + " key " + key + " secret " + secret);
            HttpClient httpclient = new DefaultHttpClient();
            if(method.equals("GET")) {
                HttpGet request = new HttpGet(serviceURL);
                request.addHeader("X-MBX-APIKEY", key);
                HttpResponse httpResponse = httpclient.execute(request);
                StringBuffer resultBuffer = getResponse(httpResponse);
                response = resultBuffer.toString();
            }
            else if(method.equals("DELETE"))
            {
                HttpDelete httpDelete = new HttpDelete(serviceURL);
                httpDelete.addHeader("X-MBX-APIKEY", key);
                httpDelete.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpResponse httpResponse = httpclient.execute(httpDelete);
                StringBuffer resultBuffer = getResponse(httpResponse);
                response = resultBuffer.toString();
            }
            else
            if(method.equals("POST"))
            {
                HttpPost httpPost = new HttpPost(serviceURL);
                httpPost.addHeader("X-MBX-APIKEY", key);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                HttpResponse httpResponse = httpclient.execute(httpPost);
                StringBuffer resultBuffer = getResponse(httpResponse);
                response = resultBuffer.toString();
            }
            Log.i("REST response " , ""+ response );


        } catch (UnknownHostException | SocketException  e ) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  response;

    }


    @NonNull
    private StringBuffer getResponse(HttpResponse httpResponse) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        StringBuffer resultBuffer = new StringBuffer();
        String line = "";

        while ((line = reader.readLine()) != null)

            resultBuffer.append(line);
        return resultBuffer;
    }

    private String generateHMAC( String secret, String datas )
    {


        Mac mac;
        String result = "";
        try
        {
            final SecretKeySpec secretKey = new SecretKeySpec( secret.getBytes(StandardCharsets.US_ASCII), "HmacSHA512" );
            mac = Mac.getInstance( "HmacSHA512" );
            mac.init( secretKey );
            final byte[] macData = mac.doFinal( datas.getBytes(StandardCharsets.US_ASCII) );
//            byte[] hex = new Hex( ).encode( macData );
//            result = new String( hex, "ISO-8859-1" );

            result = byteArrayToHexString(macData);
//            Log.d("RESTUtil", result);
        }
        catch ( final NoSuchAlgorithmException e )
        {
            Log.e("MainActivity", e.getMessage(), e);
        }
        catch ( final InvalidKeyException e )
        {
            Log.e("MainActivity", e.getMessage(), e);
        }


        return result.replace("-","");

    }

    private String generateHMAC( String secret, String datas, String algorithm)
    {


        Mac mac;
        String result = "";
        try
        {
            final SecretKeySpec secretKey = new SecretKeySpec( secret.getBytes(StandardCharsets.UTF_8), algorithm );
            mac = Mac.getInstance( algorithm );
            mac.init( secretKey );
            final byte[] macData = mac.doFinal( datas.getBytes(StandardCharsets.UTF_8) );
//            byte[] hex = new Hex( ).encode( macData );
//            result = new String( hex, "ISO-8859-1" );

            result = byteArrayToHexString(macData);
//            Log.d("RESTUtil", result);
        }
        catch ( final NoSuchAlgorithmException e )
        {
            Log.e("MainActivity", e.getMessage(), e);
        }
        catch ( final InvalidKeyException e )
        {
            Log.e("MainActivity", e.getMessage(), e);
        }


        return result.replace("-","");

    }

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();
    public static String byteArrayToHexString(final byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for(int j = 0; j < bytes.length; j++) {

            int v = bytes[j] & 0xFF;

            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static String generateNonce() {

        SecureRandom random = null;

        try {

            random = SecureRandom.getInstance("SHA1PRNG");

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        random.setSeed(System.currentTimeMillis());

        byte[] nonceBytes = new byte[16];
        random.nextBytes(nonceBytes);

        String nonce = null;
        nonce = new String(Base64.encode(nonceBytes, Base64.DEFAULT));



        return nonce;
    }

    /**
     * Converts byte array to hex string
     *
     * @param bytes The data
     * @return String represents the data in HEX string
     */
    public  String byteArrayToHex(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString();
    }

}
