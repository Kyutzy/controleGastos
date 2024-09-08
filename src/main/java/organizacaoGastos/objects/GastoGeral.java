package organizacaoGastos.objects;

public class GastoGeral {
	
	String nome;
	float valor;
	String data;
	String categoria;
	String parcelas;
	int totalParcelas;
	int parcelaAtual;
	
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

	public String getParcelas() {
		return parcelas;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	public int getTotalParcelas() {
		return totalParcelas;
	}

	public void setTotalParcelas(int totalParcelas) {
		this.totalParcelas = totalParcelas;
	}

	public int getParcelaAtual() {
		return parcelaAtual;
	}

	public void setParcelaAtual(int parcelaAtual) {
		this.parcelaAtual = parcelaAtual;
	}

	public GastoGeral(String nome, float valor, String data, String categoria, int parcelaAtual,
			int totalParcelas) {
		super();
		this.nome = nome;
		this.valor = valor;
		this.data = data;
		this.categoria = categoria;
		this.parcelas = String.format("%d/%d", parcelaAtual, totalParcelas);
	}
	
	
	
}
