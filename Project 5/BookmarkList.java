import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class BookmarkList {
	
	ArrayList<Bookmark> bm = new ArrayList<Bookmark>();
	
	BookmarkList() {			
	}
	
	BookmarkList(String bookmarkFileName){
		
		File file = new File(bookmarkFileName);
		if(!file.exists()) {		// 파일이 없는 경우
			System.err.println("Unknown BookmarkList data File ");    // error 표시
			System.exit(0);									// 강제 종료
		}
		
		Scanner input = null;
		String line;
		
		try {
			input = new Scanner(file);
		}
		catch(Exception e) {
			System.err.println("Unknown Bookmark Data File");
		}
		while (input.hasNext()) {
			line = input.nextLine();
			
			if(line.isEmpty() || line.startsWith("//"))
				continue;

			this.fileToList(line);
		}
		input.close();	
	}

	public int numBookmarks() {
		return bm.size();
	}
	
	public Bookmark getBookmark(int i) {
		return bm.get(i);
	}
	
	public void fileToList(String line) {
		String token[] = line.split("[,;]");
		String info[] = new String[5];
		
		if(token[1].isBlank()) {		// url만 들어왔을 떄 == 시간도 넘겨주지 않을 때
			bm.add(new Bookmark(token[2]));
			return;
		}

		for(int i=0; i<info.length; i++) {
			if(token.length <= i)	// 마지막 memo가 공백도 없는 경우
				info[i] = "";
			else if(token[i].isBlank() || token[i].trim().isBlank())
				info[i] = "";
			else
				info[i] = token[i].trim();
		}
		
		
		if(!this.timeDateFormat(info[1])) {
			System.err.println("Date Format Error -> No Created Time invalid Bookmark info line: "+line);
			return;
		}
		
		if(!this.timeCheck(info[1])) {
			System.err.println("Time Error -> Created time is later than the current time: "+line);
			return;
		}
		
		if(info[2].length() == 0) {
			System.err.println("MalformedURLException: wrong URL - No URL ; invalid Bookmark info line: "+line);
			return;
		}
		
		bm.add(new Bookmark(info));
	}
	
	
	public void listToFile(File file) {
		
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileWriter(file));	
		}
		catch(Exception e) {
			System.err.println("Unknown Bookmark Data File");
		}
		
		
		for(int i=0; i<bm.size(); i++) {
			output.println(bm.get(i).getInfo());
		}
		
		output.close();
	}
	
	public void mergeByGroup() {	
		
		for(int i=0; i<bm.size(); i++) {
			if(this.bm.get(i).getGroupName().equals(""))
				continue;
			
			
			for(int j=i+1; j<bm.size(); j++) {
				if(this.bm.get(j).getGroupName().equals(this.bm.get(i).getGroupName())) {
					Bookmark temp = bm.get(j);
					bm.remove(j);
					bm.add(i+1, temp);
					break;
				}
			}	
		}
	}
	
	boolean timeCheck(String time) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
		LocalDateTime t = LocalDateTime.parse(time, dtf);
		
		if(now.isBefore(t))
			return false;
		
		return true;
	}
	
	
	boolean timeDateFormat(String date) {
		if(date.charAt(4) != '-' | date.charAt(7) != '-' | date.charAt(10) != '_' | date.charAt(13) !=':')
			return false;
		
		return true;
	}
}
