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

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
