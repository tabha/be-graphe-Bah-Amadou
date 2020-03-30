package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
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
	protected int nombreSucceurTeste;
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.nombreSucceurTeste = 0;
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
        tasBinaire.insert(labels.get(originId)); // inser(s,tas)
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        boolean coutCroissant = true;
        double previousCost = 0.0;
        int count = 0;
        Label currentSortNode; 
        while(!labels.get(data.getDestination().getId()).isMarqued() && count < nbNodes) {
        	if(tasBinaire.isEmpty()) {
        		break;
        	}
        	currentSortNode = tasBinaire.deleteMin(); // extract Min 
        	currentSortNode.setMarqued();
        	if(previousCost <= currentSortNode.getCost()) {
        		previousCost = currentSortNode.getCost();
        	}else {
        		coutCroissant = false;
        	}
        	System.out.println("cout du sommet :"+currentSortNode.getNodeId() + " est => "+ currentSortNode.getCost());
        	for(Arc arc: nodes.get(currentSortNode.getNodeId()).getSuccessors()) {
        		if(!data.isAllowed(arc)) {
        			continue;
        		}
        		int nodeId = arc.getDestination().getId();
        		this.nombreSucceurTeste++;
        		if (!labels.get(nodeId).isMarqued()) {
        			
        			double w = data.getCost(arc);
                    double oldDistance = labels.get(nodeId).getCost();
                    double newDistance = labels.get(currentSortNode.getNodeId()).getCost() + w;
                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }
                    if (newDistance < oldDistance) {
                    	if(!labels.get(nodeId).isAlreadySeen()) {
            				labels.get(nodeId).setAlreadySeen();
            			}else {
            				tasBinaire.remove(labels.get(nodeId));
            			}
                        labels.get(nodeId).setCost(newDistance); // mise a jour
                        labels.get(nodeId).setFather(currentSortNode.getNodeId());
                        tasBinaire.insert(labels.get(nodeId));
                        predecessorArcs[arc.getDestination().getId()] = arc;
                    }
        		}
        	}
        	count ++;
        }
        System.out.println("L'évolution de cout de marquage dans le tas est : croissant? "+ coutCroissant);
        ShortestPathSolution solution = null;
        System.out.println("Nombre d'itération :"+ count + " interations");
        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            System.out.println("Le nombre d'arcs du PCC: 0");
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            int arcCount= 0;
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
                arcCount++;
            }
            System.out.println("Le nombre d'arcs du PCC: "+arcCount);
            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            System.out.println("Path Found is Valid? "+ solution.getPath().isValid());
            System.out.println("Longeur du chemin (théorique) =>"+solution.getPath().getLength());
            System.out.println("nombre de successeur testee :"+ this.nombreSucceurTeste);
        }
        
        return solution;
    }

}
