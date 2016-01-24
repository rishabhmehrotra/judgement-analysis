import java.io.*;
import java.util.*;
public class PValue {

	public static String aa = "F";
	public static String bb = "news";

	public static void main(String[] args) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
		String line = br.readLine();
		line = br.readLine();//skip the first line
		int skips = 0, read=0;
		String file = aa+bb+".txt";
		FileWriter fstream = new FileWriter(file);
		BufferedWriter out = new BufferedWriter(fstream);
		FileWriter fstream1 = new FileWriter(aa+bb+"_avg.txt");
		BufferedWriter out1 = new BufferedWriter(fstream1);


		double c1=0,c2=0,c3=0,c4=0,c5=0,ct=0;
		while(line!=null)
		{
			String parts[] = line.split("\t");
			read++;
			if(parts.length!=24) skips++;
			//System.out.println("ROW:_"+row);
			String userid = parts[1];
			int cand = Integer.parseInt(parts[2]);
			String fnf = parts[3];
			String method = parts[4];

			double j1 = Double.parseDouble(parts[10]);
			double j2 = Double.parseDouble(parts[12]);
			double j3 = Double.parseDouble(parts[14]);
			double j4 = Double.parseDouble(parts[16]);
			double j5 = Double.parseDouble(parts[18]);

			if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}

			if(j1==1) c1++;
			if(j2==1) c1++;
			if(j3==1) c1++;
			if(j4==1) c1++;
			if(j5==1) c1++;
			if(j1==2) c2++;
			if(j2==2) c2++;
			if(j3==2) c2++;
			if(j4==2) c2++;
			if(j5==2) c2++;
			if(j1==3) c3++;
			if(j2==3) c3++;
			if(j3==3) c3++;
			if(j4==3) c3++;
			if(j5==3) c3++;
			if(j1==4) c4++;
			if(j2==4) c4++;
			if(j3==4) c4++;
			if(j4==4) c4++;
			if(j5==4) c4++;
			if(j1==5) c5++;
			if(j2==5) c5++;
			if(j3==5) c5++;
			if(j4==5) c5++;
			if(j5==5) c5++;
			ct+=5;

			line = br.readLine();
		}
		br.close();
		System.out.println(c1+" "+c2+" "+c3+" "+c4+" "+c5+" "+ct);
		double p1= c1/ct;
		double p2= c2/ct;
		double p3= c3/ct;
		double p4= c4/ct;
		double p5= c5/ct;
		double pt = p1+p2+p3+p4+p5;
		System.out.println(p1+" "+p2+" "+p3+" "+p4+" "+p5+" "+pt);

		double prob[] = new double[5];
		prob[0]=p1;prob[1]=p2;prob[2]=p3;prob[3]=p4;prob[4]=p5;
		Multinomial m = new Multinomial(prob);
		int count=0;
		int nIter=10000;
		while(nIter>0)
		{
			br = new BufferedReader(new FileReader("/Users/rishabhmehrotra/Desktop/Rishabh_MSR15_Offline/rishabh-final-anon.txt"));
			line = br.readLine();
			line = br.readLine();//skip the first line
			if(nIter%100==0)System.out.println(nIter);
			double avgstdOrg=0, avgstdSyn=0;
			int cc=0;
			c1=0;c2=0;c3=0;c4=0;c5=0;ct=0;
			while(line!=null)
			{
				String parts[] = line.split("\t");
				String userid = parts[1];
				int cand = Integer.parseInt(parts[2]);
				String fnf = parts[3];
				String method = parts[4];

				double j1 = Double.parseDouble(parts[10]);
				double j2 = Double.parseDouble(parts[12]);
				double j3 = Double.parseDouble(parts[14]);
				double j4 = Double.parseDouble(parts[16]);
				double j5 = Double.parseDouble(parts[18]);

				if(fnf.compareTo(aa)!=0 || method.compareTo(bb)!=0) {line = br.readLine();continue;}
				
				

				int syn1 = m.sample()+1;
				int syn2 = m.sample()+1;
				int syn3 = m.sample()+1;
				int syn4 = m.sample()+1;
				int syn5 = m.sample()+1;
				
				

				double m1 = (j1+j2+j3+j4+j5)/5;
				double m2 = (syn1+syn2+syn3+syn4+syn5)/5;
				double data1[] = new double[5];
				data1[0]=j1;data1[1]=j2;data1[2]=j3;data1[3]=j4;data1[4]=j5;
				double data2[] = new double[5];
				data2[0]=syn1;data2[1]=syn2;data2[2]=syn3;data2[3]=syn4;data2[4]=syn5;
				Statistics s1 = new Statistics(data1);
				Statistics s2 = new Statistics(data2);
				double std1 = s1.getStdDev();
				double std2 = s2.getStdDev();
				avgstdOrg+=std1;
				avgstdSyn+=std2;
				cc++;

				line = br.readLine();
				j1 = syn1;j2 = syn2;j3 = syn3;j4 = syn4;j5 = syn5;
				if(j1==1) c1++;
				if(j2==1) c1++;
				if(j3==1) c1++;
				if(j4==1) c1++;
				if(j5==1) c1++;
				if(j1==2) c2++;
				if(j2==2) c2++;
				if(j3==2) c2++;
				if(j4==2) c2++;
				if(j5==2) c2++;
				if(j1==3) c3++;
				if(j2==3) c3++;
				if(j3==3) c3++;
				if(j4==3) c3++;
				if(j5==3) c3++;
				if(j1==4) c4++;
				if(j2==4) c4++;
				if(j3==4) c4++;
				if(j4==4) c4++;
				if(j5==4) c4++;
				if(j1==5) c5++;
				if(j2==5) c5++;
				if(j3==5) c5++;
				if(j4==5) c5++;
				if(j5==5) c5++;
				ct+=5;
			}
			double p11= c1/ct;
			double p21= c2/ct;
			double p31= c3/ct;
			double p41= c4/ct;
			double p51= c5/ct;
			if(nIter%100==0) System.out.println(p11+" "+p21+" "+p31+" "+p41+" "+p51);
			br.close();
			int tt=0;
			avgstdSyn/=cc;
			avgstdOrg/=cc;
			if(avgstdSyn<=avgstdOrg) {count++;tt=1;}
			out.write(tt+"\n");
			out1.write(avgstdSyn+"\t\t\t"+avgstdOrg+"\n");
			nIter--;
		}
		System.out.println(count);
		System.out.println(aa+"_"+bb);
		out.close();
		out1.close();


	}

}

class Statistics 
{
	double[] data;
	int size;   

	public Statistics(double[] data) 
	{
		this.data = data;
		size = data.length;
	}   

	double getMean()
	{
		double sum = 0.0;
		for(double a : data)
			sum += a;
		return sum/size;
	}

	double getVariance()
	{
		double mean = getMean();
		double temp = 0;
		for(double a :data)
			temp += (mean-a)*(mean-a);
		return temp/size;
	}

	double getStdDev()
	{
		return Math.sqrt(getVariance());
	}

	public double median() 
	{
		Arrays.sort(data);

		if (data.length % 2 == 0) 
		{
			return (data[(data.length / 2) - 1] + data[data.length / 2]) / 2.0;
		} 
		else 
		{
			return data[data.length / 2];
		}
	}
}
