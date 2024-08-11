package organizacaoGastos.objects;

import org.bson.codecs.pojo.annotations.BsonCreator;

public class Despesa {
	
	String nome;
	float valor;
	boolean parcelado;
	String categoria;
	boolean recorrente;
	String data;
	String mes;
	int ano;
	
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public boolean isParcelado() {
		return parcelado;
	}
	public void setParcelado(boolean parcelado) {
		this.parcelado = parcelado;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public boolean isRecorrente() {
		return recorrente;
	}
	public void setRecorrente(boolean recorrente) {
		this.recorrente = recorrente;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public Despesa(String nome, float valor, boolean parcelado, String categoria, boolean recorrente, String data,
			String mes, int ano) {
		super();
		this.nome = nome;
		this.valor = valor;
		this.parcelado = parcelado;
		this.categoria = categoria;
		this.recorrente = recorrente;
		this.data = data;
		this.mes = mes;
		this.ano = ano;
	}

	


	
	
}
