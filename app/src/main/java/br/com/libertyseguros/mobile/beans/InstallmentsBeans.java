package br.com.libertyseguros.mobile.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InstallmentsBeans {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final long DAY = 1000 * 60 * 60 * 60 * 24L;
    private boolean canExtend;
    private String dueDate;
    private int number;
    private String paidValue;
    private String paymentDate;
    private int status;
    private boolean isExpired = false;
    private boolean isPayable = false;
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
        try {
            var parsedDueDate = format.parse(dueDate);
            if (parsedDueDate == null) {
                parsedDueDate = new Date();
            }
            long now = System.currentTimeMillis();
            var delta = new Date(now + DAY * (number == 1 ? 30 : 8));
            this.isPayable = parsedDueDate.before(delta);
            this.isExpired = parsedDueDate.before(new Date());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.dueDate = dueDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public boolean isPayable() {
        return isPayable;
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
