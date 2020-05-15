package controller;

import java.io.IOException;
import java.util.Scanner;

import model.data_structures.noExisteObjetoException;
import model.logic.Modelo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 */
	public Controller ()
	{
		view = new View();
		modelo = new Modelo();
	}
		
	public void run() throws noExisteObjetoException, IOException 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		String respuesta = "";

		while( !fin ){
			view.printMenu();

			int option = lector.nextInt();
			switch(option){
				case 1:
					modelo.cargarDatos();						
					break;
				case 2: 
					modelo.generarJSon();
					view.printMessage("Listo!\n\n");
					break;
				case 3:
					modelo.leerJson();
					view.printMessage("Listo!\n\n");
					break;

				

				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}
		}
		
	}	
}
