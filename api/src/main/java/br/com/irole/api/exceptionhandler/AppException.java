package br.com.irole.api.exceptionhandler;

public  class AppException{
	
	private String mensagemUsuario;
	private String mensagemDesenvolvedor;
	
	public AppException(String mensagemUsuario, String mensagemDesenvolvedor) {
		this.mensagemUsuario = mensagemUsuario;
		this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}
	public String getMensagemUsuario() {
		return mensagemUsuario;
	}
	public String getMensagemDesenvolvedor() {
		return mensagemDesenvolvedor;
	}
}