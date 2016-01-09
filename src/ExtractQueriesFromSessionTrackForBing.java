import importedFromBHCD.Query;

import java.io.*;
import java.util.ArrayList;


public class ExtractQueriesFromSessionTrackForBing {
	
	public static ArrayList<Query> queryList;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String sessionTrack14File = "/Users/rishabhmehrotra/dev/workspace/BHCD/data/sessionTrack2014_queryList";
		FileInputStream fis = new FileInputStream("data/sessionTrack2014_queryList");
        ObjectInputStream ois = new ObjectInputStream(fis);
        queryList = (ArrayList<Query>) ois.readObject();
        ois.close();
	}

}
