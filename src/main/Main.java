package main;
import java.io.IOException;

import controller.Controller;
import model.data_structures.GrafoNoDirigido;
import model.data_structures.noExisteObjetoException;

public class Main {
	
	public static void main(String[] args) throws noExisteObjetoException, IOException 
	{
		Controller controler = new Controller();
		controler.run();
	}
}
