package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.algorithm.AbstractAlgorithm;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.Test;
/*
 * 
 * Cette classe charge une carte puis test le scenario origine destination, sur les deux algorithmes.
 */
public class DjkstraTestMap {

	@Test
	public void testTrouverChemin(String mapName,int typeTest,int origineId,int destinationId) throws Exception {
		// lecture de la carte associée
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		Graph graph = reader.read();
		
		if((typeTest!=0 && typeTest!=1) || (origineId<0 || destinationId<0 || origineId>=(graph.size()-1)|| destinationId>=(graph.size()-1))) {
			System.out.println("Arguments Invalide");
		}else {
			ArcInspector arcInspectorDijkstra;
			if(typeTest==0) { // test en Temps
				System.out.println("Mode de Test : Temps");
				arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(2);
			}else {// Test en Distance
				System.out.println("Mode de Test : Distance");
				arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(0);
			}
			
			System.out.println("Origine(ID) : " + origineId);
			System.out.println("Destination(ID) : " + destinationId);
			if(origineId==destinationId) {
				System.out.println("Origine et Destination identiques");
				System.out.println("Cout solution: 0");
			}else {
				ShortestPathData data = new ShortestPathData(graph,graph.get(origineId),graph.get(destinationId),arcInspectorDijkstra);
				BellmanFordAlgorithm Bel = new BellmanFordAlgorithm(data);
				DijkstraAlgorithm Dij = new DijkstraAlgorithm(data);
				AStarAlgorithm AStar = new AStarAlgorithm(data); 
				// Recuperation des solutions de Bellman et Dijkstra pour comparer 
				ShortestPathSolution solution = Dij.run();
				ShortestPathSolution attendu = Bel.run();
				ShortestPathSolution solutionAstar = AStar.run();
				if(solution.getPath()==null) {
					assertEquals(attendu.getPath(),solution.getPath());
					assertEquals(solution.getPath(),solutionAstar.getPath());
					System.out.println("PAS DE SOLUTION");
					System.out.println("(infini) ");
				}else {
					double coutSolution,coutAttendu,coutAstar;
					if(typeTest==0) {
						coutSolution = solution.getPath().getMinimumTravelTime();
						coutAttendu = attendu.getPath().getMinimumTravelTime();
						coutAstar = solutionAstar.getPath().getMinimumTravelTime();
					}else {
						coutSolution = solution.getPath().getLength();
						coutAttendu = attendu.getPath().getLength();
						coutAstar = solutionAstar.getPath().getLength();
					}
					
					//assertEquals(coutAttendu, coutSolution, 0.001);
					//assertEquals(coutSolution,coutAstar,0.1);
					System.out.println("Cout solution: " + coutSolution);
				}
				System.out.println("***--------Test de performance--------*******");
				System.out.println("A-Temps d'execution");
				System.out.println("Temps execution Djkstra => "+solution.getSolvingTime().getNano());
				System.out.println("Temps execution Astar => "+solutionAstar.getSolvingTime().getNano());
				if(solutionAstar.getSolvingTime().getNano()<solution.getSolvingTime().getNano()) {
					System.out.println("Astar est plus rapide ");
				}else if(solution.getSolvingTime().getNano()<solutionAstar.getSolvingTime().getNano()) {
					System.out.println("Dijkstra est plus rapide ");
				}
			}
		}
		
		System.out.println();
		System.out.println();
		
		
		
	}
	/*
	 * Ce test repond au problème du test sans oracle
	 * ainsi nous allons verifier que le cout du chemin le plus court en temps
	 * est inferieur au cout du chemin le plus court en distance
	 * mais inversement la distance est plus grande.
	 */
	@Test
	public void TestTrouverCheminSansOracle(String mapName,int origineId,int destinationId) throws Exception{
		double coutFastestSolutionEnTemps = Double.POSITIVE_INFINITY;
		double coutFastestSolutionEnDistance = Double.POSITIVE_INFINITY;
		double coutShortestSolutionEnTemps =Double.POSITIVE_INFINITY;
		double coutShortestSolutionEnDistance = Double.POSITIVE_INFINITY;
		
		
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		Graph graph = reader.read();
		if(origineId<0||destinationId<0||origineId >(graph.size()-1)||destinationId>(graph.size()-1)) {
			System.out.println("ERREUR : Paramètres invalides ");
		}else {
			System.out.println("Origine (ID) : " + origineId);
			System.out.println("Destination(ID) : " + destinationId);
			if(origineId==destinationId) {
				System.out.println("Origine et Destination identiques");
				System.out.println("Tous les couts sont 0.");
				
			} else {
				// On recherche les deux types de chemin Rapide et Court
				// 1-Recherche Rapide en Temps
				ArcInspector arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(2);
				ShortestPathData data = new ShortestPathData(graph,graph.get(origineId),graph.get(destinationId),arcInspectorDijkstra);
	
				DijkstraAlgorithm Dij = new DijkstraAlgorithm(data);
				// Recuperation des solutions de Bellman et Dijkstra pour comparer 
				ShortestPathSolution solution = Dij.run();
				if(solution.getPath()==null) {
					System.out.println("Pas de chemin soution en temps");
				}else {
					coutFastestSolutionEnTemps = solution.getPath().getMinimumTravelTime();
					coutFastestSolutionEnDistance = solution.getPath().getLength();
				}
				//2- Recherche en Distance (le plus court chemin)
				arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(0);
				data = new ShortestPathData(graph,graph.get(origineId),graph.get(destinationId),arcInspectorDijkstra);
				Dij = new DijkstraAlgorithm(data);
				solution = Dij.run();
				if(solution.getPath()==null) {
					System.out.println("Pas de chemin solution en Distance");
				}else {
					coutShortestSolutionEnTemps = solution.getPath().getMinimumTravelTime();
					coutShortestSolutionEnDistance = solution.getPath().getLength();
				}
				
				// On effectue les test sur les deux méthodes
				assertTrue(coutShortestSolutionEnDistance<=coutFastestSolutionEnDistance);
				System.out.println("Cout en Distance du chemin le plus rapide : " + coutFastestSolutionEnDistance);
				System.out.println("Cout en Distance du chemin le plus court : "+coutShortestSolutionEnDistance);
				assertTrue(coutFastestSolutionEnTemps <=coutShortestSolutionEnTemps);
				System.out.println("Cout en Temps du chemin le plus rapide : " + coutFastestSolutionEnTemps);
				System.out.println("Cout en Temps du chemin le plus court : "+coutShortestSolutionEnTemps);
			}
		}
		System.out.println();
		System.out.println();
		
	}
	
}
