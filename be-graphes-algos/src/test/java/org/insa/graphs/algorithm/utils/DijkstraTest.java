package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.junit.Test;

public class DijkstraTest {

private static Graph graph; 
	public static String absolutePathCartes = "/home/amadou/CoursINSA3MIC/BE_graphe/cartes";
	private static Node[] nodes;
	
	private static Arc a2b,a2e,b2e,b2c,c2d,d2c,d2a,e2b,e2c,e2d;
	public static String formatString(String str,boolean beg){
        String formatDelimit="%-4s";
        String formatCol ="%-10s";
        String delimiter="|";
        String header = "";
        if(beg) {
        	header+=String.format(formatDelimit+formatCol+formatDelimit,delimiter,str,delimiter);
        }else{
        	header+=String.format(formatCol+formatDelimit,str,delimiter);
        }
        return (String) header;
    }
	public static void initAll() throws IOException {
		RoadInformation RoadInfo = new RoadInformation(RoadType.UNCLASSIFIED,null,true,1,null);
		nodes = new Node[5];
		for(int i=0;i<nodes.length;i++) {
			nodes[i] = new Node(i,null);
		}
		
		// Add all arcs
		a2b = Node.linkNodes(nodes[0], nodes[1], 10, RoadInfo, null);
		a2e = Node.linkNodes(nodes[0], nodes[4], 5, RoadInfo, null);
		b2e = Node.linkNodes(nodes[1], nodes[4], 2, RoadInfo, null);
		b2c = Node.linkNodes(nodes[1], nodes[2], 1, RoadInfo, null);
		c2d = Node.linkNodes(nodes[2], nodes[3], 4, RoadInfo, null);
		d2c = Node.linkNodes(nodes[3], nodes[2], 6, RoadInfo, null);
		d2a = Node.linkNodes(nodes[3], nodes[0], 7, RoadInfo, null);
		e2b = Node.linkNodes(nodes[4], nodes[1], 3, RoadInfo, null);
		e2c = Node.linkNodes(nodes[4], nodes[2], 9, RoadInfo, null);
		e2d = Node.linkNodes(nodes[4], nodes[3], 2, RoadInfo, null);
		// create graph
		graph = new Graph("ID_D","",Arrays.asList(nodes),null);
		
	}
	
	//@Test
	public void testRun() {
		try {
			initAll();
		}catch(IOException e){
			System.out.println("Error initAll");
		}
		String[] nodesName= {"A","B","C","D","E"};
		boolean beg=true;
		System.out.println("***-----------Test de vailidité avec un exemple simple-------------******");
		String str;
		str = formatString(" ",beg);
		System.out.print(str);
		for(int i=0;i<nodes.length;i++) {
			str = nodesName[i]+" : ";
			str = formatString(str,false);
			System.out.print(str);
			
		}
		
		System.out.println();
		
		for(int i=0;i<nodes.length;i++) {
			str =nodesName[i]+":";
			
			str = formatString(str,true);
			System.out.print(str);
			
			for(int j=0;j<nodes.length;j++) {
				if(nodes[i]==nodes[j]) {
					str = "-----";
					//beg = (j==0);
					str = formatString(str,false);
					System.out.print(str);
					
				}else {
					ArcInspector arcInspectorDjikstra = ArcInspectorFactory.getAllFilters().get(0);
					ShortestPathData data = new ShortestPathData(graph,nodes[i],nodes[j],arcInspectorDjikstra);
					BellmanFordAlgorithm B = new BellmanFordAlgorithm(data);
					DijkstraAlgorithm D = new DijkstraAlgorithm(data);
					AStarAlgorithm AStar = new AStarAlgorithm(data);
					// ON execute les deux alogs.
					
					ShortestPathSolution solution = D.run();
					ShortestPathSolution attendu = B.run();
					//ShortestPathSolution solutionAstar = AStar.run();
					
					if(solution.getPath()==null) {
						assertEquals(attendu.getPath(),solution.getPath());
						str = "infini";
						//beg = (j==0);
						str = formatString(str,beg);
						System.out.print(str);
					}else {
						double coutSolution = solution.getPath().getLength();
						double coutAttendu = attendu.getPath().getLength();
						//double coutAstar = solutionAstar.getPath().getLength();
						assertEquals(coutSolution,coutAttendu,0);
						//assertEquals(coutSolution,coutAstar,0);
						List<Arc> arcs = solution.getPath().getArcs();
						Node pereDestination = arcs.get(arcs.size()-1).getOrigin();
						str = coutSolution +", "+nodesName[pereDestination.getId()] ;
						//beg = (j==0);
						str = formatString(str,false);
						System.out.print(str);
					}
				}
			}
			System.out.println();
			
		}
		System.out.println();
	}
	
	@Test
	public void testDistanceHG() throws Exception {
		String mapName = "haute-garonne.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testTrouverChemin(path, 1, origine, destination);
		  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 15978;
		destination = 74699;
		test.testTrouverChemin(path, 1,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.testTrouverChemin(path, 1,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.testTrouverChemin(path, 1,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778;
		test.testTrouverChemin(path, 1,origine,destination);  
	}
	@Test
	public void testTempsHG() throws Exception{
		String mapName = "haute-garonne.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println("#####----- Mode : Temps-----------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testTrouverChemin(path, 0, origine, destination);
		  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 15978;
		destination = 74699;
		test.testTrouverChemin(path, 0,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.testTrouverChemin(path, 0,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.testTrouverChemin(path, 0,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778;
		test.testTrouverChemin(path, 0,origine,destination); 
	}
	@Test
	public void testDistanceINSA() throws Exception{
		String mapName = "insa.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte------######");
		System.out.println("#####----- Carte : INSA     -------------------------------######");
		System.out.println("#####----- Mode :  DISTANCE -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testTrouverChemin(path, 1, origine, destination);
		  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 141;
		destination = 51;
		test.testTrouverChemin(path, 1,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.testTrouverChemin(path, 1,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.testTrouverChemin(path, 1,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778;
		test.testTrouverChemin(path, 1,origine,destination);  
	}

	@Test 
	public void testTempsINSA() throws Exception{
		String mapName = "insa.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte------######");
		System.out.println("#####----- Carte : INSA     -------------------------------######");
		System.out.println("#####----- Mode :  Temps    -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.testTrouverChemin(path, 0, origine, destination);
		  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 141;
		destination = 51;
		test.testTrouverChemin(path, 0,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.testTrouverChemin(path, 0,origine,destination);    	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.testTrouverChemin(path, 0,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778;
		test.testTrouverChemin(path, 0,origine,destination);  
	}
	@Test 
	public void testTempsPHNonConnexe() throws Exception{
		String mapName = "philippines.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte------######");
		System.out.println("#####----- Carte : INSA     -------------------------------######");
		System.out.println("#####----- Mode :  Temps    -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un graphe Non connexe ------");
		origine =882602;
		destination = 563854;
		test.testTrouverChemin(path, 0,origine,destination);    
	}

	//@Test
	public void testSansOracleHG() throws Exception{
		String mapName = "haute-garonne.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		DjkstraTestMap test = new DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité sans oracle sur une carte-----######");
		System.out.println("#####----- Carte : Haute-Garonne -------------------------######");
		System.out.println();

		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.TestTrouverCheminSansOracle(path,origine,destination);   
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 15978;
		destination = 74699;
		test.TestTrouverCheminSansOracle(path,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.TestTrouverCheminSansOracle(path,origine,destination);   	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.TestTrouverCheminSansOracle(path,origine,destination);    	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778; 
		test.TestTrouverCheminSansOracle(path,origine,destination); 
	}

	@Test
	public void TestSansOracleINSA() throws Exception{
		String mapName = "insa.mapgr";
		String path= absolutePathCartes+"/"+mapName;
		
		DjkstraTestMap test = new  DjkstraTestMap();
		int origine;
		int destination;
		
		System.out.println("#####----- Test de validité avec oracle sur une carte------######");
		System.out.println("#####----- Carte : INSA     -------------------------------######");
		System.out.println("#####----- Mode :  Temps    -------------------------------######");
		System.out.println();
		
		System.out.println("----- Cas d'un chemin nul ------");
		origine = 0 ;
		destination = 0;
		test.TestTrouverCheminSansOracle(path,origine,destination);
		  
		
		System.out.println("----- Cas d'un chemin simple ------");
		origine = 141;
		destination = 51;
		test.TestTrouverCheminSansOracle(path,origine,destination);    	
	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : Existe ------------");
		origine = -1;
		destination = 74699;
		test.TestTrouverCheminSansOracle(path,origine,destination);   	

		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : Existe ----------------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = 1545574;
		destination = 254778;
		test.TestTrouverCheminSansOracle(path,origine,destination);   	
		
		System.out.println("----- Cas de sommets inexistants ------");
		System.out.println("----- Origine : N'existe pas ----------");
		System.out.println("----- Destination : N'existe pas ------");
		origine = -1;
		destination = 254778;
		test.TestTrouverCheminSansOracle(path,origine,destination);  
	}
}
