package MSP.InstanceGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class NetMakersGens {

	private String DataFile;
	private int NumArcs;
	private int LastNode;
	private int Source;
	private  int[][] Arcs;
	static int[][] weigths;
	static int NumNodes;
	int[] lower = {1,67,134,199,265,331,397,463,529,595};
	//int[] lower = {100,300,500,700,900,1100,1300,1500,1700,1900};
	//int[] lower = {1000,3000,5000,7000,9000,11000,13000,15000,17000,19000};
	int[] upper = {32,99,165,231,297,363,429,495,561,627};
	Random rr = new Random(235346);
	int obj = 0;
	public NetMakersGens(int objectives, String ini, String variation ) throws IOException {
		//int[] lower = {1,67,133,199,265,331,397,463,529,595};
		
		gen2(objectives, ini, variation);
		
	}	
	private void gen1(int objectives, String ini, String variation) throws IOException{
		
		obj = objectives;
		setUp(ini);
		Arcs = new int[NumArcs][2];
		weigths = new int[NumArcs][objectives];
		ReadC();
		ArrayList<Integer>  objs = new ArrayList<Integer>();
		for (int i = 0; i < objectives; i++) {
			objs.add(i);
		}
		for (int i = 0; i < NumArcs; i++) {
			if(i<NumNodes){
				hamiltonialArc(i, objectives, variation);
			}else{
				ArrayList<Integer> objCopy = (ArrayList<Integer>) objs.clone();
				for (int k = 0; k < objectives; k++) {
					int index = rr.nextInt(objCopy.size());
					int ob = objCopy.get(index);
					int weiths = calcWeiht(ob,  variation, objectives);
					//int weiths = calcWeiht(k,  variation, objectives);
					weigths[i][k] = weiths;
					objCopy.remove(index);
				}
			}
		}
		
		write(objectives, ini);
	}
	private void gen2(int objectives, String ini, String variation) throws IOException{
		
		obj = objectives;
		setUp(ini);
		Arcs = new int[NumArcs][2];
		weigths = new int[NumArcs][objectives];
		ReadC();
		ArrayList<Integer>  objs = new ArrayList<Integer>();
		for (int i = 0; i < 2; i++) {
			objs.add(i);
		}
		for (int i = 0; i < NumArcs; i++) {
			
			for (int k = 0; k < objectives; k += 2) {
				if (k + 1 == objectives && objectives % 2 == 1 ) {
					weigths[i][k] = rr.nextInt(99)+1;
				} else {
					ArrayList<Integer> objCopy = (ArrayList<Integer>) objs.clone();
					int index = rr.nextInt(objCopy.size());
					int ob = objCopy.get(index);
					int weiths = calcWeiht(ob, variation, objectives);
					// int weiths = calcWeiht(k, variation, objectives);
					weigths[i][k] = weiths;
					objCopy.remove(index);
					index = rr.nextInt(objCopy.size());
					ob = objCopy.get(index);
					weiths = calcWeiht(ob, variation, objectives);
					// int weiths = calcWeiht(k, variation, objectives);
					weigths[i][k+1] = weiths;
					objCopy.remove(index);
					
				}
			}
		}

		write(objectives, ini);
	}
	
	
	private int calcWeiht(int ob, String variation, int objectives) {
		// TODO Auto-generated method stub 
		if(variation.equals("a")){
			//return rr.nextInt(100) + 1;
			return  lower[ob] + rr.nextInt(33);
		}else if (variation.equals("b") ){
			return  1 + rr.nextInt(1000000);
		}
		else{
			return  1 + rr.nextInt(10);
			/*if(ob ==0){
				return  1 + rr.nextInt(255);
			}else if(ob==1){
				return  1 + rr.nextInt(50000);
			}else{
				return  1 + rr.nextInt(1000000);
			}*/
			
		}
	}

	private void write(int objectives, String ini) throws IOException {
		String in = ini.replaceAll(".ini", "");
		in = in.replaceAll("ini/", "");
		String path = "data/MSP_NetMaker/MSP_"+objectives+"_"+ in + "alternado.txt";
		writeIni(path,in+"alternado", NumArcs, NumNodes, Source, LastNode);
		
		File file = new File(path);
		PrintWriter bufRdr = new PrintWriter(file);
		String line = null;
		line = "sp min " + NumNodes + " " + NumArcs;line+="\n";
		bufRdr.write(line);
		line = Source + " " + 1;line+="\n";
		bufRdr.write(line);
		line = LastNode + " " + "-1";line+="\n";
		bufRdr.write(line);
		for (int i = 0; i < NumArcs; i++) {
			line = Arcs[i][0] + " " +   Arcs[i][1] ;
			for (int j = 0; j < objectives; j++) {
				line += " " + weigths[i][j];
			}
			line+="\n";
		//	System.out.println(line);
			bufRdr.write(line);
		}
		bufRdr.close();
	}

	private void writeIni(String path,String originalFile, int numArcs2, int numNodes2,	int source2, int lastNode2) throws FileNotFoundException {
		File file = new File("ini/MSP"+obj+ originalFile + ".ini");
		
		PrintWriter bufRdr = new PrintWriter(file);
		String line = "DataFile:"+path+System.getProperty("line.separator");
	//	bufRdr.write(line);
		line += "Number of Arcs:"+numArcs2+System.getProperty("line.separator");
		//bufRdr.write(line);
		line += "Number of Nodes:"+numNodes2+System.getProperty("line.separator");
		//bufRdr.write(line);
		line += "Source:"+source2+System.getProperty("line.separator");
		//bufRdr.write(line);
		line += "Last Node:"+lastNode2+System.getProperty("line.separator");
		bufRdr.write(line);
		
		bufRdr.close();

		
	}

	private void hamiltonialArc(int arc , int objectives, String variation) {
		if(variation.equals("a") ||variation.equals("b") ){
			for (int i = 0; i < objectives; i++) {
				weigths[arc][i] = rr.nextInt(10001) + 1 ;
			}
		}
		else{
			for (int i = 0; i < objectives; i++) {
				weigths[arc][i] = 10000;
			}
		}
		
	}

	public void ReadC() throws NumberFormatException, IOException {

		File file = new File(DataFile);

		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;

		String[] readed = new String[4];

		int row = 0;
		int col = 0;
		
	
		
		while ((line = bufRdr.readLine()) != null && row < NumArcs + 3) {
			StringTokenizer st = new StringTokenizer(line, " ");
			while (st.hasMoreTokens()) {
				// get next token and store it in the array
				readed[col] = st.nextToken();
				col++;
			}

			if (row >= 3) {
				Arcs[row - 3][0] = (Integer.parseInt(readed[0]));
				Arcs[row - 3][1] = (Integer.parseInt(readed[1]));
				//Distance[row - 3] = Integer.parseInt(readed[2]);
				//Time[row - 3] = Integer.parseInt(readed[3]);
				//Gd.addWeightedEdge( Gd.getVertexByID(Arcs[row - 3][0]), Gd.getVertexByID(Arcs[row - 3][1]),Distance[row - 3], Time[row - 3] , row-3);
			}

			col = 0;
			row++;

		}

	
	}
	
	
	public void setUp(String ConfigFile) throws IOException{
		
		File file = new File(ConfigFile);
		 
		BufferedReader bufRdr  = new BufferedReader(new FileReader(file));
		String line = null;
		
		String [][] readed = new String [5][2];
		
		int row = 0;
		int col = 0;
	 
		//read each line of text file
		while((line = bufRdr.readLine()) != null && row < 5)
		{	
		StringTokenizer st = new StringTokenizer(line,":");
		while (st.hasMoreTokens())
		{
			//get next token and store it in the array
			readed[row][col] = st.nextToken();
			col++;
			
		}
		col = 0;
		row++;
		
		}
				
		DataFile=readed[0][1];
		NumArcs=Integer.parseInt(readed[1][1]);
		NumNodes=Integer.parseInt(readed[2][1]);
		Source=Integer.parseInt(readed[3][1]);
		LastNode=Integer.parseInt(readed[4][1]);		 
		
		
		
		
	}
	
	public static void main(String[] args) {
		try {
			int cuantas = 0;
			for (int o = 3; o <=10; o++) {
				for (int i =20; i <= 20; i++) {
					for (int var = 0; var <= 0; var++) {
						String varr = null;
						if (var == 0) {
							varr = "a";
						} else if (var == 1) {
							varr = "b";
						} else {
							varr = "c";
						}
						
						String ini = "ini/NM"+i + varr+ ".ini";
						NetMakersGens nm = new NetMakersGens(o, ini, varr);
						nm.rr.setSeed(o+cuantas+i);
						cuantas++;
						System.out.println("van " + cuantas);
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
