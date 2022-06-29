import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BookmarkInfoGUI extends JFrame {
	
	public BookmarkInfoGUI(BookmarkList bList) {
		super("Input New Bookmark");
		this.setLayout(new BorderLayout());
	
		DefaultTableModel model = new DefaultTableModel();
		JTable inputTable = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(inputTable);
		scrollPane.setPreferredSize(new Dimension(500, 80));
		
		JButton input = new JButton("INPUT");
		
		String[] headers = {"Group", "Name", "URL", "Memo"};
		model.setColumnCount(headers.length); 
		model.setColumnIdentifiers(headers);
		model.addRow(new String[] {"", "", "", ""});
		
		inputTable.getColumnModel().getColumn(0).setPreferredWidth(110);
		inputTable.getColumnModel().getColumn(1).setPreferredWidth(110);
		inputTable.getColumnModel().getColumn(2).setPreferredWidth(190);
		inputTable.getColumnModel().getColumn(3).setPreferredWidth(110);
		
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String group = (String) inputTable.getValueAt(0, 0);
				String name = (String) inputTable.getValueAt(0, 1);
				String url = (String) inputTable.getValueAt(0, 2);
				String memo = (String) inputTable.getValueAt(0, 3);
				String[] info = {name, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")), url, group, memo};
				
				Bookmark b = new Bookmark(info);
				bList.add(b);
				
				dispose();
			}	
		});

		this.add(scrollPane, BorderLayout.CENTER);
		this.add(input, BorderLayout.EAST);
		
		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);	
	}

}
