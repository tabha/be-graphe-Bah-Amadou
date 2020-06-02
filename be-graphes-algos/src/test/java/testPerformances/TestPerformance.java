package testPerformances;

import java.util.ArrayList;
import java.util.Iterator;

public class TestPerformance {
	private ArrayList<ResultatExecution> listeResultatPerformance;

	public TestPerformance() {
		this.listeResultatPerformance = new ArrayList<ResultatExecution>();
	}


	public void doRun(String fileNameRead, String fileNameWrite, int typeEvaluation) {
		
		Read lect = new Read(fileNameRead);

		String mapName = lect.getMapName();

		Iterator<Integer> origineIter = lect.getListeOrigine().iterator();
		Iterator<Integer> destinationIter = lect.getListeDestination().iterator();


		while(origineIter.hasNext()) {
			ResultatExecution  resultat = new ResultatExecution (mapName, origineIter.next(), destinationIter.next(), typeEvaluation);
			
			this.listeResultatPerformance.add(resultat);
		}

	
		Write write = new Write(fileNameWrite, mapName, this.listeResultatPerformance);
		
	}

}
