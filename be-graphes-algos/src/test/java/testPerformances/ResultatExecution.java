package testPerformances;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;



public class ResultatExecution {
	private int origine;
	private int destination;
	private int tempsExecutionDijkstra; // nanoSeconde
	private int tempsExecutionAStar;	// nanoSeconde
	private int nbSommetsDijsktra;
	private int nbSommetsAStar;
	public ResultatExecution(String mapName,int origine, int destination,int typeEvaluation) {
		this.origine = origine;
		this.destination = destination;
		try {
			
			GraphReader reader = new BinaryGraphReader(
					new DataInputStream(new BufferedInputStream(new FileInputStream(TestCampaign.getFullPath(mapName, 1)))));
		
			Graph graph = reader.read();
			
			// 
			ArcInspector arcInspector;
			if(typeEvaluation==0) { // en temps;
				arcInspector = ArcInspectorFactory.getAllFilters().get(2);
				
			}else {
				arcInspector = ArcInspectorFactory.getAllFilters().get(0);
			}
			ShortestPathData data = new ShortestPathData(graph, graph.get(origine),graph.get(destination), arcInspector);

			/* Calcul du temps d'exécution de Dijkstra */
			DijkstraAlgorithm D = new DijkstraAlgorithm(data);
			
			ShortestPathSolution solutioD =D.run();
			this.tempsExecutionDijkstra = solutioD.getSolvingTime().getNano();
			this.nbSommetsDijsktra = D.getNbSommetVisitees();

			
			/* Calcul du temps d'exécution d'AStar */
			AStarAlgorithm A = new AStarAlgorithm(data);
			ShortestPathSolution solutioA = A.run();
			
			this.nbSommetsAStar = A.getNbSommetVisitees();
			this.tempsExecutionAStar= solutioA.getSolvingTime().getNano();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public int getOrigine() {
		return this.origine;
	}
	public int getDestination() {
		return this.destination;
	}
	public int getSolingTimeDijkstra() {
		return this.tempsExecutionDijkstra;
	}
	public int getSolingTimeAstar() {
		return this.tempsExecutionAStar;
	}
	public int getNbSommetsVisitesDijkstra() {
		return this.nbSommetsDijsktra;
	}
	
	public int getNbSommetsVisitesAStar() {
		return this.nbSommetsAStar;
	}
	
}
