import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BookmarkManagerGUI extends JFrame {
	
	private BookmarkList bList = new BookmarkList("bookmark.txt");
	private BookmarkListPanel table = new BookmarkListPanel(bList);
	private int[] state = new int[101];
	
	public BookmarkManagerGUI() {
		super("Bookmark Manager");
		this.setLayout(new BorderLayout());
				
		JPanel btn = new JPanel(new GridLayout(5,1));

		JButton add = new JButton("ADD");
		JButton del = new JButton("DELETE");
		JButton up = new JButton("UP");
		JButton down = new JButton("DOWN");
		JButton save = new JButton("SAVE");
		
		btn.add(add);
		btn.add(del);
		btn.add(up);
		btn.add(down);
		btn.add(save);
		
		reset();
			
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BookmarkInfoGUI big = new BookmarkInfoGUI(bList);	
				big.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						table.repaint(bList);
						reset();
					}
				});	
			}
		});
		
		del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				int row = table.table.getSelectedRow();
				if(row < 0) {
					JOptionPane.showMessageDialog(new JButton("OK"), "Select the Bookmark in the Table !", "WARNING", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String name = (String) table.table.getValueAt(row, 2);
				if(!name.equals("")) {									// 북마크 하나 삭제 처리
					bList.delete(name);
					reset();			
					table.repaint(bList);	
				}
				else {													// 북마크 그룹 전체 삭제 처리
					String group = (String) table.table.getValueAt(row, 1);
					bList.deleteByGroup(group);
					reset();
					table.repaint(bList);
				}		
			}	
		});
		
		up.addActionListener(new ActionListener() {	

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.table.getSelectedRow();
				if(row > table.table.getRowCount() - 1 || row <= 0)
					return;
				
				String name = (String) table.table.getValueAt(row, 2);
				if(!name.equals("")) {									// 북마크 하나 이동
					int idx = bList.getIdx(name);
				
					if(idx == 0)
						return;
					
					Bookmark b = bList.getBookmark(idx);
					bList.delete(idx);
					table.model.removeRow(row);
					
					if(!bList.getBookmark(idx-1).getGroupName().equals("")) {		// 위가 그룹일 때	
						String g = bList.getBookmark(idx-1).getGroupName();
						while(idx > 0)
							if(bList.getBookmark(idx-1).getGroupName().equals(g))
								idx--;
							else
								break;
						
						if(idx == 0) {
							bList.add(b, 0);	
							table.model.insertRow(0, new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
						}
						else {
							bList.add(b, idx);
							reset();
							
							int cnt = 0;
							for(int i=0; i<idx; i++) {							
								if(state[i] == table.onlyB || state[i] == table.closeFirst)
									cnt++;
								if(state[i] == table.openFirst)
									cnt += 2;
							}
							table.model.insertRow(cnt, new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
						}
					}
					else {						// 위가 개별 북마크일 때
						bList.add(b, idx-1);
						table.model.insertRow(row-1, new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
					}
				}
				
				else {															// 북마크 그룹 전체 이동
					String group = (String) table.table.getValueAt(row, 1);
					Bookmark[] ba = bList.getBookmarkByGroup(group);
					int idx = bList.getIdx(ba[0].getName());

					bList.deleteByGroup(group);
					table.model.removeRow(row);
					
					if(!bList.getBookmark(idx-1).getGroupName().equals("")) {		// 위가 그룹일 때	
						String g = bList.getBookmark(idx-1).getGroupName();	
						
						while(idx > 0)
							if(bList.getBookmark(idx-1).getGroupName().equals(g))
								idx--;
							else
								break;
			
						if(idx == 0) {
							for(int i=0; i<ba.length; i++) 
								bList.add(ba[i], i);
							table.model.insertRow(0, new String[] {">", ba[0].getGroupName(), "", "", "", ""});
						}	
						else {
							for(int i=0; i<ba.length; i++) 
								bList.add(ba[i], idx+i);
							reset();

							int cnt = 0;
							for(int i=0; i<idx; i++) {							
								if(state[i] == table.onlyB || state[i] == table.closeFirst)
									cnt++;
								if(state[i] == table.openFirst)
									cnt += 2;
							}
							table.model.insertRow(cnt, new String[] {">", ba[0].getGroupName(), "", "", "", ""});
						}		
					}
					else {							// 위가 개별 북마크일 때
						for(int i=0; i<ba.length; i++) 
							bList.add(ba[i], idx+i-1);
						table.model.insertRow(row-1, new String[] {">", ba[0].getGroupName(), "", "", "", ""});
					}
				}
				reset();
			}
		});
		
		
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.table.getSelectedRow();
				if(row >= table.table.getRowCount() - 1 || row < 0)
					return;
				
				String name = (String) table.table.getValueAt(row, 2);
				if(!name.equals("")) {									// 북마크 하나 이동
					int idx = bList.getIdx(name);
				
					Bookmark b = bList.getBookmark(idx);
					bList.delete(idx);
					table.model.removeRow(row);

					if(!bList.getBookmark(idx).getGroupName().equals("")) {			// 아래가 그룹일 때
						String g = bList.getBookmark(idx).getGroupName();
						while(idx <= bList.numBookmarks() - 1)
							if(bList.getBookmark(idx).getGroupName().equals(g)) 
								idx++;
							else
								break;
						
						if(idx == bList.numBookmarks()) {
							bList.add(b);
							table.model.addRow(new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
						}
						else {
							bList.add(b, idx);
							reset();
							
							int cnt = 0;
							for(int i=0; i<=idx; i++) {							
								if(state[i] == table.onlyB || state[i] == table.closeFirst)
									cnt++;
								if(state[i] == table.openFirst)
									cnt += 2;
							}
							table.model.insertRow(cnt+1, new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});

						}
					}
					else {						
						bList.add(b, idx+1);			
						table.model.insertRow(row+1, new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
					}
					reset();
				}
				
				else {															// 북마크 그룹 전체 이동
					String group = (String) table.table.getValueAt(row, 1);
					Bookmark[] ba = bList.getBookmarkByGroup(group);
					int idx = bList.getIdx(ba[0].getName());
					
					if(idx + ba.length == bList.numBookmarks())
						return;
						
					bList.deleteByGroup(group);
					table.model.removeRow(row);

					if(!bList.getBookmark(idx).getGroupName().equals("")) {		// 아래가 그룹일 때
						String g = bList.getBookmark(idx).getGroupName();
						while(idx <= bList.numBookmarks() - 1)
							if(bList.getBookmark(idx).getGroupName().equals(g)) 
								idx++;
							else
								break;
						
						if(idx == bList.numBookmarks()) {
							for(int i=0; i<ba.length; i++) 
								bList.add(ba[i]);
							table.model.addRow(new String[] {">", ba[0].getGroupName(), "", "", "", ""});
						}
							
						else {
							for(int i=0; i<ba.length; i++) 
								bList.add(ba[i], idx+i);
							reset();
							
							int cnt = 0;
							for(int i=0; i<=idx; i++) {							
								if(state[i] == table.onlyB || state[i] == table.closeFirst)
									cnt++;
								if(state[i] == table.openFirst)
									cnt += 2;
							}
							table.model.insertRow(cnt-1, new String[] {">", ba[0].getGroupName(), "", "", "", ""});
						}		
					}
					else {						// 아래가 개별 북마크일 때
						for(int i=0; i<ba.length; i++) 
							bList.add(ba[i], idx+i+1);
						table.model.insertRow(row+1, new String[] {">", ba[0].getGroupName(), "", "", "", ""});
					}	
					reset();
				}
			}	
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = new File("bookmark.txt");
				bList.listToFile(file);
			}	
		});
		
		
		table.table.addMouseListener(new MouseAdapter() {			// open, close
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.table.getSelectedRow();
				int col = table.table.getSelectedColumn();
				if(col == 0) {
					String s = (String) table.table.getValueAt(row, col);
					if(s.equals(">")) {
						table.table.setValueAt("v", row, col);
						String groupName = (String) table.table.getValueAt(row, 1);
						Bookmark[] bg = bList.getBookmarkByGroup(groupName);
						int idx = bList.getIdx(bg[0].getName());
						state[idx++] = table.openFirst;
						for(int i=0; i< bg.length-1; i++) {
							state[idx+i] = table.onlyB;
						}					
					}
					else if (s.equals("v")){
						table.table.setValueAt(">", row, col);
						String groupName = (String) table.table.getValueAt(row, 1);
						Bookmark[] bg = bList.getBookmarkByGroup(groupName);
						int idx = bList.getIdx(bg[0].getName());
						state[idx++] = table.closeFirst;
						for(int i=0; i< bg.length-1; i++) {
							state[idx+i] = table.closeAfter;
						}				
					}
					table.repaint(bList, state);
				}
			}		
		});
		
		this.add(table, BorderLayout.CENTER);
		this.add(btn, BorderLayout.EAST);
		
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void reset() {				// state 배열 초기화
		for(int i=0; i<bList.numBookmarks(); i++) {			
			Bookmark b = bList.getBookmark(i);
			if(!b.getGroupName().equals("")) {	
				Bookmark[] bl = bList.getBookmarkByGroup(b.getGroupName());
				state[i] = table.closeFirst;
				for(int j=1; j<bl.length; j++) {
					state[++i] = table.closeAfter;
				}
			}
			else
				state[i] = table.onlyB;
		}
	}
	
}