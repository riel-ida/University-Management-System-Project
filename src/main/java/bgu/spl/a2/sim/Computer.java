package bgu.spl.a2.sim;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Computer {

	String computerType;
	long failSig;
	long successSig;

	public Computer(String computerType) {
		this.computerType = computerType;
	}

	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 *                       courses that should be pass
	 * @param coursesGrades
	 *                       courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		boolean pass = true;
		for(int i=0;i<courses.size() && pass; i++){
			String course = courses.get(i);
			Integer grade = coursesGrades.get(course);
			if(grade != null && grade < 57)
				pass = false;
			if(!coursesGrades.containsKey(course))
				pass = false;
		}
		if(pass)
			return successSig;
		else
			return failSig;
	}
}
