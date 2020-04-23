package br.com.libertyseguros.mobile.beans;

/**
 * Created by rodrigomacedo on 12/09/18.
 */

public class SecondCopyPolicy {

    private String pdf;
    private boolean isMigrado;
    private String produto;
    private String produtoLiberty;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public boolean isMigrado() {
        return isMigrado;
    }

    public void setMigrado(boolean migrado) {
        isMigrado = migrado;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getProdutoLiberty() {
        return produtoLiberty;
    }

    public void setProdutoLiberty(String produtoLiberty) {
        this.produtoLiberty = produtoLiberty;
    }
}
