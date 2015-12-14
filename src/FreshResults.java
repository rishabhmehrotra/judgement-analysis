import java.io.*;
import java.util.*;

public class FreshResults {
	
	public static HashMap<Integer, Integer> validJudges;
	public static HashMap<Integer, Integer> suspects;
	public static String aa = "F";
	public static String bb = "news";

	public static void main(String[] args) throws IOException {
		readJudges();
		System.out.println("Printing results for ALL judges...");
		//getAvgScoresAll();
		//getAvgScoresAllMethods();
		getAvgScoresAllPopularity();
		System.out.println("Printing results for NO SUSPECT judges...");
		//getAvgScoresNoSuspects();
		//getAvgScoresNoSuspectsMethods();
		getAvgScoresNoSuspectsPopularity();
		System.out.println(aa+" "+bb);
	}
	
	public static void getAvgScoresAll() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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
			String popularity = parts[23];

			if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}


			double avgScore = 0, num=0, den=0;
			num = j1+j2+j3+j4+j5;
			den = 5.0;
			avgScore = num/den;

			if(match>0) {mn+=avgScore;md++;}
			if(entity>0) {en+=avgScore;ed++;}
			if(odp>0) {on+=avgScore;od++;}
			if(popular>0) {pn+=avgScore;pd++;}
			line = br.readLine();
		}
		br.close();
		mavg = mn/md;
		eavg = en/ed;
		oavg = on/od;
		pavg = pn/pd;

		/*
		System.out.println(mavg+" "+mn+" "+md);
		System.out.println(eavg+" "+en+" "+ed);
		System.out.println(oavg+" "+on+" "+od);
		System.out.println(pavg+" "+pn+" "+pd);
		*/
		System.out.println(mavg);
		System.out.println(eavg);
		System.out.println(oavg);
		System.out.println(pavg);
		
		int sum = (int) ((int)md+ed+od+pd);
		System.out.println(sum);
	}

	public static void getAvgScoresNoSuspects() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;


		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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

			if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}


			double avgScore = 0, num=0, den=0;
			if(!suspects.containsKey(jid1)) {num+=j1;den++;}
			if(!suspects.containsKey(jid2)) {num+=j2;den++;}
			if(!suspects.containsKey(jid3)) {num+=j3;den++;}
			if(!suspects.containsKey(jid4)) {num+=j4;den++;}
			if(!suspects.containsKey(jid5)) {num+=j5;den++;}

			if(num>0 && den>0)
			{
				avgScore = num/den;

				if(match>0) {mn+=avgScore;md++;}
				if(entity>0) {en+=avgScore;ed++;}
				if(odp>0) {on+=avgScore;od++;}
				if(popular>0) {pn+=avgScore;pd++;}
			}

			line = br.readLine();
		}
		br.close();
		mavg = mn/md;
		eavg = en/ed;
		oavg = on/od;
		pavg = pn/pd;

		/*
		System.out.println(mavg+" "+mn+" "+md);
		System.out.println(eavg+" "+en+" "+ed);
		System.out.println(oavg+" "+on+" "+od);
		System.out.println(pavg+" "+pn+" "+pd);
		*/
		System.out.println(mavg);
		System.out.println(eavg);
		System.out.println(oavg);
		System.out.println(pavg);
		
		int sum = (int) ((int)md+ed+od+pd);
		System.out.println(sum);
		
	}

	
	public static void readJudges() throws IOException
	{
		validJudges = new HashMap<Integer, Integer>();
		suspects = new HashMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/results_dump/judges.txt"));
		String line = br.readLine();
		while(line!=null)
		{
			String parts[] = line.split(" ");
			int jid = Integer.parseInt(parts[0]);
			boolean suspect = Boolean.parseBoolean(parts[3]);
			if(suspect)
				suspects.put(jid, jid);
			else
				validJudges.put(jid, jid);
			line = br.readLine();
		}
	}

	public static void getAvgScoresAllMethods() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;
		double mavg2=0, mn2=0, md2=0, eavg2=0, en2=0, ed2=0, oavg2=0, on2=0,od2=0,pavg2=0,pn2=0,pd2=0;
		double mavg3=0, mn3=0, md3=0, eavg3=0, en3=0, ed3=0, oavg3=0, on3=0,od3=0,pavg3=0,pn3=0,pd3=0;
		double mavg4=0, mn4=0, md4=0, eavg4=0, en4=0, ed4=0, oavg4=0, on4=0,od4=0,pavg4=0,pn4=0,pd4=0;

		int c1=0,c2=0,c3=0,c4=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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

			//if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}

			int ways = 0;

			double avgScore = 0, num=0, den=0;
			num = j1+j2+j3+j4+j5;
			den = 5.0;
			avgScore = num/den;

			if(match>0) {mn+=avgScore;md++;ways++;}
			if(entity>0) {en+=avgScore;ed++;ways++;}
			if(odp>0) {on+=avgScore;od++;ways++;}
			if(popular>0) {pn+=avgScore;pd++;ways++;}
			c1++;

			if(ways>1)
			{
				if(match>0) {mn2+=avgScore;md2++;}
				if(entity>0) {en2+=avgScore;ed2++;}
				if(odp>0) {on2+=avgScore;od2++;}
				if(popular>0) {pn2+=avgScore;pd2++;}
				c2++;
			}
			if(ways>2)
			{
				c3++;
				if(match>0) {mn3+=avgScore;md3++;}
				if(entity>0) {en3+=avgScore;ed3++;}
				if(odp>0) {on3+=avgScore;od3++;}
				if(popular>0) {pn3+=avgScore;pd3++;}
			}
			if(ways>3)
			{
				c4++;
				if(match>0) {mn4+=avgScore;md4++;}
				if(entity>0) {en4+=avgScore;ed4++;}
				if(odp>0) {on4+=avgScore;od4++;}
				if(popular>0) {pn4+=avgScore;pd4++;}
			}
			//System.out.println(ways+" "+match+" "+entity+" "+odp+" "+popular);
			line = br.readLine();
		}
		br.close();
		mavg = mn/md;eavg = en/ed;oavg = on/od;pavg = pn/pd;
		mavg2 = mn2/md2;eavg2 = en2/ed2;oavg2 = on2/od2;pavg2 = pn2/pd2;
		mavg3 = mn3/md3;eavg3 = en3/ed3;oavg3 = on3/od3;pavg3 = pn3/pd3;
		mavg4 = mn4/md4;eavg4 = en4/ed4;oavg4 = on4/od4;pavg4 = pn4/pd4;

		System.out.println(mavg+" "+eavg+" "+oavg+" "+pavg+" "+c1);
		System.out.println(mavg2+" "+eavg2+" "+oavg2+" "+pavg2+" "+c2);
		System.out.println(mavg3+" "+eavg3+" "+oavg3+" "+pavg3+" "+c3);
		System.out.println(mavg4+" "+eavg4+" "+oavg4+" "+pavg4+" "+c4);
		System.out.println(md+" "+ed+" "+od+" "+pd);
		System.out.println(md2+" "+ed2+" "+od2+" "+pd2);
		System.out.println(md3+" "+ed3+" "+od3+" "+pd3);
		System.out.println(md4+" "+ed4+" "+od4+" "+pd4);
	}
	public static void getAvgScoresNoSuspectsMethods() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;
		double mavg2=0, mn2=0, md2=0, eavg2=0, en2=0, ed2=0, oavg2=0, on2=0,od2=0,pavg2=0,pn2=0,pd2=0;
		double mavg3=0, mn3=0, md3=0, eavg3=0, en3=0, ed3=0, oavg3=0, on3=0,od3=0,pavg3=0,pn3=0,pd3=0;
		double mavg4=0, mn4=0, md4=0, eavg4=0, en4=0, ed4=0, oavg4=0, on4=0,od4=0,pavg4=0,pn4=0,pd4=0;
		int c1=0,c2=0,c3=0,c4=0;

		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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

			//if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}


			double avgScore = 0, num=0, den=0;
			if(!suspects.containsKey(jid1)) {num+=j1;den++;}
			if(!suspects.containsKey(jid2)) {num+=j2;den++;}
			if(!suspects.containsKey(jid3)) {num+=j3;den++;}
			if(!suspects.containsKey(jid4)) {num+=j4;den++;}
			if(!suspects.containsKey(jid5)) {num+=j5;den++;}

			if(num>0 && den>0)
			{
				int ways = 0;
				avgScore = num/den;

				if(match>0) {mn+=avgScore;md++;}
				if(entity>0) {en+=avgScore;ed++;}
				if(odp>0) {on+=avgScore;od++;}
				if(popular>0) {pn+=avgScore;pd++;}

				if(match>0) {mn+=avgScore;md++;ways++;}
				if(entity>0) {en+=avgScore;ed++;ways++;}
				if(odp>0) {on+=avgScore;od++;ways++;}
				if(popular>0) {pn+=avgScore;pd++;ways++;}
				c1++;

				if(ways>1)
				{
					if(match>0) {mn2+=avgScore;md2++;}
					if(entity>0) {en2+=avgScore;ed2++;}
					if(odp>0) {on2+=avgScore;od2++;}
					if(popular>0) {pn2+=avgScore;pd2++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn3+=avgScore;md3++;}
					if(entity>0) {en3+=avgScore;ed3++;}
					if(odp>0) {on3+=avgScore;od3++;}
					if(popular>0) {pn3+=avgScore;pd3++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn4+=avgScore;md4++;}
					if(entity>0) {en4+=avgScore;ed4++;}
					if(odp>0) {on4+=avgScore;od4++;}
					if(popular>0) {pn4+=avgScore;pd4++;}
				}
			}

			line = br.readLine();
		}
		br.close();
		mavg = mn/md;eavg = en/ed;oavg = on/od;pavg = pn/pd;
		mavg2 = mn2/md2;eavg2 = en2/ed2;oavg2 = on2/od2;pavg2 = pn2/pd2;
		mavg3 = mn3/md3;eavg3 = en3/ed3;oavg3 = on3/od3;pavg3 = pn3/pd3;
		mavg4 = mn4/md4;eavg4 = en4/ed4;oavg4 = on4/od4;pavg4 = pn4/pd4;

		System.out.println(mavg+" "+eavg+" "+oavg+" "+pavg+" "+c1);
		System.out.println(mavg2+" "+eavg2+" "+oavg2+" "+pavg2+" "+c2);
		System.out.println(mavg3+" "+eavg3+" "+oavg3+" "+pavg3+" "+c3);
		System.out.println(mavg4+" "+eavg4+" "+oavg4+" "+pavg4+" "+c4);
		System.out.println(md+" "+ed+" "+od+" "+pd);
		System.out.println(md2+" "+ed2+" "+od2+" "+pd2);
		System.out.println(md3+" "+ed3+" "+od3+" "+pd3);
		System.out.println(md4+" "+ed4+" "+od4+" "+pd4);
	}


	public static void getAvgScoresAllPopularity() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;
		double mavg2=0, mn2=0, md2=0, eavg2=0, en2=0, ed2=0, oavg2=0, on2=0,od2=0,pavg2=0,pn2=0,pd2=0;
		double mavg3=0, mn3=0, md3=0, eavg3=0, en3=0, ed3=0, oavg3=0, on3=0,od3=0,pavg3=0,pn3=0,pd3=0;
		double mavg4=0, mn4=0, md4=0, eavg4=0, en4=0, ed4=0, oavg4=0, on4=0,od4=0,pavg4=0,pn4=0,pd4=0;
		double mavg5=0, mn5=0, md5=0, eavg5=0, en5=0, ed5=0, oavg5=0, on5=0,od5=0,pavg5=0,pn5=0,pd5=0;

		int c1=0,c2=0,c3=0,c4=0,c5=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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
			String popularity = parts[23];

			//if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}

			int ways = 0;

			double avgScore = 0, num=0, den=0;
			num = j1+j2+j3+j4+j5;
			den = 5.0;
			avgScore = num/den;

			if(popularity.compareTo("100s")==0)
			{
				if(match>0) {mn+=avgScore;md++;ways++;}
				if(entity>0) {en+=avgScore;ed++;ways++;}
				if(odp>0) {on+=avgScore;od++;ways++;}
				if(popular>0) {pn+=avgScore;pd++;ways++;}
				c1++;
			}

			if(popularity.compareTo("1000s")==0)
			{
				if(match>0) {mn2+=avgScore;md2++;}
				if(entity>0) {en2+=avgScore;ed2++;}
				if(odp>0) {on2+=avgScore;od2++;}
				if(popular>0) {pn2+=avgScore;pd2++;}
				c2++;
			}
			if(popularity.compareTo("10000s")==0)
			{
				c3++;
				if(match>0) {mn3+=avgScore;md3++;}
				if(entity>0) {en3+=avgScore;ed3++;}
				if(odp>0) {on3+=avgScore;od3++;}
				if(popular>0) {pn3+=avgScore;pd3++;}
			}
			if(popularity.compareTo("100K")==0)
			{
				c4++;
				if(match>0) {mn4+=avgScore;md4++;}
				if(entity>0) {en4+=avgScore;ed4++;}
				if(odp>0) {on4+=avgScore;od4++;}
				if(popular>0) {pn4+=avgScore;pd4++;}
			}
			if(popularity.compareTo("1M")==0)
			{
				c5++;
				if(match>0) {mn5+=avgScore;md5++;}
				if(entity>0) {en5+=avgScore;ed5++;}
				if(odp>0) {on5+=avgScore;od5++;}
				if(popular>0) {pn5+=avgScore;pd5++;}
			}

			line = br.readLine();
		}
		br.close();
		mavg = mn/md;eavg = en/ed;oavg = on/od;pavg = pn/pd;
		mavg2 = mn2/md2;eavg2 = en2/ed2;oavg2 = on2/od2;pavg2 = pn2/pd2;
		mavg3 = mn3/md3;eavg3 = en3/ed3;oavg3 = on3/od3;pavg3 = pn3/pd3;
		mavg4 = mn4/md4;eavg4 = en4/ed4;oavg4 = on4/od4;pavg4 = pn4/pd4;
		mavg5 = mn5/md5;eavg5 = en5/ed5;oavg5 = on5/od5;pavg5 = pn5/pd5;

		System.out.println(mavg+" "+eavg+" "+oavg+" "+pavg+" "+c1);
		System.out.println(mavg2+" "+eavg2+" "+oavg2+" "+pavg2+" "+c2);
		System.out.println(mavg3+" "+eavg3+" "+oavg3+" "+pavg3+" "+c3);
		System.out.println(mavg4+" "+eavg4+" "+oavg4+" "+pavg4+" "+c4);
		System.out.println(mavg5+" "+eavg5+" "+oavg5+" "+pavg5+" "+c5);
		System.out.println(md+" "+ed+" "+od+" "+pd);
		System.out.println(md2+" "+ed2+" "+od2+" "+pd2);
		System.out.println(md3+" "+ed3+" "+od3+" "+pd3);
		System.out.println(md4+" "+ed4+" "+od4+" "+pd4);
		System.out.println(md5+" "+ed5+" "+od5+" "+pd5);
	}
	public static void getAvgScoresNoSuspectsPopularity() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg=0, mn=0, md=0, eavg=0, en=0, ed=0, oavg=0, on=0,od=0,pavg=0,pn=0,pd=0;
		double mavg2=0, mn2=0, md2=0, eavg2=0, en2=0, ed2=0, oavg2=0, on2=0,od2=0,pavg2=0,pn2=0,pd2=0;
		double mavg3=0, mn3=0, md3=0, eavg3=0, en3=0, ed3=0, oavg3=0, on3=0,od3=0,pavg3=0,pn3=0,pd3=0;
		double mavg4=0, mn4=0, md4=0, eavg4=0, en4=0, ed4=0, oavg4=0, on4=0,od4=0,pavg4=0,pn4=0,pd4=0;
		double mavg5=0, mn5=0, md5=0, eavg5=0, en5=0, ed5=0, oavg5=0, on5=0,od5=0,pavg5=0,pn5=0,pd5=0;

		int c1=0,c2=0,c3=0,c4=0,c5=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");

			int row = Integer.parseInt(parts[0]);
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			String fnf = parts[3];
			String method = parts[4];
			int match = Integer.parseInt(parts[6]);
			int entity = Integer.parseInt(parts[7]);
			int odp = Integer.parseInt(parts[8]);
			int popular = Integer.parseInt(parts[9]);
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
			String popularity = parts[23];

			//if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}

			int ways = 0;

			double avgScore = 0, num=0, den=0;

			if(!suspects.containsKey(jid1)) {num+=j1;den++;}
			if(!suspects.containsKey(jid2)) {num+=j2;den++;}
			if(!suspects.containsKey(jid3)) {num+=j3;den++;}
			if(!suspects.containsKey(jid4)) {num+=j4;den++;}
			if(!suspects.containsKey(jid5)) {num+=j5;den++;}

			if(num>0 && den>0)
			{
				avgScore = num/den;

				if(popularity.compareTo("100s")==0)
				{
					if(match>0) {mn+=avgScore;md++;ways++;}
					if(entity>0) {en+=avgScore;ed++;ways++;}
					if(odp>0) {on+=avgScore;od++;ways++;}
					if(popular>0) {pn+=avgScore;pd++;ways++;}
					c1++;
				}

				if(popularity.compareTo("1000s")==0)
				{
					if(match>0) {mn2+=avgScore;md2++;}
					if(entity>0) {en2+=avgScore;ed2++;}
					if(odp>0) {on2+=avgScore;od2++;}
					if(popular>0) {pn2+=avgScore;pd2++;}
					c2++;
				}
				if(popularity.compareTo("10000s")==0)
				{
					c3++;
					if(match>0) {mn3+=avgScore;md3++;}
					if(entity>0) {en3+=avgScore;ed3++;}
					if(odp>0) {on3+=avgScore;od3++;}
					if(popular>0) {pn3+=avgScore;pd3++;}
				}
				if(popularity.compareTo("100K")==0)
				{
					c4++;
					if(match>0) {mn4+=avgScore;md4++;}
					if(entity>0) {en4+=avgScore;ed4++;}
					if(odp>0) {on4+=avgScore;od4++;}
					if(popular>0) {pn4+=avgScore;pd4++;}
				}
				if(popularity.compareTo("1M")==0)
				{
					c5++;
					if(match>0) {mn5+=avgScore;md5++;}
					if(entity>0) {en5+=avgScore;ed5++;}
					if(odp>0) {on5+=avgScore;od5++;}
					if(popular>0) {pn5+=avgScore;pd5++;}
				}
			}

			line = br.readLine();
		}
		br.close();
		mavg = mn/md;eavg = en/ed;oavg = on/od;pavg = pn/pd;
		mavg2 = mn2/md2;eavg2 = en2/ed2;oavg2 = on2/od2;pavg2 = pn2/pd2;
		mavg3 = mn3/md3;eavg3 = en3/ed3;oavg3 = on3/od3;pavg3 = pn3/pd3;
		mavg4 = mn4/md4;eavg4 = en4/ed4;oavg4 = on4/od4;pavg4 = pn4/pd4;
		mavg5 = mn5/md5;eavg5 = en5/ed5;oavg5 = on5/od5;pavg5 = pn5/pd5;

		System.out.println(mavg+" "+eavg+" "+oavg+" "+pavg+" "+c1);
		System.out.println(mavg2+" "+eavg2+" "+oavg2+" "+pavg2+" "+c2);
		System.out.println(mavg3+" "+eavg3+" "+oavg3+" "+pavg3+" "+c3);
		System.out.println(mavg4+" "+eavg4+" "+oavg4+" "+pavg4+" "+c4);
		System.out.println(mavg5+" "+eavg5+" "+oavg5+" "+pavg5+" "+c5);
		System.out.println(md+" "+ed+" "+od+" "+pd);
		System.out.println(md2+" "+ed2+" "+od2+" "+pd2);
		System.out.println(md3+" "+ed3+" "+od3+" "+pd3);
		System.out.println(md4+" "+ed4+" "+od4+" "+pd4);
		System.out.println(md5+" "+ed5+" "+od5+" "+pd5);
	}

}
