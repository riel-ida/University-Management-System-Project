package bgu.spl.a2.sim.privateStates;

import bgu.spl.a2.PrivateState;

import java.util.ArrayList;
import java.util.List;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		regStudents = new ArrayList<>();
		prequisites = new ArrayList<>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public void addRegStudent(String name) {
		regStudents.add(name);
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setAvailableSpots(int newAvailableSpots){
		availableSpots = newAvailableSpots;
	}

	public void setRegistered(int newRegistered){
		registered = newRegistered;
	}

}
