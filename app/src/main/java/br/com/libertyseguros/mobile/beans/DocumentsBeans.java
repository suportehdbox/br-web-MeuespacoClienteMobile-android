package br.com.libertyseguros.mobile.beans;


import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

public class DocumentsBeans {

    private ArrayList<DocumentData> documents;

    public DocumentsBeans() {
        documents = new ArrayList<>();
    }

    public ArrayList<DocumentData> getDocumentsData() {
        return documents;
    }

    public void setDocumentsData(ArrayList<DocumentData> documents) {
        this.documents = documents;
    }

    public DocumentData newInstanceDocumentData() {
        return new DocumentData();
    }

    public class DocumentData {
        private String dataExpurgo;
        private String idDocumento;
        private boolean download;
        private boolean removed;
        private boolean selected;
        private String path;
        private Uri uri;
        private Bitmap bm;

        public boolean isRemoved() {
            return removed;
        }

        public void setRemoved(boolean removed) {
            this.removed = removed;
        }

        public String getDataExpurgo() {
            return dataExpurgo;
        }

        public void setDataExpurgo(String dataExpurgo) {
            this.dataExpurgo = dataExpurgo;
        }

        public String getIdDocumento() {
            return idDocumento;
        }

        public void setIdDocumento(String idDocumento) {
            this.idDocumento = idDocumento;
        }

        public boolean isDownload() {
            return download;
        }

        public void setDownload(boolean download) {
            this.download = download;
        }

        public Bitmap getBm() {
            return bm;
        }

        public void setBm(Bitmap bm) {
            this.bm = bm;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Uri getUri() {
            return uri;
        }

        public void setUri(Uri uri) {
            this.uri = uri;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
