import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkListPanel extends JPanel{

	DefaultTableModel model = new DefaultTableModel();
	JTable table = new JTable(model);
	JScrollPane scrollPane = new JScrollPane(table);
	
	public BookmarkListPanel(BookmarkList bList) {
		
		scrollPane.setPreferredSize(new Dimension(600, 400));
		
		String[] headers = {" ", "Group", "Name", "URL", "Created Time", "Memo"};
		model.setColumnCount(headers.length); 
		model.setColumnIdentifiers(headers);
		
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
	
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(190);
		table.getColumnModel().getColumn(4).setPreferredWidth(140);
		table.getColumnModel().getColumn(5).setPreferredWidth(70);
		
		this.add(scrollPane);
		
		// open하면 insertRow로 추가
	}
}
