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

public class GridGens {

	private String DataFile;
	private int NumArcs;
	private int LastNode;
	private int Source;
	private  int[][] Arcs;
	static int[][] weigths;
	static int NumNodes;
	//int[] lower = {1,67,134,199,265,331,397,463,529,595};
	int[] lower = {100,300,500,700,900,1100,1300,1500,1700,1900};
	int[] upper = {32,99,165,231,297,363,429,495,561,627};
	Random rr = new Random(235346);
	int obj = 0;
	public GridGens(int objectives, String ini ) throws IOException {
		//int[] lower = {1,67,133,199,265,331,397,463,529,595};
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
			for (int k = 0; k < objectives; k++) {
				int weiths = calcWeiht();
				// int weiths = calcWeiht(k, variation, objectives);
				weigths[i][k] = weiths;
			}
		}
		
		write(objectives, ini);
		
		
	}	
	
	private int calcWeiht() {
		
			return  1 + rr.nextInt(10);
	
	}

	private void write(int objectives, String ini) throws IOException {
		String in = ini.replaceAll(".ini", "");
		in = in.replaceAll("ini/", "");
		String path = "data/MSP_Grids/MSP_"+objectives+"_"+ in + ".txt";
		writeIni(path,in, NumArcs, NumNodes, Source, LastNode);
		
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
		System.out.println(file.getName());
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
			for (int o = 3; o <= 3; o++) {
				for (int i = 1; i <= 33; i++) {
					
						String ini = "ini/G"+i +".ini";
						GridGens nm = new GridGens(o, ini);
						cuantas++;
						System.out.println("van " + cuantas);
					
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
