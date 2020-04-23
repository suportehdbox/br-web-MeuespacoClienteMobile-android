package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.beans.PaymentPriceBeans;

public interface OnPaymentPriceListener
{
    void OnPaymentValue(PaymentPriceBeans price);
    void OnPaymentPriceError(String message);
}
