package MSP.InstanceGenerator;

public class Raster {

	public String name;
	public double[][] rasterVals;
	public int rows;
	public int cols;
	public double xcornner1;
	public double ycornner1;
	public double noData;
	public double cellsize;
	
	public Raster(int nr, int nc) {
		rows = nr;
		cols = nc;
		rasterVals = new double[rows][cols];
	}
	
	
	
}
