package br.ufba.mata27.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.repositoryminer.codemetric.direct.EC;
import org.repositoryminer.codemetric.indirect.AC;
import org.repositoryminer.mining.RepositoryMiner;
import org.repositoryminer.model.Repository;
import org.repositoryminer.parser.java.JavaParser;
import org.repositoryminer.persistence.Connection;
import org.repositoryminer.scm.ReferenceType;
import org.repositoryminer.scm.SCMType;

public class Crawler {

	public static void main(String[] args) throws IOException {
		Connection connection = Connection.getInstance(); 		 
		connection.connect("mongodb://localhost:27017", "mina" );
		RepositoryMiner miner = new RepositoryMiner("Mina");
		final ArrayList<Repository> repo = new ArrayList<>();
		miner.addParser(new JavaParser());
		miner.setPath("/home/paulo/Documents/development/java/eclipse/Mina");
		miner.setScm(SCMType.GIT);
		miner.addReference("master", ReferenceType.BRANCH);
		miner.setDirectCodeMetrics(Arrays.asList(new EC()));
		miner.setIndirectCodeMetrics(Arrays.asList(new AC()));
		miner.mine();
	}

}
