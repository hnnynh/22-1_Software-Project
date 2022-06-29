import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkInfoGUI extends JFrame {
	
	public BookmarkInfoGUI(BookmarkList bList) {
		super("Input New Bookmark");
		this.setLayout(new BorderLayout());
	
		JButton input = new JButton("INPUT");
			
		DefaultTableModel model = new DefaultTableModel();
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(500, 80));
		
		String[] headers = {"Group", "Name", "URL", "Memo"};
		model.setColumnCount(headers.length); 
		model.setColumnIdentifiers(headers);
		model.addRow(new String[] {"", "", "", ""});
		
		table.getColumnModel().getColumn(0).setPreferredWidth(110);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(190);
		table.getColumnModel().getColumn(3).setPreferredWidth(110);

		this.add(scrollPane, BorderLayout.CENTER);
		this.add(input, BorderLayout.EAST);
		
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);	
	}

}
