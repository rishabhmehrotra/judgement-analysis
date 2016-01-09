import java.io.*;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

public class BingSearch {

	public static void main(String[] args) throws IOException {

		FileWriter fstream = new FileWriter("BING_SearchResults", true);
		BufferedWriter out = new BufferedWriter(fstream);

		BufferedReader br = new BufferedReader(new FileReader("BingQueries"));
		String line = br.readLine();
		int cc=0;
		while(line!=null)
		{
			out.write("============================================= \n");
			String parts[] = line.split("\t");
			String query = parts[0];
			System.out.println(query);

			AzureSearchWebQuery aq = new AzureSearchWebQuery();
			aq.setAppid("mcYhru+vbZkn5peHi8Z2uThxPPrp81blUl8eBnKIsk0");
			aq.setQuery(query);

			aq.setPerPage(50);
			aq.doQuery();
			out.write("Query:\t"+query+"\n");
			AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
			for (AzureSearchWebResult result : ars)
			{
				out.write("ID:\t"+result.getId()+"\n");
				out.write("Title:\t"+result.getTitle()+"\n");
				out.write("URL:\t"+result.getUrl()+"\n");
				out.write("DisplayURL:\t"+result.getDisplayUrl()+"\n");
				out.write(":\t"+result.getDescription()+"\n");
			}
			cc++;
			line = br.readLine();
		}
		System.out.println(cc);
		br.close();
		out.close();
	}
}
