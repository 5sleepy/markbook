package markbook.classes;

// stores markschemes for a test

public class Markscheme {
	private int unit; 
	private int paper; // paper type
	private int seven, six, five, four, three, two, one; // int values representing the upper bounds of a level
	private String[] arr; // array containing all the bounds
	
	public Markscheme(int unit, int paper, String[] arr) {
		this.unit = unit;
		this.paper = paper;
		seven = Integer.parseInt(arr[0]);
		six = Integer.parseInt(arr[1]);
		five = Integer.parseInt(arr[2]);
		four = Integer.parseInt(arr[3]);
		three = Integer.parseInt(arr[4]);
		two = Integer.parseInt(arr[5]);
		one = Integer.parseInt(arr[6]);
		this.arr = arr;
	}

	public int getUnit() {
		return unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public int getPaper() {
		return paper;
	}

	public void setPaper(int paper) {
		this.paper = paper;
	}

	public int getSeven() {
		return seven;
	}

	public void setSeven(int seven) {
		this.seven = seven;
		arr[0]=Integer.toString(seven);
	}

	public int getSix() {
		return six;
	}

	public void setSix(int six) {
		this.six = six;
		arr[1]=Integer.toString(six);
	}

	public int getFive() {
		return five;
	}

	public void setFive(int five) {
		this.five = five;
		arr[2]=Integer.toString(five);
	}

	public int getFour() {
		return four;
	}

	public void setFour(int four) {
		this.four = four;
		arr[3]=Integer.toString(four);
	}

	public int getThree() {
		return three;
	}

	public void setThree(int three) {
		this.three = three;
		arr[4]=Integer.toString(three);
	}

	public int getTwo() {
		return two;
	}

	public void setTwo(int two) {
		this.two = two;
		arr[5]=Integer.toString(two);
	}

	public int getOne() {
		return one;
	}

	public void setOne(int one) {
		this.one = one;
		arr[6]=Integer.toString(one);
	}
	
	public String getBounds() {
		return arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[3]+" "+arr[4]+" "+arr[5]+" "+arr[6];
	}
	
	public String[] getBounds2() {
		return arr;
	}
	
	public int compareTo(Markscheme other) {
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
	}
	
	public String toString() {
		return unit+" "+paper+" "+seven+" "+six+" "+five+" "+four+" "+three+" "+two+" "+one;
	}
}
