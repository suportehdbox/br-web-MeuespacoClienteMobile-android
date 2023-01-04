package br.com.libertyseguros.mobile.beans;

import java.util.ArrayList;

public class ListVision360Beans {

    private ArrayList<Vision360Beans> eventos;

    private ArrayList<AssitanceVision360Beans> assistencias;

    private String message;

    private String totalPremio;

    private boolean success;

    private boolean possuiEvento;

    public String totalDesconto;

    public ArrayList<AssitanceVision360Beans> getAssistencia() {
        return assistencias;
    }

    public void setAssistencia(ArrayList<AssitanceVision360Beans> assistencia) {
        this.assistencias = assistencia;
    }

    public ArrayList<Vision360Beans> getEventos() {
        return eventos;
    }

    public void setEventos(ArrayList<Vision360Beans> eventos) {
        this.eventos = eventos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalPremio() {
        return totalPremio;
    }

    public void setTotalPremio(String totalPremio) {
        this.totalPremio = totalPremio;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTotalDesconto() {
        return totalDesconto;
    }

    public void setTotalDesconto(String totalDesconto) {
        this.totalDesconto = totalDesconto;
    }

    public boolean isPossuiEvento() {
        return possuiEvento;
    }

    public void setPossuiEvento(boolean possuiEvento) {
        this.possuiEvento = possuiEvento;
    }
}
