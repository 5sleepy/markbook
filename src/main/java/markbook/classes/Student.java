package markbook.classes;
import java.io.*;
import java.util.*;

// stores student information

public class Student {
	
	private int id;
	private String name;
	private double raw; // raw percentage
	private String level; // level achieved accounting for weighting
	
	public Student(int id, String name) {
		this.id = id;
		this.name = name;
		
	}
	
	public void writeToFile() { // writes student id and name to Students.txt file
		try {
			File file = new File("Students.txt");
			Scanner reader = new Scanner(file);
			ArrayList<String> temp = new ArrayList<String>();
			int counter=0, index=0;
			boolean duplicate=false;
			
			// reads in all lines in the file
			while (reader.hasNextLine()) {
				String a = reader.nextLine();
				temp.add(a);
				if (a.split(",")[0].equals(Integer.toString(id))) { // identifies the index of a duplicate
					index=counter;
					duplicate=true;
				}
				counter++;
			}
			reader.close();
			
			// if there is a duplicate, replace it
			if (duplicate) {
				temp.set(index, id+","+name);
			} else {
				temp.add(id+","+name);
			}
				
			
			FileWriter writer = new FileWriter(file, false);
			
			// writes all information back into the file
			for (String i : temp) {
				writer.write(i+"\n");
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	public int getRaw() {
		return (int) (raw*100); // returns it as a whole number rather than a percent, display purposes only
	}
	
	public String getLevel() {
		return level;
	}
	
	public void calcAll(ArrayList<String> lines) { // calculates the level and raw given the arraylist of lines contained in StudentScores.txt
		ArrayList<String> values = binSearch(id, lines); // searches through the lines arraylist and returns only an arraylist with tests written by this student number.
		int cF=0, cT=0; // counters
		double fifty=0, twenty=0, raw = 0;
		// fifty and twenty represent how much the papers 1/2 and papers 3/other take up out of 70. Did not have access to full 100 weighting, so it's accounted for later.
				
		if (!values.isEmpty()) {
			for (String i : values) {
				String[] arr = i.split(",");
				
				// uses Test class to calculate a level based off of achieved test score
				Test test = new Test(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), new File("Tests.txt"));
				String level = test.convert();
				LevelToValue conv = new LevelToValue(level); 
				raw += test.getRaw();
				
				// finds the type of test and increments the right counters and sums correctly
				if (arr[1].equals("1")||arr[1].equals("2")) {
					cF++;
					fifty += conv.convert();
				} else {
					cT++;
					twenty += conv.convert();
				}
			}
		}
		
		double a=0.0;
		
		// calculating weighted average due to the possibility of not having written a p1/2 or p3/other
		if (cF==0) {
			a=(double)twenty/cT;
		} else if (cT==0) {
			a=(double)fifty/cF;
		} else {
			a = (double)((fifty/cF)*0.5 + (twenty/cT)*0.2)*(10/7); // calculating weighted average
		}
		double r = Math.floor((a-Math.floor(a))*10), i = Math.floor(a); // r is the decimal portion used for calculating whether it's a +/-. eg. level 6+, level 4-, etc.

		// determining whether the level is a +, -, or nothing.
		if (r>=6) {
			this.level = (int)i+"+";
		} else if (r<3) {
			this.level = (int)i+"-";
		} else {
			this.level = (int)i+" ";
		}
		// calculating raw average.
		if (!values.isEmpty()) {
			this.raw = raw/values.size();
		} else {
			this.raw=0;
		}
	}

	private ArrayList<String> binSearch(int value, ArrayList<String> lines) {
		int l=0, r=lines.size()-1, m;
		// binary search algorithm, uses an linear search at the end because all the terms of the same student number will be together.
		while (l<=r) {
			m = l+(r-l)/2;
			int n = Integer.parseInt(lines.get(m).split(",")[2]);

			if (n==value) {
				ArrayList<String> a=linSearch(value,m,lines);
				return a;
			} else if (n<value) {
				l=m+1;
			} else {
				r=m-1;
			}
			
		}
		return new ArrayList<String>();
	}
	
	private ArrayList<String> linSearch(int value, int index, ArrayList<String> bounds) {
		// finds all the terms with the same student number given that all the terms of the same number will be next to each other
		ArrayList<String> lines = new ArrayList<String>();
		lines.add(bounds.get(index));
		int counter = 1, len = bounds.size();
		while (index+counter<len&&Integer.parseInt(bounds.get(index+counter).split(",")[2])==value) {
			lines.add(bounds.get(index+counter));
			counter++;
		}
		counter = 1;
		while (index-counter>-1&&Integer.parseInt(bounds.get(index-counter).split(",")[2])==value) {
			lines.add(bounds.get(index-counter));
			counter++;
		}
		
		return lines;
	}
	
	public int compareTo(Student other, boolean alpha) {
		if (alpha) {
			return this.name.compareTo(other.name);
		} else {
			if (this.id<other.id) {
				return -1;
			} else if (this.id>other.id) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	public String toString() {
		return name+" "+id+" "+level;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String s) {
		this.name=s;
	}
	
	public void setId(int i, ArrayList<String> lines) {
		this.id=i;
		// recalculating all values because id changed.
		calcAll(lines);
	}
}
