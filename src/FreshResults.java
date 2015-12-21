import java.io.*;
import java.util.*;

public class FreshResults {

	public static HashMap<Integer, Integer> validJudges;
	public static HashMap<Integer, Integer> suspects;
	public static String aa = "NF";
	public static String bb = "nf_news";

	public static void main(String[] args) throws IOException {
		readJudges();
		System.out.println("Printing results for ALL judges...");
		//getAvgScoresAll();
		//getAvgScoresAllMethods();
		//getAvgScoresAllMethodsPopularity();

		//getAvgScoresAllPopularity();
		System.out.println("Printing results for NO SUSPECT judges...");
		//getAvgScoresNoSuspects();
		//getAvgScoresNoSuspectsMethods();
		//getAvgScoresNoSuspectsMethodsPopularity();
		//getAvgScoresNoSuspectsPopularity();
		//onlyExactMatches();
		countPerMethodSuggestionsPerGroup();
		System.out.println(aa+" "+bb);
	}
	
	public static void countPerMethodSuggestionsPerGroup() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		int skips = 0, read=0, start=1, grp=0,ss=0, grpCount=0, maxjid=0;
		String group="", prevgroup="";

		double xy[][][] = new double[11][20][2];
		double jxy[][][] = new double[11][20][2];
		for(int i=0;i<11;i++)
			for(int j=0;j<20;j++)
				for(int k=0;k<2;k++)
				{xy[i][j][k]=-1;jxy[i][j][k]=-1;}

		ArrayList<Item> last5m = new ArrayList<Item>();
		ArrayList<Item> last5e = new ArrayList<Item>();
		ArrayList<Item> last5o = new ArrayList<Item>();
		ArrayList<Item> last5p = new ArrayList<Item>();
		double ndcg5m=0,ndcg5e=0,ndcg5o=0,ndcg5p=0;
		double ndcg5count=0;
		int c0=0,c1=0,c2=0,c3=0,c4=0,c5=0,call=0;
		int cmt=0,cet=0,cot=0,cpt=0;//c1Temp for c1t
		while(line!=null)
		{
			String parts[] = line.split("\t");
			read++;
			if(parts.length!=24) skips++;
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
			
			
			double avgScore = 0, num=0, den=0;
			if(!suspects.containsKey(jid1)) {num+=j1;den++;}
			if(!suspects.containsKey(jid2)) {num+=j2;den++;}
			if(!suspects.containsKey(jid3)) {num+=j3;den++;}
			if(!suspects.containsKey(jid4)) {num+=j4;den++;}
			if(!suspects.containsKey(jid5)) {num+=j5;den++;}

			if(num>0 && den>0)
			{
				avgScore = num/den;
			}
			
			if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}
			
			//System.out.println(j1+"_"+j2+"_"+j3+"_"+j4+"_"+j5);
			group = userid+fnf+method;
			//System.out.println("group: "+group+"____prevgroup: "+prevgroup);
			if(group.compareTo(prevgroup)!=0 && start!=1)
			{
				//now we have  a group of (max) 20 pairwise X-Y values of 10 pairs, we need to calculate Spearman for each of these 10 pairs
				//so we have cmt giving the no of m suggestions in this group
				// find the min of cmt, etc
				int min = Math.min(cmt, Math.min(cet, Math.min(cot, cpt)));
				if(min == 0)
					c0++;
				if(min == 1)
					c1++;
				if(min == 2)
					c2++;
				if(min == 3)
					c3++;
				if(min == 4)
					c4++;
				if(min == 5)
					c5++;
				call++;
				System.out.println("============== "+cmt+" "+cet+" "+cot+" "+cpt);
				cmt=0;cet=0;cot=0;cpt=0;
				
				// compute NDCG now
				if(min == 5)
				{
					//System.out.println(last5m.get(0).rank+" "+last5m.get(1).rank+" "+last5m.get(2).rank+" "+last5m.get(3).rank+" "+last5m.get(4).rank);
					//System.out.println(last5m.get(0).score+" "+last5m.get(1).score+" "+last5m.get(2).score+" "+last5m.get(3).score+" "+last5m.get(4).score);
					last5m.sort(new ItemComparator());
					//System.out.println(last5m.get(0).rank+" "+last5m.get(1).rank+" "+last5m.get(2).rank+" "+last5m.get(3).rank+" "+last5m.get(4).rank);
					//System.out.println(last5m.get(0).score+" "+last5m.get(1).score+" "+last5m.get(2).score+" "+last5m.get(3).score+" "+last5m.get(4).score);
					last5e.sort(new ItemComparator());
					last5o.sort(new ItemComparator());
					last5p.sort(new ItemComparator());
					//now the list is sorted based on rank, compute the NDCG
					ArrayList<Double> list = new ArrayList<Double>();
					list.add(last5m.get(0).score);
					list.add(last5m.get(1).score);
					list.add(last5m.get(2).score);
					list.add(last5m.get(3).score);
					list.add(last5m.get(4).score);
					//compute NDCG on this list
					System.out.println("exact match");
					//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
					double ndcg = computeNDCG(list);
					System.out.println(ndcg);
					ndcg5m+=ndcg;
					System.out.println("entity");
					list = new ArrayList<Double>();
					list.add(last5e.get(0).score);
					list.add(last5e.get(1).score);
					list.add(last5e.get(2).score);
					list.add(last5e.get(3).score);
					list.add(last5e.get(4).score);
					//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
					ndcg = computeNDCG(list);
					System.out.println(ndcg);
					ndcg5e+=ndcg;
					System.out.println("odp");
					list = new ArrayList<Double>();
					list.add(last5o.get(0).score);
					list.add(last5o.get(1).score);
					list.add(last5o.get(2).score);
					list.add(last5o.get(3).score);
					list.add(last5o.get(4).score);
					//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
					ndcg = computeNDCG(list);
					ndcg5o+=ndcg;
					System.out.println(ndcg);
					System.out.println("popular");
					list = new ArrayList<Double>();
					list.add(last5p.get(0).score);
					list.add(last5p.get(1).score);
					list.add(last5p.get(2).score);
					list.add(last5p.get(3).score);
					list.add(last5p.get(4).score);
					//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
					ndcg = computeNDCG(list);
					ndcg5p+=ndcg;
					System.out.println(ndcg);
					ndcg5count++;
					//System.exit(0);
				}
				
				last5m = new ArrayList<Item>();
				last5e = new ArrayList<Item>();
				last5o = new ArrayList<Item>();
				last5p = new ArrayList<Item>();
				
				grpCount++;
				ss=0;
				grp=0;
				if(match>0) cmt++;
				if(entity>0) cet++;
				if(odp>0) cot++;
				if(popular>0) cpt++;
				if(match>0)
				{
					Item i1 = new Item(match,avgScore);
					last5m.add(i1);
				}
				if(entity>0)
				{
					Item i1 = new Item(entity,avgScore);
					last5e.add(i1);
				}
				if(odp>0)
				{
					Item i1 = new Item(odp,avgScore);
					last5o.add(i1);
				}
				if(popular>0)
				{
					Item i1 = new Item(popular,avgScore);
					last5p.add(i1);
				}
			}
			else
			{
				if(start==1) 
				{//System.out.println("11");
					start=0;ss=0;grp=0;
				}

				ss=0;

				if(match>0) cmt++;
				if(entity>0) cet++;
				if(odp>0) cot++;
				if(popular>0) cpt++;
				System.out.println(match+" "+entity+" "+odp+" "+popular);
				grp++;
				
				if(match>0)
				{
					Item i1 = new Item(match,avgScore);
					last5m.add(i1);
				}
				if(entity>0)
				{
					Item i1 = new Item(entity,avgScore);
					last5e.add(i1);
				}
				if(odp>0)
				{
					Item i1 = new Item(odp,avgScore);
					last5o.add(i1);
				}
				if(popular>0)
				{
					Item i1 = new Item(popular,avgScore);
					last5p.add(i1);
				}
				
				//System.out.println("33");
			}
			prevgroup=group;

			line = br.readLine();
		}
		br.close();
		int min = Math.min(cmt, Math.min(cet, Math.min(cot, cpt)));
		if(min == 0)
			c0++;
		if(min == 1)
			c1++;
		if(min == 2)
			c2++;
		if(min == 3)
			c3++;
		if(min == 4)
			c4++;
		if(min == 5)
			c5++;
		if(min == 5)
		{
			//System.out.println(last5m.get(0).rank+" "+last5m.get(1).rank+" "+last5m.get(2).rank+" "+last5m.get(3).rank+" "+last5m.get(4).rank);
			//System.out.println(last5m.get(0).score+" "+last5m.get(1).score+" "+last5m.get(2).score+" "+last5m.get(3).score+" "+last5m.get(4).score);
			last5m.sort(new ItemComparator());
			//System.out.println(last5m.get(0).rank+" "+last5m.get(1).rank+" "+last5m.get(2).rank+" "+last5m.get(3).rank+" "+last5m.get(4).rank);
			//System.out.println(last5m.get(0).score+" "+last5m.get(1).score+" "+last5m.get(2).score+" "+last5m.get(3).score+" "+last5m.get(4).score);
			last5e.sort(new ItemComparator());
			last5o.sort(new ItemComparator());
			last5p.sort(new ItemComparator());
			//now the list is sorted based on rank, compute the NDCG
			ArrayList<Double> list = new ArrayList<Double>();
			list.add(last5m.get(0).score);
			list.add(last5m.get(1).score);
			list.add(last5m.get(2).score);
			list.add(last5m.get(3).score);
			list.add(last5m.get(4).score);
			//compute NDCG on this list
			System.out.println("exact match");
			//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
			double ndcg = computeNDCG(list);
			System.out.println(ndcg);
			ndcg5m+=ndcg;
			System.out.println("entity");
			list = new ArrayList<Double>();
			list.add(last5e.get(0).score);
			list.add(last5e.get(1).score);
			list.add(last5e.get(2).score);
			list.add(last5e.get(3).score);
			list.add(last5e.get(4).score);
			//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
			ndcg = computeNDCG(list);
			System.out.println(ndcg);
			ndcg5e+=ndcg;
			System.out.println("odp");
			list = new ArrayList<Double>();
			list.add(last5o.get(0).score);
			list.add(last5o.get(1).score);
			list.add(last5o.get(2).score);
			list.add(last5o.get(3).score);
			list.add(last5o.get(4).score);
			//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
			ndcg = computeNDCG(list);
			ndcg5o+=ndcg;
			System.out.println(ndcg);
			System.out.println("popular");
			list = new ArrayList<Double>();
			list.add(last5p.get(0).score);
			list.add(last5p.get(1).score);
			list.add(last5p.get(2).score);
			list.add(last5p.get(3).score);
			list.add(last5p.get(4).score);
			//System.out.println(list.get(0)+" "+list.get(1)+" "+list.get(2)+" "+list.get(3)+" "+list.get(4));
			ndcg = computeNDCG(list);
			ndcg5p+=ndcg;
			System.out.println(ndcg);
			ndcg5count++;
			//System.exit(0);
		}
		
		call++;
		int sum = c0+c1+c2+c3+c4+c5;
		System.out.println(c0+" "+c1+" "+c2+" "+c3+" "+c4+" "+c5+" "+call+" "+sum);
		ndcg5m/=ndcg5count;
		ndcg5e/=ndcg5count;
		ndcg5o/=ndcg5count;
		ndcg5p/=ndcg5count;
		System.out.println("NDCG@5 values: "+ndcg5m+" "+ndcg5e+" "+ndcg5o+" "+ndcg5p+" "+ndcg5count);
	}

	public static void onlyExactMatches() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		double fnavg = 0, fnn = 0, fnd = 0;
		double fsavg = 0, fsn = 0, fsd = 0;
		double fsnaavg = 0, fsnan = 0, fsnad = 0;
		double fsnnaavg = 0, fsnnan = 0, fsnnad = 0;
		double nfnavg = 0, nfnn = 0, nfnd = 0;
		double nfsavg = 0, nfsn = 0, nfsd = 0;
		double nfsnaavg = 0, nfsnan = 0, nfsnad = 0;
		double nfsnnaavg = 0, nfsnnan = 0, nfsnnad = 0;

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

			double avgScore = 0, num=0, den=0;
			if(!suspects.containsKey(jid1)) {num+=j1;den++;}
			if(!suspects.containsKey(jid2)) {num+=j2;den++;}
			if(!suspects.containsKey(jid3)) {num+=j3;den++;}
			if(!suspects.containsKey(jid4)) {num+=j4;den++;}
			if(!suspects.containsKey(jid5)) {num+=j5;den++;}

			if(num>0 && den>0)
			{
				avgScore = num/den;

				if(match<1) {line = br.readLine();continue;}

				if(fnf.compareTo("F")==0)
				{
					if(method.compareTo("news")==0) {fnn+=avgScore;fnd++;}
					if(method.compareTo("search")==0) {fsn+=avgScore;fsd++;}
					if(method.compareTo("sna")==0) {fsnan+=avgScore;fsnad++;}
					if(method.compareTo("snna")==0) {fsnnan+=avgScore;fsnnad++;}
				}
				else if(fnf.compareTo("NF")==0)
				{
					if(method.compareTo("nf_news")==0) {nfnn+=avgScore;nfnd++;}
					if(method.compareTo("nf_search")==0) {nfsn+=avgScore;nfsd++;}
					if(method.compareTo("nf_sna")==0) {nfsnan+=avgScore;nfsnad++;}
					if(method.compareTo("nf_snna")==0) {nfsnnan+=avgScore;nfsnnad++;}
				}
			}
			line = br.readLine();
		}
		br.close();
		fnavg = fnn/fnd;
		fsavg = fsn/fsd;
		fsnaavg = fsnan/fsnad;
		fsnnaavg = fsnnan/fsnnad;
		nfnavg = nfnn/nfnd;
		nfsavg = nfsn/nfsd;
		nfsnaavg = nfsnan/nfsnad;
		nfsnnaavg = nfsnnan/nfsnnad;
		
		System.out.println(fnavg+" "+fsavg+" "+fsnaavg+" "+fsnnaavg);
		System.out.println(nfnavg+" "+nfsavg+" "+nfsnaavg+" "+nfsnnaavg);
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

	public static void getAvgScoresAllMethodsPopularity() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line

		double mavg11=0, mn11=0, md11=0, eavg11=0, en11=0, ed11=0, oavg11=0, on11=0,od11=0,pavg11=0,pn11=0,pd11=0;
		double mavg12=0, mn12=0, md12=0, eavg12=0, en12=0, ed12=0, oavg12=0, on12=0,od12=0,pavg12=0,pn12=0,pd12=0;
		double mavg13=0, mn13=0, md13=0, eavg13=0, en13=0, ed13=0, oavg13=0, on13=0,od13=0,pavg13=0,pn13=0,pd13=0;
		double mavg14=0, mn14=0, md14=0, eavg14=0, en14=0, ed14=0, oavg14=0, on14=0,od14=0,pavg14=0,pn14=0,pd14=0;
		double mavg15=0, mn15=0, md15=0, eavg15=0, en15=0, ed15=0, oavg15=0, on15=0,od15=0,pavg15=0,pn15=0,pd15=0;
		double mavg21=0, mn21=0, md21=0, eavg21=0, en21=0, ed21=0, oavg21=0, on21=0,od21=0,pavg21=0,pn21=0,pd21=0;
		double mavg22=0, mn22=0, md22=0, eavg22=0, en22=0, ed22=0, oavg22=0, on22=0,od22=0,pavg22=0,pn22=0,pd22=0;
		double mavg23=0, mn23=0, md23=0, eavg23=0, en23=0, ed23=0, oavg23=0, on23=0,od23=0,pavg23=0,pn23=0,pd23=0;
		double mavg24=0, mn24=0, md24=0, eavg24=0, en24=0, ed24=0, oavg24=0, on24=0,od24=0,pavg24=0,pn24=0,pd24=0;
		double mavg25=0, mn25=0, md25=0, eavg25=0, en25=0, ed25=0, oavg25=0, on25=0,od25=0,pavg25=0,pn25=0,pd25=0;
		double mavg31=0, mn31=0, md31=0, eavg31=0, en31=0, ed31=0, oavg31=0, on31=0,od31=0,pavg31=0,pn31=0,pd31=0;
		double mavg41=0, mn41=0, md41=0, eavg41=0, en41=0, ed41=0, oavg41=0, on41=0,od41=0,pavg41=0,pn41=0,pd41=0;
		double mavg32=0, mn32=0, md32=0, eavg32=0, en32=0, ed32=0, oavg32=0, on32=0,od32=0,pavg32=0,pn32=0,pd32=0;
		double mavg42=0, mn42=0, md42=0, eavg42=0, en42=0, ed42=0, oavg42=0, on42=0,od42=0,pavg42=0,pn42=0,pd42=0;
		double mavg33=0, mn33=0, md33=0, eavg33=0, en33=0, ed33=0, oavg33=0, on33=0,od33=0,pavg33=0,pn33=0,pd33=0;
		double mavg43=0, mn43=0, md43=0, eavg43=0, en43=0, ed43=0, oavg43=0, on43=0,od43=0,pavg43=0,pn43=0,pd43=0;
		double mavg34=0, mn34=0, md34=0, eavg34=0, en34=0, ed34=0, oavg34=0, on34=0,od34=0,pavg34=0,pn34=0,pd34=0;
		double mavg44=0, mn44=0, md44=0, eavg44=0, en44=0, ed44=0, oavg44=0, on44=0,od44=0,pavg44=0,pn44=0,pd44=0;
		double mavg35=0, mn35=0, md35=0, eavg35=0, en35=0, ed35=0, oavg35=0, on35=0,od35=0,pavg35=0,pn35=0,pd35=0;
		double mavg45=0, mn45=0, md45=0, eavg45=0, en45=0, ed45=0, oavg45=0, on45=0,od45=0,pavg45=0,pn45=0,pd45=0;

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
			String popularity = parts[23];
			//if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}

			int ways = 0;

			double avgScore = 0, num=0, den=0;
			num = j1+j2+j3+j4+j5;
			den = 5.0;
			avgScore = num/den;

			if(popularity.compareTo("100s")==0)
			{

				if(match>0) {mn11+=avgScore;md11++;ways++;}
				if(entity>0) {en11+=avgScore;ed11++;ways++;}
				if(odp>0) {on11+=avgScore;od11++;ways++;}
				if(popular>0) {pn11+=avgScore;pd11++;ways++;}
				c1++;

				if(ways>1)
				{
					if(match>0) {mn21+=avgScore;md21++;}
					if(entity>0) {en21+=avgScore;ed21++;}
					if(odp>0) {on21+=avgScore;od21++;}
					if(popular>0) {pn21+=avgScore;pd21++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn31+=avgScore;md31++;}
					if(entity>0) {en31+=avgScore;ed31++;}
					if(odp>0) {on31+=avgScore;od31++;}
					if(popular>0) {pn31+=avgScore;pd31++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn41+=avgScore;md41++;}
					if(entity>0) {en41+=avgScore;ed41++;}
					if(odp>0) {on41+=avgScore;od41++;}
					if(popular>0) {pn41+=avgScore;pd41++;}
				}
			}

			if(popularity.compareTo("1000s")==0)
			{

				if(match>0) {mn12+=avgScore;md12++;ways++;}
				if(entity>0) {en12+=avgScore;ed12++;ways++;}
				if(odp>0) {on12+=avgScore;od12++;ways++;}
				if(popular>0) {pn12+=avgScore;pd12++;ways++;}
				c2++;

				if(ways>1)
				{
					if(match>0) {mn22+=avgScore;md22++;}
					if(entity>0) {en22+=avgScore;ed22++;}
					if(odp>0) {on22+=avgScore;od22++;}
					if(popular>0) {pn22+=avgScore;pd22++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn32+=avgScore;md32++;}
					if(entity>0) {en32+=avgScore;ed32++;}
					if(odp>0) {on32+=avgScore;od32++;}
					if(popular>0) {pn32+=avgScore;pd32++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn42+=avgScore;md42++;}
					if(entity>0) {en42+=avgScore;ed42++;}
					if(odp>0) {on42+=avgScore;od42++;}
					if(popular>0) {pn42+=avgScore;pd42++;}
				}
			}


			if(popularity.compareTo("10000s")==0)
			{

				if(match>0) {mn13+=avgScore;md13++;ways++;}
				if(entity>0) {en13+=avgScore;ed13++;ways++;}
				if(odp>0) {on13+=avgScore;od13++;ways++;}
				if(popular>0) {pn13+=avgScore;pd13++;ways++;}
				c3++;

				if(ways>1)
				{
					if(match>0) {mn23+=avgScore;md23++;}
					if(entity>0) {en23+=avgScore;ed23++;}
					if(odp>0) {on23+=avgScore;od23++;}
					if(popular>0) {pn23+=avgScore;pd23++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn33+=avgScore;md33++;}
					if(entity>0) {en33+=avgScore;ed33++;}
					if(odp>0) {on33+=avgScore;od33++;}
					if(popular>0) {pn33+=avgScore;pd33++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn43+=avgScore;md43++;}
					if(entity>0) {en43+=avgScore;ed43++;}
					if(odp>0) {on43+=avgScore;od43++;}
					if(popular>0) {pn43+=avgScore;pd43++;}
				}
			}


			if(popularity.compareTo("100K")==0)
			{

				if(match>0) {mn14+=avgScore;md14++;ways++;}
				if(entity>0) {en14+=avgScore;ed14++;ways++;}
				if(odp>0) {on14+=avgScore;od14++;ways++;}
				if(popular>0) {pn14+=avgScore;pd14++;ways++;}
				c4++;

				if(ways>1)
				{
					if(match>0) {mn24+=avgScore;md24++;}
					if(entity>0) {en24+=avgScore;ed24++;}
					if(odp>0) {on24+=avgScore;od24++;}
					if(popular>0) {pn24+=avgScore;pd24++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn34+=avgScore;md34++;}
					if(entity>0) {en34+=avgScore;ed34++;}
					if(odp>0) {on34+=avgScore;od34++;}
					if(popular>0) {pn34+=avgScore;pd34++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn44+=avgScore;md44++;}
					if(entity>0) {en44+=avgScore;ed44++;}
					if(odp>0) {on44+=avgScore;od44++;}
					if(popular>0) {pn44+=avgScore;pd44++;}
				}
			}


			if(popularity.compareTo("1M")==0)
			{

				if(match>0) {mn15+=avgScore;md15++;ways++;}
				if(entity>0) {en15+=avgScore;ed15++;ways++;}
				if(odp>0) {on15+=avgScore;od15++;ways++;}
				if(popular>0) {pn15+=avgScore;pd15++;ways++;}
				c1++;

				if(ways>1)
				{
					if(match>0) {mn25+=avgScore;md25++;}
					if(entity>0) {en25+=avgScore;ed25++;}
					if(odp>0) {on25+=avgScore;od25++;}
					if(popular>0) {pn25+=avgScore;pd25++;}
					c2++;
				}
				if(ways>2)
				{
					c3++;
					if(match>0) {mn35+=avgScore;md35++;}
					if(entity>0) {en35+=avgScore;ed35++;}
					if(odp>0) {on35+=avgScore;od35++;}
					if(popular>0) {pn35+=avgScore;pd35++;}
				}
				if(ways>3)
				{
					c4++;
					if(match>0) {mn45+=avgScore;md45++;}
					if(entity>0) {en45+=avgScore;ed45++;}
					if(odp>0) {on45+=avgScore;od45++;}
					if(popular>0) {pn45+=avgScore;pd45++;}
				}
			}

			//System.out.println(ways+" "+match+" "+entity+" "+odp+" "+popular);
			line = br.readLine();
		}
		br.close();
		mavg11 = mn11/md11;eavg11 = en11/ed11;oavg11 = on11/od11;pavg11 = pn11/pd11;
		mavg12 = mn12/md12;eavg12 = en12/ed12;oavg12 = on12/od12;pavg12 = pn12/pd12;
		mavg13 = mn13/md13;eavg13 = en13/ed13;oavg13 = on13/od13;pavg13 = pn13/pd13;
		mavg14 = mn14/md14;eavg14 = en14/ed14;oavg14 = on14/od14;pavg14 = pn14/pd14;
		mavg15 = mn15/md15;eavg15 = en15/ed15;oavg15 = on15/od15;pavg15 = pn15/pd15;
		mavg21 = mn21/md21;eavg21 = en21/ed21;oavg21 = on21/od21;pavg21 = pn21/pd21;
		mavg22 = mn22/md22;eavg22 = en22/ed22;oavg22 = on22/od22;pavg22 = pn22/pd22;
		mavg23 = mn23/md23;eavg23 = en23/ed23;oavg23 = on23/od23;pavg23 = pn23/pd23;
		mavg24 = mn24/md24;eavg24 = en24/ed24;oavg24 = on24/od24;pavg24 = pn24/pd24;
		mavg25 = mn25/md25;eavg25 = en25/ed25;oavg25 = on25/od25;pavg25 = pn25/pd25;
		mavg31 = mn31/md31;eavg31 = en31/ed31;oavg31 = on31/od31;pavg31 = pn31/pd31;
		mavg32 = mn32/md32;eavg32 = en32/ed32;oavg32 = on32/od32;pavg32 = pn32/pd32;
		mavg33 = mn33/md33;eavg33 = en33/ed33;oavg33 = on33/od33;pavg33 = pn33/pd33;
		mavg34 = mn34/md34;eavg34 = en34/ed34;oavg34 = on34/od34;pavg34 = pn34/pd34;
		mavg35 = mn35/md35;eavg35 = en35/ed35;oavg35 = on35/od35;pavg35 = pn35/pd35;
		mavg41 = mn41/md41;eavg41 = en41/ed41;oavg41 = on41/od41;pavg41 = pn41/pd41;
		mavg42 = mn42/md42;eavg42 = en42/ed42;oavg42 = on42/od42;pavg42 = pn42/pd42;
		mavg43 = mn43/md43;eavg43 = en43/ed43;oavg43 = on43/od43;pavg43 = pn43/pd43;
		mavg44 = mn44/md44;eavg44 = en44/ed44;oavg44 = on44/od44;pavg44 = pn44/pd44;
		mavg45 = mn45/md45;eavg45 = en45/ed45;oavg45 = on45/od45;pavg45 = pn45/pd45;


		System.out.println(mavg11+" "+mavg12+" "+mavg13+" "+mavg14+" "+mavg15);
		System.out.println(mavg21+" "+mavg22+" "+mavg23+" "+mavg24+" "+mavg25);
		System.out.println(mavg31+" "+mavg32+" "+mavg33+" "+mavg34+" "+mavg35);
		System.out.println(mavg41+" "+mavg42+" "+mavg43+" "+mavg44+" "+mavg45);
		System.out.println();
		System.out.println(eavg11+" "+eavg12+" "+eavg13+" "+eavg14+" "+eavg15);
		System.out.println(eavg21+" "+eavg22+" "+eavg23+" "+eavg24+" "+eavg25);
		System.out.println(eavg31+" "+eavg32+" "+eavg33+" "+eavg34+" "+eavg35);
		System.out.println(eavg41+" "+eavg42+" "+eavg43+" "+eavg44+" "+eavg45);
		System.out.println();
		System.out.println(oavg11+" "+oavg12+" "+oavg13+" "+oavg14+" "+oavg15);
		System.out.println(oavg21+" "+oavg22+" "+oavg23+" "+oavg24+" "+oavg25);
		System.out.println(oavg31+" "+oavg32+" "+oavg33+" "+oavg34+" "+oavg35);
		System.out.println(oavg41+" "+oavg42+" "+oavg43+" "+oavg44+" "+oavg45);
		System.out.println();
		System.out.println(pavg11+" "+pavg12+" "+pavg13+" "+pavg14+" "+pavg15);
		System.out.println(pavg21+" "+pavg22+" "+pavg23+" "+pavg24+" "+pavg25);
		System.out.println(pavg31+" "+pavg32+" "+pavg33+" "+pavg34+" "+pavg35);
		System.out.println(pavg41+" "+pavg42+" "+pavg43+" "+pavg44+" "+pavg45);
		/*
		System.out.println(md1+" "+ed1+" "+od1+" "+pd1);
		System.out.println(md2+" "+ed2+" "+od2+" "+pd2);
		System.out.println(md3+" "+ed3+" "+od3+" "+pd3);
		System.out.println(md4+" "+ed4+" "+od4+" "+pd4);*/
	}
	public static void getAvgScoresNoSuspectsMethodsPopularity() throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		double mavg11=0, mn11=0, md11=0, eavg11=0, en11=0, ed11=0, oavg11=0, on11=0,od11=0,pavg11=0,pn11=0,pd11=0;
		double mavg12=0, mn12=0, md12=0, eavg12=0, en12=0, ed12=0, oavg12=0, on12=0,od12=0,pavg12=0,pn12=0,pd12=0;
		double mavg13=0, mn13=0, md13=0, eavg13=0, en13=0, ed13=0, oavg13=0, on13=0,od13=0,pavg13=0,pn13=0,pd13=0;
		double mavg14=0, mn14=0, md14=0, eavg14=0, en14=0, ed14=0, oavg14=0, on14=0,od14=0,pavg14=0,pn14=0,pd14=0;
		double mavg15=0, mn15=0, md15=0, eavg15=0, en15=0, ed15=0, oavg15=0, on15=0,od15=0,pavg15=0,pn15=0,pd15=0;
		double mavg21=0, mn21=0, md21=0, eavg21=0, en21=0, ed21=0, oavg21=0, on21=0,od21=0,pavg21=0,pn21=0,pd21=0;
		double mavg22=0, mn22=0, md22=0, eavg22=0, en22=0, ed22=0, oavg22=0, on22=0,od22=0,pavg22=0,pn22=0,pd22=0;
		double mavg23=0, mn23=0, md23=0, eavg23=0, en23=0, ed23=0, oavg23=0, on23=0,od23=0,pavg23=0,pn23=0,pd23=0;
		double mavg24=0, mn24=0, md24=0, eavg24=0, en24=0, ed24=0, oavg24=0, on24=0,od24=0,pavg24=0,pn24=0,pd24=0;
		double mavg25=0, mn25=0, md25=0, eavg25=0, en25=0, ed25=0, oavg25=0, on25=0,od25=0,pavg25=0,pn25=0,pd25=0;
		double mavg31=0, mn31=0, md31=0, eavg31=0, en31=0, ed31=0, oavg31=0, on31=0,od31=0,pavg31=0,pn31=0,pd31=0;
		double mavg41=0, mn41=0, md41=0, eavg41=0, en41=0, ed41=0, oavg41=0, on41=0,od41=0,pavg41=0,pn41=0,pd41=0;
		double mavg32=0, mn32=0, md32=0, eavg32=0, en32=0, ed32=0, oavg32=0, on32=0,od32=0,pavg32=0,pn32=0,pd32=0;
		double mavg42=0, mn42=0, md42=0, eavg42=0, en42=0, ed42=0, oavg42=0, on42=0,od42=0,pavg42=0,pn42=0,pd42=0;
		double mavg33=0, mn33=0, md33=0, eavg33=0, en33=0, ed33=0, oavg33=0, on33=0,od33=0,pavg33=0,pn33=0,pd33=0;
		double mavg43=0, mn43=0, md43=0, eavg43=0, en43=0, ed43=0, oavg43=0, on43=0,od43=0,pavg43=0,pn43=0,pd43=0;
		double mavg34=0, mn34=0, md34=0, eavg34=0, en34=0, ed34=0, oavg34=0, on34=0,od34=0,pavg34=0,pn34=0,pd34=0;
		double mavg44=0, mn44=0, md44=0, eavg44=0, en44=0, ed44=0, oavg44=0, on44=0,od44=0,pavg44=0,pn44=0,pd44=0;
		double mavg35=0, mn35=0, md35=0, eavg35=0, en35=0, ed35=0, oavg35=0, on35=0,od35=0,pavg35=0,pn35=0,pd35=0;
		double mavg45=0, mn45=0, md45=0, eavg45=0, en45=0, ed45=0, oavg45=0, on45=0,od45=0,pavg45=0,pn45=0,pd45=0;
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
			String popularity = parts[23];

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

				if(popularity.compareTo("100s")==0)
				{

					if(match>0) {mn11+=avgScore;md11++;ways++;}
					if(entity>0) {en11+=avgScore;ed11++;ways++;}
					if(odp>0) {on11+=avgScore;od11++;ways++;}
					if(popular>0) {pn11+=avgScore;pd11++;ways++;}
					c1++;

					if(ways>1)
					{
						if(match>0) {mn21+=avgScore;md21++;}
						if(entity>0) {en21+=avgScore;ed21++;}
						if(odp>0) {on21+=avgScore;od21++;}
						if(popular>0) {pn21+=avgScore;pd21++;}
						c2++;
					}
					if(ways>2)
					{
						c3++;
						if(match>0) {mn31+=avgScore;md31++;}
						if(entity>0) {en31+=avgScore;ed31++;}
						if(odp>0) {on31+=avgScore;od31++;}
						if(popular>0) {pn31+=avgScore;pd31++;}
					}
					if(ways>3)
					{
						c4++;
						if(match>0) {mn41+=avgScore;md41++;}
						if(entity>0) {en41+=avgScore;ed41++;}
						if(odp>0) {on41+=avgScore;od41++;}
						if(popular>0) {pn41+=avgScore;pd41++;}
					}
				}

				if(popularity.compareTo("1000s")==0)
				{

					if(match>0) {mn12+=avgScore;md12++;ways++;}
					if(entity>0) {en12+=avgScore;ed12++;ways++;}
					if(odp>0) {on12+=avgScore;od12++;ways++;}
					if(popular>0) {pn12+=avgScore;pd12++;ways++;}
					c2++;

					if(ways>1)
					{
						if(match>0) {mn22+=avgScore;md22++;}
						if(entity>0) {en22+=avgScore;ed22++;}
						if(odp>0) {on22+=avgScore;od22++;}
						if(popular>0) {pn22+=avgScore;pd22++;}
						c2++;
					}
					if(ways>2)
					{
						c3++;
						if(match>0) {mn32+=avgScore;md32++;}
						if(entity>0) {en32+=avgScore;ed32++;}
						if(odp>0) {on32+=avgScore;od32++;}
						if(popular>0) {pn32+=avgScore;pd32++;}
					}
					if(ways>3)
					{
						c4++;
						if(match>0) {mn42+=avgScore;md42++;}
						if(entity>0) {en42+=avgScore;ed42++;}
						if(odp>0) {on42+=avgScore;od42++;}
						if(popular>0) {pn42+=avgScore;pd42++;}
					}
				}


				if(popularity.compareTo("10000s")==0)
				{

					if(match>0) {mn13+=avgScore;md13++;ways++;}
					if(entity>0) {en13+=avgScore;ed13++;ways++;}
					if(odp>0) {on13+=avgScore;od13++;ways++;}
					if(popular>0) {pn13+=avgScore;pd13++;ways++;}
					c3++;

					if(ways>1)
					{
						if(match>0) {mn23+=avgScore;md23++;}
						if(entity>0) {en23+=avgScore;ed23++;}
						if(odp>0) {on23+=avgScore;od23++;}
						if(popular>0) {pn23+=avgScore;pd23++;}
						c2++;
					}
					if(ways>2)
					{
						c3++;
						if(match>0) {mn33+=avgScore;md33++;}
						if(entity>0) {en33+=avgScore;ed33++;}
						if(odp>0) {on33+=avgScore;od33++;}
						if(popular>0) {pn33+=avgScore;pd33++;}
					}
					if(ways>3)
					{
						c4++;
						if(match>0) {mn43+=avgScore;md43++;}
						if(entity>0) {en43+=avgScore;ed43++;}
						if(odp>0) {on43+=avgScore;od43++;}
						if(popular>0) {pn43+=avgScore;pd43++;}
					}
				}


				if(popularity.compareTo("100K")==0)
				{

					if(match>0) {mn14+=avgScore;md14++;ways++;}
					if(entity>0) {en14+=avgScore;ed14++;ways++;}
					if(odp>0) {on14+=avgScore;od14++;ways++;}
					if(popular>0) {pn14+=avgScore;pd14++;ways++;}
					c4++;

					if(ways>1)
					{
						if(match>0) {mn24+=avgScore;md24++;}
						if(entity>0) {en24+=avgScore;ed24++;}
						if(odp>0) {on24+=avgScore;od24++;}
						if(popular>0) {pn24+=avgScore;pd24++;}
						c2++;
					}
					if(ways>2)
					{
						c3++;
						if(match>0) {mn34+=avgScore;md34++;}
						if(entity>0) {en34+=avgScore;ed34++;}
						if(odp>0) {on34+=avgScore;od34++;}
						if(popular>0) {pn34+=avgScore;pd34++;}
					}
					if(ways>3)
					{
						c4++;
						if(match>0) {mn44+=avgScore;md44++;}
						if(entity>0) {en44+=avgScore;ed44++;}
						if(odp>0) {on44+=avgScore;od44++;}
						if(popular>0) {pn44+=avgScore;pd44++;}
					}
				}


				if(popularity.compareTo("1M")==0)
				{

					if(match>0) {mn15+=avgScore;md15++;ways++;}
					if(entity>0) {en15+=avgScore;ed15++;ways++;}
					if(odp>0) {on15+=avgScore;od15++;ways++;}
					if(popular>0) {pn15+=avgScore;pd15++;ways++;}
					c1++;

					if(ways>1)
					{
						if(match>0) {mn25+=avgScore;md25++;}
						if(entity>0) {en25+=avgScore;ed25++;}
						if(odp>0) {on25+=avgScore;od25++;}
						if(popular>0) {pn25+=avgScore;pd25++;}
						c2++;
					}
					if(ways>2)
					{
						c3++;
						if(match>0) {mn35+=avgScore;md35++;}
						if(entity>0) {en35+=avgScore;ed35++;}
						if(odp>0) {on35+=avgScore;od35++;}
						if(popular>0) {pn35+=avgScore;pd35++;}
					}
					if(ways>3)
					{
						c4++;
						if(match>0) {mn45+=avgScore;md45++;}
						if(entity>0) {en45+=avgScore;ed45++;}
						if(odp>0) {on45+=avgScore;od45++;}
						if(popular>0) {pn45+=avgScore;pd45++;}
					}
				}
			}

			line = br.readLine();
		}
		br.close();
		mavg11 = mn11/md11;eavg11 = en11/ed11;oavg11 = on11/od11;pavg11 = pn11/pd11;
		mavg12 = mn12/md12;eavg12 = en12/ed12;oavg12 = on12/od12;pavg12 = pn12/pd12;
		mavg13 = mn13/md13;eavg13 = en13/ed13;oavg13 = on13/od13;pavg13 = pn13/pd13;
		mavg14 = mn14/md14;eavg14 = en14/ed14;oavg14 = on14/od14;pavg14 = pn14/pd14;
		mavg15 = mn15/md15;eavg15 = en15/ed15;oavg15 = on15/od15;pavg15 = pn15/pd15;
		mavg21 = mn21/md21;eavg21 = en21/ed21;oavg21 = on21/od21;pavg21 = pn21/pd21;
		mavg22 = mn22/md22;eavg22 = en22/ed22;oavg22 = on22/od22;pavg22 = pn22/pd22;
		mavg23 = mn23/md23;eavg23 = en23/ed23;oavg23 = on23/od23;pavg23 = pn23/pd23;
		mavg24 = mn24/md24;eavg24 = en24/ed24;oavg24 = on24/od24;pavg24 = pn24/pd24;
		mavg25 = mn25/md25;eavg25 = en25/ed25;oavg25 = on25/od25;pavg25 = pn25/pd25;
		mavg31 = mn31/md31;eavg31 = en31/ed31;oavg31 = on31/od31;pavg31 = pn31/pd31;
		mavg32 = mn32/md32;eavg32 = en32/ed32;oavg32 = on32/od32;pavg32 = pn32/pd32;
		mavg33 = mn33/md33;eavg33 = en33/ed33;oavg33 = on33/od33;pavg33 = pn33/pd33;
		mavg34 = mn34/md34;eavg34 = en34/ed34;oavg34 = on34/od34;pavg34 = pn34/pd34;
		mavg35 = mn35/md35;eavg35 = en35/ed35;oavg35 = on35/od35;pavg35 = pn35/pd35;
		mavg41 = mn41/md41;eavg41 = en41/ed41;oavg41 = on41/od41;pavg41 = pn41/pd41;
		mavg42 = mn42/md42;eavg42 = en42/ed42;oavg42 = on42/od42;pavg42 = pn42/pd42;
		mavg43 = mn43/md43;eavg43 = en43/ed43;oavg43 = on43/od43;pavg43 = pn43/pd43;
		mavg44 = mn44/md44;eavg44 = en44/ed44;oavg44 = on44/od44;pavg44 = pn44/pd44;
		mavg45 = mn45/md45;eavg45 = en45/ed45;oavg45 = on45/od45;pavg45 = pn45/pd45;


		System.out.println(mavg11+" "+mavg12+" "+mavg13+" "+mavg14+" "+mavg15);
		System.out.println(mavg21+" "+mavg22+" "+mavg23+" "+mavg24+" "+mavg25);
		System.out.println(mavg31+" "+mavg32+" "+mavg33+" "+mavg34+" "+mavg35);
		System.out.println(mavg41+" "+mavg42+" "+mavg43+" "+mavg44+" "+mavg45);
		System.out.println();
		System.out.println(eavg11+" "+eavg12+" "+eavg13+" "+eavg14+" "+eavg15);
		System.out.println(eavg21+" "+eavg22+" "+eavg23+" "+eavg24+" "+eavg25);
		System.out.println(eavg31+" "+eavg32+" "+eavg33+" "+eavg34+" "+eavg35);
		System.out.println(eavg41+" "+eavg42+" "+eavg43+" "+eavg44+" "+eavg45);
		System.out.println();
		System.out.println(oavg11+" "+oavg12+" "+oavg13+" "+oavg14+" "+oavg15);
		System.out.println(oavg21+" "+oavg22+" "+oavg23+" "+oavg24+" "+oavg25);
		System.out.println(oavg31+" "+oavg32+" "+oavg33+" "+oavg34+" "+oavg35);
		System.out.println(oavg41+" "+oavg42+" "+oavg43+" "+oavg44+" "+oavg45);
		System.out.println();
		System.out.println(pavg11+" "+pavg12+" "+pavg13+" "+pavg14+" "+pavg15);
		System.out.println(pavg21+" "+pavg22+" "+pavg23+" "+pavg24+" "+pavg25);
		System.out.println(pavg31+" "+pavg32+" "+pavg33+" "+pavg34+" "+pavg35);
		System.out.println(pavg41+" "+pavg42+" "+pavg43+" "+pavg44+" "+pavg45);
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

	public static double computeNDCG(List<Double> curList){
		// Inspired from: http://plash2.iis.sinica.edu.tw/svn/autoquiz/HeilmanQGAutoquiz/src/edu/cmu/ark/ranking/RankingEval.java
		double ndcg = 0.0;
		
		double point=0;
		ndcg = 0.0;
		double gain;
		List<Double> labels = new ArrayList<Double>();
		for(int j=0;j<curList.size(); j++){
			point = curList.get(j);
			
				gain = point;
			
			ndcg += gain * ndcgDiscount(j);
			//System.out.println(ndcg);
		}
		
		double max = 0.0;
		for(int j=0;j<curList.size(); j++) labels.add(curList.get(j));
		Collections.sort(labels);
		Collections.reverse(labels);
		for(int j=0;j<labels.size(); j++){
			
				gain = labels.get(j);
			
			max += gain * ndcgDiscount(j);
		}
		
		if(max == 0.0){
			return -1.0;
		}
		//System.out.println("\n\n"+ndcg+" "+max);
		ndcg /= max;
		
		if(Double.isNaN(ndcg)) ndcg = 0.0;
		
		return ndcg;
	}
	
	
	public static double ndcgDiscount(int i){
		double res = 0.0;
		if(i==0){
			res = 1.0;
		}else{
			res = 1.0/(Math.log(1.0+i)/Math.log(2.0));
		}
		return res;
	}
}

class Item
{
	public int rank;
	public double score;
	
	public Item(int rank, double score)
	{
		this.rank = rank;
		this.score = score;
	}
}

class ItemComparator implements Comparator<Item> {

    public int compare(Item r1, Item r2) {
            if(r2.rank > r1.rank)
                    return -1;
            else if(r2.rank < r1.rank)
                    return 1;
            else
                    return 0;
    }
}
