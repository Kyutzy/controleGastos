package organizacaoGastos.mainId;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import organizacaoGastos.objects.Despesa;
import organizacaoGastos.objects.GastoGeral;
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
	@FXML ProgressBar progresso;
	
	boolean parcelado = true;
	
	/*
	 * Componentes da Tab de visualização
	 */
	
	@FXML TableView<GastoVisualizacao> visualizacaoPorCategoria;
	@FXML TableColumn<GastoVisualizacao, String> visualizacaoPorCategoriaCategoria;
	@FXML TableColumn<GastoVisualizacao, Float> visualizacaoPorCategoriaValor;
	
	@FXML TableView<GastoGeral> visualizacaoGeral;
	@FXML TableColumn<GastoGeral, String> visualizacaoNome;
	@FXML TableColumn<GastoGeral, Float> visualizacaoValor;
	@FXML TableColumn<GastoGeral, String> visualizacaoData;
	@FXML TableColumn<GastoGeral, String> visualizacaoCategoria;
	
	@FXML TextField total;
	
	
	
	//@FXML TableColumn<>
	
	
	
	public void initialize() {
		db = new DatabaseConnection("mongodb://localhost:27017");
		total.setText(String.format("Total: %.2f", db.getValorGastos()));
		inicializarTableCategorias();
		inicializarTableTodosGastos();
		dataGasto.setValue(LocalDate.now());
		ArrayList<String> categorias = db.getAllCategorias();
		setComboCategorias(categorias);
		Platform.runLater(() -> {
	        Scene scene = lancarGasto.getScene();
	        if (scene != null) {
	            // Configurações na Scene
	            scene.setOnKeyPressed(event -> {
	                if (event.getCode() == KeyCode.ENTER) {
	                	lancarGasto.fire();
	                }
	            });
	        }
	    });
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
		if (spinnerParcelas.getValue() == 1) {
			Despesa novaDespesa = this.criarDespesa(false, LocalDate.now().getMonth().toString(), LocalDate.now().getYear());
			this.db.insertOneDespesa(novaDespesa);
			inicializarTableCategorias();
			nomeGasto.setText("");
			valorGasto.setText("");
			
			dataGasto.setValue(LocalDate.now());
			finalizarLancamento();
		} else {
			for(int i=0; i<spinnerParcelas.getValue(); i++) {
			int mesAtual = LocalDate.now().getMonth().getValue();
			int anoAtual = LocalDate.now().getYear();
			int mesSomado = mesAtual + i;
			if (mesSomado > 12) {
				anoAtual++;
			}
			Despesa novaParcelada = this.criarDespesa(false, LocalDate.now().getMonth().plus(i).toString(), anoAtual);
			this.db.insertOneDespesa(novaParcelada);
			}
			finalizarLancamento();
		}

	}
	
	private void finalizarLancamento() {
		nomeGasto.setText("");
		valorGasto.setText("");
		dataGasto.setValue(LocalDate.now());
		progresso.setProgress(100);
		spinnerParcelas.getValueFactory().setValue(1);
		PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
		pause.setOnFinished(e -> {
			progresso.setProgress(-1);
		});
		pause.play();
		nomeGasto.requestFocus();
		inicializarTableCategorias();
		inicializarTableTodosGastos();
		total.setText(String.format("Total: %.2f", db.getValorGastos()));
	}
	
	@FXML
	public void criarDespesaRecorrente() {
		Despesa novaDespesa = this.criarDespesa(true, LocalDate.now().getMonth().toString(), LocalDate.now().getYear());
		this.db.insertOneRecorrente(novaDespesa);
		inicializarTableCategorias();
		inicializarTableTodosGastos();
		finalizarLancamento();
		
	}
	
	public Despesa criarDespesa(boolean recorrencia, String mes, int ano) {
		String nomeDespesa = nomeGasto.getText();
		float valorDespesa = Float.parseFloat(valorGasto.getText().replace(',', '.'));
		String dataDespesa = dataGasto.getValue().toString();
		String categoria = categoriaGasto.getValue();
	
		
		return new Despesa(nomeDespesa, valorDespesa, parcelado, categoria, recorrencia, dataDespesa, mes, ano);
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
	
	
	private void inicializarTableCategorias() {
		visualizacaoPorCategoriaCategoria.setCellValueFactory(
				new PropertyValueFactory<>("categoria")
				);
		visualizacaoPorCategoriaValor.setCellValueFactory(
				new PropertyValueFactory<>("valorGasto")
				);
		ObservableList<GastoVisualizacao> visualizacao = FXCollections.observableArrayList(db.getGastosByCategoria());
		visualizacaoPorCategoria.getItems().setAll(visualizacao);
	}
	
	private void inicializarTableTodosGastos() {
		visualizacaoValor.setCellValueFactory(
				new PropertyValueFactory<>("valor")
				);
		visualizacaoNome.setCellValueFactory(
				new PropertyValueFactory<>("nome")
				);
		visualizacaoData.setCellValueFactory(
				new PropertyValueFactory<>("data")
				);
		visualizacaoCategoria.setCellValueFactory(
				new PropertyValueFactory<>("categoria")
				);
		
		ObservableList<GastoGeral> visualizacao = FXCollections.observableArrayList(db.getAllGastos());
		visualizacaoGeral.getItems().setAll(visualizacao);
		
	}
}
