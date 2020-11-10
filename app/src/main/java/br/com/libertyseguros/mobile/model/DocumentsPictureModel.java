package br.com.libertyseguros.mobile.model;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.DocumentBase64Beans;
import br.com.libertyseguros.mobile.beans.DocumentsBeans;
import br.com.libertyseguros.mobile.beans.DocumentsUploadBeans;
import br.com.libertyseguros.mobile.beans.MessageBeans;
import br.com.libertyseguros.mobile.libray.Config;
import br.com.libertyseguros.mobile.libray.Connection;
import br.com.libertyseguros.mobile.libray.DocumentsImageManager;
import br.com.libertyseguros.mobile.libray.InfoUser;
import br.com.libertyseguros.mobile.util.OnConnection;
import br.com.libertyseguros.mobile.util.OnConnectionResult;
import br.com.libertyseguros.mobile.view.DocumentView;

import br.com.libertyseguros.mobile.view.Support;

public class DocumentsPictureModel extends BaseModel {

    public final static int TOTAL_DOCUMENTS = 5;

    public static boolean exit;

    public static String numberPolicy;

    private Activity context;

    private Connection conn;

    private OnConnectionResult onConnectionResult;

    private MessageBeans message;

    private Gson gson;

    private DocumentsBeans documents;

    private ArrayList<String> idsDeleted;

    private DocumentsImageManager dim;

    private InfoUser infoUser;

    private Uri outPutfileUri;

    private String msgError[];

    private String mCurrentPhotoPath;
    private String extension = ".jpg";

    private long sizeFile;

    private int typeError;
    private int indexRemove;
    private int typeConnection;
    private int indexDownload;
    private int imageX;
    private int imageY;

    public DocumentsPictureModel(OnConnectionResult onConnectionResult, Context context, int imageX, int imageY) {
        documents = new DocumentsBeans();
        this.onConnectionResult = onConnectionResult;
        gson = new Gson();
        dim = new DocumentsImageManager(context);
        indexRemove = -1;
        infoUser = new InfoUser();
        infoUser.getUserInfo(context);
        this.imageY = imageY;
        this.imageX = imageX;
    }


    /**
     * get Documents
     *
     * @param ctx
     */
    public void getDocuments(Context ctx) {
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 1;

        createConnectionGetDocuments();

        conn.startConnection("Documento/GetDocumentsByPolicy?NumeroApolice=" + numberPolicy, "", 2, true);
    }

    /**
     * Create
     */
    private void createConnectionGetDocuments() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {
                    //Log.i(Config.TAG, "Error get document");
                    onConnectionResult.onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "Documents: " + result);

                try {
                    if (result.contains("idDocumento")) {

                        message = null;

                        documents = gson.fromJson(result, DocumentsBeans.class);
                        DocumentsBeans documentsTemp = new DocumentsBeans();

                        for (int ind = 0; ind < documents.getDocumentsData().size(); ind++) {

                            if (!dim.verifyImageCache(documents.getDocumentsData().get(ind).getIdDocumento() + extension, documents.getDocumentsData().get(ind).getDataExpurgo())) {

                                if (dim.isImageCache(documents.getDocumentsData().get(ind).getIdDocumento() + extension)) {
                                    documents.getDocumentsData().get(ind).setDownload(true);
                                } else {
                                    documents.getDocumentsData().get(ind).setDownload(false);
                                }
                                documentsTemp.getDocumentsData().add(documents.getDocumentsData().get(ind));
                            }

                        }
                        documents = documentsTemp;

                        createDownloadDocument();

                    } else {
                        documents = new DocumentsBeans();
                        onConnectionResult.onSucess();
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    /**
     * Delete Documents
     */
    public void deleteImages() {

        conn = new Connection(context);

        typeConnection = 4;

        createConnectionDeleteImage();

        String params = createDeleteFiles();

        conn.startConnection("/Documento/RemoveDocuments", params, 1, true);
    }

    /**
     * Create Connection Delete Documents
     */
    private void createConnectionDeleteImage() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {
                    onConnectionResult.onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "Delete: " + result);

                try {
                    dim.deleteImageDocuments(idsDeleted, extension);
                    onConnectionResult.onSucess();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    /**
     * get Image
     */
    public void getImage(String id) {

        conn = new Connection(context);

        typeConnection = 2;

        createConnectionGetImage();

        conn.startConnection("Documento/Download?idDocumento=" + id, "", 2, true);
    }

    /**
     * Create
     */
    private void createConnectionGetImage() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {

                    documents.getDocumentsData().get(indexDownload).setDownload(true);
                    createDownloadDocument();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "Documents: " + result);

                try {
                    if (result.contains("conteudo")) {

                        DocumentBase64Beans documentsUpload = gson.fromJson(result, DocumentBase64Beans.class);
                        dim.saveImageDocuments(documentsUpload.getConteudo(), documents.getDocumentsData().get(indexDownload).getIdDocumento() + extension, documents.getDocumentsData().get(indexDownload).getDataExpurgo());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                documents.getDocumentsData().get(indexDownload).setDownload(true);
                createDownloadDocument();
            }
        });
    }

    /**
     * Convert Bitmap in base 64
     *
     * @param bitmap
     * @return
     */
    private String getImageBase64(byte[] bitmap) {
        String image64 = Base64.encodeToString(bitmap, Base64.DEFAULT);
        return image64;
    }

    /**
     * Get message error
     *
     * @return
     */
    public MessageBeans getMessage() {
        return message;
    }

    /**
     * Get type error connection
     *
     * @return
     */
    public int getTypeError() {
        return typeError;
    }


    /**
     * Open Support Screen
     */
    public void openSupport(Context context) {
        Intent it = new Intent(context, Support.class);
        context.startActivity(it);
    }


    /**
     * /**
     * Open Make Photo Intent
     *
     * @param title
     * @param activity
     */
    public void openMakePhotoIntent(String title, Activity activity) {
        Intent intent = makePhotoIntent(title, activity);

        if (intent != null) {
            activity.startActivityForResult(intent, 1);
        }
    }

    /**
     * Open make photo intent
     *
     * @param title
     * @param activity
     * @return
     */
    public Intent makePhotoIntent(String title, Activity activity) {

        outPutfileUri = null;

        Intent chooser = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = new File(Environment.getExternalStorageDirectory(), "JPEG_" + System.currentTimeMillis() + ".jpg");


                mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
            } catch (Exception ex) {
                //Log.v((Config.TAG, "Error image = " + ex.toString());
            }
            if (photoFile != null) {
                outPutfileUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            }

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");

            if (photoFile != null) {
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
            }

            //Create chooser
            chooser = Intent.createChooser(galleryIntent, title);
            Intent[] extraIntents = {takePictureIntent};
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);

        }

        return chooser;
    }


    /**
     * @param dateNow
     * @return
     */
    public String getDateHour(String dateNow, Context context) {

        String value = dateNow;

        if (!dateNow.contains("T")) {

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
     * Get Real Path From URI
     *
     * @param contentURI
     * @param context
     * @return
     */
    public String getRealPathFromURI(Uri contentURI, Context context) {
        Cursor cursor = null;

        String result;

        try {

            if ("content".equals(contentURI.getScheme())) {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = context.getContentResolver().query(contentURI, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                result = cursor.getString(column_index);
            } else {
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
     * Check file size
     *
     * @param context
     * @param path
     * @return
     */
    public boolean checkFileSize(Context context, String path) {
        boolean ok;

        try {
            File fileSize = new File(path);

            sizeFile += fileSize.length();

            if (sizeFile <= Config.fileSizeAllowed) {
                ok = true;
                //Log.i(Config.TAG, "File Size Allowed ok: " + fileSize.length());
            } else {
                ok = false;
                //Log.i(Config.TAG, "File Size Allowed not ok: " + fileSize.length());
            }

            //Log.i(Config.TAG, "File Size: " + sizeFile);

        } catch (Exception ex) {
            ex.printStackTrace();
            //Log.i(Config.TAG, "File Size Allowed error");
            ok = false;
        }


        return ok;
    }

    /**
     * Get Text Size Photos
     *
     * @param context
     * @return
     */
    public String getTextSizePhots(Context context) {
        int sizeDocument = 0;
        if (documents.getDocumentsData() != null) {
            sizeDocument = documents.getDocumentsData().size();
        }

        String sourceString = context.getString(R.string.photo_size_part1) + " <b>" +
                sizeDocument + " " + context.getString(R.string.photos) + " </b> " +
                context.getString(R.string.photo_size_part2);


        return sourceString;
    }


    /**
     * Get TypeConnection
     *
     * @return
     */
    public int getTypeConnection() {
        return typeConnection;
    }

    /**
     * Get Index Remove
     *
     * @return
     */
    public long getIndexRemove() {
        return indexRemove;
    }


    /**
     * Image rotate
     *
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
     * Create Upload
     */
    private void createDownloadDocument() {
        boolean upload = false;

        for (int ind = 0; ind < documents.getDocumentsData().size(); ind++) {
            if (!documents.getDocumentsData().get(ind).isDownload()) {
                indexDownload = ind;
                upload = true;
                ind = documents.getDocumentsData().size();
            }
        }

        if (!upload) {
            onConnectionResult.onSucess();
        } else {
            getImage(documents.getDocumentsData().get(indexDownload).getIdDocumento());

        }
    }

    /**
     * Get Documents
     *
     * @return
     */
    public DocumentsBeans getDocuments() {
        return documents;
    }

    /**
     * Set Documents
     *
     * @param documents
     */
    public void setDocuments(DocumentsBeans documents) {
        this.documents = documents;
    }

    /**
     * Get bitamp image
     *
     * @param name
     * @return
     */
    public Bitmap getImageBitmap(String name) {
        return dim.getImageBitmap(name);
    }

    /**
     * Get Extension
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }


    public String createDeleteFiles() {

        ArrayList<DocumentsBeans.DocumentData> documentsData = getDocuments().getDocumentsData();

        String documentParam = "";

        int count = 0;
        idsDeleted = new ArrayList<>();

        for (DocumentsBeans.DocumentData item : documentsData) {
            if (item.isSelected()) {
                documentParam += "&Documents[" + count + "]=" + item.getIdDocumento();
                idsDeleted.add(item.getIdDocumento());
                count++;
            }
        }

        return "Policy=" + numberPolicy + documentParam;

    }

    /**
     * Create Upload Files
     *
     * @param activity
     */
    public void createUploadFiles(Activity activity) {
        if (context == null) {
            context = activity;
        }

        ArrayList<DocumentsBeans.DocumentData> documentsData = getDocuments().getDocumentsData();

        try {
            int size = getDocuments().getDocumentsData().size();

            DocumentsUploadBeans documentsUploadBeans = new DocumentsUploadBeans(size);

            for (int ind = 0; ind < documentsData.size(); ind++) {

                if (documentsData.get(ind).getIdDocumento() == null) {
                    if (checkFileSize(activity, documentsData.get(ind).getPath())) {
                        ByteArrayOutputStream out = null;

                        try {
                            try {
                                File imagefile = new File(documentsData.get(ind).getPath());
                                BitmapFactory.Options o = new BitmapFactory.Options();
                                o.inSampleSize = 2;
                                Bitmap bm = BitmapFactory.decodeFile(imagefile.getAbsolutePath(), o);
                                out = new ByteArrayOutputStream();
                                bm.compress(Bitmap.CompressFormat.JPEG, 32, out);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            DocumentsUploadBeans.Files files = documentsUploadBeans.getInstaceFile();
                            files.setConteudo(getImageBase64(out.toByteArray()));
                            files.setExtensao("jpg");
                            files.setNome(System.currentTimeMillis() + "_photo");

                            documentsUploadBeans.getArquivos()[ind] = files;

                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            documentsUploadBeans.setNumeroApolice(numberPolicy);
            documentsUploadBeans.setCpfCnpj(infoUser.getCpfCnpj(context));

            createJson(documentsUploadBeans, activity);

        } catch (Exception ex) {
            ex.printStackTrace();
            onConnectionResult.onError();
            //Log.i(Config.TAG, "Error create upload file");
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            onConnectionResult.onError();
            //Log.i(Config.TAG, "Error create upload file outofMemory ");
        }


    }

    public void createJson(DocumentsUploadBeans documentsUploadBeanses, Context context) {

        Gson gson = new Gson();
        String json = gson.toJson(documentsUploadBeanses);
        json = json.replace("null,", "");

        uploadPhotos(context, json);
    }


    /**
     * Upload Files
     *
     * @param ctx
     */
    public void uploadPhotos(Context ctx, String json) {
        context = (Activity) ctx;

        conn = new Connection(context);

        typeConnection = 3;

        createConnectionUpload();

        String param = "";

        try {
            param = json;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        conn.startConnection("Documento/Upload", param, 5, false);
    }

    /**
     * Create Connection Upload
     */
    private void createConnectionUpload() {
        conn = new Connection(context);

        conn.setOnConnection(new OnConnection() {
            @Override
            public void onError(String msg) {
                try {
                    //Log.i(Config.TAG, "Error upload connection");
                    onConnectionResult.onError();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onSucess(String result) {
                //Log.i(Config.TAG, "Upload: " + result);

                try {
                    if (result.contains("idDocumento")) {
                        onConnectionResult.onSucess();
                    } else {
                        message = null;
                        Gson gson = new Gson();
                        message = gson.fromJson(result, MessageBeans.class);

                        onConnectionResult.onError();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    message = gson.fromJson(result, MessageBeans.class);
                    onConnectionResult.onError();
                }
            }
        });
    }

    /**
     * Get URI in Bitmap
     *
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /**
     * Open Activity DocumentView
     *
     * @param context
     * @param uriImage
     */
    public void openDocumentView(Context context, String uriImage, boolean isPath) {
        Intent it = new Intent(context, DocumentView.class);
        if(isPath){
            it.putExtra("image", uriImage);
        } else {
            it.putExtra("nameImage", uriImage + extension);
        }

        context.startActivity(it);
    }

    public Uri getOutPutfileUri() {
        return outPutfileUri;
    }

    public void setOutPutfileUri(Uri outPutfileUri) {
        this.outPutfileUri = outPutfileUri;
    }


    public DocumentsBeans.DocumentData addNewPhoto(String path) {
        DocumentsBeans.DocumentData documentData = getDocuments().newInstanceDocumentData();
        documentData.setPath(path);

        return documentData;
    }


    /**
     * Create Thumb Documents new image
     *
     * @param path
     * @return
     */
    public Bitmap createBitmapThumb(String path) {
        Bitmap thumbImage = null;

        try {
            thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), imageX, imageY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return thumbImage;
    }


    /**
     * Create Thumb Documents image saved
     *
     * @param position
     * @return
     */
    public Bitmap createThumbImageSaved(int position) {
        Bitmap bm = getImageBitmap(getDocuments().getDocumentsData().get(position).getIdDocumento() +
                getExtension());
        Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(bm, imageX, imageY);
        Bitmap bitmap = imageOreintationValidator(ThumbImage, getDocuments().getDocumentsData().get(position).getIdDocumento() +
                getExtension());

        return bitmap;
    }
}


