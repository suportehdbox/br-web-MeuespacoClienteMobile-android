package br.com.libertyseguros.mobile.beans;


public class BarCodeBeans {

    private String digitableLine;

    private String dueDate;

    private String summaryInstructions;

    private String completeInstructions;

    private String value;

    public String getDigitableLine() {
        return digitableLine;
    }

    public void setDigitableLine(String digitableLine) {
        this.digitableLine = digitableLine;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getSummaryInstructions() {
        return summaryInstructions;
    }

    public void setSummaryInstructions(String summaryInstructions) {
        this.summaryInstructions = summaryInstructions;
    }

    public String getCompleteInstructions() {
        return completeInstructions;
    }

    public void setCompleteInstructions(String completeInstructions) {
        this.completeInstructions = completeInstructions;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
