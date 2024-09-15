package markbook.classes;
import java.io.*;
import java.util.*;

// used to keep te

public class Test {
	private int unit;
	private int paper;
	private int score;// score achieved on the test
	private int id;// student id of the student who wrote it
	private double raw;
	private String level;
	private File file;// grade boundary file(Tests.txt)
	private ArrayList<Integer> bounds = new ArrayList<Integer>();// grade boundaries
	
	public Test(int unit, int paper, int id, int rawScore, File file) {
		this.unit = unit;
		this.paper = paper;
		this.score = rawScore;
		this.file = file;
		this.id = id;
	}

	public void writeToFile() {// checks for duplicate tests, if found replaces them in the file. if not, appends itself to the file
		try {
			ArrayList<String> lines = new ArrayList<String>();
			Scanner in = new Scanner(new File("StudentScores.txt"));
			String key = String.format("%s,%s,%s", unit, paper, id);
			
			// read all lines in the file and only add the ones that aren't this exact test
			while (in.hasNextLine()) {
				String s = in.nextLine();
				if (!s.contains(key)) {
					lines.add(s);
				}
			}
			
			// add the new test to the arraylist
			lines.add(key+","+score);
			
			FileWriter write = new FileWriter(new File("StudentScores.txt"),false);
			
			// overwrite old file with new changes
			for (String s : lines) {
				write.write(s+"\n");
			}
			
			write.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	public String convert() throws IndexOutOfBoundsException {
		calcBounds(); // 
		
		if (score>bounds.get(0)) { // checks which level the score achieved falls in
			throw new IndexOutOfBoundsException ("too big buddy");
	    } else if (score>bounds.get(1)&&score<=bounds.get(0)) {
			level="7";
		} else if (score>bounds.get(2)) {
			level="6";
		} else if (score>bounds.get(3)) {
			level="5";
		} else if (score>bounds.get(4)) {
			level="4";
		} else if (score>bounds.get(5)) {
			level="3";
		} else if (score>bounds.get(6)) {
			level="2";
		} else {
			level="1";
		} 
			
		// determines whether there will be a +/-
		if (level!="1")
			level = boundary(bounds.get(7-Integer.parseInt(level)), bounds.get(8-Integer.parseInt(level)));
		else
			level = boundary(bounds.get(6), 0);
		
		// raw percentage
		this.raw = (double)score/bounds.get(0);
		return level;
	}
	
	public void calcBounds() { // gets the grade boundaries from the file.
		ArrayList<Integer> boundaries = new ArrayList<Integer>(7);
		try {
			
			Scanner reader = new Scanner(file);
			// reads lines in from file, checks if the unit and paper match, and if so, takes the bounds.
			while (reader.hasNextLine()) {
				String nextLine = reader.nextLine();
				if (Integer.parseInt(nextLine.split(",")[0])==unit&&Integer.parseInt(nextLine.split(",")[1])==paper) {
					for(String i : nextLine.split(",")[2].split(" ")) {
						boundaries.add(Integer.parseInt(i));
					}
					break;
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		this.bounds = boundaries;

	}
	
	private String boundary(int upper, int lower) { // determines whether there should be a +/-
		int range = upper-lower;
		int s = score-lower;
		
		if (s>(int)((range*2)/3)) {
			level=level+"+";
		} else if (s<=(int)(range/3)) {
			level=level+"-";
		} else {
			level=level+" ";
		}
		return level;
	}
	
	public Double getRaw() {
		return raw;
	}
	
	public String getLevel() {
		convert(); // makes sure that there will be a level to return.
		return level;
	}
	
	public int getId() {
		return id;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public int getPaper() {
		return paper;
	}
	
	public ArrayList<Integer> getBounds() {
		calcBounds();
		return bounds;
	}
	
	public void changeId(int newId) {
		this.id=newId;
	}
	
	public void changeScore(int newScore) {
		score = newScore;
		convert();
	}
	
	public void changeUnit(int newUnit) {
		unit = newUnit;
		convert();
	}
	
	public void changePaper(int newPaper) {
		paper = newPaper;
		convert();
	}
	
	public int compareTo(Test other, boolean unit) {
		if (unit)
			if (this.unit>other.unit) 
				return 1;
			else if (this.unit<other.unit) 
				return -1;
			else 
				if (this.paper>other.paper)
					return 1;
				else if (this.paper<other.paper)
					return -1;
				else
					return 0;
		else
			if (this.id>other.id)
				return 1;
			else if (this.id<other.id)
				return -1;
			else
				return compareTo(other, true);
	}

	public String toString() {
		return unit+" "+paper+" "+id+" "+score;
	}
}
