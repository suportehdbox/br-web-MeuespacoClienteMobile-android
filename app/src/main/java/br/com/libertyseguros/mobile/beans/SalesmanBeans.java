package br.com.libertyseguros.mobile.beans;


public class SalesmanBeans {

    private int brokerCode;

    private int code;

    private String description;

    private String phone;

    private String email;

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    private String policy;

    public int getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(int brokerCode) {
        brokerCode = brokerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
