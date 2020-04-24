package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;


public class Label implements Comparable<Label>{
	/**
	 *	Node number
	 **/
	private int currentNode;
	/**
	 *	boolean to assert if the cost of the current Node is known or not by 
	 **/
	private boolean marqued;
	/**
	 *	cost of the current Node from the Head of the Graph 
	 **/
	private double cost;
	
	private boolean alreadySeen;
	private int father;
	/*
	 * create all Label from the graph
	 *  a la postion i on a le bale i
	 */
	public static ArrayList<Label> createAllLabelFromGraph(Graph graph) {
		ArrayList<Label> labels = new ArrayList<Label>();
		for(Node node: graph.getNodes()) {
			labels.add(new Label(node));
		}
		return labels;
	}
	

	public Label(Node node) {
		this.currentNode = node.getId();
		this.marqued = false;
		this.alreadySeen = false;
		this.cost = Double.POSITIVE_INFINITY;
		this.father = -1;
	}
	public int getNodeId() {
		return this.currentNode;
	}
	public boolean isMarqued() {
		return this.marqued;
	}
	public boolean isAlreadySeen() {
		return this.alreadySeen;
	}
	public double getCost() {
		return this.cost;
	}
	public double getTotalCost() {
		return this.cost;
	}
	public void setMarqued() {
		this.marqued = true;
	}
	public void setCost(double newcost) {
		this.cost = newcost;
	}
	public void setAlreadySeen() {
		this.alreadySeen = true;
	}
	public void setFather(int numFather) {
		this.father = numFather;
	}

	
	@Override
	public int compareTo(Label other) {
		// TODO Auto-generated method stub
		return Double.compare(this.getTotalCost(), other.getTotalCost());
	}
	
	

	
	
}
