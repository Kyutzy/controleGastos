package organizacaoGastos.services;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import organizacaoGastos.objects.Despesa;
import organizacaoGastos.objects.GastoGeral;
import organizacaoGastos.objects.GastoVisualizacao;
import organizacaoGastos.models.DBFunctions;

public class DatabaseConnection {
	
	MongoClient connection;
	MongoDatabase database;
	MongoCollection<Document> collection;
	
	public DatabaseConnection(String uri) {
		this.connection = MongoClients.create(uri);
		this.database = this.connection.getDatabase("GastosCesar");
		this.database.createCollection("Gastos");
		System.out.println("Oiii!!");
	}
	
	public void closeConnection() {
		this.connection.close();
	}
	
	public void insertOneDespesa(Despesa despesa) {	
		this.collection = this.database.getCollection("Gastos");
		Document documento = criaMongoDocumento(despesa);
		this.collection.insertOne(documento);
		
	}
	public void insertOneRecorrente(Despesa despesa) {
		this.collection = this.database.getCollection("Recorrente");
		Document documento = criaMongoDocumento(despesa);
		this.collection.insertOne(documento);
	}
	
	public Document criaMongoDocumento(Despesa despesa) {
		return new Document()
				.append("_id", new ObjectId())
				.append("Nome", despesa.getNome())
				.append("Valor", despesa.getValor())
				.append("Categoria", despesa.getCategoria())
				.append("Parcelado", despesa.isParcelado())
				.append("Recorrente", despesa.isRecorrente())
				.append("Data", despesa.getData())
				.append("Mes", despesa.getMes())
				.append("Ano", despesa.getAno())
				.append("ParcelaAtual", despesa.getParcelaAtual())
				.append("TotalParcelas", despesa.getQtdParcelas());
		
	}
	
	public void insertOneCategoria(String categoria) {
		this.collection = this.database.getCollection("Categorias");
		this.collection.insertOne(new Document()
				.append("_id", new ObjectId())
				.append("categoria", categoria));
	}
	
	public ArrayList<String> getAllCategorias() {
		
		ArrayList<String> finalCombo = new ArrayList<>();
		
		Bson projection = Projections.fields(
				Projections.include("categoria"),
				Projections.excludeId());
		 
		this.collection = this.database.getCollection("Categorias");
		MongoCursor<Document> findResults = this.collection.find().projection(projection).iterator();
		
		findResults.forEachRemaining(x -> {
			finalCombo.add((String)x.get("categoria"));
		});
		
		return finalCombo;
	}
	
	public ArrayList<GastoVisualizacao> getGastosByCategoria(){
		MongoCollection<Document> collection;
		ArrayList<GastoVisualizacao> listFinal = new ArrayList<>();
		collection = this.database.getCollection("Gastos");
		HashMap<String, Float> gastosGerais = new DBFunctions().aggregatePorCategoriaEMes(collection);
		collection = this.database.getCollection("Recorrente");
		HashMap<String, Float> gastosRecorrentes = new DBFunctions().aggregatePorCategoria(collection);
		
		for(Map.Entry<String, Float> entry: gastosGerais.entrySet()){
			if (gastosRecorrentes.containsKey(entry.getKey())){
				listFinal.add(new GastoVisualizacao(
						entry.getKey(), entry.getValue() + gastosRecorrentes.get(entry.getKey())
						));
			} else {
				listFinal.add(new GastoVisualizacao(
						entry.getKey(), entry.getValue()
						));
			}
			
		}
		
		return listFinal;
	}
	
	public ArrayList<GastoVisualizacao> getGastosByCategoria(Month mes, int ano){
		MongoCollection<Document> collection;
		ArrayList<GastoVisualizacao> listFinal = new ArrayList<>();
		collection = this.database.getCollection("Gastos");
		HashMap<String, Float> gastosGerais = new DBFunctions().aggregatePorCategoriaEMes(collection, mes, ano);
		collection = this.database.getCollection("Recorrente");
		HashMap<String, Float> gastosRecorrentes = new DBFunctions().aggregatePorCategoria(collection);
		
		for(Map.Entry<String, Float> entry: gastosGerais.entrySet()){
			if (gastosRecorrentes.containsKey(entry.getKey())){
				listFinal.add(new GastoVisualizacao(
						entry.getKey(), entry.getValue() + gastosRecorrentes.get(entry.getKey())
						));
			} else {
				listFinal.add(new GastoVisualizacao(
						entry.getKey(), entry.getValue()
						));
			}
			
		}
		
		return listFinal;
	}
	
	public ArrayList<GastoGeral> getAllGastos(Month month, int ano){
		collection = this.database.getCollection("Gastos");
		ArrayList<GastoGeral> todosGastos = new DBFunctions().getDocumentsByMonth(collection, month, ano);
		collection = this.database.getCollection("Recorrente");
		ArrayList<GastoGeral> recorrentes = new DBFunctions().getDocumentsByMonth(collection, month, ano);
		todosGastos.addAll(recorrentes);
		
		return todosGastos;
	}
	
	public float getValorGastos() {
		collection = this.database.getCollection("Gastos");
		float allGastosNoRecorrence = new DBFunctions().getSumOfValuesByMonth(collection);
		collection = this.database.getCollection("Recorrente");
		new DBFunctions().getSumOfValuesByMonth(collection);
		float allGastosForReal = allGastosNoRecorrence + new DBFunctions().getSumOfValuesByMonth(collection);
		return allGastosForReal;
	}
	
	
}
