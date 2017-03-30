package crawler.main;

import java.io.IOException;
import java.util.Arrays;

import org.repositoryminer.codemetric.direct.EC;
import org.repositoryminer.codemetric.indirect.AC;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.parser.java.JavaParser;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMType;

public class Main {
	
	
	public static void main(String[] args) throws IOException {
		
		Connection con = Connection.getInstance();
		con.connect("mongodb://localhost:27017", "mina");
		
		
		RepositoryMiner miner = new RepositoryMiner("Mina");
		miner.addParser(new JavaParser());
		miner.setPath("/home/paulo/Documents/development/java/eclipse/Mina");
		miner.setScm(SCMType.GIT);
		miner.addReference("master", ReferenceType.BRANCH);

//		miner.setDirectCodeMetrics(Arrays.asList(new EC()));
		miner.setIndirectCodeMetrics(Arrays.asList(new AC()));
		
		miner.mine();
			
	}

}
