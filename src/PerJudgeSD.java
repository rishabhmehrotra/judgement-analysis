import java.io.*;
import java.util.*;


public class PerJudgeSD {
	
	public static ArrayList<Vector<Double>> perJudgeMeanOfDifferences;

	public static void main(String[] args) throws IOException{
		perJudgeMeanOfDifferences  = new ArrayList<Vector<Double>>();
		for(int i=0;i<500;i++)
		{
			Vector<Double> v = new Vector<Double>();
			perJudgeMeanOfDifferences.add(v);
		}
		
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		int skips = 0, read=0, start=1, grp=0,ss=0, grpCount=0, maxjid=0;
		String group="", prevgroup="";
		
		double globalAvg=0; int globalCount=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");
			read++;
			if(parts.length!=24) skips++;
			String row = parts[0];
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			double j1 = Double.parseDouble(parts[10]);
			double j2 = Double.parseDouble(parts[12]);
			double j3 = Double.parseDouble(parts[14]);
			double j4 = Double.parseDouble(parts[16]);
			double j5 = Double.parseDouble(parts[18]);
			int jid1 = Integer.parseInt(parts[11]);
			int jid2 = Integer.parseInt(parts[13]);
			int jid3 = Integer.parseInt(parts[15]);
			int jid4 = Integer.parseInt(parts[17]);
			int jid5 = Integer.parseInt(parts[19]);
			
			int mx = Math.max(jid1, Math.max(jid2, Math.max(jid3, Math.max(jid4, jid5))));
			maxjid = Math.max(mx, maxjid);
			
			double sumdiff=0;
			
			sumdiff += (Math.abs(j1-j2)+Math.abs(j1-j3)+Math.abs(j1-j4)+Math.abs(j1-j5));
			sumdiff/=4;
			Vector<Double> v = perJudgeMeanOfDifferences.get(jid1);
			v.add(sumdiff);
			perJudgeMeanOfDifferences.set(jid1, v);
			
			sumdiff += (Math.abs(j2-j1)+Math.abs(j2-j3)+Math.abs(j2-j4)+Math.abs(j2-j5));
			sumdiff/=4;
			v = perJudgeMeanOfDifferences.get(jid2);
			v.add(sumdiff);
			perJudgeMeanOfDifferences.set(jid2, v);
			
			sumdiff += (Math.abs(j3-j2)+Math.abs(j1-j3)+Math.abs(j3-j4)+Math.abs(j3-j5));
			sumdiff/=4;
			v = perJudgeMeanOfDifferences.get(jid3);
			v.add(sumdiff);
			perJudgeMeanOfDifferences.set(jid3, v);
			
			sumdiff += (Math.abs(j4-j2)+Math.abs(j4-j3)+Math.abs(j1-j4)+Math.abs(j4-j5));
			sumdiff/=4;
			v = perJudgeMeanOfDifferences.get(jid4);
			v.add(sumdiff);
			perJudgeMeanOfDifferences.set(jid4, v);
			
			sumdiff += (Math.abs(j5-j2)+Math.abs(j5-j3)+Math.abs(j5-j4)+Math.abs(j1-j5));
			sumdiff/=4;
			v = perJudgeMeanOfDifferences.get(jid5);
			v.add(sumdiff);
			perJudgeMeanOfDifferences.set(jid5, v);
			
			line = br.readLine();
		}
		int sumsize=0;
		for(int i=0;i<246;i++)
		{
			Vector<Double> v = perJudgeMeanOfDifferences.get(i);
			int size = v.size();
			Iterator<Double> itr = v.iterator();
			double avg=0;
			while(itr.hasNext())
			{
				double d = itr.next();
				avg+=d;
			}
			sumsize+=size;
			avg = avg/size;
			//System.out.println("JudgeId: "+i+" AvgMeanDiff: "+avg+" size: "+size);
			System.out.println(i+" "+avg+" "+size);
		}
		System.out.println("Lines skipped:"+skips+" Lines read:"+read);
		System.out.println(sumsize);
	}
}