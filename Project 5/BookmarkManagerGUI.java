import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BookmarkManagerGUI extends JFrame {
	
	public BookmarkManagerGUI() {
		
		super("Bookmark Manager");
		this.setLayout(new BorderLayout());
		
		BookmarkList bList = new BookmarkList("bookmark.txt");
		bList.mergeByGroup();
		
		BookmarkListPanel table = new BookmarkListPanel(bList);
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
		
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BookmarkInfoGUI(bList);
			}
		});
		
		this.add(table, BorderLayout.CENTER);
		this.add(btn, BorderLayout.EAST);
		
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

}