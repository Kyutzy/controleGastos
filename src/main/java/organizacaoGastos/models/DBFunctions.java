package organizacaoGastos.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import organizacaoGastos.objects.GastoGeral;

public class DBFunctions {
	
	public HashMap<String, Float> aggregatePorCategoriaEMes(MongoCollection<Document> collection) {
	    HashMap<String, Float> gastos = new HashMap<>();
	    
	    // Obtém o mês e o ano atuais
	    String mesAtual = LocalDate.now().getMonth().toString();
	    int anoAtual = LocalDate.now().getYear();
	    
	    // Cria a pipeline de agregação com o filtro de mês e ano
	    AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
	            // Etapa de match para filtrar pelo mês e ano
	            new Document("$match", new Document("Mes", mesAtual)
	                    .append("Ano", anoAtual)),
	            // Etapa de group para agrupar por categoria e somar os valores
	            new Document("$group", new Document("_id", "$Categoria")
	                    .append("valor", new Document("$sum", "$Valor"))),
	            // Etapa de project para formatar a saída
	            new Document("$project", new Document("_id", 0)
	                    .append("categoria", "$_id")
	                    .append("valor", 1))
	    ));

	    // Itera sobre o resultado e popula o HashMap
	    for (Document doc : result) {
	        gastos.put(
	                doc.getString("categoria"), Float.parseFloat(doc.get("valor").toString())
	        );
	    }
	    return gastos;
	}
	
	public HashMap<String, Float> aggregatePorCategoria(MongoCollection<Document> collection) {
	    HashMap<String, Float> gastos = new HashMap<>();
	    
	    // Obtém o mês e o ano atuais
	    String mesAtual = LocalDate.now().getMonth().toString();
	    int anoAtual = LocalDate.now().getYear();
	    
	    // Cria a pipeline de agregação com o filtro de mês e ano
	    AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
	            // Etapa de group para agrupar por categoria e somar os valores
	            new Document("$group", new Document("_id", "$Categoria")
	                    .append("valor", new Document("$sum", "$Valor"))),
	            // Etapa de project para formatar a saída
	            new Document("$project", new Document("_id", 0)
	                    .append("categoria", "$_id")
	                    .append("valor", 1))
	    ));

	    // Itera sobre o resultado e popula o HashMap
	    for (Document doc : result) {
	        gastos.put(
	                doc.getString("categoria"), Float.parseFloat(doc.get("valor").toString())
	        );
	    }
	    return gastos;
	}
	
    public ArrayList<GastoGeral> getDocumentsByMonth(MongoCollection<Document> collection) {
        // Lista para armazenar os resultados
        ArrayList<GastoGeral> gastos = new ArrayList<>();

        // Cria um filtro para o mês especificado
        Document filtro = new Document("Mes", LocalDate.now().getMonth().toString());
        
        // Define a projeção para incluir apenas os campos desejados
        Document projecao = new Document("Nome", 1)
                                .append("Valor", 1)
                                .append("Data", 1)
                                .append("Categoria", 1)
                                .append("_id", 0); // Exclui o campo _id
        
        // Usar find() com o filtro e a projeção
        MongoCursor<Document> cursor = collection.find(filtro).projection(projecao).iterator();

        try {
            // Itera sobre os documentos e cria instâncias de GastoGeral
            while (cursor.hasNext()) {
                Document x = cursor.next();
                System.out.println("Documento encontrado: " + x.toJson());
                GastoGeral gasto = new GastoGeral(
                    x.getString("Nome"),
                    x.getDouble("Valor").floatValue(), // Alteração para Double
                    x.getString("Data"),
                    x.getString("Categoria")
                );
                gastos.add(gasto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close(); // Fechar o cursor após a iteração
        }

        return gastos; // Retorna a lista de GastoGeral
    }
    
    public float getSumOfValuesByMonth(MongoCollection<Document> collection) {
        // Cria a pipeline de agregação
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                // Etapa de match para filtrar pelo mês
                new Document("$match", new Document("Mes", LocalDate.now().getMonth().toString())),
                // Etapa de group para somar os valores
                new Document("$group", new Document("_id", null)
                        .append("total", new Document("$sum", "$Valor")))
        ));

        // Obtém o resultado e retorna a soma
        Document doc = result.first();
        if (doc != null && doc.containsKey("total")) {
            return doc.getDouble("total").floatValue();
        } else {
            return 0.0f; // Retorna 0 se não houver resultados
        }
    }

}
