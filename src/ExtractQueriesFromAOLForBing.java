import java.io.*;
import java.util.*;

public class ExtractQueriesFromAOLForBing {

	public static HashMap<String, Integer> queries;

	public static void main(String args[]) throws IOException
	{
		queries = new HashMap<String, Integer>();
		populateQueriesFromAOL();
	}

	public static void populateQueriesFromAOL() throws IOException
	{
		String filename = "/Users/rishabhmehrotra/dev/workspace/TaskBasedUserModeling/src/data/AOL/AOL1.txt";
		BufferedReader br;
		br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		line = br.readLine();
		int start = 1;
		String prevUserID = "";
		int c=0, count=10;
		while(count>0)
		{
			filename = "/Users/rishabhmehrotra/dev/workspace/TaskBasedUserModeling/src/data/AOL/AOL"+count+".txt";
			count--;
			br = new BufferedReader(new FileReader(filename));
			line = br.readLine();line = br.readLine();
			while(line!=null)
			{
				try{
					c++;
					//if(c==100) break;
					String parts[] = line.split("\t");
					String userID = "";


					if(line.length()<1 || parts.length<1) {line = br.readLine();continue;}
					userID = parts[0];
					//if(!usersOverThreshold.containsKey(userID)) {line = br.readLine();continue;}
					//if(line.contains("2317930")) System.out.println("-----------------------"+userID);

					String query = parts[1];
					String terms[] = query.trim().split(" ");
					if(c<20) System.out.println(query+"_"+terms.length);

					if(terms.length < 2) {line = br.readLine(); continue;}

					if(queries.containsKey(query))
					{
						int freq = queries.get(query);
						queries.put(query, new Integer (freq+1));
					}
					else
						queries.put(query, new Integer(1));

					line = br.readLine();
				} catch(Exception e) {e.printStackTrace();}

			}
			System.out.println("Done with AOL"+(count+1)+".txt & no of queries right now: "+c+"_"+queries.size());
		}


		System.out.println("The total no of queries: "+queries.size()+"_");
		System.out.println("Total no of queries scanned: "+c);
		System.out.println("Sorting the hashmap now...");
		queries = (HashMap<String, Integer>) sortByValue(queries);
		System.out.println("printing top 10 queries:");
		
		FileWriter fstream = new FileWriter("top10KQueries");
		BufferedWriter out = new BufferedWriter(fstream);
		FileWriter fstream1 = new FileWriter("3Word_top10KQueries");
		BufferedWriter out1 = new BufferedWriter(fstream1);
		
		int t=0, tt=0;
		Iterator<String> itr = queries.keySet().iterator();
		while(itr.hasNext())
		{
			String q = itr.next();
			int frq = queries.get(q);
			String terms[] = q.split(" ");
			if(t<10) System.out.println("Query:"+q+"__Freq:"+queries.get(q));
			t++;
			if(t<15000)
			{
				out.write(q);
				out.write("\t"+frq);
				out.write("\n");
			}
			if(terms.length>2)
			{
				tt++;
				if(tt<10000)
				{
					out1.write(q);
					out1.write("\t"+frq);
					out1.write("\n");
				}
			}
		}
		out.close();
		out1.close();
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> 
	sortByValue( Map<K, V> map )
	{
		List<Map.Entry<K, V>> list =
				new LinkedList<>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
				{
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return (o2.getValue()).compareTo( o1.getValue() );
			}
				} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
}
