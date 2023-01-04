package br.com.libertyseguros.mobile.beans;

public class Vision360Beans {
    private String dataOcorrencia;

    private String descricao;

    private String status;

    private String valorFranquia;

    private String valorPago;

    private int color = -1;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValorFranquia() {
        return valorFranquia;
    }

    public void setValorFranquia(String valorFranquia) {
        this.valorFranquia = valorFranquia;
    }

    public String getValorPago() {
        return valorPago;
    }

    public void setValorPago(String valorPago) {
        this.valorPago = valorPago;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
