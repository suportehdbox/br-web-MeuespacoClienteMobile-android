/**
 * 
 */
package br.com.libertyseguros.mobile.common;

/**
 * @author Evandro
 *
 */
public class LibertyException extends Exception {

	private static final long serialVersionUID = 7366348917805311402L;
	
	private LibertyExceptionEnum libertyExceptionEnum;
	

	/**
	 * Construtor
	 * @param throwable
	 * @param libertyExceptionEnum
	 */
	public LibertyException(Throwable throwable, LibertyExceptionEnum libertyExceptionEnum, String detailMessage) {
		super(detailMessage, throwable);
		this.libertyExceptionEnum = libertyExceptionEnum;
	}
	
	/**
	 * Construtor
	 * @param throwable
	 * @param libertyExceptionEnum
	 */
	public LibertyException(Throwable throwable, LibertyExceptionEnum libertyExceptionEnum) {
		super(throwable);
		this.libertyExceptionEnum = libertyExceptionEnum;
	}

	/**
	 * @param detailMessage
	 */
	public LibertyException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public LibertyException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public LibertyException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * @return Tipo liberty do erro
	 */
	public LibertyExceptionEnum getLibertyExceptionEnum() {
		return libertyExceptionEnum;
	}

	/**
	 * seta o tipo do liberty erro @param libertyExceptionEnum
	 */
	public void setLibertyExceptionEnum(LibertyExceptionEnum libertyExceptionEnum) {
		this.libertyExceptionEnum = libertyExceptionEnum;
	}
}
