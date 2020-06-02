package org.insa.graphs.algorithm.packageswitch;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.AbstractInputData.Mode;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.algorithm.utils.Intersection;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class PackageSwitchSolution extends AbstractSolution {
	/* Intersection des deux robots
	 * */
	private final Intersection intersection;
	private  Path newPathFirstRobot; // Les nouveau chemin qui s'interceptent
	private  Path newPathSecondRobot;	
	protected PackageSwitchSolution(PackageSwitchData data, Status status,Intersection intersection) {
        super(data, status);
        this.intersection = intersection;
        this.newPathFirstRobot = null;
        this.newPathSecondRobot = null;
    }
	
    protected PackageSwitchSolution(PackageSwitchData data, Status status) {
        super(data, status);
        this.intersection = null;
        this.newPathFirstRobot = null;
        this.newPathSecondRobot = null;
    }

    
    public Intersection getIntersection() {
    	return this.intersection;
    }
    
    public void setNewPathRobot(int robot,Path path) {
    	if(robot==1) {
    		this.newPathFirstRobot = path;
    	}else {
    		this.newPathSecondRobot = path;
    	}
    }
    
    
    public PackageSwitchData getInputData() {
    	return (PackageSwitchData) super.getInputData();
    }
    public Path getNewPathRobot(int robot) {
    	if(robot==1) {
    		return this.newPathFirstRobot;
    	}else {
    		return this.newPathSecondRobot;
    	}
    }
  
    @Override
    public String toString() {
    	String info = null;
        if (!isFeasible()) {
            info = String.format("No Intersection found from node #%d to node #%d and from node #%d to node #%d ",
                    getInputData().getOriginA().getId(), getInputData().getDestinationB().getId(),getInputData().getOriginB().getId(),getInputData().getDestinationB().getId());
        }
        else {
            double cost = 0;
            
            info = String.format("Found an Intersection In node#%d ",
                  this.intersection.getId());
            //info += String.format("New TravelTime for first Robot time=#%d \n", this.newPathFirstRobot.getMinimumTravelTime());
            //info += String.format("New TravelTime for Second Robot time=#%d \n", this.newPathSecondRobot.getMinimumTravelTime());
            
        }
        info += "Resolution in " + getSolvingTime().getSeconds() + " seconds.";
    	return info;
    }
}
