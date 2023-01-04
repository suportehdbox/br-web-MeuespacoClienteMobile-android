package br.com.libertyseguros.mobile.beans;


import com.google.gson.annotations.Expose;

public class HomeBeansV2 {
    private PolicyBeansV2.InsurancesV2 insurance;

    @Expose(serialize = false, deserialize = false)
    private PaymentBeans[] payments;

    public PolicyBeansV2.InsurancesV2 getInsurance() {
        return insurance;
    }

    public void setInsurances(PolicyBeansV2.InsurancesV2 insurance) {
        this.insurance = insurance;
    }


    public PaymentBeans[] getPayment() {
        return payments;
    }

    public void setPayment(PaymentBeans[] payment) {
        this.payments = payment;
    }
}
