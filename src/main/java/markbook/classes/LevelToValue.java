package markbook.classes;

// used to convert between a level to a decimal value

public class LevelToValue {
	public String level;
	public double value;
	
	public LevelToValue(String level) {
		this.level = level;
	}
	
	public double convert() {
		
		double temp = Double.parseDouble(level.substring(0,1));
		if (level.charAt(1)=='-') {
		} else if (level.charAt(1)=='+') {
			temp+=(double)2/3;
		} else {
			temp+=(double)1/3;
		}
		return temp;
	}
}
