package MSP.InstanceGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

import com.sun.java.browser.net.ProxyServiceProvider;

public class RasterBaseGen {

	private String[] fileNames;
	private int[] reference;
	private Raster[] rasters;
	private int disScale = 1;
	private ArrayList<int[]>arcsAtributes  =   new ArrayList<int[]>();
	private ArrayList<int[]>arcsNodes  =   new ArrayList<int[]>();
	private Hashtable<String, Integer> gridToNodesMap = new Hashtable<String, Integer>();
	private Hashtable<Integer,String> nodesToGridMap = new Hashtable<Integer,String>(); 
	private ArrayList<Integer> nodesIntegers = new ArrayList<Integer>();
	
	static int NumNodes;
	double walkSpeed = 5.0*1000/60.0;
	double maxSpeed = 20.0*1000/60.0;
	int minCols = 999999;
	int minRows = 999999;
	int cellSize = 100;
	private int[][] existeNODO;
	private double culturalBuff= 500;
	private int objetivos = 5;
	String ruta = "data/MSPtxts/Raster200/";
	public void ReadF() throws NumberFormatException, IOException {
		
		File file = new File(ruta+"FILES.txt");
		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		
		String line = bufRdr.readLine();
		String[] readed = null;
		
		readed = line.split(" ");
		int nFiles = Integer.parseInt(readed[1]);
		fileNames = new String[nFiles];
		rasters = new Raster[nFiles];
		reference = new int[nFiles];
		for (int i = 0; i < nFiles; i++) {
			line = bufRdr.readLine();
			readed = line.split(" ");
			fileNames[i] = ruta+readed[0];
			reference[i] = Integer.parseInt(readed[1]);
		}
		
	}
	
	public void genLayers() throws IOException{
		String line = null;
		StringTokenizer st = null;
		String[] readed = null;
		int token = 0;
		File file = null;
		BufferedReader bufRdr = null;
		for (int k = 0; k < fileNames.length; k++) {
			int cols =-1;
			int rows =-1;
			double  xcor =-1;
			double ycor =-1;
			int cellsize = -1;
			double nodata= -1;
			//Cols
			file = new File(fileNames[k]);
			bufRdr = new BufferedReader(new FileReader(file));
			token = 0;
			line = bufRdr.readLine();
			st = new StringTokenizer(line, " ");
			readed = new String[st.countTokens()];
			while (st.hasMoreTokens()) {
				readed[token]=st.nextToken();
				token++;
			}
			cols =Integer.parseInt(readed[1]);
			if(cols<minCols){
				minCols=cols;
			}
			//Rows
			token = 0;
			line = bufRdr.readLine();
			st = new StringTokenizer(line, " ");
			readed = new String[st.countTokens()];
			while (st.hasMoreTokens()) {
				readed[token]=st.nextToken();
				token++;
			}
			rows =Integer.parseInt(readed[1]);
			if(rows<minRows){
				minRows=rows;
			}
			//xcor and y 
			
			line = bufRdr.readLine();
			line = bufRdr.readLine();
			//cell size
			token = 0;
			line = bufRdr.readLine();
			st = new StringTokenizer(line, " ");
			readed = new String[st.countTokens()];
			while (st.hasMoreTokens()) {
				readed[token]=st.nextToken();
				token++;
			}
			cellsize =Integer.parseInt(readed[1]);
			this.cellSize = cellsize;
			//nodata
			token = 0;
			line = bufRdr.readLine();
			st = new StringTokenizer(line, " ");
			readed = new String[st.countTokens()];
			while (st.hasMoreTokens()) {
				readed[token]=st.nextToken();
				token++;
			}
			nodata =Integer.parseInt(readed[1]);
			
			rasters[k] = new Raster(rows, cols);
			rasters[k].noData =  nodata;
			rasters[k].cellsize=  cellsize;
			rasters[k].name = fileNames[k];
			System.out.println("Comenzando a leer raster " + rasters[k].name);
			for (int i = 0; i < rows; i++) {
				line = bufRdr.readLine();
				st = new StringTokenizer(line, " " );
				int cuantos = st.countTokens();
				for (int j = 0; j < cuantos; j++) {
					String value = st.nextToken();
					value = value.replaceAll(",", ".");
					rasters[k].rasterVals[i][j] = Double.parseDouble(value);
				}
			}
		}
	}

	private void genNetwork() {
		existeNODO = new int[minRows][minCols];
		
		//int[] delta_i = {-1,-1,-1,0,1,1,1,0};
		//int[] delta_j = {-1,0,1,1,1,0,-1,-1};
		int[] delta_i = {-1,0,1,0};
		int[] delta_j = {0,1,0,-1};
		for (int j = 0; j < minCols; j++) {
			for (int i = 0; i < minRows; i++) {
				//estoy en la celda i,j
				
				if(nodeExists(i, j)){//Empiezo a hacer todos los arcos de salida desde i,j
					
					for (int l = 0; l < delta_i.length; l++) {
						int head_i = i + delta_i[l];
						int head_j = j + delta_j[l];
						boolean headExists = nodeExists(head_i, head_j);
						if(headExists){
							int distance = genDistance(i, j, head_i, head_j);
							int time = genTime(i, j, head_i, head_j,distance);
							int cicloCost = genCicloviaCost(i, j, head_i,head_j);
							int culturalCost = genCulturalCos(i, j, head_i,head_j);
							int contCost = genContamincost(i, j, head_i, head_j);
							int[] costs = { distance,time, cicloCost, culturalCost, contCost  };
							arcsAtributes.add(costs);
							String key = i + "," + j;
							int tail = gridToNodesMap.get(key);
							key = head_i + "," + head_j;
							int head = gridToNodesMap.get(key);
							int[] head_tail = { tail, head };
							arcsNodes.add(head_tail);
						}
					}
				}
			}
		}

	}
	
	
	
	
	public boolean nodeExists(int i, int j) {
		if (i > 0 && i < minRows && j > 0 && j < minCols) {

			if (existeNODO[i][j] == 1) {
				return true;
			}
			boolean breakFor = false;
			for (int k = 0; k < fileNames.length && !breakFor; k++) {
				if (reference[k] == 1 && rasters[k].rasterVals[i][j] != rasters[k].noData) {
					breakFor = true;
					existeNODO[i][j] = 1;
					String hasing = i + "," + j;
					if(gridToNodesMap.containsKey(hasing)){
						System.err.println("Error, ya existia "+ hasing);
					}
					int nodeID = nodesIntegers.size()+1;
					gridToNodesMap.put(hasing, nodeID);
					nodesToGridMap.put(nodeID, hasing);
					nodesIntegers.add(nodeID);
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	
	private int genContamincost(int i, int j, int i2, int j2) {

		double contij = rasters[8].rasterVals[i][j];
		double contij2 = rasters[8].rasterVals[i2][j2];
		if (contij != rasters[8].noData && contij2 != rasters[8].noData) {
			double average = (contij + contij2) / 2.0;
			int returnable = (int) average;
			return returnable;
		} else {
			return 10000;
		}
	}

	private int genTime(int i, int j, int i2, int j2, int distance) {
		double realDist = (distance+0.0)/(disScale+0.0);
		double ijHigh = rasters[7].rasterVals[i][j];
		double ij2High = rasters[7].rasterVals[i2][j2];
		double deltaHeight = ijHigh-ij2High;
		double atribute = Math.abs(deltaHeight) / realDist;
		double deegre = Math.toDegrees(Math.abs(Math.atan(atribute)));
		
		double speed =166.66;		
		if (deltaHeight>0) {
			speed = speed*Math.pow((1-0.05),deegre);
			speed = Math.max(speed, walkSpeed );
		}else if (deltaHeight<0){
			speed = speed*Math.pow((1+0.07),deegre);
			speed = Math.min(speed, maxSpeed);
		}
		double time = realDist/speed;
		int returnable = (int)Math.floor(time*100);
		return returnable;
	}

	private int genCulturalCos(int i, int j, int i2, int j2) {
		double minDist = 999999999;
		double score = 1;
		for (int k = 3; k <= 6; k++) {
			if (rasters[k].rasterVals[i][j] != rasters[k].noData) {
				if (rasters[k].rasterVals[i2][j2] != rasters[k].noData) {
					double distIJ = Math.min(culturalBuff,rasters[k].rasterVals[i][j]) / culturalBuff;
					double distIJ2 = Math.min(culturalBuff,rasters[k].rasterVals[i2][j2]) / culturalBuff;
					
					double average = (distIJ + distIJ2) / 2.0;
					score *= average;
				}
			}
		}
		
		return (int)Math.floor(score*100);
		
		
	}

	private int genCicloviaCost(int i, int j,int i2, int j2) {
		//Main road = 0
		//Cicloruta = 1
		//Ciclovia = 2
		
		int cost = -1;
		if(rasters[2].rasterVals[i][j]!=rasters[2].noData ){//Cicloruta
			if(rasters[2].rasterVals[i2][j2]!=rasters[2].noData){
				cost  = 1; //Ciclovia - Ciclovia
			}else if(rasters[1].rasterVals[i2][j2]!=rasters[1].noData){
				cost  = 3; //Ciclovia - Cicloruta
			}
			else if(rasters[0].rasterVals[i2][j2]!=rasters[0].noData){
				cost  = 8; //Ciclovia - Main road
			}else{
				System.err.println("Bicycle path connectivity cost.. Ciclovia");
			}
		}
		else if(rasters[1].rasterVals[i][j]!=rasters[1].noData ){//Cicloruta
			if(rasters[1].rasterVals[i2][j2]!=rasters[1].noData){
				cost  = 4; //Cicloruta - Cicloruta
			}else if(rasters[2].rasterVals[i2][j2]!=rasters[2].noData){
				cost  = 2; //Cicloruta - Ciclovia
			}
			else if(rasters[0].rasterVals[i2][j2]!=rasters[0].noData){
				cost  = 9; //Cicloruta- Main road
			}else{
				System.err.println("Bicycle path connectivity cost.. Cicloruta");
			}
		}
		else if(rasters[0].rasterVals[i][j]!=rasters[0].noData ){//Cicloruta
			if(rasters[1].rasterVals[i2][j2]!=rasters[1].noData){
				cost  = 6; //Main road - Cicloruta
			}else if(rasters[2].rasterVals[i2][j2]!=rasters[2].noData){
				cost  = 7; //Main road - Ciclovia
			}
			else if(rasters[0].rasterVals[i2][j2]!=rasters[0].noData){
				cost  = 10; //Main road- Main road
			}else{
				System.err.println("Bicycle path connectivity cost.. Cicloruta");
			}
		}
		else{
			System.err.println("Bicycle conc problem");
		}
	
		return cost;
	}

	private int genDistance(int i, int j, int head_i, int head_j) {
		double squares = Math.pow(i-head_i, 2) + Math.pow(j-head_j, 2); 
		int disUnitario = (int)Math.floor(disScale*Math.sqrt(squares))*cellSize;
		return disUnitario;
	}

	
	public static void main(String[] args) {
		RasterBaseGen rbg = new RasterBaseGen();
		try {
			rbg.ReadF();
			rbg.genLayers();
			rbg.genNetwork();

			rbg.report();
			rbg.printToFile();

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void printToFile() throws FileNotFoundException {
		File file = new File("data/MSPtxts/Raster200/MSP_5_bogotaRAS200.txt");
		PrintWriter bufRdr = new PrintWriter(file);
		String line = null;
		int NumArcs = arcsAtributes.size(); 
		NumNodes = nodesIntegers.size();
		line = "sp min " +  NumNodes + " " + NumArcs;
		line+="\n";
		bufRdr.write(line);
		Random r = new Random();
		int Source = 1+ r.nextInt(NumNodes);
		int  LastNode = 1+ r.nextInt(NumNodes);
		line = Source + " " + 1;line+="\n";
		bufRdr.write(line);
		line = LastNode + " " + "-1";line+="\n";
		bufRdr.write(line);
		for (int i = 0; i < NumArcs; i++) {
			line = arcsNodes.get(i)[0] + " " +   arcsNodes.get(i)[1];
			for (int j = 0; j < objetivos ; j++) {
				line += " " + arcsAtributes.get(i)[j];
			}
			line+="\n";
		//	System.out.println(line);
			bufRdr.write(line);
		}
		bufRdr.close();
		printNodeMap();
	}
	private void printNodeMap() throws FileNotFoundException {
		File file = new File("data/MSPtxts/Raster200/bogotaRAS200Map.txt");
		PrintWriter bufRdr = new PrintWriter(file);
		String line = null;
		
		for (int i = 0; i < NumNodes; i++) {
			line = nodesIntegers.get(i) + " " + nodesToGridMap.get(nodesIntegers.get(i));
			line+="\n";
		
			bufRdr.write(line);
		}
		bufRdr.close();
	}
	private void report() {
		System.out.println("arcos: " + arcsAtributes.size());
		System.out.println("nodos: " + nodesIntegers.size());
	}

	
}
