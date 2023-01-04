package br.com.libertyseguros.mobile.beans;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.libertyseguros.mobile.libray.Config;

public class AssitanceVision360Beans {
    private String dataOcorrencia;

    private String descricao;

    private String data;

    private String hora;

    private String imagem;


    public String getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(String dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        if(TextUtils.isEmpty(dataOcorrencia)){
            return "";
        } else {
            try {
                String arrayDate[] = dataOcorrencia.split("T");

                String dateSplit = arrayDate[0];

                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate = spf.parse(dateSplit);
                spf = new SimpleDateFormat("dd/MM/yyyy");

                data = spf.format(newDate);

                return data;

            } catch (Exception ex) {
                Log.i(Config.TAG, ex.toString());
                return "";
            }

        }

    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {

        if(TextUtils.isEmpty(dataOcorrencia)){
            return "";
        } else {
            try{

                String arrayHours[] = dataOcorrencia.split("T");

                String hoursSplit = arrayHours[1].split("-")[0];

                SimpleDateFormat spf=new SimpleDateFormat("HH:mm:ss");
                Date newDate=spf.parse(hoursSplit);
                spf= new SimpleDateFormat("HH:mm");

                hora = spf.format(newDate);

                return hora;
            }catch (Exception ex){
                Log.i(Config.TAG, ex.toString());
                return "";
            }
        }
    }

    public void setHora(String hora) {
        this.hora = hora;
    }


    public String getImagem() {
        return TextUtils.isEmpty(imagem) ? "": imagem.toLowerCase();
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
