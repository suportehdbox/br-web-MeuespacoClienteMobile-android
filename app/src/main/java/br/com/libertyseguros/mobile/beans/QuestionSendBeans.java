package br.com.libertyseguros.mobile.beans;


import java.util.ArrayList;

public class QuestionSendBeans {

    private ListAnswers listaDePerguntas[];

    public ListAnswers[] createInstance(int size){

        listaDePerguntas= new ListAnswers[size];
        return listaDePerguntas;
    }

    public ListAnswers getAnswers(){
        return new ListAnswers();
    }

    public ListAnswers[] getListaDePerguntas() {
        return listaDePerguntas;
    }

    public void setListaDePerguntas(ListAnswers[] listaDePerguntas) {
        this.listaDePerguntas = listaDePerguntas;
    }

    public class ListAnswers{
        private int idPergunta = 0;

        private ArrayList<String> resposta = new ArrayList<>();

        public int getIdPergunta() {
            return idPergunta;
        }

        public void setIdPergunta(int idPergunta) {
            this.idPergunta = idPergunta;
        }

        public ArrayList<String> getResposta() {
            return resposta;
        }

        public void setResposta(ArrayList<String> resposta) {
            this.resposta = resposta;
        }

        public void addResposta(String res){
            resposta.add(res);
        }
    }
}
