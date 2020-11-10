package br.com.libertyseguros.mobile.libray;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import br.com.libertyseguros.mobile.BuildConfig;
import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.util.OnConnection;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Connection {

    // private String URL = "https://act-dmz.libertyseguros.com.br/MobileApi/api/";
    // private String URL = "https://mobile.libertyseguros.com.br/api/";
    private String URL;

    private String methodName;

    private String urlParameters;

    private Context context;

    private OnConnection onConnection;

    private int type;

    private static boolean inProgress;

    private String serverReturn;

    private boolean token;

    /**
     * Contrutor
     *
     * @param context
     */
    public Connection(Context context) {
        this.context = context;

        if (BuildConfig.prod) {
            URL = context.getString(R.string.url_prod);
        } else {
            URL = context.getString(R.string.url_act);

        }
    }


    /**
     * Configures and starts connection
     *
     * @param name
     * @param parameters
     */
    public void startConnection(String name, String parameters, int type) {
        URL += "v1/";
        methodName = name;
        urlParameters = parameters; //+  "&brandMarketing=" + BuildConfig.brandMarketing;
        this.type = type;

        connectionJson();

    }


    /**
     * Configures and starts connection
     *
     * @param name
     * @param parameters
     */
    public void startConnectionV2(String name, String parameters, int type, boolean header) {
        URL += "v2/";
        this.token = header;
        methodName = name;
        urlParameters = parameters;
        this.type = type;

        connectionJson();

    }

    /**
     * Configures and starts connection
     *
     * @param name
     * @param parameters
     */
    public void startConnectionV3(String name, String parameters, int type, boolean header) {
        URL += "v3/";
        this.token = header;
        methodName = name;
        urlParameters = parameters;
        this.type = type;

        connectionJson();

    }

    /**
     * Configures and starts connection
     *
     * @param name
     * @param parameters
     */
    public void startConnection(String name, String parameters, int type, boolean header) {
        URL += "v1/";
        this.token = header;
        methodName = name;
        urlParameters = parameters; //+ "&brandMarketing=" + BuildConfig.brandMarketing;
        this.type = type;

        connectionJson();

    }

    private void connectionJson() {

        inProgress = true;

        new Thread() {
            public void run() {
                try {
                    Request request = null;

                    URL url = null;
                    if (type == 1 || type == 3 || type == 5) {
                        url = new URL(URL + methodName);
                    } else if (type == 2) {
                        url = new URL(URL + methodName + urlParameters);
                    } else if (type == 4) {
                        url = new URL(URL + methodName + urlParameters);
                    }

                    if (!BuildConfig.prod) {
                        //Log.i(Config.TAG, "Connecion URL: " + url.toString());
                        //Log.i(Config.TAG, "Connecion PARAM: " + urlParameters);
                    }

                    if (type == 1) {
                        OkHttpClient client = createClientRequest();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, urlParameters);

                        if (!token) {
                            request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .addHeader("X-Liberty-AtivarTrace", "false")
                                    .build();
                        } else {
                            InfoUser infoUser = new InfoUser();
                            request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .addHeader("X-Liberty-AtivarTrace", "false")
                                    .addHeader("Authorization", "Bearer " + infoUser.getUserInfo(context).getAccess_token())
                                    .build();
                        }

                        Response response = client.newCall(request).execute();

                        serverReturn = response.body().string();

                    } else if (type == 2) {

                        if (token) {
                            InfoUser infoUser = new InfoUser();

                            OkHttpClient client = createClientRequest(5000);


                            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                            RequestBody body = RequestBody.create(mediaType, urlParameters);
                            request = null;

                            request = new Request.Builder()
                                    .url(url)
                                    .get()
                                    .addHeader("content-type", "application/x-www-form-urlencoded")
                                    .addHeader("cache-control", "no-cache")
                                    .addHeader("X-Liberty-AtivarTrace", "false")
                                    .addHeader("Authorization", "Bearer " + infoUser.getUserInfo(context).getAccess_token())
                                    .build();


                            Response response = client.newCall(request).execute();

                            serverReturn = response.body().string();
                            //conn.setRequestProperty("Authorization", "Bearer " + infoUser.getUserInfo(context).getAccess_token());

                        } else {

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(10000);
                            conn.setRequestMethod("GET");


                            InputStream is = conn.getInputStream();
                            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                            String line;
                            StringBuffer response = new StringBuffer();

                            while ((line = rd.readLine()) != null) {
                                response.append(line);
                                response.append('\r');
                            }

                            if (!BuildConfig.prod) {
                                //Log.i(Config.TAG, "Response: " + response.toString());
                            }
                            serverReturn = response.toString();

                            rd.close();
                        }

                    } else if (type == 3) {
                        OkHttpClient client = createClientRequest();

                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, urlParameters);


                        InfoUser infoUser = new InfoUser();
                        request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("cache-control", "no-cache")
                                .addHeader("X-Liberty-AtivarTrace", "false")
                                .addHeader("Content-Length", Integer.toString(urlParameters.getBytes().length) + "")
                                .build();


                        Response response = client.newCall(request).execute();

                        serverReturn = response.body().string();

                    } else if (type == 4) {
                        InfoUser infoUser = new InfoUser();

                        OkHttpClient client = createClientRequest();

                        request = new Request.Builder()
                                .url(url)
                                .get()
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .addHeader("X-Liberty-AtivarTrace", "false")
                                .addHeader("Authorization", "Bearer " + infoUser.getUserInfo(context).getAccess_token())
                                .build();


                        Response response = client.newCall(request).execute();

                        serverReturn = response.body().string();
                    } else if (type == 5) {
                        OkHttpClient client = createClientRequest();

                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, urlParameters);


                        InfoUser infoUser = new InfoUser();
                        request = new Request.Builder()
                                .url(url)
                                .post(body)
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("Content-Type", "application/json")
                                .addHeader("cache-control", "no-cache")
                                .addHeader("X-Liberty-AtivarTrace", "false")
                                .addHeader("Content-Length", Integer.toString(urlParameters.getBytes().length) + "")
                                .addHeader("Authorization", "Bearer " + infoUser.getUserInfo(context).getAccess_token())
                                .build();


                        Response response = client.newCall(request).execute();

                        serverReturn = response.body().string();

                    }


                    if (onConnection != null) {
                        onConnection.onSucess(serverReturn.toString());
                    }

                    inProgress = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    inProgress = false;

                    if (onConnection != null) {
                        onConnection.onError("");
                    }

                }
            }
        }.start();
    }

    private OkHttpClient createClientRequest(int timeoutextra) {
        OkHttpClient.Builder b = new OkHttpClient.Builder();
        b.readTimeout(25000 + timeoutextra, TimeUnit.MILLISECONDS);
        b.writeTimeout(25000 + timeoutextra, TimeUnit.MILLISECONDS);
        b.connectTimeout(100000 + timeoutextra, TimeUnit.MILLISECONDS);

        return b.build();
    }

    private OkHttpClient createClientRequest() {
        return this.createClientRequest(0);
    }

    /**
     * Method of connection
     */
    public void connection() {

        inProgress = true;

        new Thread() {
            public void run() {
                try {

                    URL url = new URL(URL + methodName);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(false);
                    //conn.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(
                            conn.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    InputStream is = conn.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));

                    String line;
                    StringBuffer response = new StringBuffer();

                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }

                    if (!BuildConfig.prod) {
                        //Log.i(Config.TAG, "Response: " + response.toString());
                    }

                    rd.close();


                    if (onConnection != null) {
                        onConnection.onSucess(response.toString());
                    }

                    inProgress = false;

                } catch (Exception e) {
                    e.printStackTrace();
                    inProgress = false;

                    if (onConnection != null) {
                        onConnection.onError("");
                    }


                }


            }

        }.start();
    }

    /**
     * Method set interface onConnection
     *
     * @param onConnection
     */
    public void setOnConnection(OnConnection onConnection) {
        this.onConnection = onConnection;
    }

    /**
     * Checks for connection
     *
     * @param context
     * @return
     */
    public boolean verifyConnection(Context context) {

        this.context = context;

        ConnectivityManager cm = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isAvailable() &&
                cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }


}
