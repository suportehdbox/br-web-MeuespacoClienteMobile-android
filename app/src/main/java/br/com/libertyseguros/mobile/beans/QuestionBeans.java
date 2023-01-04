package br.com.libertyseguros.mobile.beans;


import java.util.ArrayList;

public class QuestionBeans {

    private Question perguntas;

    public Question getQuestion(){
        return perguntas;
    }

    public class Question{

        private QuestionArray listaDePerguntas[];

        public class QuestionArray{
            private int idPergunta;

            private String descricao;

            public String getDescricao() {
                return descricao;
            }

            public void setDescricao(String descricao) {
                this.descricao = descricao;
            }

            private ArrayList<String> resposta;

            private boolean respondida;

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

            public boolean isRespondida() {
                return respondida;
            }

            public void setRespondida(boolean respondida) {
                this.respondida = respondida;
            }


        }


        public QuestionArray[] getQuestionArray(){
            return listaDePerguntas;
        }
    }


}
