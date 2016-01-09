import java.io.*;
import java.util.*;
// Library: https://code.google.com/p/sprwikiwordrelatedness/source/browse/trunk/src/main/java/edu/osu/slate/experiments/Spearman.java?r=93
//Row Number	UserID	Candidate Query	F/NF	method	UserID File Name	Rank_ExactMatch	Rank_Entity	Rank_ODP	Rank_GeneralPopular	
//Judgement1	Judgement1_JudgeID	Judgement_2	J2_JudgeID	J3	J3_JudgeID	J4	J4_JudgeID	J5	J5_JudgeID	Matching_ExactQuery	
//Matching_Entity	Matching_ODP	Candidate_Popularity
public class Spearman_backup7Dec {

	//public static Vector<Double> perJudgeCorrValues[] = new Vector<Double>()[246];
	public static ArrayList<Vector<Double>> perJudgeCorrValues;
	public static ArrayList<Vector<Double>> perJudgeCorrValuesR;
	public static HashMap<Integer, Double> perJudgeAverages;
	public static ArrayList<Double> perJudgePValueNum;
	public static HashMap<Integer, Integer> jNGroups;
	public static HashMap<Integer, Integer> jNBadGroups;
	public static HashMap<Integer, Integer> jNBadGroups1,jNBadGroups2,jNBadGroups3,jNBadGroups4,jNBadGroups5;
	public static int nItr = 1000;

	public static void main(String[] args) throws IOException {
		int num=0;
		perJudgePValueNum = new ArrayList<Double>();
		for(int i=0;i<500;i++)
		{
			perJudgePValueNum.add(0.0);
		}
		FileWriter fstream = new FileWriter("perRowCorr.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		for(int ttt=1;ttt<=nItr;ttt++)
		{
			//System.out.println("Iteration: "+ttt);
			int rowStart=1;
			jNGroups = new HashMap<Integer, Integer>();
			jNBadGroups = new HashMap<Integer, Integer>();
			jNBadGroups1 = new HashMap<Integer, Integer>();
			jNBadGroups2 = new HashMap<Integer, Integer>();
			jNBadGroups3 = new HashMap<Integer, Integer>();
			jNBadGroups4 = new HashMap<Integer, Integer>();
			jNBadGroups5 = new HashMap<Integer, Integer>();
			perJudgeAverages = new HashMap<Integer, Double>();
			perJudgeCorrValues  = new ArrayList<Vector<Double>>();
			perJudgeCorrValuesR  = new ArrayList<Vector<Double>>();
			
			for(int i=0;i<500;i++)
			{
				Vector<Double> v = new Vector<Double>();
				perJudgeCorrValues.add(v);
				Vector<Double> vR = new Vector<Double>();
				perJudgeCorrValuesR.add(vR);
				jNGroups.put(i, 0);
				jNBadGroups.put(i, 0);
				jNBadGroups1.put(i, 0);
				jNBadGroups2.put(i, 0);
				jNBadGroups3.put(i, 0);
				jNBadGroups4.put(i, 0);
				jNBadGroups5.put(i, 0);
				perJudgeAverages.put(i, -1.0);
				//perJudgePValueNum.add(0.0);
			}
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

			double globalAvg=0; int globalCount=0;
			double globalAvgR=0;
			while(line!=null)
			{
				String parts[] = line.split("\t");
				read++;
				if(parts.length!=24) skips++;
				int row = Integer.parseInt(parts[0]);
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

				//if(fnf.compareTo("F")!=0 || method.compareTo("snna")!=0) {line = br.readLine();continue;}
				//if(fnf.compareTo("F")!=0) {line = br.readLine();continue;}

				//System.out.println(j1+"_"+j2+"_"+j3+"_"+j4+"_"+j5);
				group = userid+fnf+method;
				//System.out.println("group: "+group+"____prevgroup: "+prevgroup);
				if(group.compareTo(prevgroup)!=0 && start!=1)
				{
					//System.out.println("grp:"+grp+"__ss="+ss);
					/*
				for(int i=0;i<11;i++)
				{
					for(int j=0;j<20;j++)
					{
						System.out.print("i:"+i+" j:"+j+"____ ");
						System.out.print(xy[i][j][0]+"_"+xy[i][j][1]+"\t");
					}
					System.out.println();
				}
					 */

					//now we have  a group of (max) 20 pairwise X-Y values of 10 pairs, we need to calculate Spearman for each of these 10 pairs
					double corr=0;int size=0;
					double corrR=0;

					for(int kk=0;kk<ss;kk++)
					{
						size++;
						Vector<Double>X = new Vector<Double>();
						Vector<Double>Y = new Vector<Double>();
						int flag1=0, flag2=0;
						for(int i=0;i<grp;i++)
						{
							double d1 = xy[kk][i][0];
							double d2 = xy[kk][i][1];
							if(i>0 && xy[kk][i][0]!=xy[kk][i-1][0]) flag1=1;
							if(i>0 && xy[kk][i][1]!=xy[kk][i-1][1]) flag2=1;
							int jd1=(int)jxy[kk][i][0];
							int jd2=(int)jxy[kk][i][1];
							//System.out.println(d1+" "+d2);
							//System.out.println(jd1+"__"+jd2);
							X.add(d1);
							Y.add(d2);
						}

						double res = GetCorrelation(X, Y);
						Collections.shuffle(X);
						double resR = GetCorrelation(X, Y);
						X.clear();Y.clear();
						//System.out.println("__"+res);
						//System.exit(0);
						corr+=res;
						corrR+=resR;
						int judge1=(int)jxy[kk][0][0];
						int judge2=(int)jxy[kk][0][1];
						if(flag1==0)
						{
							int t = jNBadGroups.get(judge1);
							t++;
							jNBadGroups.put(judge1, t);
							if(xy[kk][0][0]==1){int tt = jNBadGroups1.get(judge1);tt++;jNBadGroups1.put(judge1, tt);}
							if(xy[kk][0][0]==2){int tt = jNBadGroups2.get(judge1);tt++;jNBadGroups2.put(judge1, tt);}
							if(xy[kk][0][0]==3){int tt = jNBadGroups3.get(judge1);tt++;jNBadGroups3.put(judge1, tt);}
							if(xy[kk][0][0]==4){int tt = jNBadGroups4.get(judge1);tt++;jNBadGroups4.put(judge1, tt);}
							if(xy[kk][0][0]==5){int tt = jNBadGroups5.get(judge1);tt++;jNBadGroups5.put(judge1, tt);}
						}
						if(flag2==0)
						{
							int t = jNBadGroups.get(judge2);
							t++;
							jNBadGroups.put(judge2, t);
							if(xy[kk][0][1]==1){int tt = jNBadGroups1.get(judge2);tt++;jNBadGroups1.put(judge2, tt);}
							if(xy[kk][0][1]==2){int tt = jNBadGroups2.get(judge2);tt++;jNBadGroups2.put(judge2, tt);}
							if(xy[kk][0][1]==3){int tt = jNBadGroups3.get(judge2);tt++;jNBadGroups3.put(judge2, tt);}
							if(xy[kk][0][1]==4){int tt = jNBadGroups4.get(judge2);tt++;jNBadGroups4.put(judge2, tt);}
							if(xy[kk][0][1]==5){int tt = jNBadGroups5.get(judge2);tt++;jNBadGroups5.put(judge2, tt);}
						}
						int tt = jNGroups.get(judge1);
						tt++;
						jNGroups.put(judge1, tt);
						tt = jNGroups.get(judge2);
						tt++;
						jNGroups.put(judge2, tt);
						//System.out.println(judge1+"=="+judge2);
						Vector<Double> vec1 = perJudgeCorrValues.get(judge1);
						vec1.add(res);
						perJudgeCorrValues.set(judge1,vec1);
						Vector<Double> vec2 = perJudgeCorrValues.get(judge2);
						vec2.add(res);
						perJudgeCorrValues.set(judge2,vec2);
						Vector<Double> vec1R = perJudgeCorrValuesR.get(judge1);
						vec1R.add(resR);
						perJudgeCorrValuesR.set(judge1,vec1R);
						Vector<Double> vec2R = perJudgeCorrValuesR.get(judge2);
						vec2R.add(resR);
						perJudgeCorrValuesR.set(judge2,vec2R);
					}
					double grpCorr = corr/size;
					double grpCorrR = corrR/size;
					for(int kk=rowStart;kk<row;kk++)
					{
						//System.out.println("Row: "+kk+" Corr: "+grpCorr);
						//out.write(kk+" "+grpCorr+"\n");
					}
					rowStart = row;
					//System.out.println("-------  "+grpCorr);
					//System.out.println("-------  "+grpCorrR);
					globalAvg+=grpCorr;
					globalAvgR+=grpCorrR;
					globalCount++;
					//System.exit(0);
					grpCount++;
					ss=0;
					grp=0;
				}
				else
				{
					if(start==1) 
					{//System.out.println("11");
						start=0;ss=0;grp=0;
					}
					//System.out.println("22");
					//System.out.println("grp:"+grp+"__ss="+ss);
					ss=0;
					for(int ii=1;ii<=5;ii++)
					{
						for(int jj=ii+1;jj<=5;jj++)
						{
							if(ii==1) {xy[ss][grp][0]=j1;jxy[ss][grp][0]=jid1;}
							if(ii==2) {xy[ss][grp][0]=j2;jxy[ss][grp][0]=jid2;}
							if(ii==3) {xy[ss][grp][0]=j3;jxy[ss][grp][0]=jid3;}
							if(ii==4) {xy[ss][grp][0]=j4;jxy[ss][grp][0]=jid4;}
							if(ii==5) {xy[ss][grp][0]=j5;jxy[ss][grp][0]=jid5;}
							if(jj==1) {xy[ss][grp][1]=j1;jxy[ss][grp][1]=jid1;}
							if(jj==2) {xy[ss][grp][1]=j2;jxy[ss][grp][1]=jid2;}
							if(jj==3) {xy[ss][grp][1]=j3;jxy[ss][grp][1]=jid3;}
							if(jj==4) {xy[ss][grp][1]=j4;jxy[ss][grp][1]=jid4;}
							if(jj==5) {xy[ss][grp][1]=j5;jxy[ss][grp][1]=jid5;}
							//System.out.println(ss+" ~~ "+grp);
							ss++;

						}
					}
					grp++;
					//System.out.println("33");
				}
				prevgroup=group;

				line = br.readLine();
			}
			globalAvg = globalAvg/globalCount;
			globalAvgR = globalAvgR/globalCount;
			//System.out.println("\n\n");
			//System.out.println(globalAvg+"  "+globalCount+"  "+grpCount);
			//System.out.println(globalAvgR+"  "+globalCount+"  "+grpCount);
			//System.out.println("Lines skipped:"+skips+" Lines read:"+read);
			//System.out.println("Max Judge ID: "+maxjid);
			//System.out.println("Iteration: "+ttt+" Observed Avg Corr: "+globalAvg+" Randomized Agv Corr: "+globalAvgR);
			System.out.println(ttt+" "+globalAvg+" "+globalAvgR);
			if(globalAvgR>=globalAvg) num++;
			perJudgeAverages();
		}
		double p = num/nItr;
		System.out.println("p value: "+p);
		out.close();
		System.out.println("Printing per judge p values...");
		for(int i=0;i<246;i++)
		{
			double n = perJudgePValueNum.get(i);
			double pval = n/nItr;
			System.out.println(i+" "+pval+" "+perJudgeAverages.get(i));
		}
	}

	public static void perJudgeAverages()
	{
		int sumSize=0;
		for(int i=0;i<246;i++)
		{
			Vector<Double> v = perJudgeCorrValues.get(i);
			int size = v.size();
			Iterator<Double> itr = v.iterator();
			double avg=0;
			while(itr.hasNext())
			{
				double d = itr.next();
				avg+=d;
			}
			sumSize+=size;
			avg = avg/size;
			perJudgeAverages.put(i, avg);
			
			Vector<Double> vR = perJudgeCorrValuesR.get(i);
			Iterator<Double> itrR = vR.iterator();
			double avgR=0;int sizeR = vR.size();
			while(itrR.hasNext())
			{
				double d = itrR.next();
				avgR+=d;
			}
			avgR = avgR/sizeR;
			//System.out.println("JudgeId: "+i+" AvgSpearmanCorr: "+avg+" size: "+size);
			//System.out.println(i+" "+avg+" "+avgR+" "+size+" "+(jNGroups.get(i)/4)+" "+(jNBadGroups.get(i)/4)+" "+(jNBadGroups1.get(i)/4)+" "+(jNBadGroups2.get(i)/4)+" "+(jNBadGroups3.get(i)/4)+" "+(jNBadGroups4.get(i)/4)+" "+(jNBadGroups5.get(i)/4));
			if(avgR>=avg)
			{
				double numm = perJudgePValueNum.get(i);
				numm++;
				perJudgePValueNum.set(i, numm);
			}
		}
		//System.out.println(sumSize);
	}

	/**
	 * Calculate the Spearman's Rank Correlation Coefficient of X and Y
	 * 
	 * @param X original human relatedness values
	 * @param Y metric relatedness values 
	 * @return
	 */
	public static double GetCorrelation(Vector<Double> X, Vector<Double> Y) {
		//System.out.println("in here");
		// Set up the Ranking elements
		Ranking[] XList = new Ranking[X.size()];
		Ranking[] YList = new Ranking[Y.size()];                
		for(int i=0; i<XList.length; i++) {
			XList[i] = new Ranking(i,X.elementAt(i));
			YList[i] = new Ranking(i,Y.elementAt(i));
		}

		// Sort the Ranking lists
		Arrays.sort(XList, new RankingComparator());
		Arrays.sort(YList, new RankingComparator());

		// Set the rank for the new lists
		for(int i=0; i<XList.length; i++) {
			XList[i].setRank(i+1);
			YList[i].setRank(i+1);
		}

		// Check for ties and modify rankings as needed
		SetRank(XList);
		//System.out.println("\n\nDONE WITH XLIST\n\n");
		SetRank(YList);
		//System.exit(0);
		//System.out.println("-----------------");
		/*for(int i=0; i<XList.length; i++) {
                Ranking r = XList[i];
                //System.out.println(i+" "+r.getValue()+"_==_"+r.getRank());
            }
            System.out.println("-----------------");
            for(int i=0; i<YList.length; i++) {
                Ranking r = YList[i];
                //System.out.println(i+" "+r.getValue()+"_==_"+r.getRank());
            }
            //System.out.println("-----------------");
		 */
		double d2 = 0.0;
		for(int i=0; i<XList.length; i++) {
			Ranking r = XList[i];

			boolean found = false;
			for(int j=0; j<YList.length && !found; j++) {
				Ranking r2 = YList[j];
				if(r2.getID() == r.getID()) {
					double d = r.getRank() - r2.getRank();
					//System.out.println(r.getRank() + "\t" + r2.getRank());
					found = true;
					d2 += (d*d);
				}//end: if()
			}//end: for(j)
		}//end: for(i)

			double n = X.size();
			double den = n * ((n * n) -1);
			double num = 6.0 * d2;
			//System.out.println(d2);
			double rho = 1- (num/den);
			return rho;
	}//end: GetCorrelation(X,Y)

	/**
	 * 
	 * @param List
	 */
	private static void SetRank(Ranking[] List) {

		// Set finalized flag for each element in the list
		boolean[] finalized = new boolean[List.length];
		for(int i=0; i<finalized.length; i++) {
			finalized[i] = false;
		}//end: for(i)

		// For each element in the list
		int index=0, num=1;
		double newRank =0;
		while(index < List.length) {
			//if(!finalized[index])
			{

				// Get current ranking & val
				double val = List[index].getValue();
				double rank = List[index].getRank();
				num = 1;
				//System.out.println("----Val: "+val+"   rank: "+rank);

				while(index+num != List.length && List[index+num].getValue() == val) {
					rank += List[index+num].getRank();
					num++;
					//System.out.println("Rank: "+rank+" num: "+num);
					//System.out.println("Condition 1: "+(index+num)+" == "+List.length);
					//System.out.println("Condition 2: "+List[index+num].getValue()+" == "+val);
				}
				//System.out.println(" OUT ");
				//if(num > 1 && index+num == List.length) {
				//        num--;
				//}

				if(num > 1) {
					newRank = rank / num;
					//System.out.println("Newrank.... rank: "+rank+" num: "+num+" newrank= "+newRank);
					for(int j=0; j<num; j++) {
						List[index+j].setRank(newRank);
						//finalized[index+j]=true;
					}//end: for(j)   
					if(index+num == List.length) List[index+num-1].setRank(newRank);
				}//end: if(num)
				//if(index+num == List.length-1) {List[index+num].setRank(newRank);break;}
			}//end: if(!finalized)
			//index = index + 1;
			index = index + num;
			//System.out.println("\t\tDONE\t\t\n\n");
		}//end: while(index)
		//List[List.length-1].setRank(newRank);
	}//end: SetRank(Ranking[])

}
