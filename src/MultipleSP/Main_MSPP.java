package MultipleSP;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main_MSPP {
	

	public static void main(String[] args) throws InterruptedException {

		try {
			String ini = "instanca!!!!";
			Settings instance = new Settings(ini);
			DataHandler data = new DataHandler(instance, instance.numObjs);
			data.ReadC();

			PulseGraph netWork = data.getGd();
			long Atime = System.currentTimeMillis();

			// We need one SP algorithm for each weight/objective
			DIKBD[] spAlgo = new DIKBD[instance.numObjs];
			Thread[] tSp = new Thread[instance.numObjs];
			for (int i = 0; i < spAlgo.length; i++) {
				spAlgo[i] = new DIKBD(netWork, instance.sink - 1, i);
				tSp[i] = new Thread(new ShortestPathTask(i, spAlgo[i]));
			}

			for (int i = 0; i < spAlgo.length; i++) {
				tSp[i].start();
			}

			for (int i = 0; i < spAlgo.length; i++) {
				tSp[i].join();
			}

			System.out.print("\tEXE:/" + (System.currentTimeMillis() - Atime));

			netWork.vertexes = null;
			data.Arcs = null;
			data.atributes = null;
			data = null;
			netWork = null;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
