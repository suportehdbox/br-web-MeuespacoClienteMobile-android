package br.com.libertyseguros.mobile.beans;


public class PaymentBeans {

    private String value;

    private boolean canExtend;

    private String dueDate;

    private String nextDate;

    private String nextValue;

    private int status;

    private int installmentNumber;

    private int amountOfInstallment;

    private String codigoTipoModalidadeCobranca;

    private int showComponent;

    public String getCodigoTipoModalidadeCobranca() {
        return codigoTipoModalidadeCobranca;
    }

    public void setCodigoTipoModalidadeCobranca(String codigoTipoModalidadeCobranca) {
        this.codigoTipoModalidadeCobranca = codigoTipoModalidadeCobranca;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    public String getNextDueDate() {
        return nextDate;
    }

    public void setNextDueDate(String nextDueDate) {
        this.nextDate = nextDueDate;
    }

    public String getNextValue() {
        return nextValue;
    }

    public void setNextValue(String nextValue) {
        this.nextValue = nextValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(int installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public int getAmountOfInstallment() {
        return amountOfInstallment;
    }

    public int getShowComponent() {
        return showComponent;
    }

    public void setShowComponent(int showComponent) {
        this.showComponent = showComponent;
    }
}
