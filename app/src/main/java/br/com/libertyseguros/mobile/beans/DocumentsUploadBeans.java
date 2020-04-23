package br.com.libertyseguros.mobile.beans;


import java.util.ArrayList;

public class DocumentsUploadBeans {

    private String CpfCnpj;

    private String NumeroApolice;

    private Files Arquivos[];

    public DocumentsUploadBeans(int ind){
        Arquivos = new Files[ind];
    }

    public DocumentsUploadBeans.Files getInstaceFile(){
        DocumentsUploadBeans.Files arquivos = new DocumentsUploadBeans.Files();
        return arquivos;
    }

    public String getCpfCnpj() {
        return CpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        CpfCnpj = cpfCnpj;
    }

    public String getNumeroApolice() {
        return NumeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        NumeroApolice = numeroApolice;
    }

    public Files[] getArquivos() {
        return Arquivos;
    }

    public void setArquivos(Files[] arquivos) {
        Arquivos = arquivos;
    }

    public class Files{
        private String Nome;

        private String Conteudo;

        private String Extensao;

        public String getNome() {
            return Nome;
        }

        public void setNome(String nome) {
            this.Nome = nome;
        }

        public String getConteudo() {
            return Conteudo;
        }

        public void setConteudo(String conteudo) {
            this.Conteudo = conteudo;
        }

        public String getExtensao() {
            return Extensao;
        }

        public void setExtensao(String extensao) {
            this.Extensao = extensao;
        }
    }



}
