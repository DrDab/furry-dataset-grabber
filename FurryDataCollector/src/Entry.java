
public class Entry 
{
	public int id;
	public String fileURL;
	public String tags;
	public String rating;
	public int score;
	public long creationDate;
	
	public Entry(int id, String fileURL, String tags, String rating, int score, long creationDate)
	{
		this.id = id;
		this.fileURL = fileURL;
		this.tags = tags;
		this.rating = rating;
		this.score = score;
		this.creationDate = creationDate;
	}
	
	public String toString()
	{
		return "id=" + id + " url=" + fileURL + " tags=(" + tags + ") rating=" + rating + " score=" + score + " creationdate=" + creationDate;
	}
}
