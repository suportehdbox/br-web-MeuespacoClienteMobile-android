package br.com.libertyseguros.mobile.model;

import java.util.ArrayList;
import java.util.List;

public class DadosLoginSegurado {
	
	private static DadosLoginSegurado instance = null;
	
	private boolean logado;
	private String cpf;
	private String placa;
	private String tokenAutenticacao;
	private String tokenNotificacao;
	private int appVersion;

	private List<Object> apolices;
	private List<Object> apolicesAnteriores;
	
	/**
	 * Método construtor privado.
	 */
	private DadosLoginSegurado() {
		super();
	}

	/**
	 * @return DadosLoginSegurado - unica instancia da classe
	 */
    public static DadosLoginSegurado getInstance()
    {
        if (instance == null)
        {
            instance = new DadosLoginSegurado();
        }
        return instance;
    }

	public int getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(int appVersion) {
		this.appVersion = appVersion;
	}

	public boolean getLogado()
	{
		return logado;
	}

	public void setLogado(boolean logado)
	{
		this.logado = logado;
	}

	public String getCpf()
	{
		return cpf;
	}

	public void setCpf(String cpf)
	{
		this.cpf = cpf;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public List<Object> getApolices() {
		return apolices;
	}

	public void setApolices(List<Object> apolices) {
		this.apolices = apolices;
	}
	
	public String getTokenAutenticacao() {
		return tokenAutenticacao;
	}

	public void setTokenAutenticacao(String tokenAutenticacao) {
		this.tokenAutenticacao = tokenAutenticacao;
	}

	public String getTokenNotificacao() {
		return tokenNotificacao;
	}

	public void setTokenNotificacao(String tokenNotificacao) {
		this.tokenNotificacao = tokenNotificacao;
	}

	public List<Object> getApolicesAnteriores() {
		return apolicesAnteriores;
	}

	public void setApolicesAnteriores(List<Object> apolicesAnteriores) {
		this.apolicesAnteriores = apolicesAnteriores;
	}

	public void reset() {
		instance.setApolices(new ArrayList<Object>());
		instance.setApolicesAnteriores(new ArrayList<Object>());
		instance.setCpf("");
		instance.setLogado(false);
		instance.setPlaca("");
		instance.setTokenAutenticacao("");
		instance.setTokenNotificacao("");
	}
}
