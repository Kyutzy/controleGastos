package organizacaoGastos.mainId;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import organizacaoGastos.objects.Despesa;
import organizacaoGastos.objects.GastoVisualizacao;
import organizacaoGastos.services.DatabaseConnection;


import java.text.DateFormatSymbols;

public class PrimaryController {
	/*
	 * Conteúdos do controller do lançar gastos
	 */
	@FXML
	Spinner<Integer> spinnerParcelas;
	@FXML TextField valorGasto;
	@FXML TextField nomeGasto;
	@FXML DatePicker dataGasto;
	@FXML RadioButton radioParcelaSim;
	@FXML RadioButton radioParcelaNao;
	@FXML ComboBox<String> categoriaGasto;
	@FXML Button novaCategoria;
	@FXML Button lancarGasto;
	DatabaseConnection db;
	
	boolean parcelado = true;
	
	/*
	 * Componentes da Tab de visualização
	 */
	
	@FXML TableView<GastoVisualizacao> visualizacaoPorCategoria;
	@FXML TableColumn<GastoVisualizacao, String> visualizacaoPorCategoriaCategoria;
	@FXML TableColumn<GastoVisualizacao, Float> visualizacaoPorCategoriaValor;
	
	
	public void initialize() {
		db = new DatabaseConnection("Insert your own");
		inicializarTables();
		dataGasto.setValue(LocalDate.now());
		
		System.out.println('d');
		ArrayList<String> categorias = db.getAllCategorias();
		setComboCategorias(categorias);
	}
	
	private void inicializarTables() {
		visualizacaoPorCategoriaCategoria.setCellValueFactory(
				new PropertyValueFactory<>("categoria")
				);
		visualizacaoPorCategoriaValor.setCellValueFactory(
				new PropertyValueFactory<>("valorGasto")
				);
		ObservableList<GastoVisualizacao> visualizacao = FXCollections.observableArrayList(db.getGastosByCategoria());
		visualizacaoPorCategoria.getItems().setAll(visualizacao);
		
	}
	
	@FXML
	public void formatValor() {
	}
	
	public void setComboCategorias(ArrayList<String> categorias) {
		categoriaGasto.getItems().clear();
		categoriaGasto.getItems().addAll(categorias);
	}
	
	@FXML
	public void criarDepesaUmaVez() {
		Despesa novaDespesa = this.criarDespesa(false);
		this.db.insertOneDespesa(novaDespesa);
	}
	
	@FXML
	public void criarDespesaRecorrente() {
		Despesa novaDespesa = this.criarDespesa(true);
		this.db.insertOneRecorrente(novaDespesa);
	}
	
	public Despesa criarDespesa(boolean recorrencia) {
		String nomeDespesa = nomeGasto.getText();
		float valorDespesa = Float.parseFloat(valorGasto.getText());
		String dataDespesa = dataGasto.getValue().toString();
		String categoria = categoriaGasto.getValue();
		
		nomeGasto.setText("");
		valorGasto.setText("");
		dataGasto.setValue(LocalDate.now());
		
		return new Despesa(nomeDespesa, valorDespesa, parcelado, categoria, recorrencia, dataDespesa);
	}
	
	@FXML
	public void handleParcelado() {
		parcelado = parcelado ? false : true;
		spinnerParcelas.setDisable(parcelado);
		System.out.println(parcelado);
	}
	
	@FXML
	public void handleNovaCategoria() throws IOException {
		db.closeConnection();
		App.setRoot("secondary");
	}
	
	/*
	 * A partir daqui é o manejo da segunda aba, chamada verificar
	 * */
	
	
	//@FXML TableView<?> visualizacaoPorCategoria;
	//@FXML TableColumn<?, ?> visualizacaoPorCategoriaCategoria;
	//@FXML TableColumn<?, ?> visualizacaoPorCategoriaValor;
}
