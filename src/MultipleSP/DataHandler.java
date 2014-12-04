package MultipleSP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.StringTokenizer;

public class DataHandler {
	
	/***
	 * Number of labels per node for dominance
	 */
	public static final int numLabels = 10;

	/**
	 * Constant that stays the number of attributes per arc
	 */
	public static int num_attributes;
	
	String CvsInput;
	int NumArcs;
	int LastNode;
	int Source;
	
	//Tail and head of each arc
	static int[][] Arcs;
	static int NumNodes;
	
	//Attributes for each arc
	static int[][] atributes;
	private PulseGraph Gd;
	static Random r = new Random(0);
	private int seed;
	
	public DataHandler(Settings Instance, int n_attributess ) {
		num_attributes = n_attributess;
		CvsInput = Instance.DataFile;
		NumArcs = Instance.NumArcs;
		NumNodes = Instance.NumNodes;
		LastNode = Instance.sink;
		Source = Instance.source;
		seed = Instance.seed;//System.out.println(seed+"sedd");
		Arcs = new int[Instance.NumArcs][2];
		atributes = new int[Instance.NumArcs][num_attributes];
		Gd = new PulseGraph(NumNodes);
	}

	/**
	 * This reader must be adapted according to the instan file
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void ReadC() throws NumberFormatException, IOException {
		Random weithgen = new Random(seed);
		File file = new File(CvsInput);

		BufferedReader bufRdr = new BufferedReader(new FileReader(file));
		String line = null;

		String[] readed = new String[50];

		int row = 0;
		int col = 0;
		
		upLoadNodes();
		int fileStep = 1;
		while ((line = bufRdr.readLine()) != null && row < NumArcs + fileStep) {
			StringTokenizer st = new StringTokenizer(line, " ");
			while (st.hasMoreTokens()) {
				// get next token and store it in the array
				readed[col] = st.nextToken();
				col++;
			}

			if (row >= fileStep) {
				Arcs[row - fileStep][0] = (Integer.parseInt(readed[0]) - 1);
				Arcs[row - fileStep][1] = (Integer.parseInt(readed[1]) - 1);
					
				int[] atris = new int[num_attributes];
				for (int i = 0; i < num_attributes; i++) {
					atris[i] = Integer.parseInt(readed[2 + i]);
					atributes[row - fileStep][i] = atris[i];
				}
				
				Gd.addWeightedEdge( Gd.getVertexByID(Arcs[row - fileStep][0]), Gd.getVertexByID(Arcs[row - fileStep][1]),atris , row-fileStep);
			}

			col = 0;
			row++;

		}

	
	}
	public void upLoadNodes(){
		for (int i = 0; i < NumNodes; i++) {
			if(i!=(LastNode-1)){
				Gd.addVertex(new VertexPulse(i) );
			}
		}
		FinalVertexPulse vv = new FinalVertexPulse(LastNode-1, Gd);
		Gd.addFinalVertex(vv);
	}
	
	public PulseGraph getGd()
	{
		return Gd;
	}
	
}

