package br.com.libertyseguros.mobile.beans;

public class ClaimBeans {
    private int type;

    private String date;

    private int status;

    private int number;

    private String policy;

    public int getClaimType() {
        return type;
    }

    public void setClaimType(int claimType) {
        this.type = claimType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatusClaim() {
        return status;
    }

    public void setStatusClaim(int statusClaim) {
        this.status = statusClaim;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
