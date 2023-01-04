package br.com.libertyseguros.mobile.beans;

public class Salesman {
    private SalesmanBeans brokers[];

    public SalesmanBeans[] getSalesmen(){
        return brokers;
    }

    public void setSalesmen(SalesmanBeans salesmen[]) {
        this.brokers = brokers;
    }
}
