package organizacaoGastos.mainId;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import organizacaoGastos.services.DatabaseConnection;

public class SecondaryController {
	
	DatabaseConnection db;
	@FXML TextField nomeCategoria;
	@FXML Button addCategoria;
	
	public void initialize() {
		db = new DatabaseConnection("mongodb://localhost:27017");
		System.out.println('d');
	}
	
	public void addNewCategoria() throws IOException {
		String nomeCat = nomeCategoria.getText();
		db.insertOneCategoria(nomeCat);
		App.setRoot("primary");
	}
}