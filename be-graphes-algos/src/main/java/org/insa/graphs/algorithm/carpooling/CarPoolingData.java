package org.insa.graphs.algorithm.carpooling;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.model.Graph;

public class CarPoolingData extends AbstractInputData {
	
    protected CarPoolingData(Graph graph, ArcInspector arcFilter,ShortestPathSolution pathFirst,ShortestPathSolution pathSecond) {
        super(graph, arcFilter);
        
    }

    
}
