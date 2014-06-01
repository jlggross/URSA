package br.ufrgs.inf.jlggross.documentclustering.window;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import br.ufrgs.inf.jlggross.clustering.ClusteringProcess;

public class ClusteringWindow extends JFrame implements Observer {
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	private JTabbedPane tabbedPane;
	
	private DocumentFeatureList documentFeatureList;
	
	private ClusteringProcess clustering;
	
	public ClusteringWindow(ClusteringProcess clustering) {
		super("Document Clustering");
		this.clustering = clustering;
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		this.add(this.progressBar, BorderLayout.PAGE_END);
		
		this.documentFeatureList = new DocumentFeatureList();
		
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.addTab("Selected features", this.documentFeatureList);
		this.tabbedPane.addTab("Similarity matrix", new JPanel());
		this.tabbedPane.addTab("Produced clusters", new JPanel());
		this.tabbedPane.addTab("Logs", new JPanel());
		
		this.add(this.tabbedPane, BorderLayout.CENTER);
		
		this.setMinimumSize(new Dimension(640, 480));
		this.pack();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	@Override
	public void update(Observable o, Object arg) {
		// this.progressBar.setValue(...);
		ClusteringProcess process = (ClusteringProcess) o;
		int step = process.getProcessedSteps();
		switch (step) {
		case 0:
			this.progressBar.setString("Selecting terms...");
			break;
		case 1:
			this.progressBar.setString("Calculating similarity...");
			this.documentFeatureList.updateList(this.clustering.getDataObjectsArray());
			break;
		case 2:
			this.progressBar.setString("Clustering documents...");
			break;
		case 3:
			this.progressBar.setString("Document clustering complete.");
		}
		int progress = (int) (process.getStepProgress() * 100);
		this.progressBar.setValue(progress);
	}

}
