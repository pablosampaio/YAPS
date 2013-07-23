package yaps.metrics.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This class is a list of visits in a simulation. It is used to calculate metrics of 
 * the simulation (to measure how "efficiently" the nodes were visited). 
 * <br><br>
 * The visits must be inserted ordered by time.
 * 
 * @author Pablo A. Sampaio
 */
public class VisitsList {
	private List<Visit> visitList;
	private long lastTime;
		
	public VisitsList() {
		this.visitList = new LinkedList<Visit>();
		this.lastTime = 0;
	}

	public VisitsList(List<Visit> visits) {
		this.visitList = new ArrayList<Visit>(visits);
		this.lastTime = visits.get(visits.size() - 1).time;
	}
	
	public void addVisit(Visit visit) {
		if (visit.time < this.lastTime) {
			throw new IllegalArgumentException("Visit inserted in wrong order!");
		}
		visitList.add(visit);
		this.lastTime = visit.time;
	}
	
	public void addVisit(int time, int agent, int node) {
		addVisit(new Visit(time, agent, node));
	}

	public void addVisit(int time, int node) {
		visitList.add(new Visit(time, -1, node));
	}
	
	public int getNumVisits() {
		return visitList.size();
	}
	
	public Visit getVisit(int index) {
		return visitList.get(index);
	}

	public VisitsList filterByAgent(int agent) {
		List<Visit> filteredVisits = new LinkedList<Visit>();
		
		for (Visit visit : visitList) {
			if (visit.agent == agent) {
				filteredVisits.add(visit);
			}
		}
		
		return new VisitsList(filteredVisits);
	}

	public VisitsList filterByVertex(int vertex) {
		List<Visit> filteredVisits = new LinkedList<Visit>();
		
		for (Visit visit : visitList) {
			if (visit.node == vertex) {
				filteredVisits.add(visit);
			}
		}
		
		return new VisitsList(filteredVisits);
	}
	
	// inclusive limits (closed interval)
	public VisitsList filterByTime(int from, int to) {
		List<Visit> filteredVisits = new LinkedList<Visit>();
		
		for (Visit visit : visitList) {
			if (visit.time >= from && visit.time <= to) {
				filteredVisits.add(visit);
			}
		}
		
		return new VisitsList(filteredVisits);
	}

	// parameter 'from' is an inclusive limit (closed interval)
	public VisitsList filterByTime(int from) {
		List<Visit> filteredVisits = new LinkedList<Visit>();
		
		for (Visit visit : visitList) {
			if (visit.time >= from) {
				filteredVisits.add(visit);
			}
		}
		
		return new VisitsList(filteredVisits);
	}


}
