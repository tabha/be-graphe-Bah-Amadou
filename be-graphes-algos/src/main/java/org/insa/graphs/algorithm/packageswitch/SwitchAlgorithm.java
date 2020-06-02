package org.insa.graphs.algorithm.packageswitch;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.insa.graphs.algorithm.AbstractAlgorithm;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.Intersection;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class SwitchAlgorithm extends PackageSwitchAlgorithm{

	public SwitchAlgorithm(PackageSwitchData data) {
		super(data);
	}
	
	public PackageSwitchSolution doRun() {
		final PackageSwitchData data = getInputData();
		Graph graph = data.getGraph();
		List<Node>nodes = graph.getNodes();
		final int nbNodes = graph.size();
		Intersection[] intersections= new Intersection[nbNodes];
		
		//On lance les dijkstraD pour les 4 sommets.
		DijkstraD dijOA = new DijkstraD(data,data.getOriginA(),false);
		DijkstraD dijOB = new DijkstraD(data,data.getOriginB(),false);
		DijkstraD dijDA = new DijkstraD(data,data.getDestinationA(),true);
		DijkstraD dijDB = new DijkstraD(data,data.getDestinationB(),true);
		dijOA.doRunD();
		dijOB.doRunD();
		dijDA.doRunD();
		dijDB.doRunD();
		for(Node node:nodes) {
			if(!isProcessed(node,dijOA,dijOB,dijDA,dijDB)) {
				continue;
			}
			// node may be the intersection
			Path fromOAToCurrentIntersection = dijOA.getPathToDestination(graph, node.getId());
			Path fromOBToCurrentIntersection = dijOB.getPathToDestination(graph, node.getId());
			Path fromCurrentIntersectionToDA = dijDA.getPathToDestination(graph, node.getId());
			Path fromCurrentIntersectionTODB = dijDB.getPathToDestination(graph, node.getId());
			double cost =(
					fromOAToCurrentIntersection.getMinimumTravelTime()+
					fromOBToCurrentIntersection.getMinimumTravelTime()+
					fromCurrentIntersectionToDA.getMinimumTravelTime()+
					fromCurrentIntersectionTODB.getMinimumTravelTime()
					);
					
			intersections[node.getId()] = new Intersection(node.getId(),cost);
		}
		Intersection minIntersection=new Intersection(data.getOriginA().getId(),Double.MAX_VALUE);
		for(Intersection inter:intersections) {
			if(inter==null) {
				continue;
			}
			if(inter.compareTo(minIntersection)<0) {
				minIntersection = inter;
			}
		}
		PackageSwitchSolution solution;
		if(minIntersection.getCostIntersection()==Double.MAX_VALUE) {
			solution = new PackageSwitchSolution(data,Status.INFEASIBLE,minIntersection);
			System.out.println("Pas de solution");
		}else {
			solution = new PackageSwitchSolution(data,Status.OPTIMAL,minIntersection);
			DijkstraD dijInt = new DijkstraD(data,data.getGraph().getNodes().get(minIntersection.getId()),false);
			dijInt.doRunD();
			Path OAToMinInter = dijOA.getPathToDestination(graph, minIntersection.getId());
			Path OBToMinInter = dijOB.getPathToDestination(graph, minIntersection.getId());
			Path MinInterToDA = dijInt.getPathToDestination(graph, data.getDestinationA().getId());
			Path MinInterToDB = dijInt.getPathToDestination(graph, data.getDestinationB().getId());
			Path pathFirstRobot = Path.concatenate(OAToMinInter,MinInterToDA);
			Path pathSecondRobot = Path.concatenate(OBToMinInter,MinInterToDB);
			solution.setNewPathRobot(1, pathFirstRobot);
			solution.setNewPathRobot(2, pathSecondRobot);
		}

		return solution;
	}
	/*
	public static void main(String[] args) throws IOException{
		//String mapName = "/home/amadou/CoursINSA3MIC/BE_graphe/projet/be-graphes/Maps/insa/europe/france/insa.mapgr";
		String mapName = "/home/amadou/CoursINSA3MIC/BE_graphe/projet/be-graphes/Maps/toulouse/europe/france/toulouse.mapgr";
		GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		Graph graph = reader.read();
		ArcInspector arcInspector = ArcInspectorFactory.getAllFilters().get(0);
		Node originA,originB,destinationA,destinationB;
		originA = graph.getNodes().get(5333);
		destinationA = graph.getNodes().get(18633);
		originB = graph.getNodes().get(13307);
		destinationB = graph.getNodes().get(1698);
		PackageSwitchData data = new PackageSwitchData(graph,arcInspector,originA,originB,destinationA,destinationB);
		SwitchAlgorithm switchRobot = new SwitchAlgorithm(data);
		PackageSwitchSolution solution = switchRobot.doRun();
		
		Path newPathFirstRobot = solution.getNewPathRobot(1);
		Path newPathSecondRobot = solution.getNewPathRobot(2);
		System.out.println("Intersection obtenu au node n° "+solution.getIntersection().getId());
		System.out.println("Nouveau Temps de trajet robot 1 "+newPathFirstRobot.getMinimumTravelTime());
		System.out.println("Longeur du nouveau chemin robot 1 "+newPathFirstRobot.getLength());
		System.out.println("Nouveau Temps de trajet robot 2 "+newPathSecondRobot.getMinimumTravelTime());
		System.out.println("Longeur du nouveau chemin robot 2 "+newPathSecondRobot.getLength());
		
		
	}
	*/
	private boolean isProcessed(Node node,DijkstraD dijOA,DijkstraD dijOB,DijkstraD dijDA,DijkstraD dijDB) {
		
		return dijOA.isProceed(node) && dijOB.isProceed(node) && dijDA.isProceed(node) && dijDB.isProceed(node);
	}
}
