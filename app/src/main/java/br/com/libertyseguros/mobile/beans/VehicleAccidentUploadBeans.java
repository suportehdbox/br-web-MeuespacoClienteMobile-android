package br.com.libertyseguros.mobile.beans;


import java.util.ArrayList;

public class VehicleAccidentUploadBeans {

    private String NumeroSinistro;

    private ArrayList<UploadFileBeans> Arquivos;

    public String getNumeroSinistro() {
        return NumeroSinistro;
    }

    public void setNumeroSinistro(String numeroSinistro) {
        NumeroSinistro = numeroSinistro;
    }

    public ArrayList<UploadFileBeans> getArquivos() {
        return Arquivos;
    }

    public void setArquivos(ArrayList<UploadFileBeans> arquivos) {
        Arquivos = arquivos;
    }
}
