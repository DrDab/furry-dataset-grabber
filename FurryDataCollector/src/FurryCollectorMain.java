import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class FurryCollectorMain 
{
	public static void main(String[] args)
	{
		System.setProperty("http.agent", "FurryDataCollector/1.0");
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please enter the number of entries to grab.");
		int total = Integer.parseInt(sc.nextLine());
		
		System.out.println("Please select a mode:");
		System.out.println("[C]reate an archive of ESix data");
		System.out.println("[G]enerate training data for CV classification");
		
		boolean archiveMode = sc.nextLine().toLowerCase().contains("c");
		
		System.out.println("Enable educational mode? (Y/N)");
		
		boolean sfw = sc.nextLine().toLowerCase().indexOf("y") != -1;
		
		// To increase sample set diversity, comment the 4 lines following the next line.
		// WARNING: Doing this will enable explicit content in your sample set.
		//if (!sfw)
		//{
		//	System.out.println("Kirby's calling the police");
		//	return;
		//}
		
		double eta = total * 0.26;
		
		if (eta >= 86400.0)
		{
			double eta_days = eta / 86400.0;
			System.out.printf("Estimated time: %.1f days\n", eta_days);
		}
		else if (eta >= 3600.0)
		{
			double eta_hours = eta / 3600.0;
			System.out.printf("Estimated time: %.1f hours\n", eta_hours);
		}
		else if (eta >= 60.0)
		{
			double eta_minutes = eta / 60.0;
			System.out.printf("Estimated time: %.0f minutes\n", eta_minutes);
		}
		else
		{
			System.out.printf("Estimated time: %.0f seconds\n", eta);
		}
		System.out.printf("Sample size: %d images\n", total);
		System.out.printf("SFW: %b\n", sfw);
		
		System.out.println("Confirm? (Y/N)");
		boolean confirm = sc.nextLine().toLowerCase().indexOf("y") != -1;
		
		if (!confirm)
		{
			return;
		}
		
		File outputDir = archiveMode ? new File("archive") : new File("trainingdata");
		
		if (!outputDir.exists())
		{
			outputDir.mkdir();
		}
		
		try
		{
			SQLUtils.initConnection(archiveMode ? "archive/sources.db" : "trainingdata/sources.db");
			SQLUtils.initTables();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		int unknown = 0;
		int remaining = total;
		int before_id = -1;
		int actual = total - remaining;
		while (remaining > 0)
		{
			String jsonData = getJSONDataNewest(sfw, remaining, before_id);
			ArrayList<Entry> entries = getEntriesFromJSONData(jsonData);
			
			if (entries.size() == 0)
			{
				System.out.printf("[!] We don't have %d pictures yet. (%d pictures found instead, exiting)\n", total, actual);
				break;
			}
			for (int i = 0; i < entries.size(); i++)
			{
				Entry entry = entries.get(i);
				String tags = entry.tags;
				try
				{
					int ratingInt = 0;
					if (entry.rating.equals("e"))
					{
						ratingInt = 2;
					}
					else if (entry.rating.equals("q"))
					{
						ratingInt = 1;
					}
					SQLUtils.addEntry(entry.id, tags, entry.artists.toString(), ratingInt, entry.score, entry.sources == null ? "unknown sources" : entry.sources.toString(), entry.creationDate);
				} 
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int accum = actual + i;
				System.out.printf("[%d / %d] %s\n", accum + 1, total, entry);
				
				if (archiveMode)
				{
					File f = new File(outputDir, "assets");
					if (!f.exists())
					{
						f.mkdir();
					}
					String[] extArr = entry.fileURL.split(Pattern.quote("."));
					downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					unknown++;
				}
				else
				{
					if (tags.contains("feline"))
					{
						File f = new File(outputDir, "feline");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else if (tags.contains("canine"))
					{
						File f = new File(outputDir, "canine");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else if (tags.contains("dragon"))
					{
						File f = new File(outputDir, "dragon");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else if (tags.contains("lagomorph"))
					{
						File f = new File(outputDir, "lagomorph");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else if (tags.contains("avian"))
					{
						File f = new File(outputDir, "avian");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else if (tags.contains("pokémon"))
					{
						// other pokemon
						File f = new File(outputDir, "pokemon");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
					}
					else
					{
						File f = new File(outputDir, "other");
						if (!f.exists())
						{
							f.mkdir();
						}
						String[] extArr = entry.fileURL.split(Pattern.quote("."));
						downloadFile(entry.fileURL, f, entry.id + "." + extArr[extArr.length - 1]);
						unknown++;
					}
				}
				before_id = entry.id;
			}
			remaining -= entries.size();
			actual = total - remaining;
		}
		System.out.printf("Total files downloaded: %d. Unknown: %d\n", actual, unknown);
		
	}
	
	public static void downloadFile(String link, File hostFolder, String name)
	{
		 URL url;
         URLConnection con;
         DataInputStream dis; 
         FileOutputStream fos; 
         byte[] fileData;  
         try {
        	 File toWrite = new File(hostFolder, name);
        	 if (!toWrite.exists())
        	 {
        		 System.out.println("Downloading new file " + link);
        		 url = new URL(link);
                 con = url.openConnection();
                 dis = new DataInputStream(con.getInputStream());
                 fileData = new byte[con.getContentLength()]; 
                 for (int q = 0; q < fileData.length; q++)
                 { 
                     fileData[q] = dis.readByte();
                 }
                 dis.close();
                 fos = new FileOutputStream(toWrite);
                 fos.write(fileData);
                 fos.close();
        	 }
        	 else
        	 {
        		 System.out.println("[!] File exists already.");
        	 }
         }
         catch(Exception m) 
         {
             m.printStackTrace();
         }
	}
	
	public static ArrayList<Entry> getEntriesFromJSONData(String jsonData)
	{
		ArrayList<Entry> al = new ArrayList<Entry>();
		JSONArray head = new JSONArray(jsonData);
		for (int i = 0; i < head.length(); i++)
		{
			JSONObject curObj = head.getJSONObject(i);
			String url = curObj.getString("file_url");
			String rating = curObj.getString("rating");
			String tags = curObj.getString("tags");
			int score = curObj.getInt("score");
			int id = curObj.getInt("id");
			JSONObject createdAt = curObj.getJSONObject("created_at");
			JSONArray artists = null;
			JSONArray sources = null;
			if (curObj.has("artist"))
			{
				artists = curObj.getJSONArray("artist");
			}
			if (curObj.has("sources"))
			{
				sources = curObj.getJSONArray("sources");
			}
			long creationdate = createdAt.getLong("s");
			al.add(new Entry(id, url, tags, rating, score, creationdate, artists, sources));
		}
		return al;
	}
	
	public static String getJSONDataNewest(boolean sfw, int imageCount, int before_id)
	{
		return getJSONDataNewest(sfw, imageCount, before_id, null);
	}
	
	public static String getJSONDataNewest(boolean sfw, int imageCount, int before_id, ArrayList<String> tags)
	{
		String url_base = "";
		if (sfw)
		{
			url_base += "https://e926.net/post/index.json?";
		}
		else
		{
			url_base += "https://e621.net/post/index.json?";
		}
		
		if (imageCount != -1)
		{
			url_base += "&limit=" + imageCount;
		}
		
		if (before_id != -1)
		{
			url_base += "&before_id=" + before_id;
		}
		
		if (tags != null)
		{
			url_base += "&tags=";
			for (int i = 0; i < tags.size(); i++)
			{
				String tag = tags.get(i).replaceAll(" ", "_");
				url_base += tag;
				if (i < tags.size() - 1)
				{
					url_base += "+";
				}
			}
		}
		
		try
		{
			URL jsonURL = new URL(url_base);
			URLConnection yc = jsonURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
			String inputLine;
			String returnData = "";
			while ((inputLine = in.readLine()) != null)
			{
				returnData += inputLine;
			}
			return returnData;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "[]";
	}
}
