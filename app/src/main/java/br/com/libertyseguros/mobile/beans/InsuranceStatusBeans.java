package br.com.libertyseguros.mobile.beans;


public class InsuranceStatusBeans {

    private String dataEndPolicy;

    private String dataStartPolicy;

    private String licensePlate;

    private int status;

    public String getDataEndPolicy() {
        return dataEndPolicy;
    }

    public void setDataEndPolicy(String dataEndPolicy) {
        this.dataEndPolicy = dataEndPolicy;
    }

    public String getDataStartPolicy() {
        return dataStartPolicy;
    }

    public void setDataStartPolicy(String dataStartPolicy) {
        this.dataStartPolicy = dataStartPolicy;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
