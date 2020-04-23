package br.com.libertyseguros.mobile.beans;



public class UploadFileBeans {

    private String NumeroSinistro;

    private String IdDocumentos[];

    private Files Arquivos[];


    public String[] getIdDocumentos() {
        return IdDocumentos;
    }

    public void setIdDocumentos(String[] idDocumentos) {
        IdDocumentos = idDocumentos;
    }

    public UploadFileBeans(int ind){
        Arquivos = new Files[ind];
    }

    public Files getInstaceFile(){
        Files arquivos = new Files();
        return arquivos;
    }

    public Files[] getArquivos() {
        return Arquivos;
    }

    public void setArquivos(Files arquivos[]) {
        Arquivos = arquivos;
    }

    public String getNumeroSinistro() {
        return NumeroSinistro;
    }

    public void setNumeroSinistro(String numeroSinistro) {
        this.NumeroSinistro = numeroSinistro;
    }

    public class Files{
        private String TipoDocumento;
        private String Nome;
        private String Conteudo;

        public String getTipoDocumento() {
            return TipoDocumento;
        }

        public void setTipoDocumento(String tipoDocumento) {
            TipoDocumento = tipoDocumento;
        }

        public String getNome() {
            return Nome;
        }

        public void setNome(String nome) {
            Nome = nome;
        }

        public String getConteudo() {
            return Conteudo;
        }

        public void setConteudo(String conteudo) {
            Conteudo = conteudo;
        }
    }


}
