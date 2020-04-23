package br.com.libertyseguros.mobile.beans;

public class HomePaymentsBeans {

    private PaymentBeans[] payments;


    public PaymentBeans[] getPayment() {
        return payments;
    }

    public void setPayment(PaymentBeans[] payment) {
        this.payments = payment;
    }
}
