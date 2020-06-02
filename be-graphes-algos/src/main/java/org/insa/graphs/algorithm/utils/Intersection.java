package org.insa.graphs.algorithm.utils;

public class Intersection implements Comparable<Intersection> {
	
	private int nodeId;
	private double costIntersection; // qui est la somme des temps de trajet  
	private boolean visited;
	public Intersection(int nodeId,double costIntersection) {
		this.nodeId = nodeId;
		this.costIntersection = costIntersection;
		this.visited = false;
	}
	
	public void setVisited() {
		this.visited = true;
	}
	public boolean isVisited() {
		return this.visited;
	}
	public double getCostIntersection() {
		return this.costIntersection;
	}
	
	public int getId() {
		return this.nodeId;
	}
	
	@Override
	public int compareTo(Intersection autre) {
		// TODO Auto-generated method stub
		return Double.compare(this.getCostIntersection(), autre.getCostIntersection());
	}
	@Override
	public String toString() {
		return "Intersection => (Node Id ="+this.nodeId + ",Cout(en temps) Intesection="+this.costIntersection+")";
	}
}
