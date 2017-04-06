package br.ufba.mata27.web;

import java.io.IOException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
public class Main extends Application {
	
	
	public Main() throws IOException{
		System.out.println(" Rodando Crawler");
	}
}
