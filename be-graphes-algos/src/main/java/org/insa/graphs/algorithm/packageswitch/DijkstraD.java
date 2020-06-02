package org.insa.graphs.algorithm.packageswitch;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

public class DijkstraD extends PackageSwitchAlgorithm {
	
	protected Node Origin;
	protected Arc[] predecessorsArcDestinations;
	protected boolean reverseGraph;
	protected Label[] labels;
	
    public DijkstraD(PackageSwitchData data,Node Origin,boolean reverseGraph) {
        super(data);
        int nbNodes =data.getGraph().getNodes().size();
        this.Origin = Origin;
        this.reverseGraph= reverseGraph;
        this.predecessorsArcDestinations = new Arc[nbNodes];
        this.labels = new Label[nbNodes];
        
    }
    @Override
    protected PackageSwitchSolution doRun() {
    	return null;
    }
    
    public boolean isReverseGraph() {
    	return this.reverseGraph;
    }
    public void setReverseGraph() {
    	this.reverseGraph = true;
    }
    
    //@Override
    protected void doRunD() {
        final PackageSwitchData data = getInputData();
        Graph graph = data.getGraph();
        if(isReverseGraph()) {
        	graph = graph.transpose();
        }
        List<Node>	nodes = graph.getNodes();
        // Initialisation
        final int nbNodes = graph.size();
        // Initialize array of distances.
        BinaryHeap<Label> tasBinaire = new BinaryHeap<Label>();
   
        int originId = this.Origin.getId();
        this.labels[originId] = this.newLabel(nodes.get(originId),data);
        this.labels[originId].setCost(0); // cost(s) = 0
        tasBinaire.insert(this.labels[originId]); // inser(s,tas)
        // Notify observers about the first event (origin processed).
        //notifyOriginProcessed(data.getOrigin());
        int count = 0;
        Label currentSortNode; 
        while(count < nbNodes && !tasBinaire.isEmpty()) {
        	if(tasBinaire.isEmpty()) {
        		break;
        	}
        	currentSortNode = tasBinaire.deleteMin(); // extract Min 
        	currentSortNode.setMarqued();
        	
        	for(Arc arc: nodes.get(currentSortNode.getNodeId()).getSuccessors()) {
        		if(!data.isAllowed(arc)) {
        			continue;
        		}
        		int nodeId = arc.getDestination().getId();
        		if(this.labels[nodeId]==null) {
        			this.labels[nodeId] = this.newLabel(arc.getDestination(), data);
        		}
        		if (!this.labels[nodeId].isMarqued()) {
        			
        			double w = data.getCost(arc);
                    double oldDistance = this.labels[nodeId].getTotalCost();
                    double newDistance = this.labels[currentSortNode.getNodeId()].getTotalCost() + w;
                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        //notifyNodeReached(arc.getDestination());
                    }
                    
                    if (newDistance <oldDistance) {
                    	if(!this.labels[nodeId].isAlreadySeen()) {
            				this.labels[nodeId].setAlreadySeen();
            			}else {
            				tasBinaire.remove(this.labels[nodeId]);
            			}
                        this.labels[nodeId].setCost(this.labels[currentSortNode.getNodeId()].getCost()+w); // mise a jour
                        this.labels[nodeId].setFather(currentSortNode.getNodeId());
                        tasBinaire.insert(this.labels[nodeId]);
                        this.predecessorsArcDestinations[arc.getDestination().getId()] = arc;
                    }
        		}
        	}
        	
        	count ++;
        }
       
        
    }
    public static void main(String[] args) throws IOException{
    	String mapName = "/home/amadou/CoursINSA3MIC/BE_graphe/projet/be-graphes/Maps/insa/europe/france/insa.mapgr";
    	GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
		Graph graph = reader.read();
		ArcInspector arcInspectorDijkstra = ArcInspectorFactory.getAllFilters().get(0);
		Node originA,originB,destinationA,destinationB;
		originA = graph.getNodes().get(552);
		originB = graph.getNodes().get(0);
		destinationA= graph.getNodes().get(526);
		destinationB=graph.getNodes().get(0);
		PackageSwitchData data=new PackageSwitchData(graph,arcInspectorDijkstra,originA,originB,destinationA,destinationB);
		
    	DijkstraD dij = new DijkstraD(data,originA,false);
    	DijkstraD dij2 = new DijkstraD(data,destinationA,true);
    	dij.doRunD();
    	dij2.doRunD();
    	Path pathToDestinationA = dij.getPathToDestination(graph, 102);
    	Path pathToOriginA = dij2.getPathToDestination(graph,552 );
    	System.out.println("temps de chemin de OriginA à destinationA "+pathToDestinationA.getMinimumTravelTime());
    	System.out.println("Longeur Path de OriginA à destinationA "+pathToDestinationA.getLength());
    	System.out.println("temps de chemin de destinationA à OriginA (utilisation du reverse graph) "+pathToOriginA.getMinimumTravelTime());
    	System.out.println("Longeur Path "+pathToOriginA.getLength());
		reader.close();
    }
    
    public Label newLabel(Node node, PackageSwitchData data) {
    	return new Label(node);
    }
    
    public Label[] getLabels() {
    	return this.labels;
    }
    public Arc[] getPrecedessorsArc() {
    	return this.predecessorsArcDestinations;
    }
    
    public boolean isProceed(Node node) {
    	if(this.labels[node.getId()]==null) {
    		return false;
    	}
    	return this.labels[node.getId()].isMarqued();
    }
    public Path getPathToDestination(Graph graph,int nodeId) {
    	if(this.labels[nodeId]==null) {
    		return new Path(graph);
    	}
    	
    	if(!this.labels[nodeId].isMarqued()) {
    		return new Path(graph);
    	}
    	Arc arc = predecessorsArcDestinations[nodeId];
    	ArrayList<Arc> arcs = new ArrayList<>();
    	while(arc!=null) {
    		arcs.add(arc);
            arc = predecessorsArcDestinations[arc.getOrigin().getId()]; 
    	}
 	
		Collections.reverse(arcs);
		return new Path(graph,arcs);
    }
}
