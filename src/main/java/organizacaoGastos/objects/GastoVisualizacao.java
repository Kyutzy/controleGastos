package organizacaoGastos.objects;

public class GastoVisualizacao {
	String categoria;
	Float valorGasto;
	
	
	public String getCategoria() {
		return categoria;
	}


	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}


	public Float getValorGasto() {
		return valorGasto;
	}


	public void setValorGasto(Float valorGasto) {
		this.valorGasto = valorGasto;
	}


	public GastoVisualizacao(String categoria, Float valorGasto) {
		super();
		this.categoria = categoria;
		this.valorGasto = valorGasto;
	}
	
	

}
