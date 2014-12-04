package MultipleSP;

import java.util.ArrayList;

public class VertexPulse {
	
	public static final int infinity = (int) Double.POSITIVE_INFINITY;

	private EdgePulse reverseEdges;
	ArrayList<Integer> magicIndex;
	public int id;
	
	public VertexPulse[] left;
	public VertexPulse[] rigth;

	/**
	 * This matrix contains the sp bounds for each node. spMatrix(i,i) correspond to the
	 * sp from the end node to this node of the objective i. M_ij correspond to
	 * the objective consumptions of the objective j when the objective i is
	 * minimum. 
	 */
	public int[][] spMatrix;

	public boolean[] inserted;
	/**
	 * PULSE
	 */

	private int labels[][];

	boolean firstTime = true;

	private int usedLabels = 0;

	public VertexPulse(int iD) {
		id = iD;
		spMatrix = new int[DataHandler.num_attributes][DataHandler.num_attributes];
		left  = new  VertexPulse[DataHandler.num_attributes];
		rigth = new  VertexPulse[DataHandler.num_attributes];
		inserted = new boolean[DataHandler.num_attributes];
		for (int i = 0; i < DataHandler.num_attributes; i++) {
			spMatrix[i][i] = infinity;
			inserted[i] = false;
			left[i] = this;
			rigth[i] = this;
		}

		labels = new int[DataHandler.numLabels][DataHandler.num_attributes];
		for (int k = 0; k < DataHandler.numLabels; k++) {
			for (int j = 0; j < DataHandler.num_attributes; j++) {
				labels[k][j] = infinity;
			}
		}
		magicIndex = new ArrayList<Integer>();
	}

	public int getID() {
		return id;
	}

	public void addReversedEdge(EdgePulse e) {
		if (reverseEdges != null) {
			reverseEdges.addNextCommonTailEdge(e);
		} else
			reverseEdges = e;
	}

	public EdgePulse getReversedEdges() {
		return reverseEdges;
	}

	/**
	 * Sets dijkstra's shortest path label on the objective specified
	 * 
	 * @param obj
	 *            objective of the shortest path
	 * @param c
	 *            label of the objective obj
	 */
/*	public void setMinObjective(int obj, int c) {
		spMatrix[obj][obj] = c;
	}

	public int getDualBound(int obj) {
		return spMatrix[obj][obj];
	}

	public void setMaxObjectives(int obj, int maxObj, int c) {
		spMatrix[obj][maxObj] = c;
	}

	public int getObjectiveLabel(int obj, int maxObj) {
		return spMatrix[obj][maxObj];
	}
	public int getMaxObjLabel(int obj, int maxObj) {
		return spMatrix[obj][maxObj];
	}
*/

	/**
	 * Unlink a vertex from the bucket
	 * 
	 * @return true, if the buckets gets empty
	 */
	/*public boolean unLinkVertex(int obj) {
		if (rigth[obj].getID() == id) {
			left[obj] = this;
			rigth[obj] = this;
			return true;
		} else {
			left[obj].rigth[obj] =rigth[obj];
			rigth[obj].left[obj] = left[obj];
			left[obj] = this;
			rigth[obj] = this;
			return false;
		}
	}
*/

	/*public void fastUnlink(int obj) {
		left[obj] = this;
		rigth[obj] = this;
	}
*/
	/*public void unlinkRighBound(int obj) {
		rigth[obj] = null;
	}*/

	/**
	 * Insert a vertex in a bucket. New vertex is inserted on the left of the
	 * bucket entrance
	 * 
	 * @param vertex in progress to be inserted 
	 * @param obj objective been optimized
	 */
	/*public void insertVertex(int obj, VertexPulse v) {
		v.setLeft(obj, left[obj]);
		v.setRigth(obj, this);
		left[obj].setRigth(obj, v);
		left[obj] = v;
	}*/


	/*public void setRigth(int obj, VertexPulse v) {
		rigth[obj] = v;
	}

	public void setLeft(int obj, VertexPulse v) {
		left[obj] = v;
	}*/

	/*public VertexPulse getRigth(int obj) {
		return rigth[obj];
	}

	public VertexPulse getLeft(int obj) {
		return left[obj];
	}
*/

	/*public void setInserted( int obj ) {
		inserted[obj] = true;
	}
	
	public boolean isInserted(int obj) {
		return inserted[obj];
	}

*/
	public void reset() {
		for (int i = 0; i < DataHandler.num_attributes; i++) {
			inserted[i] = false;
		}
	}

	/*
	 * public void setBounds(int MT, int MD){ maxDist = MD- minDist; maxTime =
	 * MT - minTime; bLeft = null; bRigth = null; reverseEdges = null; }
	 */

	private void Sort(ArrayList<Integer> set) {
		QS(magicIndex, 0, magicIndex.size() - 1);
	}

	public int colocar(ArrayList<Integer> e, int b, int t) {
		int i;
		int pivote, valor_pivote;
		int temp;

		pivote = b;
		valor_pivote = PulseGraph.vertexes[DataHandler.Arcs[e.get(pivote)][1]]
				.getCompareCriteria();
		for (i = b + 1; i <= t; i++) {
			if (PulseGraph.vertexes[DataHandler.Arcs[e.get(i)][1]]
					.getCompareCriteria() < valor_pivote) {
				pivote++;
				temp = e.get(i);
				e.set(i, e.get(pivote));
				e.set(pivote, temp);
			}
		}
		temp = e.get(b);
		e.set(b, e.get(pivote));
		e.set(pivote, temp);
		return pivote;
	}

	public void QS(ArrayList<Integer> e, int b, int t) {
		int pivote;
		if (b < t) {
			pivote = colocar(e, b, t);
			QS(e, b, pivote - 1);
			QS(e, pivote + 1, t);
		}
	}

	
	public int getCompareCriteria() {
		int suma = 0;
		for (int j = 0; j <  DataHandler.num_attributes; j++) {
			suma += this.spMatrix[j][j];
		}
		return suma;
	}

	
	public String toString(){
		return ""+ id;
	}

	
}
