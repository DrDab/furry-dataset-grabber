
public class Entry 
{
	public int id;
	public String fileURL;
	public String tags;
	public String rating;
	public int score;
	
	public Entry(int id, String fileURL, String tags, String rating, int score)
	{
		this.id = id;
		this.fileURL = fileURL;
		this.tags = tags;
		this.rating = rating;
		this.score = score;
	}
	
	public String toString()
	{
		return "id=" + id + " url=" + fileURL + " tags=(" + tags + ") rating=" + rating + " score=" + score;
	}
}
