package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.inthecheesefactory.thecheeselibrary.widget.AdjustableImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.LoginBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.beans.NumberWarningVehicleAccidentBeans;
import br.com.libertyseguros.mobile.beans.UploadFileBeans;
import br.com.libertyseguros.mobile.beans.VehicleAccidentSendBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.util.ValidCNPJ;
import br.com.libertyseguros.mobile.util.ValidCPF;
import br.com.libertyseguros.mobile.util.ValidEmail;
import br.com.libertyseguros.mobile.view.Assistance;
import br.com.libertyseguros.mobile.view.Assistance24WebView;
import br.com.libertyseguros.mobile.view.ChangePassword;
import br.com.libertyseguros.mobile.view.Cities;
import br.com.libertyseguros.mobile.view.ListVehicleAccident;
import br.com.libertyseguros.mobile.view.Login;
import br.com.libertyseguros.mobile.view.Main;
import br.com.libertyseguros.mobile.view.Register;
import br.com.libertyseguros.mobile.view.Support;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep2;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep3;
import br.com.libertyseguros.mobile.view.VehicleAccidentStep4;
import br.com.libertyseguros.mobile.view.ViewerDocuments;

public class VehicleAccidentModel extends BaseModel{

    public static final int CROP_PIC_REQUEST_CODE = 46;

    public static final int CAMERA_PIC_REQUEST_CODE = 56;

    public static boolean exit;

    public static String cpfCnpj;

    public static String numberPolicy;

    private Activity context;

    private Connection conn;

    private int typeError;

    private OnConnectionResult onConnectionResult;

    private MessageBeans message;

    private Gson gson;

    private  MediaRecorder recorder;

    private File audiofile = null;

    private boolean audioRecord;

    private boolean isPlay;

    private MediaPlayer mPlayer;

    private AdjustableImageView ivAudio;

    private Uri outPutfileUri;

    private String msgError[];

    private ArrayList<String> arrayOutPutfileUri;

    private ValidEmail validEmail;

    private ValidCNPJ validCNPJ;

    private ValidCPF validCPF;

    public static VehicleAccidentSendBeans vasb;

    private NumberWarningVehicleAccidentBeans nwvhbeans;

    private String mCurrentPhotoPath;

    private int typeConnection;

    private long sizeFile;

    private int indexRemove;

    private VehicleAccidentStep4.TimeoutRecordAudio timeoutRecordAudio;

    private LoginBeans lb;

    private ArrayList<String> arrayStateCod;

    private ArrayList<String> arrayState;

    private DocumentsImageManager dim;


    public VehicleAccidentModel(OnConnectionResult onConnectionResult){
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        arrayOutPutfileUri = new ArrayList<>();
        validCNPJ = new ValidCNPJ();
        validCPF = new ValidCPF();
        indexRemove = -1;

        mPlayer = null;

    }


    /**
     * Upload Files
     * @param ctx
     */
    public void uploadPhotos(Context ctx, String json){
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 2;

        createConnectionUpload();

        String param = json;
        
        conn.startConnection("Sinistro/Upload/", param, 3, false);
    }

    /**
     * Create
     */
    private void createConnectionUpload() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try{
                    //Log.i(Config.TAG, "Error upload connection");
                    typeError = 1;
                    indexRemove = -1;
                    onConnectionResult.onError();
                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "Upload: " + result);

                try {
                        if(result.contains("idsDocumentos")){
                        onConnectionResult.onSucess();
                    } else {
                        message = gson.fromJson(result, MessageBeans.class);
                        indexRemove = -1;
                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message = gson.fromJson(result, MessageBeans.class);
                    indexRemove = -1;
                    typeError = 2;
                    onConnectionResult.onError();
                }
            }
        });
    }


    public void createJson(UploadFileBeans uploadList, Context context){

        Gson gson = new Gson();
        String json = gson.toJson(uploadList);

        json = json.replace(",null", "");
        json = json.replace("null", "");

    //    json=  json.substring(1, json.length());
     //   json = json.substring(0, json.length() - 1);

        uploadPhotos(context, json);

    }


    /**
     * Create Upload Files
     * @param activity
     */
    public void createUploadFiles(Activity activity){
        ArrayList<UploadFileBeans> uploadList = new ArrayList<>();
            if(context == null){
                context = activity;
            }

            try {
                int size = arrayOutPutfileUri.size();

                byte[] bytesAudio = null;

                if (indexRemove != -1 && indexRemove != 0) {

                    ArrayList<String> arrayOutPutfileUriTemp = new ArrayList<>();

                    for (int ind = 0; ind < arrayOutPutfileUri.size(); ind++) {

                        if(ind >= indexRemove){
                            arrayOutPutfileUriTemp.add(arrayOutPutfileUri.get(ind));
                        }
                    }

                    arrayOutPutfileUri = arrayOutPutfileUriTemp;

                    indexRemove = -1;

                    audiofile = null;
                }


                if (audiofile != null) {
                    size++;

                    File file = audiofile;

                    int sizeFile = (int) file.length();
                    bytesAudio = new byte[sizeFile];
                    try {
                        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                        buf.read(bytesAudio, 0, bytesAudio.length);
                        buf.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                UploadFileBeans uploadFileBeans = new UploadFileBeans(size);

                uploadFileBeans.setNumeroSinistro(nwvhbeans.getNumeroAvisoSinistro());

                 uploadList = new ArrayList<>();

                if (bytesAudio != null) {
                    UploadFileBeans.Files files = uploadFileBeans.getInstaceFile();

                    files.setNome(System.currentTimeMillis() + "_audio.mp4");
                    files.setTipoDocumento("1");
                    String audio = Base64.encodeToString(bytesAudio, Base64.DEFAULT);
                    files.setConteudo(audio);

                    uploadFileBeans.getArquivos()[size - 1] = files;
                    uploadList.add(uploadFileBeans);

                    sizeFile = audiofile.length();

                } else {
                    sizeFile = 0;
                }

                ArrayList<String> ids = new ArrayList<>();
                ArrayList<UploadFileBeans.Files> arrayFiles = new ArrayList<>();

                for (int ind = 0; ind < arrayOutPutfileUri.size(); ind++) {

                    if (checkFileSize(activity, Uri.parse(arrayOutPutfileUri.get(ind)))) {

                        boolean documents = false;

                        UploadFileBeans.Files files = uploadFileBeans.getInstaceFile();

                        files.setNome(System.currentTimeMillis() + "_photo.jpg");
                        files.setTipoDocumento("2");

                        Bitmap bitmap = null;
                        ByteArrayOutputStream out = null;

                        try {
                            try {
                                //bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(arrayOutPutfileUri.get(ind)));

                                File imagefile = new File(getRealPathFromURI(Uri.parse(arrayOutPutfileUri.get(ind)), context));
                                FileInputStream fis = null;
                                try{
                                    fis = new FileInputStream(imagefile);
                                }catch(FileNotFoundException e){
                                    e.printStackTrace();

                                    fis = context.openFileInput(getRealPathFromURI(Uri.parse(arrayOutPutfileUri.get(ind)), context));

                                }

                                BitmapFactory.Options o = new BitmapFactory.Options();
                                o.inSampleSize = 2;

                                Bitmap bm = BitmapFactory.decodeStream(fis,null,o);
                                //Bitmap bm = BitmapFactory.decodeStream(fis);

                                out = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 32, out);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                                documents = true;
                                ids.add(arrayOutPutfileUri.get(ind).replace(".jpg", ""));
                            }

                            if(!documents){
                                files.setConteudo(getImageBase64(out.toByteArray()));
                                arrayFiles.add(files);
                            }

                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }


                    //   uploadList.add(uploadFileBeans);
                    } else {
                        indexRemove = ind;
                        ind = arrayOutPutfileUri.size();
                    }

                }

                if(ids.size() > 0){
                    String idsVector[] = new String[ids.size()];
                    for(int ind =  0; ind < ids.size(); ind++){
                        idsVector[ind] = ids.get(ind);
                    }

                    uploadFileBeans.setIdDocumentos(idsVector);

                }


                if(arrayFiles.size() > 0){
                    UploadFileBeans.Files fileVector[] = new UploadFileBeans.Files[arrayFiles.size()];
                    for(int ind =  0; ind < arrayFiles.size(); ind++){
                        fileVector[ind] = arrayFiles.get(ind);

                        uploadFileBeans.getArquivos()[ind] = arrayFiles.get(ind);

                    }

                    //uploadFileBeans.setArquivos(fileVector);

                }

                createJson(uploadFileBeans, activity);

            }catch(Exception ex){
                ex.printStackTrace();
                onConnectionResult.onError();
                //Log.i(Config.TAG, "Error create upload file");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                onConnectionResult.onError();
                //Log.i(Config.TAG, "Error create upload file outofMemory ");
            }
    }

    /**
     * Convert Bitmap in base 64
     * @param bitmap
     * @return
     */
    private String getImageBase64(byte[] bitmap) {
        String image64 = Base64.encodeToString(bitmap, Base64.DEFAULT);
        return image64;
    }

    /**
     * Get message error
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     * @return
     */
    public int getTypeError() {
        return typeError;
    }


    /**
     * Start recording
     * @throws IOException
     */
    public void startRecording(VehicleAccidentStep4.TimeoutRecordAudio timeoutRecord) throws IOException {
        this.timeoutRecordAudio = timeoutRecord;

        File dir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".mp4", dir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFile(audiofile.getAbsolutePath());
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setMaxDuration(Config.STOPAUDIO);



            recorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {

                    if(what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
                        timeoutRecordAudio.onStop();
                        //Log.i(Config.TAG, "Timeout record audio");

                    }
                }
            });

            try {
                recorder.prepare();
            } catch (IOException e) {
                //Log.e(Config.TAG, "prepare() failed");
            }

            recorder.start();
        } catch (IOException e) {
            //Log.e(Config.TAG, "external storage access error");
            return;
        }

    }

    /**
     * Stop recording Audio
     */
    public void stopRecording(Context context) {

        recorder.stop();
        recorder.release();
        mPlayer = null;
        addRecordingToMediaLibrary(context);
    }

    /**
     * Save file Audio
     */
    protected void addRecordingToMediaLibrary(Context context) {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp4");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());

        //Log.i(Config.TAG, "Audio Path: " + audiofile.getAbsolutePath());

        ContentResolver contentResolver = context.getContentResolver();
        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));

        audioRecord = true;
    }

    /**
     * Start Play audio
     */
    public void startPlaying(AdjustableImageView iv, Activity ctx) {
        this.context = ctx;

        if(mPlayer == null) {
            mPlayer = new MediaPlayer();


            ivAudio = iv;
            try {

                String path = audiofile.getAbsoluteFile().toString();

                //Log.i(Config.TAG, path);

                mPlayer.setDataSource(path);
                mPlayer.prepare();
                mPlayer.start();

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        setPlay(false);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivAudio.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_play));
                            }
                        });
                    }
                });
            } catch (IOException e) {
                //Log.e(Config.TAG, "prepare() failed");
            }

        } else {
            mPlayer.start();
        }

        setPlay(true);
    }


    /**
     * Delete File Audio
     */
    public void deleteAudio(){
        File file = new File(audiofile.getAbsoluteFile().toString());
        boolean deleted = file.delete();

        audiofile = null;
    }

    /**
     * Stop Play audio
     */
    public void stopPlaying() {
        mPlayer.release();
        mPlayer = null;

        setPlay(false);

    }


    /**
     * Stop Play audio
     */
    public void pause() {
        mPlayer.pause();

        setPlay(false);
    }

    /**
     * Get Audio play/stop
     * @return
     */
    public boolean isPlay() {
        return isPlay;
    }

    /**
     * Set Audio play/stop
     * @return
     */
    public void setPlay(boolean play) {
        isPlay = play;
    }

    /**
     * Get Audio Record
     * @return
     */
    public boolean isAudioRecord() {
        return audioRecord;
    }

    /**
     * Set Audio Record
     * @return
     */
    public void setAudioRecord(boolean audioRecord) {
        this.audioRecord = audioRecord;
    }


    /**
     * Open Vehicle Accident Step 1
     * @param context
     */
    public void openStep1Logoff(Context context){
        Intent it = new Intent(context, ListVehicleAccident.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        it.putExtra("type", "1");
        it.putExtra("vehicleAccident", "0");

        context.startActivity(it);

    }

    /**
     * Open 24 hours
     * @param context
     * @param cpf
     * @param plate
     */
    public void open24hours(Context context, String cpf, String plate){
        String bt;

        bt = "btEmergencia";
//
        Intent intent = new Intent(context, Assistance24WebView.class);
        intent.putExtra("chassi", "");
        intent.putExtra("cpf", cpf);
        intent.putExtra("plate", plate);
        intent.putExtra("callButton", bt);

        //Log.i(Config.TAG, "New 24 hours: " + "CPF " + cpf + " - Plate " + plate);


        context.startActivity(intent);

    }

    /**
     * Open next step screen
     * @param context
     * @param index
     */
    public void openNextStep(Context context, int index){
        Intent it = null;
        switch(index){
            case 1:
                it = new Intent(context, VehicleAccidentStep2.class);
                break;
            case 2:
                it = new Intent(context, VehicleAccidentStep3.class);
                break;
            case 3:
                it = new Intent(context, VehicleAccidentStep4.class);
                break;
        }

        context.startActivity(it);
    }

    /**
     * Open Change Password Screen
     */
    public void openForgotPassword(Context context){
        Intent it = new Intent(context, ChangePassword.class);
        context.startActivity(it);
    }

    /**
     * Open Support Screen
     */
    public void openSupport(Context context){
        Intent it = new Intent(context, Support.class);
        context.startActivity(it);
    }

    /**
     * Open Screen Login Class
     */
    public void openLogin(Activity activity){
        Intent it = new Intent(activity, Login.class);
        activity.startActivity(it);
        activity.finish();
        Main.activity.finish();
        Assistance.activity.finish();
    }

    /**
     * Open Register Screen
     */
    public void openRegister(Activity activity){
        Intent it = new Intent(activity, Register.class);
        Register.activityBefore = activity;
        Register.activityBefore_2 = Main.activity;
        Register.activityBefore_3 = Assistance.activity;
        activity.startActivity(it);

    }

    /**
     * Open Change Password Screen
     */
    public void openChangePassword(Activity activity){
        Intent it = new Intent(activity, ChangePassword.class);
        activity.startActivity(it);
    }


    /**
     * Crop Image
     * @param uri
     * @param INT_CROP
     * @param activity
     */
    public void cropImage(Uri uri, int INT_CROP, Activity activity){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);

        cropIntent.putExtra("return-data", true);
        activity.startActivityForResult(cropIntent, INT_CROP);
    }

    /**
     * Open Make Photo Intent
     * @param title
     * @param activity
     */
    public void openMakePhotoIntent(String title, Activity activity){
        Intent intent = makePhotoIntent(title, activity);

        if(intent != null) {
            activity.startActivityForResult(intent, 1);
        }
    }

    /**
     * Open make photo intent
     * @param title
     * @param activity
     * @return
     */
    public Intent makePhotoIntent(String title, Activity activity){


        Intent chooser = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), "JPEG_" + System.currentTimeMillis() + ".jpg");

                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (Exception ex) {
                //Log.v((Config.TAG, "Error image = " + ex.toString());
            }

            if (photoFile != null) {
                outPutfileUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            }

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");

            if (photoFile != null) {
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            }


            //Create chooser
            chooser = Intent.createChooser(galleryIntent,title);
            Intent[] extraIntents = {takePictureIntent};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

        }

        return chooser;
    }

    /**
     * Get Array OutPut File Uri
     * @return
     */
    public ArrayList<String> getArrayOutPutfileUri() {
        return arrayOutPutfileUri;
    }

    /**
     * Set Array OutPut File Uri
     * @return
     */
    public void setArraryOutPutfileUri(ArrayList<String> array) {
         arrayOutPutfileUri = array ;
    }

    /**
     * Add Array OutPut File Uri
     */
    public void addOutPutfileUri() {
        arrayOutPutfileUri.add(outPutfileUri.toString());
    }

    public String getOutPutfileUri(){
        return arrayOutPutfileUri.get(arrayOutPutfileUri.size() - 1);
    }


    /**
     * Valido Step Login off
     * @param context
     * @param number
     * @param cpfCnpj
     * @return
     */
    public String[] validStepOff(Context context, String number, String cpfCnpj, int type){
        msgError = new String[2];
        validEmail = new ValidEmail();

        if(number.trim().equals("")){
            if(type == 0){
                msgError[0] = context.getResources().getString(R.string.message_empty_number_vehicle);
            } else {
                msgError[0] = context.getResources().getString(R.string.message_empty_number_plate);
            }

        } else {
            msgError[0] = "";
        }

        if(!cpfCnpj.equals("")){
            while(cpfCnpj.length() < 11){
                cpfCnpj = "0" + cpfCnpj;
            }
        }

        if(cpfCnpj.trim().equals("")){

            if(type == 0){
                msgError[1] = context.getResources().getString(R.string.message_empty_cpf_cnpj);
            } else {
                msgError[1] = context.getResources().getString(R.string.message_empty_cpf);
            }
        } else if(!validCPF.isCPF(cpfCnpj) && !validCNPJ.isCNPJ(cpfCnpj)){


            if(type == 0){
                msgError[1] = context.getResources().getString(R.string.message_error_cpf_cnpj);
            } else {
                msgError[1] = context.getResources().getString(R.string.message_error_cpf);
            }        }  else {
            msgError[1] = "";
        }

        return msgError;

    }

    /**
     * Valid step 2
     * @param context
     * @param name
     * @param email
     * @param phone
     * @param yes
     * @param nameDriver
     * @param birthday
     * @param phoneDriver
     * @return
     */
    public String[] validStep2(Context context, String name, String email, String phone, boolean yes, String nameDriver, String birthday, String phoneDriver){
        msgError = new String[6];
        validEmail = new ValidEmail();


        if(name.trim().length() < 1){
            msgError[0] = context.getString(R.string.message_empty_name);
        } else {
            msgError[0] = "";
        }

        if(email.trim().length() < 1){
            msgError[1] = context.getString(R.string.message_empty_email);
        } else if(!validEmail.checkemail(email)){
            msgError[1] = context.getString(R.string.message_error_email);
        } else {
            msgError[1] = "";
        }

        if(phone.trim().length() < 1){
            msgError[2] = context.getString(R.string.message_empty_phone);
        } else if (phone.length() < 9 ) {
            msgError[2] = context.getString(R.string.message_error_phone);
        } else {
            msgError[2] = "";
        }

        if(yes){
            msgError[3] = "";
            msgError[4] = "";
            msgError[5] = "";
        } else {
            if(nameDriver.trim().length() < 1){
                msgError[3] = context.getString(R.string.message_error_drive_name);
            } else {
                msgError[3] = "";
            }

            if(birthday.trim().length() < 1) {
                msgError[4] = context.getString(R.string.message_empty_birthday);
            } else {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                df.setLenient(false);
                try {
                    df.parse(birthday);
                    msgError[4] = "";

                } catch (ParseException ex) {
                    msgError[4] = context.getString(R.string.message_error_birthday);
                }
            }


            if(phoneDriver.trim().length() < 1){
                msgError[5] = context.getString(R.string.message_empty_phone);
            } else if (phoneDriver.length() < 9 ) {
                msgError[5] = context.getString(R.string.message_error_phone);
            } else {
                msgError[5] = "";
            }
        }

        return msgError;
    }

    /**
     * Valid step 3
     * @param context
     * @param location
     * @param ref
     * @param city
     * @param district
     * @return
     */
    public String[] validStep3(Context context, String location, String ref, String city, String district, String number){


        msgError = new String[8];


        if(location.trim().length() < 1){
            msgError[0] = context.getString(R.string.message_empty_location);
        } else {
            msgError[0] = "";
        }


        if(ref.trim().length() < 1){
            msgError[1] = context.getString(R.string.message_empty_ref);
        } else {
            msgError[1] = "";
        }

        if(city.trim().length() < 1){
            msgError[2] = context.getString(R.string.message_empty_city);
        } else {
            msgError[2] = "";
        }

        if(district.trim().length() < 1){
            msgError[3] = context.getString(R.string.message_empty_district);
        } else {
            msgError[3] = "";
        }

        if(number.trim().length() < 1){
            msgError[4] = context.getString(R.string.message_empty_number);
        } else {
            msgError[4] = "";
        }


        return msgError;
    }

    /**
     * Delete Image
     */
    public void removePhoto(String index){

        for(int ind = 0; ind < arrayOutPutfileUri.size(); ind++ ){
            if(arrayOutPutfileUri.get(ind).equals(index)){
                arrayOutPutfileUri.remove(ind);
            }
        }

        //Log.i(Config.TAG, "ArraySize: " + arrayOutPutfileUri.size());
    }

    /**
     *
     * Send Vehicle Accident
     * @param ctx
     */
    public void sendVehicleAccident(Context ctx){
        context = (Activity) ctx;

        conn = new Connection(context);

        createConnection();

        String param = "";

        String phone = vasb.getUserPhone();

        phone = phone.replace("(", "");
        phone = phone.replace(")", "");
        phone = phone.replace("-", "");


        String driverPhone = vasb.getDriverPhone();

        driverPhone = driverPhone.replace("(", "");
        driverPhone = driverPhone.replace(")", "");
        driverPhone = driverPhone.replace("-", "");

        if(vasb.getDescription().trim().equalsIgnoreCase("")){
            vasb.setDescription(ctx.getString(R.string.claim_withou_msg));
        }
        try{
            param = "Policy=" + URLEncoder.encode(vasb.getPolicy(), "UTF-8") +
                    "&ClaimType=" +   URLEncoder.encode(vasb.getClaimType(), "UTF-8") +
                    "&LicensePlate=" + URLEncoder.encode(vasb.getLicensePlate(), "UTF-8") +
                    "&ClaimDateTime=" + URLEncoder.encode(getDateHour(vasb.getClaimDateTime(), ctx), "UTF-8") +
                    "&Description=" + URLEncoder.encode(vasb.getDescription(), "UTF-8") +
                    "&ItemCode=" + URLEncoder.encode(vasb.getItemCode(), "UTF-8") +
                    "&ContractCode=" + URLEncoder.encode(vasb.getContract(), "UTF-8") +
                    "&IssueCode=" + URLEncoder.encode(vasb.getIssuance(), "UTF-8") +
                    "&CIACode=" + URLEncoder.encode(vasb.getCiaCode(), "UTF-8") +
                    "&IssuingAgency=" + URLEncoder.encode(vasb.getIssuingAgency(), "UTF-8") +
                    "&UserName=" + URLEncoder.encode(vasb.getUserName(), "UTF-8") +
                    "&UserEmail=" + URLEncoder.encode(vasb.getUserEmail(), "UTF-8") +
                    "&UserPhone=" + URLEncoder.encode(phone, "UTF-8") +
                    "&UserIsDriver=" + URLEncoder.encode(vasb.getUserIsDriver(), "UTF-8") +
                    "&AddressLine1=" + URLEncoder.encode(vasb.getAddressLine1(), "UTF-8") +
                    "&AddressLine2=" + URLEncoder.encode(vasb.getAddressLine2(), "UTF-8") +
                    "&Number=" + URLEncoder.encode(vasb.getNumber(), "UTF-8") +
                    "&AddressSupport=" + URLEncoder.encode(vasb.getAddressSupport(), "UTF-8") +
                    "&City=" + URLEncoder.encode(vasb.getCityBeans().getCity(), "UTF-8") +
                    "&CityCode=" + URLEncoder.encode(vasb.getCityBeans().getCityCode()+"", "UTF-8") +
                    "&District=" + URLEncoder.encode(vasb.getDistrict(), "UTF-8") +
                    "&DriverName=" + URLEncoder.encode(vasb.getDriverName(), "UTF-8") +
                    "&DriverBirthDate=" + URLEncoder.encode(getDate(vasb.getDriverBirthDate(), ctx), "UTF-8") +
                    "&DriverPhone=" + URLEncoder.encode(driverPhone, "UTF-8") +
                    "&State=" + URLEncoder.encode(getStateCod().get(vasb.getState()), "UTF-8");


            if(!cpfCnpj.equals("")){
                param = param + "&cpfCnpj=" + cpfCnpj;
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }

        typeConnection = 1;


        if(!cpfCnpj.equals("")){
            conn.startConnection("Sinistro/NovoSinistro/", param, 1, true);
        } else {
            conn.startConnection("Sinistro/", param, 1, true);

        }
    }

    /**
     * Create
     */
    private void createConnection() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {

                try{
                    typeError = 1;
                    onConnectionResult.onError();
                } catch(Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "VehicleAccident: " + result);

                try {
                    if(result.contains("numeroAvisoSinistro")){

                        nwvhbeans = gson.fromJson(result, NumberWarningVehicleAccidentBeans.class);
                        onConnectionResult.onSucess();
                    } else {
                        message = gson.fromJson(result, MessageBeans.class);
                        typeError = 2;
                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();

                    message = gson.fromJson(result, MessageBeans.class);
                    typeError = 2;
                    onConnectionResult.onError();
                }
            }
        });
    }


    /**
     *
     * @param dateNow
     * @return
     */
    public String getDateHour(String dateNow, Context context){

        String value = dateNow;

        if(!dateNow.contains("T")) {

            Locale locale = new Locale("pt", "BR");

            dateNow = dateNow.replace("T", " ");
            dateNow = dateNow.replace("Z", " ");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date dateSdf = null;
            try {
                dateSdf = sdf.parse(dateNow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            value = formatter.format(dateSdf);
        }

        return value;

    }

    /**
     *
     * @param dateNow
     * @return
     */
    public String getDate(String dateNow, Context context){

        String value = dateNow;

        if(!dateNow.equals("") && !dateNow.equals("Nascimento") && !dateNow.equals("Data")) {

            Locale locale = new Locale("pt", "BR");

            dateNow = dateNow.replace("T", " ");
            dateNow = dateNow.replace("Z", " ");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateSdf = null;
            try {
                dateSdf = sdf.parse(dateNow);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd");
            value = formatter.format(dateSdf);
        }

        return value;

    }

    /**
     * Get Real Path From URI
     * @param contentURI
     * @param context
     * @return
     */
    public String getRealPathFromURI(Uri contentURI, Context context) {
        Cursor cursor = null;

        String result;


        try {

            if("content".equals(contentURI.getScheme())) {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                result = cursor.getString(column_index);
            }
            else{
                result = contentURI.getPath();
            }


        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;

    }


    /**
     *
     Check file size
     * @param context
     * @param uri
     * @return
     */
    public boolean checkFileSize(Context context, Uri uri){
        boolean ok = false;

        try{
            File fileSize = new File(getRealPathFromURI(uri, context));

            sizeFile += fileSize.length();

            if(sizeFile <= Config.fileSizeAllowed){
                ok = true;
                //Log.i(Config.TAG,  "File Size Allowed ok: " + fileSize.length());
            } else {
                ok = false;
                //Log.i(Config.TAG,  "File Size Allowed not ok: " + fileSize.length());
            }

            //Log.i(Config.TAG,  "File Size: " + sizeFile);

        } catch(Exception ex){
            ex.printStackTrace();
            //Log.i(Config.TAG,  "File Size Allowed error");
            ok = false;
        }


        return ok;
    }

    /**
     * Get Text Size Photos
     * @param context
     * @return
     */
    public String getTextSizePhots(Context context){
        String sourceString = context.getString(R.string.photo_size_part1) + " <b>" +
                getArrayOutPutfileUri().size() + " " + context.getString(R.string.photos) + " </b> " +
                context.getString(R.string.photo_size_part2);


        return sourceString;
    }

    /**
     * Get Audio File
     * @return
     */
    public File getAudioFile(){
        return audiofile;
    }

    /**
     * Get NumberWarningVehicleAccidentBeans
     * @return
     */
    public NumberWarningVehicleAccidentBeans getNwvhbeans() {
        return nwvhbeans;
    }

    /**
     * Get TypeConnection
     * @return
     */
    public int getTypeConnection(){
        return typeConnection;
    }


    /**
     * Set uri
     * @param uri
     */
    public void setUri(Uri uri){
        outPutfileUri = uri;
    }
    /**
     * Get Uri Image
     * @return
     */
    public Uri getUri(){
        return outPutfileUri;
    }

    /**
     * Get Index Remove
     * @return
     */
    public long getIndexRemove(){
        return indexRemove;
    }


    /**
     * Image rotate
     * @param bitmap
     * @param path
     * @return
     */
    public Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Get Login Beans
     * @param context
     * @return
     */
    public LoginBeans getLoginBeans(Context context){
        if(lb == null){
            InfoUser infoUser = new InfoUser();
            lb = infoUser.getUserInfo(context);
        }

        return lb;
    }

    /**
     * Get State
     * @return
     */
    public ArrayList<String> getState(){
        arrayState = new ArrayList<>();

        arrayState.add("Estado");
        arrayState.add("Acre");
        arrayState.add("Alagoas");
        arrayState.add("Amazonas");
        arrayState.add("Amapá");
        arrayState.add("Bahia");
        arrayState.add("Ceará");
        arrayState.add("Distrito Federal");
        arrayState.add("Espírito Santo");
        arrayState.add("Goiás");
        arrayState.add("Maranhão");
        arrayState.add("Minas Gerais");
        arrayState.add("Mato Grosso do Sul");
        arrayState.add("Mato Grosso ");
        arrayState.add("Pará");
        arrayState.add("Paraíba");
        arrayState.add("Pernambuco");
        arrayState.add("Piauí");
        arrayState.add("Paraná");
        arrayState.add("Rio de Janeiro");
        arrayState.add("Rio Grande do Norte");
        arrayState.add("Rondônia");
        arrayState.add("Roraima");
        arrayState.add("Rio Grande do Sul");
        arrayState.add("Santa Catarina");
        arrayState.add("Sergipe");
        arrayState.add("São Paulo");
        arrayState.add("Tocantins");

        return arrayState;
    }

    /**
     * Get Cod State
     * @return
     */
    private ArrayList<String> getStateCod(){
        arrayStateCod = new ArrayList<>();

        arrayStateCod.add("");
        arrayStateCod.add("AC");
        arrayStateCod.add("AL");
        arrayStateCod.add("AM");
        arrayStateCod.add("AP");
        arrayStateCod.add("BA");
        arrayStateCod.add("CE");
        arrayStateCod.add("DF");
        arrayStateCod.add("ES");
        arrayStateCod.add("GO");
        arrayStateCod.add("MA");
        arrayStateCod.add("MG");
        arrayStateCod.add("MS");
        arrayStateCod.add("MT");
        arrayStateCod.add("PA");
        arrayStateCod.add("PB");
        arrayStateCod.add("PE");
        arrayStateCod.add("PI");
        arrayStateCod.add("PR");
        arrayStateCod.add("RJ");
        arrayStateCod.add("RN");
        arrayStateCod.add("RO");
        arrayStateCod.add("RR");
        arrayStateCod.add("RS");
        arrayStateCod.add("SC");
        arrayStateCod.add("SE");
        arrayStateCod.add("SP");
        arrayStateCod.add("TO");

        return arrayStateCod;
    }

    public void openCities(Context context, int index){

        Intent it = new Intent(context, Cities.class);
        it.putExtra("state", getStateCod().get(index));
        context.startActivity(it);

    }


    public void setNumberClaim(String stringNumber){

        if(nwvhbeans == null){
            nwvhbeans = new NumberWarningVehicleAccidentBeans();
        }

        nwvhbeans.setNumeroAvisoSinistro(stringNumber);


    }

    /**
     * Open ViewerDocument
     * @param context
     */
    public void openDocuments(Activity context){
        DocumentsPictureModel.numberPolicy = vasb.getPolicy();
        Intent it = new Intent(context, ViewerDocuments.class);
        context.startActivityForResult(it, 98);
    }

    /**
     * Open ViewerDocument
     * @param context
     */
    public void openDocuments(Activity context, String numberPolicy){
        DocumentsPictureModel.numberPolicy = numberPolicy;
        Intent it = new Intent(context, ViewerDocuments.class);
        context.startActivityForResult(it, 98);
    }
    /**
     * Documents Image Manager
     * @param context
     * @return
     */
    public DocumentsImageManager getDim(Context context){
        if(dim == null){
            dim = new DocumentsImageManager(context);
        }

        return dim;
    }
}


