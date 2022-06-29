import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkListPanel extends JPanel{

	protected DefaultTableModel model = new DefaultTableModel();
	protected JTable table = new JTable(model);
	protected final int onlyB = 0;
	protected final int closeFirst = 1;
	protected final int openFirst = 2;
	protected final int closeAfter = 3;
	
	public BookmarkListPanel(BookmarkList bList) {
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(600, 400));
		
		String[] headers = {" ", "Group", "Name", "URL", "Created Time", "Memo"};
		model.setColumnCount(headers.length); 
		model.setColumnIdentifiers(headers);
		
		paint(bList);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(190);
		table.getColumnModel().getColumn(4).setPreferredWidth(140);
		table.getColumnModel().getColumn(5).setPreferredWidth(70);
		
		this.add(scrollPane);
		
	}
	
	public void paint(BookmarkList bList) {
		String gn = "";
		for(int i=0; i<bList.numBookmarks(); i++) {
			Bookmark b = bList.getBookmark(i);
			if(!b.getGroupName().equals("")) {
				if(!gn.equals(b.getGroupName())) {
					model.addRow(new String[] {">", b.getGroupName(), "", "", "", ""});
					gn = b.getGroupName();
				}
				else
					continue;
			}	
			else
				model.addRow(new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
		}	
	}
	
	public void paint(BookmarkList bList, int[] state) {		
		for(int i=0; i<bList.numBookmarks(); i++) {
			Bookmark b = bList.getBookmark(i);

			switch(state[i]) {
			case onlyB:
				model.addRow(new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
				break;
			case closeFirst:
				model.addRow(new String[] {">", b.getGroupName(), "", "", "", ""});
				break;
			case openFirst:
				model.addRow(new String[] {"v", b.getGroupName(), "", "", "", ""});
				model.addRow(new String[] {"", b.getGroupName(), b.getName(), b.getUrl(), b.getTime(), b.getMemo()});
				break;
			case closeAfter:
				break;
			default:
			}
		}
	}
	
	public void repaint(BookmarkList bList) {
		model.setNumRows(0);			// 초기화
		paint(bList);
	}
	
	public void repaint(BookmarkList bList, int[] state) {
		model.setNumRows(0);			// 초기화
		paint(bList, state);
	}
	
}
