import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class BookmarkList {
	
	private ArrayList<Bookmark> bm = new ArrayList<Bookmark>();
	
	BookmarkList() {			
	}
	
	BookmarkList(String bookmarkFileName){
		
		File file = new File(bookmarkFileName);
		if(!file.exists()) {		// ������ ���� ���
			System.err.println("Unknown BookmarkList data File ");    // error ǥ��
			System.exit(0);									// ���� ����
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
		
		mergeByGroup();
	}
	
	public void add(Bookmark b) {
		bm.add(b);
		mergeByGroup();
	}
	
	public void add(Bookmark b, int idx) {
		bm.add(idx, b);
		mergeByGroup();
	}
	
	public void delete(int idx) {
		bm.remove(idx);
		mergeByGroup();
	}
	
	public void delete(String name) {
		for(int i=0; i<bm.size(); i++) {
			if(bm.get(i).getName().equals(name))
				bm.remove(i);
		}
		mergeByGroup();
	}

	public void deleteByGroup(String group) {
		int idx = 0;
		for(int i=0; i<bm.size(); i++) {
			
			if(bm.get(i).getGroupName().equals(group)) {
				idx = i;
				break;
			}
		}
		while(true) {
			bm.remove(idx);
			if(bm.size() <= idx)
				break;
			if(!bm.get(idx).getGroupName().equals(group))
				break;
		}
	}
	
	public int numBookmarks() {
		return bm.size();
	}
	
	public int getIdx(String name) {
		for(int i=0; i<bm.size(); i++) {
			if(bm.get(i).getName().equals(name))
				return i;
		}
		return -1;
	}
	
	public Bookmark[] getBookmarkByGroup(String group) {
		int cnt = 0, idx = 0;
		for(int i=0; i<bm.size(); i++) {
			if(bm.get(i).getGroupName().equals(group)) {
				idx = i;
				cnt++;
			}
		}
		Bookmark[] bma = new Bookmark[cnt];
		for(int i=0; i<cnt; i++) {
			bma[i] = bm.get(idx - cnt + 1 + i);
		}
		return bma;
	}
	
	public Bookmark getBookmark(int i) {
		return bm.get(i);
	}
	
	public void fileToList(String line) {
		String token[] = line.split("[,;]");
		String info[] = new String[5];
		
		if(token[1].isBlank()) {		// url�� ������ �� == �ð��� �Ѱ����� ���� ��
			bm.add(new Bookmark(token[2]));
			return;
		}

		for(int i=0; i<info.length; i++) {
			if(token.length <= i)	// ������ memo�� ���鵵 ���� ���
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
