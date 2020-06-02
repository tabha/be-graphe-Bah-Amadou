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
        BinaryHeap<Label> tasBinaire = new BinaryHeap<Label>();
        // Initailize all labels
        //ArrayList<Label> labels= Label.createAllLabelFromGraph(graph);
        Label[] labels = new Label[nbNodes];
        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        int originId = data.getOrigin().getId();
        labels[originId] = this.newLabel(data.getOrigin(), data);
        labels[originId].setCost(0); // cost(s) = 0
        tasBinaire.insert(labels[originId]); // inser(s,tas)
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        boolean coutCroissant = true;
        boolean tasValid = true;
        double previousCost = 0.0;
        int count = 0;
        Label currentSortNode; 
        
        boolean finish = false;
        while(!finish && count < nbNodes && !tasBinaire.isEmpty()) {
        	if(tasBinaire.isEmpty()) {
        		break;
        	}
        	currentSortNode = tasBinaire.deleteMin(); // extract Min 
        	currentSortNode.setMarqued();
        	if(currentSortNode.getNodeId() == data.getDestination().getId()) {
        		finish = true;
        	}
        	if(previousCost <= currentSortNode.getCost()) {
        		previousCost = currentSortNode.getCost();
        	}else {
        		coutCroissant = false;
        	}
        	
        	for(Arc arc: nodes.get(currentSortNode.getNodeId()).getSuccessors()) {
        		if(!data.isAllowed(arc)) {
        			continue;
        		}
        		int nodeId = arc.getDestination().getId();
        		if(labels[nodeId]==null) {
        			labels[nodeId] = this.newLabel(arc.getDestination(), data);
        		}
        		this.nombreSucceurTeste++;
        		if (!labels[nodeId].isMarqued()) {
        			double w = data.getCost(arc);
                    double oldDistance = labels[nodeId].getTotalCost();
                    double newDistance = labels[currentSortNode.getNodeId()].getTotalCost() + w;
                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                    }
                    if (newDistance <oldDistance) {
                    	if(!labels[nodeId].isAlreadySeen()) {
            				labels[nodeId].setAlreadySeen();
            			}else {
            				tasBinaire.remove(labels[nodeId]);
            			}
                        labels[nodeId].setCost(labels[currentSortNode.getNodeId()].getCost()+w); // mise a jour
                        labels[nodeId].setFather(currentSortNode.getNodeId());
                        tasBinaire.insert(labels[nodeId]);
                        predecessorArcs[arc.getDestination().getId()] = arc;
                    }
        		}
        	}
        	if(tasValid) {
        		tasValid = tasBinaire.isValid(0);
        	}
        	count ++;
        }
        //System.out.println("tas valid durant tout le traitement? " + tasBinaire.isValid(0));
        //System.out.println("L'évolution de cout de marquage dans le tas est : croissant? "+ coutCroissant);
        ShortestPathSolution solution = null;
        //System.out.println("Nombre d'itération :"+ count + " interations");
        // Destination has no predecessor, the solution is infeasible...
        double distanceChemin = 0.0;
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            //System.out.println("Le nombre d'arcs du PCC: 0");
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
                distanceChemin += arc.getLength();
                arc = predecessorArcs[arc.getOrigin().getId()];
                arcCount++;
            }
            //System.out.println("Le nombre d'arcs du PCC: "+arcCount);
            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
            //System.out.println("Path Found is Valid? "+ solution.getPath().isValid());
            //System.out.println("Longeur du chemin (théorique) =>"+solution.getPath().getLength());
            //System.out.println("nombre de successeur testee :"+ this.nombreSucceurTeste);
            //System.out.println("Longeur du chemin (obtenu =>)"+distanceChemin);
        }
        
        return solution;
    }
    public int getNbSommetVisitees() {
    	return this.nombreSucceurTeste;
    }
    public Label newLabel(Node node, ShortestPathData data) {
    	return new Label(node);
    }
    
}
