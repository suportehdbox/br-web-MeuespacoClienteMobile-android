package br.com.libertyseguros.mobile.beans;



public class UpdateRequiredResponse {

    private boolean updateRequired;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getUpdatedRequired() {
        return updateRequired;
    }

    public void setUpdateRequired(boolean updateRequired) {
        this.updateRequired = updateRequired;
    }
}
