package testPerformances;

import java.io.FileWriter;
import java.util.ArrayList;

public class Write {
	public Write(String fileNameWrite, String mapName, ArrayList<ResultatExecution>listeResultatPerformance) {
		 String commaDelimiter = ",";
		 String newLineSeparator ="\n";
		 String fileHeader ="Carte,Origine,Destination,Temps CPU Dijkstra (en ns), Nb Sommets, Temps CPU A* (en ns), Nb Sommets"; // Nb sommets
		 try {
		 FileWriter fileWriter = new FileWriter(fileNameWrite);
		 fileWriter.append(fileHeader);
		 	for (ResultatExecution p : listeResultatPerformance) {
		 		fileWriter.append(newLineSeparator);
		 		fileWriter.append(mapName);
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getOrigine()));
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getDestination()));
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getSolingTimeDijkstra()));
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getNbSommetsVisitesDijkstra()));
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getSolingTimeAstar()));
		 		fileWriter.append(commaDelimiter);
		 		fileWriter.append(String.valueOf(p.getNbSommetsVisitesAStar()));
		 	}
	 		fileWriter.flush();
	 		fileWriter.close();
	 		System.out.println("Done");	
	 		} 
		 catch (Exception e){
			 System.out.println(e.getMessage());
		 }
	 }
}
