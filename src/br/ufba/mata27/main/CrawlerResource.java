package br.ufba.mata27.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.types.BasicBSONList;
import org.repositoryminer.codemetric.direct.EC;
import org.repositoryminer.codemetric.indirect.AC;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Repository;
import org.repositoryminer.parser.java.JavaParser;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.persistence.handler.DirectCodeAnalysisDocumentHandler;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMType;

import com.mongodb.BasicDBObject;

@Path("/crawler")
public class CrawlerResource {
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response crawl(String path) throws IOException{
		Connection connection = Connection.getInstance(); 		 
		connection.connect("mongodb://localhost:27017", "mina");
		RepositoryMiner miner = new RepositoryMiner("Mina");
		final ArrayList<Repository> repo = new ArrayList<>();
		miner.addParser(new JavaParser());
		miner.setPath(path);
		miner.setScm(SCMType.GIT);
		miner.addReference("master", ReferenceType.BRANCH);
		miner.setDirectCodeMetrics(Arrays.asList(new EC()));
		miner.setIndirectCodeMetrics(Arrays.asList(new AC()));
		miner.mine();
//		return Response.ok().build();
		return Response.ok().build();
	}
	
	@GET
	@Path("/ec")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllEC(){
		Connection connection = Connection.getInstance(); 		 
		connection.connect("mongodb://localhost:27017", "mina");
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
