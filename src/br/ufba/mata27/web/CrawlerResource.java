package br.ufba.mata27.web;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;

import com.mongodb.BasicDBObject;

@Path("/crawler")
public class CrawlerResource {
		
	@GET
	public String oi(){
		return " OI" ;
	}
	
	@GET
	@Path("/ec/{bd}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEC(@QueryParam("bd") String bd){
		Connection connection = Connection.getInstance(); 		 
		connection.connect("mongodb://localhost:27017", bd);
		DirectCodeAnalysisDocumentHandler ref = new DirectCodeAnalysisDocumentHandler();
		BasicDBObject projection = new BasicDBObject();
		projection.append("classes", "1");
		BasicDBObject where = new BasicDBObject();
		where.append("classes.0.metrics.0.metric", "EC");		
		List<String> lista = new ArrayList<>();		
		ref.findMany(where,projection).forEach( doc -> {
			List<Document> classesDoc = (List<Document>)doc.get("classes" );
			classesDoc.forEach( clazz -> {
				final Document toReturn = new Document("nome", clazz.get("name"));
				((List<Document>)clazz.get("metrics")).forEach( metric -> {
					if(metric.get("metric").equals("EC")){
						toReturn.append("classes", metric.get("classes"));
						toReturn.append("efferentCount", metric.getInteger("efferentCount" ));
						lista.add(toReturn.toJson());
					}
				});
			} );
		});
		
		return Response.ok(lista.toString()).build();
	}
	
	@GET
	@Path("/ac/{bd}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAC(@QueryParam("bd") String bd){
		Connection connection = Connection.getInstance(); 		 
		connection.connect("mongodb://localhost:27017", bd);
		DirectCodeAnalysisDocumentHandler ref = new DirectCodeAnalysisDocumentHandler();
		BasicDBObject projection = new BasicDBObject();
		projection.append("classes", "1");
		BasicDBObject where = new BasicDBObject();
		where.append("classes.0.metrics.0.metric", "AC");		
		List<String> lista = new ArrayList<>();		
		ref.findMany(where,projection).forEach( doc -> {
			List<Document> classesDoc = (List<Document>)doc.get("classes" );
			classesDoc.forEach( clazz -> {
				final Document toReturn = new Document("nome", clazz.get("name"));
				((List<Document>)clazz.get("metrics")).forEach( metric -> {
					if(metric.get("metric").equals("AC")){
						toReturn.append("classes", metric.get("classes"));
						toReturn.append("efferentCount", metric.getInteger("efferentCount" ));
						lista.add(toReturn.toJson());
					}
				});
			} );
		});
		
		return Response.ok(lista.toString()).build();
	}
	
	
//	public static void main(String[] args) throws IOException {
//		new Main();
//		new CrawlerResource().crawl("/home/paulo/Documents/development/java/eclipse/Mina");
//		DirectCodeAnalysisDocumentHandler ref = new DirectCodeAnalysisDocumentHandler();
//		BasicDBObject projection = new BasicDBObject();
//		projection.append("classes", "1");
//		BasicDBObject where = new BasicDBObject();
//		where.append("classes.0.metrics.0.metric", "EC");		
//		List<Document> lista = new ArrayList<>();		
//		ref.findMany(where,projection).forEach( doc -> {
//			List<Document> classesDoc = (List<Document>)doc.get("classes" );
//			classesDoc.forEach( clazz -> {
//				final Document toReturn = new Document("nome", clazz.get("name"));
//				((List<Document>)clazz.get("metrics")).forEach( metric -> {
//					if(metric.get("metric").equals("EC")){
//						toReturn.append("classes", metric.get("classes"));
//						toReturn.append("efferentCount", metric.getInteger("efferentCount" ));
//						lista.add(toReturn);
//					}
//				});
//			} );
//		});
//		lista.forEach( l -> System.out.println(l.toJson()));
//	
//	}
		
}
