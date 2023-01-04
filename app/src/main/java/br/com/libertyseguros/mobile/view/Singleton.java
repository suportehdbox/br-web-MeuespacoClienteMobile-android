package br.com.libertyseguros.mobile.view;

public class Singleton {

    private String email;

 private static Singleton mInstance  = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public static Singleton getInstance() {
        if (mInstance == null) {
            mInstance = new Singleton();
        }
        return mInstance;
    }


}


