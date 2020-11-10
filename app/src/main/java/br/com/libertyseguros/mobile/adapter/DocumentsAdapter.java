package br.com.libertyseguros.mobile.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import br.com.libertyseguros.mobile.R;
import br.com.libertyseguros.mobile.beans.DocumentsBeans;
import br.com.libertyseguros.mobile.controller.DocumentPicturesController;
import br.com.libertyseguros.mobile.view.Documents;

public class DocumentsAdapter extends BaseAdapter {

    private OnSelectedPhoto onSelectedPhoto;

    private RelativeLayout rlContent;

    private ImageView ivDocuments;

    private LayoutInflater inflater;

    private Context context;

    private int indexSelected;

    private ArrayList<DocumentsBeans.DocumentData> documentsData;

    private DocumentPicturesController documentPicturesController;

    public DocumentsAdapter(Context context, ArrayList<DocumentsBeans.DocumentData> documentsData, DocumentPicturesController documentPicturesController, OnSelectedPhoto onSelectedPhoto) {
        this.context = context;
        this.documentsData = documentsData;
        this.documentPicturesController = documentPicturesController;
        this.onSelectedPhoto = onSelectedPhoto;
        inflater = LayoutInflater.from(context);

        indexSelected = 0;

    }

    @Override
    public int getCount() {
        return documentsData.size();
    }

    @Override
    public Object getItem(int i) {
        return documentsData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vi;

        if (view == null) {
            vi = inflater.inflate(R.layout.item_documents, null);
        } else {
            vi = view;
        }

        rlContent = (RelativeLayout) vi.findViewById(R.id.rl_content);
        rlContent.setTag(String.valueOf(i));
        ivDocuments = (ImageView) vi.findViewById(R.id.iv_documents);

        if (documentsData.get(i).getBm() == null) {
            Bitmap bitmap;
            if (documentsData.get(i).getIdDocumento() == null) {
                bitmap = documentPicturesController.createBitmapThumb(documentsData.get(i).getPath());
            } else {
                bitmap = documentPicturesController.createThumbImageSaved(i);
            }

            ivDocuments.setImageBitmap(bitmap);
            documentsData.get(i).setBm(bitmap);

        }

        rlContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("teste", "teste1");
                if(documentsData.get(Integer.parseInt(v.getTag().toString())).getPath() == null){
                    documentPicturesController.openDocumentView(context,
                            documentsData.get(Integer.parseInt(v.getTag().toString())).getIdDocumento(), false);
                } else{
                    documentPicturesController.openDocumentView(context,
                            documentsData.get(Integer.parseInt(v.getTag().toString())).getPath(), true);
                }


            }
        });

        rlContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean selected = documentsData.get(Integer.parseInt(v.getTag().toString())).isSelected();
                ImageView ivDocuments = (ImageView) v.findViewById(R.id.iv_documents);
                v.findViewById(R.id.iv_check_box).setVisibility(selected ? View.GONE : View.VISIBLE);
                documentsData.get(Integer.parseInt(v.getTag().toString())).setSelected(!selected);

                if (selected) {
                    ivDocuments.clearColorFilter();
                    indexSelected--;
                } else {
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);

                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    ivDocuments.setColorFilter(filter);
                    indexSelected++;
                }

                onSelectedPhoto.onSelected(indexSelected);

                return true;
            }
        });

        return vi;
    }

    public void add(DocumentsBeans.DocumentData data) {
        this.documentsData.add(data);
        documentPicturesController.getDocuments().setDocumentsData(documentsData);
    }

    public interface OnSelectedPhoto {
        void onSelected(int index);
    }

}
