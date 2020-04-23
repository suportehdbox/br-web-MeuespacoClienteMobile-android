package br.com.libertyseguros.mobile.beans;


public class InstallmentsBeans {

    private boolean canExtend;
    private String dueDate;
    private int number;
    private String paidValue;
    private String paymentDate;
    private int status;
    private String value;
    private int showComponent;

    public String getCodigoTipoModalidadeCobranca() {
        return codigoTipoModalidadeCobranca;
    }

    public void setCodigoTipoModalidadeCobranca(String codigoTipoModalidadeCobranca) {
        this.codigoTipoModalidadeCobranca = codigoTipoModalidadeCobranca;
    }

    private String codigoTipoModalidadeCobranca;

    public boolean isCanExtend() {
        return canExtend;
    }

    public void setCanExtend(boolean canExtend) {
        this.canExtend = canExtend;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPaidValue() {
        return paidValue;
    }

    public void setPaidValue(String paidValue) {
        this.paidValue = paidValue;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getShowComponent() {
        return showComponent;
    }

    public void setShowComponent(int showComponent) {
        this.showComponent = showComponent;
    }
}
