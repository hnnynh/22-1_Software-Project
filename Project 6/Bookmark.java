import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bookmark {
	
	private String name = "";
	private String time = "";
	private String url = "";
	private String groupName = "";
	private String memo = "";
	
	Bookmark(){
	}
	
	Bookmark(String url){
		this.url = url;
		this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm"));
	}
	
	Bookmark(String[] info){
		this.name = info[0];
		this.time = info[1];
		this.url = info[2];
		this.groupName = info[3];
		this.memo = info[4];
	}
	
	public void print() {
		System.out.println(this.getInfo());
	}
	
	public String getInfo() {
		return (this.name+";"+this.time+";"+this.url+";"+this.groupName+";"+this.memo);
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public String getUrl() {
		return url;
	}

	public String getMemo() {
		return memo;
	}

	public String getGroupName() {
		return groupName;
	}

}
