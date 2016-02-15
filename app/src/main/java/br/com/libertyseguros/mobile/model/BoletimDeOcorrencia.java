package br.com.libertyseguros.mobile.model;

import br.com.libertyseguros.mobile.common.util.ValidationUtils;

/**
 * Representa o Boletim de Ocorrencia na base de dados. 
 * @author Evandro
 */
public class BoletimDeOcorrencia {

	private Long id;
	
	private String entidade;
	
	private String localidade;
	
	private String notas;
	
	public BoletimDeOcorrencia() {
	}

	public BoletimDeOcorrencia(Long id, String entidade, String localidade, String notas) {
		super();
		this.id = id;
		this.entidade = entidade;
		this.localidade = localidade;
		this.notas = notas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	
	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	/**
     * Verifica se algum campo n�o est� preenchido. Se qualquer campo foi preenchido, retorna falso,
     * caso contr�rio retorna verdadeiro. Isso verifica apenas dados que o usuario altera (ou seja, ID n�o � verificado)
     * 
     * @return true se boletimDeOcorrencia n�o tiver dados, false caso contrario.
     */
    public boolean isEmpty()
    {
        boolean returnVal = false;

        if (ValidationUtils.isStringEmpty(this.entidade) 
        		&& ValidationUtils.isStringEmpty(this.localidade)
        		&& ValidationUtils.isStringEmpty(this.notas))
        {
            returnVal = true;
        }

        return returnVal;
    }
    
    /**
     * Compara dois boletins de ocorr�ncia para determinar se os dados s�o iguais.Somente verifica se o usu�rio alterou os dados (id n�o eh comparado)
     * 
     * @param boletimDeOcorrenciaToCompare
     *            o boletim de ocorrencia para comparar com este
     * @return true se eles s�o iguais, false caso contrario.
     */
    public boolean isEqualTo(BoletimDeOcorrencia boletimDeOcorrenciaToCompare)
    {
        boolean returnVal = true;

        if (this.isEmpty() && boletimDeOcorrenciaToCompare.isEmpty())
        {
            returnVal = true;
        }
        else if (!this.getEntidade().equals(boletimDeOcorrenciaToCompare.getEntidade()))
        {
            returnVal = false;
        }
        else if (!this.getLocalidade().equals(boletimDeOcorrenciaToCompare.getLocalidade()))
        {
            returnVal = false;
        }
        else if (!this.getNotas().equals(boletimDeOcorrenciaToCompare.getNotas()))
        {
        	returnVal = false;
        }

        return returnVal;
    }

    /**
     * This method will reset the required attributes of the object
     */
    public void reset()
    {
        setEntidade("");
        setLocalidade("");
        setNotas("");
    }

}
