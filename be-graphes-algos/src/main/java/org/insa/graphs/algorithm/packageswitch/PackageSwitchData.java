package org.insa.graphs.algorithm.packageswitch;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.PerimetreRecherche;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Point;

public class PackageSwitchData extends AbstractInputData {
	private final Node originA, originB, destinationA,destinationB;
    public PackageSwitchData(Graph graph, ArcInspector arcFilter,Node originA,Node originB,Node destinationA,Node destinationB) {
        super(graph, arcFilter);
        this.originA = originA;
        this.originB = originB;
        this.destinationA = destinationA;
        this.destinationB = destinationB;
    }
  
    public Node getOriginA() {
    	return this.originA;
    }
    public Node getOriginB() {
    	return this.originB;
    }
    public Node getDestinationA() {
    	return this.destinationA;
    }
    public Node getDestinationB() {
    	return this.destinationB;
    }
    
    public ArcInspector getArcInspector() {
    	return this.arcInspector;
    }
    
    
}
