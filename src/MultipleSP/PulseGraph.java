package MultipleSP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

public class PulseGraph  implements Graph<VertexPulse, EdgePulse> {

	private static  int[] C;

	static VertexPulse[] vertexes;
	
	/*static int estMT=0;
	static int estMD=0;
	static int estBound=0;
	static int estLABELS=0;
	static String megaString;
	*/
	
	private int numNodes;
	private int Cd;
	private int Ct;
	
	/**
	 * Pulse Stuff
	 */
	public static int[][] nadir;
	static int MaxTime;
	static int MaxDist;
	static int MinTime;
	static int MinDist;
	
	static ArrayList<ArrayList<Integer>> Paths;
	static ArrayList<Integer>[] PathObj;
	
	
	
	
	public PulseGraph( int numNodes) {
		super();
		C = new int[DataHandler.num_attributes];
		this.numNodes = numNodes;
		//nodeList = new Hashtable<Integer, VertexPulse>(numNodes);
		Cd=0;
		Ct=0;
		vertexes = new VertexPulse[numNodes];
		Paths= new ArrayList<ArrayList<Integer>>();
		PathObj= new ArrayList[DataHandler.num_attributes];
		for (int i = 0; i <DataHandler.num_attributes; i++) {
			PathObj[i] = new ArrayList<Integer>();
		}
		
	}
	@Override
	public EdgePulse addEdge(VertexPulse sourceVertex, VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public  int getNumNodes()
	{
		return numNodes;
	}
	public VertexPulse getVertexByID(int id){
		return vertexes[id];
	}
	
	public EdgePulse addWeightedEdge(VertexPulse sourceVertex, VertexPulse targetVertex,  int[] atris , int id) {
		for (int i = 0; i < DataHandler.num_attributes; i++) {
			if(atris[i]>C[i]){
				C[i]=atris[i];
			}
		}
		
		
		vertexes[targetVertex.getID()].addReversedEdge(new EdgePulse(targetVertex , sourceVertex, id, atris));
		vertexes[sourceVertex.getID()].magicIndex.add(id);
		return null;
	}
	
	
	@Override
	public boolean addEdge(VertexPulse sourceVertex, VertexPulse targetVertex, EdgePulse e) {
		return false;
	}

	@Override
	public boolean addVertex(VertexPulse v) {
		vertexes[v.getID()] = v;
		return true;
	}
	public boolean addFinalVertex(FinalVertexPulse v) {
		vertexes[v.getID()] = v;
		return true;
	}

	@Override
	public boolean containsEdge(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsEdge(EdgePulse e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsVertex(VertexPulse v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<EdgePulse> edgeSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EdgePulse> edgesOf(VertexPulse vertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<EdgePulse> getAllEdges(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdgePulse getEdge(VertexPulse sourceVertex, VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdgeFactory<VertexPulse, EdgePulse> getEdgeFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VertexPulse getEdgeSource(EdgePulse e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VertexPulse getEdgeTarget(EdgePulse e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getEdgeWeight(EdgePulse e) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean removeAllEdges(Collection<? extends EdgePulse> edges) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<EdgePulse> removeAllEdges(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAllVertices(Collection<? extends VertexPulse> vertices) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EdgePulse removeEdge(VertexPulse sourceVertex,
			VertexPulse targetVertex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeEdge(EdgePulse e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeVertex(VertexPulse v) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<VertexPulse> vertexSet() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public int getCd()
	{
		return Cd;
	}
	public int getCt()
	{
		return Ct;
	}
	
	public void resetNetwork(){
		for (int i = 0; i < numNodes ; i++) {
			vertexes[i].reset();
		}
	}
	
	public ArrayList<ArrayList<Integer>> getPaths()
	{
		return Paths;
	}
	
	
	public void addCorners(int source)
	{
		for (int i = 0; i < DataHandler.num_attributes; i++) {
			for (int j = 0; j < DataHandler.num_attributes; j++) {
				PathObj[i].add(vertexes[source].spMatrix[j][i]);
			}
			Paths.add(new ArrayList<Integer>());
		}
		for (int i = 0; i < Math.min(DataHandler.num_attributes,PathObj[0].size()); i++) {

			for (int j = i + 1; j < Math.min(DataHandler.num_attributes,PathObj[0].size()); j++) {
				int cont = 0;

				for (int k = 0; k < DataHandler.num_attributes; k++) {
					if (PathObj[k].get(i) == PathObj[k].get(j)) {
						cont++;
					}
				}
				if (cont == DataHandler.num_attributes) {
					for (int k = 0; k < DataHandler.num_attributes; k++) {
						PathObj[k].remove(j);
					}
					Paths.remove(j);
					j--;
				}
			}

		}
		nadir = vertexes[source].spMatrix;
		
	}
	public int getC(int obj) {
		return C[obj];
	}
	public void printEF() {
		for (int i = 0; i < Paths.size(); i++) {
			System.out.print("Path: " + Paths.get(i));
			for (int j = 0; j < DataHandler.num_attributes; j++) {
				System.out.print("  FO_"+j+": " + PathObj[j].get(i));
			}System.out.println();
		}
		
	}
	
	
	
	
	
	public void Sort(ArrayList<Integer>[] set) {
		QS(set, 0, set[0].size() - 1);
	}

	public int colocar(ArrayList<Integer>[] e, int b, int t) {
		int i;
		int pivote, valor_pivote;
		int temp;
		ArrayList<Integer> tempPaths;
		pivote = b;
		valor_pivote = e[0].get(pivote);
		for (i = b + 1; i <= t; i++) {
			if (e[0].get(i)< valor_pivote) {
				pivote++;
				for (int j = 0; j < e.length; j++) {
					temp = e[j].get(i);
					e[j].set(i, e[j].get(pivote));
					e[j].set(pivote, temp);
				}
				tempPaths = Paths.get(i);
				Paths.set(i, Paths.get(pivote));
				Paths.set(pivote, tempPaths);
				
			}
		}

		for (int j = 0; j < e.length; j++) {
			temp = e[j].get(b);
			e[j].set(b, e[j].get(pivote));
			e[j].set(pivote, temp);
		}
		tempPaths = Paths.get(b);
		Paths.set(b, Paths.get(pivote));
		Paths.set(pivote, tempPaths);
		return pivote;
	}

	public void QS(ArrayList<Integer>[] e, int b, int t) {
		int pivote;
		if (b < t) {
			pivote = colocar(e, b, t);
			QS(e, b, pivote - 1);
			QS(e, pivote + 1, t);
		}

	}
	public  double calcDominatedArea() {
		if(Paths.size()>2){
			Sort(PathObj);
			double area=0.0;
			for (int i = 1; i < PathObj[0].size(); i++) {
				double deltax= PathObj[0].get(i)-PathObj[0].get(i-1);
				double deltay= nadir[0][1] - PathObj[1].get(i-1);
				area+= (deltax*deltay);
			}
			double total = (nadir[0][1]-nadir[1][1])*(nadir[1][0]-nadir[0][0]);
			return area/total;
		}else{
			return 0.0;
		}
	
	}
	
	


}
