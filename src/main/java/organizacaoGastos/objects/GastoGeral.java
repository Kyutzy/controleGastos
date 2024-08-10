package organizacaoGastos.objects;

public class GastoGeral {
	
	String nome;
	float valor;
	String data;
	String categoria;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public GastoGeral(String nome, float valor, String data, String categoria) {
		super();
		this.nome = nome;
		this.valor = valor;
		this.data = data;
		this.categoria = categoria;
	}
	
	
	
	
}
