package inspector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Inspector extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTree clusterTree;
	private JTable termsTable;
	private DefaultTableModel tableModel;
	
	public Inspector() {
		super("Document Clustering Inspector");
		this.setSize(600, 460);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JTabbedPane tabs = new JTabbedPane();
		JPanel tab1 = new JPanel();
		tab1.setLayout(new BorderLayout());
		JPanel tab2 = new JPanel();
		tab2.setLayout(new BorderLayout());
		
		this.setupTree();
		JScrollPane pane1 = new JScrollPane(this.clusterTree);	
		tab1.add(pane1, BorderLayout.WEST);
		
		this.setupTermsTable();
		JScrollPane pane2 = new JScrollPane(this.termsTable);		
		tab1.add(pane2, BorderLayout.CENTER);
		
		this.clusterTree.setSelectionRow(1);
		
		tab2.add(setupChart());
		
		tabs.add("Clusters", tab1);
		tabs.add("Chart", tab2);
		this.add(tabs);
		
		this.setVisible(true);
	}
	
	public void setupTree() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("output/clusters.txt"));
			
			String line = "";
			int c = 1;
			while ((line = reader.readLine()) != null) {
				int clusterSize = Integer.valueOf(line);
				DefaultMutableTreeNode cluster = new DefaultMutableTreeNode("Cluster " + c++);
				for (int i = 0; i < clusterSize; i++) {
					String[] title = reader.readLine().split("\\\\");
					DefaultMutableTreeNode document = new DefaultMutableTreeNode(title[title.length-1]);
					cluster.add(document);
				}
				root.add(cluster);
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.clusterTree = new JTree(root);
		this.clusterTree.addTreeSelectionListener(new TreeSelectionListener() {
		    public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		                           clusterTree.getLastSelectedPathComponent();

		    /* if nothing is selected */ 
		        if (node == null) return;

		    /* retrieve the node that was selected */ 
		        Object nodeInfo = node.getUserObject();
		    /* React to the node selection. */
		        String doc = nodeInfo.toString();
		        if (doc.endsWith(".txt")) {
			        String[] aux = doc.split("\\\\");
			        refreshTermsTable(aux[aux.length-1]);
		        }
		    }
		});
		this.clusterTree.setRootVisible(false);
		for (int i = 0; i < this.clusterTree.getRowCount(); i++) {
			this.clusterTree.expandRow(i);
		}
	}
	
	public void setupTermsTable() {
		this.tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
			Class<?>[] types = { String.class, Integer.class, Double.class };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return this.types[columnIndex];
            }
        };
		this.tableModel.setColumnIdentifiers(new String[] { "Term", "Absolute Frequency", "Relative Frequency" });
		this.termsTable = new JTable(this.tableModel);
		this.termsTable.setAutoCreateRowSorter(true);
	}
	
	public void refreshTermsTable(String document) {
		while (this.tableModel.getRowCount() > 0) {
			this.tableModel.removeRow(0);
		}
		
		try {
			String[] title = document.split("\\\\");
			
			BufferedReader reader = new BufferedReader(new FileReader("output/documents/" + title[title.length-1]));
			
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] values = line.replace(" ", "").split(";");
					Object[] objects = new Object[] { values[0], Integer.valueOf(values[1]), Double.valueOf(values[2].replace(",", ".")) };
					this.tableModel.addRow(objects);
				}
			}
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.termsTable.getRowSorter().toggleSortOrder(1);
		this.termsTable.getRowSorter().toggleSortOrder(1);
	}
	
	private ChartPanel setupChart() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//dataset.addValue(1.0, "First", "First");
		// TODO Add the size of each cluster!
		
		TreeModel model = this.clusterTree.getModel();
		Object root = model.getRoot();
		for (int i = 0; i < model.getChildCount(root); i++) {
			dataset.addValue(model.getChildCount(model.getChild(root, i)), "Cluster " + (i+1), "");
		}
		
		//JFreeChart chart = ChartFactory.createXYBarChart("Clusters", "Clusters", false, "Size", dataset, PlotOrientation.VERTICAL, true, true, false);
		JFreeChart chart = ChartFactory.createBarChart("", "Clusters", "Size", dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setFillZoomRectangle(true);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        return chartPanel;
	}
	
	public static void main(String args[]) {
		new Inspector();
	}
}
