package br.com.libertyseguros.mobile.beans;


public class LoginBeans {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String userName;
    private String authToken;
    private String issued;
    private String expires;
    private String CpfCnpj;
    private String Email;
    private String Photo;
    private String idFacebook;
    private boolean hasFacebook;
    private boolean hasGooglePlus;
    private boolean forceResetPassword;

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }


    public LoginBeans() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }


    public boolean isHasFacebook() {
        return hasFacebook;
    }

    public void setHasFacebook(boolean hasFacebook) {
        this.hasFacebook = hasFacebook;
    }

    public boolean isHasGooglePlus() {
        return hasGooglePlus;
    }

    public void setHasGooglePlus(boolean hasGooglePlus) {
        this.hasGooglePlus = hasGooglePlus;
    }

    public boolean isForceResetPassword() { return forceResetPassword; }

    public void setForceResetPassword(boolean forceResetPassword) {  this.forceResetPassword = forceResetPassword; }
}
