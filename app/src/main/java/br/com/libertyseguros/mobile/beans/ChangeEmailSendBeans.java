package br.com.libertyseguros.mobile.beans;



public class ChangeEmailSendBeans {


    public ChangeEmailSendBeans(){
        Respostas = new Answer();
    }

    private String CpfCnpj;

    private String Email;

    private Answer Respostas;

    private int brandMarketing;

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public Answer getRespostas() {
        return Respostas;
    }

    public void setRespostas(Answer respostas) {
        Respostas = respostas;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getBrandMarketing() {
        return brandMarketing;
    }

    public void setBrandMarketing(int brandMarketing) {
        this.brandMarketing = brandMarketing;
    }

    public class Answer{
        private  QuestionSendBeans.ListAnswers listaDePerguntas[];

        public QuestionSendBeans.ListAnswers[] getListaDePerguntas() {
            return listaDePerguntas;
        }

        public void setListaDePerguntas(QuestionSendBeans.ListAnswers[] listaDePerguntas) {
            this.listaDePerguntas = listaDePerguntas;
        }
    }
}
