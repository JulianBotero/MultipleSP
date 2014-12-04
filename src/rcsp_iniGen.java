import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.StringTokenizer;

public class rcsp_iniGen {

	public static void main(String[] args) throws FileNotFoundException {
		Random r1 = new Random(351543623);
		int[] networks = { 5, 7,  13,  15, 21, 23, };
//		int[] networks = { 5, 7,  13,  15, 21, 23, };
		int[] numOFObjs = { 3, 5, 10 };
		int numOfIntances = 1;
		Path root = Paths.get(System.getProperty("user.dir"));
		for (int k = 1; k <= numOfIntances; k++) {
			for (int i = 0; i < networks.length; i++) {// 24 network
				int[] netData = getNetData(root.getParent() + "/data/RCSP/rcsp"
						+ networks[i] + ".txt");
				int numArcs = netData[1];
				int numNodes = netData[0];
				int source = 1 ;//+ r1.nextInt(numNodes);
				int sink = numNodes;//1 + r1.nextInt(numNodes);
				if (sink == source) {
					System.err.println("ERROR");
				}
				for (int j = 0; j < numOFObjs.length; j++) {
					String finalINIpath = root.getParent() + "/ini/RCSP/rcsp"
							+ networks[i] + "_" + numOFObjs[j] + "_" + k
							+ ".ini";

					File file = new File(finalINIpath);

					PrintWriter bufRdr = new PrintWriter(file);
					String line = "DataFile:" + "/data/RCSP/rcsp" + networks[i]
							+ ".txt" + System.getProperty("line.separator");
					// bufRdr.write(line);

					line += "Number of Arcs:" + numArcs
							+ System.getProperty("line.separator");
					// bufRdr.write(line);

					line += "Number of Nodes:" + numNodes
							+ System.getProperty("line.separator");
					// bufRdr.write(line);

					line += "Source:" + source
							+ System.getProperty("line.separator");
					// bufRdr.write(line);

					line += "Last Node:" + sink
							+ System.getProperty("line.separator");

					line += "objs:" + numOFObjs[j]+ System.getProperty("line.separator");
					line += "seed:" +  (networks[i]*100 + 10*numOFObjs[j]+k);
					
					bufRdr.write(line);
					bufRdr.close();
					System.out.println(file + "nodes: "+ numNodes +" s: " + source + " end: "
							+ sink+ " seed: " + (networks[i]*100 + 10*numOFObjs[j]+k));

				}

			}

		}
	}

	private static int[] getNetData(String file) {
		// TODO Auto-generated method stub
		try {
			BufferedReader bf = new BufferedReader(new FileReader(
					new File(file)));
			String line = bf.readLine();
			int returned[] = { 0, 0 };
			StringTokenizer st = new StringTokenizer(line, " ");
			returned[0] = Integer.parseInt(st.nextToken());
			returned[1] = Integer.parseInt(st.nextToken());
			return returned;

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
