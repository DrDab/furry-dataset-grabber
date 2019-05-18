import org.json.JSONArray;

public class Entry 
{
	public int id;
	public String fileURL;
	public String tags;
	public String rating;
	public int score;
	public long creationDate;
	public JSONArray artists;
	public JSONArray sources;
	
	public Entry(int id, String fileURL, String tags, String rating, int score, long creationDate, JSONArray artists, JSONArray sources)
	{
		this.id = id;
		this.fileURL = fileURL;
		this.tags = tags;
		this.rating = rating;
		this.score = score;
		this.creationDate = creationDate;
		this.artists = artists;
		this.sources = sources;
	}
	
	public String toString()
	{
		String tmp = "id=" + id + " url=" + fileURL + " tags=(" + tags + ") rating=" + rating + " score=" + score + " creationdate=" + creationDate;
		if (artists != null)
		{
			tmp += " artists=" + artists;
		}
		if (sources != null)
		{
			tmp += " sources=" + sources;
		}
		return tmp;
	}
}
