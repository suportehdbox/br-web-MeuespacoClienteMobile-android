package br.com.libertyseguros.mobile.beans;

public class FacebookBeans {

    private DataBeans data;

    public DataBeans getData() {
        return data;
    }

    public void setData(DataBeans data) {
        this.data = data;
    }

    public class DataBeans{
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
