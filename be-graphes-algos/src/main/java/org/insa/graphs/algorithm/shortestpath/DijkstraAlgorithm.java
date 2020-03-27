package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.PriorityQueue;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        List<Node>	nodes = graph.getNodes();
       
        // Initialisation

        final int nbNodes = graph.size();

        // Initialize array of distances.
        PriorityQueue<Label> tasBinaire = new BinaryHeap<Label>();
        // Initailize all labels
        ArrayList<Label> labels= Label.createAllLabelFromGraph(graph);
        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        int originId = data.getOrigin().getId();
        labels.get(originId).setCost(0); // cost(s) = 0
        tasBinaire.insert(labels.get(originId));	// inser(s,tas)
        
        Label currentSortNode; 
        int count=0;
        while(labels.get(data.getDestination().getId()).isMarqued() && count < nbNodes) {
        	currentSortNode = tasBinaire.deleteMin(); // extraction du min 
        	for(Arc arc: nodes.get(currentSortNode.getNodeId()).getSuccessors()) { // marquage des successeurs
        		int nodeId = arc.getDestination().getId(); // recuperation du noeud successeur
        		 
        		if (!labels.get(nodeId).isMarqued()) { //
        			double w = data.getCost(arc);
                    double oldDistance = labels.get(nodeId).getCost();
                    double newDistance = labels.get(currentSortNode.getNodeId()).getCost() + w;
                    
                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }
                    if (newDistance < oldDistance) {
                        int indexNodeTas = tasBinaire.indexOf(labels.get(nodeId));
                        if (indexNodeTas == -1 ) { // introuvable dans le tas , on l'insère
                        	labels.get(nodeId).setCost(newDistance);
                        	tasBinaire.insert(labels.get(nodeId));
                        }else {
                        	labels.get(nodeId).setCost(newDistance);
                        	//tasBinaire.update(indexNodeTas,labels.get(nodeId));
                        }
                        labels.get(nodeId).setFather(currentSortNode.getNodeId());
                    }
                    predecessorArcs[arc.getDestination().getId()] = arc;
        		}
        	}
        	labels.get(currentSortNode.getNodeId()).setMarqued(); // Marquage du Noeud traite
        	
        	count++;
        }
       
        ShortestPathSolution solution = null;

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
        
        return solution;
    }

}
