module organizacaoGastos.mainId {
	exports organizacaoGastos.mainId;
	opens organizacaoGastos.mainId to javafx.fxml;
	exports organizacaoGastos.objects;
	exports organizacaoGastos.services;

	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.mongodb.bson;
	requires org.mongodb.driver.core;
	requires org.mongodb.driver.sync.client;
}
