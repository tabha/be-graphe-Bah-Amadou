package org.insa.graphs.algorithm.shortestpath;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Label;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
    	
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();
        
        BinaryHeap<Label> tas = new BinaryHeap<Label>() ;
        Label[] labels = new Label[nbNodes];


        int i = 0;
        for (Node node: graph.getNodes()) {
        	if (node.equals(data.getOrigin())){
            	Label label = new Label(node,false,0,null);
            	labels[i]=label;
                tas.insert(labels[i]);
        	} else {
        	Label label = new Label(node,false,Float.MAX_VALUE,null);
        	labels[i]=label;
        	}
        	i++;
        }
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
        boolean dejaDansTas = false ;
        
        while (!labels[data.getDestination().getId()].marque) {
        	Label labelActuel = tas.deleteMin() ;
        	Node nodeActuel = labelActuel.sommet_courant;
        	labelActuel.marque = true ;
        	dejaDansTas = false ;
        	
            for (Arc arcSuc: nodeActuel.getSuccessors()) {
        		
                // Small test to check allowed roads...
                if (!data.isAllowed(arcSuc)) {
                    continue;
                }
                
        		Node nodeSuccesseur = arcSuc.getDestination();

            	if (!labels[nodeSuccesseur.getId()].marque) {
            		if (labels[nodeSuccesseur.getId()].getCost() != Float.MAX_VALUE) {
            			dejaDansTas = true ;
            		}
            		if (labels[nodeSuccesseur.getId()].getCost() > (labels[nodeActuel.getId()].getCost() + arcSuc.getLength())) {
            			labels[nodeSuccesseur.getId()].cout = labels[nodeActuel.getId()].getCost() + arcSuc.getLength();
            			if (dejaDansTas) {
            				tas.remove(labels[nodeSuccesseur.getId()]);
            				tas.insert(labels[nodeSuccesseur.getId()]);
            			} else {
            				tas.insert(labels[nodeSuccesseur.getId()]);
            			}
            			labels[nodeSuccesseur.getId()].pere = arcSuc ;
            		}
            	}
            	dejaDansTas = false ;
        	}
        }
        
        if (labels[data.getDestination().getId()].pere == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());
            
            
            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels[data.getDestination().getId()].pere;
            while (arc != null) {
                arcs.add(arc);
                arc = labels[arc.getOrigin().getId()].pere;
            }
            
            Collections.reverse(arcs);
            
            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));    
            
        }
        
        return solution;
    }

}
