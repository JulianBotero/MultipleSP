package MultipleSP;

import java.util.ArrayList;



public class FinalVertexPulse extends VertexPulse{
	
	public int id;
	private EdgePulse reverseEdges;
	
		/**
	 * This matrix contains the sp bounds for each node M_ii correspond to the
	 * sp from the end node to this node of the objective i. M_ij correspond to
	 * the objective consumptions of the objective j when the objective i is
	 * minimum;
	 */
	public int[][] spMatrix;
	private PulseGraph pg;
	private boolean[] inserted;
	int c=0;
	int d=0;
	public FinalVertexPulse(int iD, PulseGraph npg) {
		super(iD);
		pg= npg;
		id = iD;
		spMatrix = new int[DataHandler.num_attributes][DataHandler.num_attributes];
		inserted = new boolean[DataHandler.num_attributes];
		for (int i = 0; i < DataHandler.num_attributes; i++) {
			spMatrix[i][i] = infinity;
			inserted[i] = false;
			//left[i] = this;
			//rigth[i] = this;
		}
		
	}

	
	public int  getID()
	{
		return id;
	}
	
	public void addReversedEdge(EdgePulse e)
	{
		if(reverseEdges!=null){
			reverseEdges.addNextCommonTailEdge(e);
		}else
			reverseEdges = e;
	}
	
	
	public EdgePulse findEdgeByTarget(VertexPulse target){
		if(reverseEdges!=null){
			reverseEdges.findEdgebyTarget(target);
		}
		return null;
	}
	public EdgePulse getReversedEdges() {
		return reverseEdges;
	}
	
	
	public void pulse(int[] pObjs, ArrayList<Integer> path) {
		path.add(id);
		
		if(!CheckDominance(pObjs)){
			PulseGraph.Paths.add(new ArrayList<Integer>());
			PulseGraph.Paths.get(PulseGraph.Paths.size()-1).addAll(path);
			for (int i = 0; i < pObjs.length; i++) {
				PulseGraph.PathObj[i].add(pObjs[i]);
			}
			
			//double data  = pg.calcDominatedArea();
			//System.out.println("porcentaje " + data );
			
			//System.out.println("Llego pulso con " +path + " " + pObjs[0]+ " " + pObjs[1]+ " " + pObjs[2]);
			//System.out.println("llegooo otrooooooooo y contando: p   " + PulseGraph.Paths.size());
		}
		path.remove((path.size()-1));
	}

	private boolean CheckDominance(int[] objs) {

		for (int i = 0; i < PulseGraph.Paths.size(); i++) {

			int domObjs = 0;

			for (int j = 0; j < objs.length; j++) {

				if (objs[j] >= PulseGraph.PathObj[j].get(i)) {
					domObjs++;
				} else {
					j = objs.length + 10;
				}
			}

			if (domObjs == objs.length) {
				return true;
			}
		}

		// DOMINATOOOR!
		
		for (int i = 0; i < PulseGraph.Paths.size(); i++) {
			int domObjs = 0;
			for (int j = 0; j < objs.length; j++) {

				if (objs[j] <= PulseGraph.PathObj[j].get(i)) {
					domObjs++;
				} else {
					j = objs.length + 10;
				}
			}

			if (domObjs == objs.length) {
				
				PulseGraph.Paths.remove(i);
				for (int j = 0; j < objs.length; j++) {
				PulseGraph.PathObj[j].remove(i);
				}
				
				i--;
				
			}
		}
		
		
		
		return false;
	}

}
