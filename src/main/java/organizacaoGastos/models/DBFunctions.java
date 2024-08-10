package organizacaoGastos.models;

import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;

import organizacaoGastos.objects.GastoVisualizacao;

public class DBFunctions {
	
	public ArrayList<GastoVisualizacao> aggregatePorCategoria(MongoCollection<Document> collection){
		ArrayList<GastoVisualizacao> gastos = new ArrayList<>();
			
			AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
		            new Document("$group", new Document("_id", "$Categoria")
		                    .append("valor", new Document("$sum", "$Valor"))),
		            new Document("$project", new Document("_id", 0)
		                    .append("categoria", "$_id")
		                    .append("valor", 1))
		        ));
	
		        // Itera sobre o resultado e exibe os documentos
		        for (Document doc : result) {
		            gastos.add(
		            		new GastoVisualizacao(doc.getString("categoria"),
		            				Float.parseFloat(doc.get("valor").toString())
		            		));
		        }
		        return gastos;
			
		}
}
