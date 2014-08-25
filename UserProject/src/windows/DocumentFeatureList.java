package windows;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import datastructures.core.DataObject;

public class DocumentFeatureList extends JPanel {
	private static final long serialVersionUID = 1L;
	private JList documentList;
	private DefaultListModel listModel;
	private JTable featuresTable;
	
	public DocumentFeatureList() {
		super();
		this.setLayout(new BorderLayout());
		this.documentList = new JList();
		this.documentList.setFixedCellWidth(160);
		JScrollPane pane = new JScrollPane(this.documentList);
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Documents"));
		this.add(pane, BorderLayout.WEST);
		
		this.featuresTable = new JTable(new String[][] { {"LOL", "45" }, {"ROFL", "10" } }, new String[] { "Feature", "Weight" });
		pane = new JScrollPane(this.featuresTable);
		pane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Features"));
		this.add(pane, BorderLayout.CENTER);
	}

	public void updateList(DataObject[] documents) {
		this.documentList = new JList(documents);
	}
}
