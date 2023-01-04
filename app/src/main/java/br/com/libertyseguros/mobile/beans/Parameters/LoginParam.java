package br.com.libertyseguros.mobile.beans.Parameters;


public class LoginParam {

    private String grant_type;
    private String type;
    private String pwd;
    private String userId;
    private int deviceOS;
    private boolean useToken;


    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getUseToken() {
        return useToken;
    }

    public void setDUseToken(boolean useToken) {
        this.useToken = useToken;
    }

    public int getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(int deviceOS) {
        this.deviceOS = deviceOS;
    }

    public boolean isUseToken() {
        return useToken;
    }

    public void setUseToken(boolean useToken) {
        this.useToken = useToken;
    }


}
