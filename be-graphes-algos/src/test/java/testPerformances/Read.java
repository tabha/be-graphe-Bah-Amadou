package testPerformances;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Read {
	private String mapName ;
	private ArrayList<Integer> listeOrigine;
	private ArrayList<Integer> listeDestination;

	public Read(String fileName) {
		this.listeOrigine = new ArrayList<Integer>();
		this.listeDestination = new ArrayList<Integer>();
		this.readFile(fileName);
		System.out.println("Fichier "+fileName+"lu!");
	}

	public void readFile (String fileName) {
		try {
			Scanner scan = new Scanner(new File(fileName));
			int origine;
			int destination;
			
			/* Recupere le nom de la carte */
			if (scan.hasNext()) {
				this.mapName = scan.nextLine();
			}
			
			/* Recupere les origines et destinations */
			while (scan.hasNext()) {
				String ligne = scan.nextLine();
				String[] colones = ligne.split(" ");
				if(colones.length!=2) {
					throw new Exception("Le fichier contient un nombre impair de points!");
				}
				origine = Integer.parseInt(colones[0]);
				destination = Integer.parseInt(colones[1]);
				//System.out.println("Origine = "+origine+" desintation= "+destination);
				this.listeOrigine.add(origine);
				this.listeDestination.add(destination);
				
			}
			scan.close();
		}
		catch(Exception e) {
				System.out.println(e.getMessage());
		}
	}

	public String getMapName() {
		return this.mapName;
	}

	public ArrayList<Integer> getListeOrigine(){
		return this.listeOrigine;
	}

	public ArrayList<Integer> getListeDestination(){
		return this.listeDestination;
	}
}
