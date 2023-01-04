package br.com.libertyseguros.mobile.beans.Parameters;


public class WorkshopParam {

    private String UserId;
    private String CEP;
    private String Radius;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getRadius() {
        return Radius;
    }

    public void setRadius(String radius) {
        Radius = radius;
    }
}
