package datastructures.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Matrix2D {
	private double[][] matrix;
	private int width;
	
	public Matrix2D(int width) {
		this.width = width;
		this.matrix = new double[this.width][this.width];
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void set(int x, int y, double value) {
		if ((x >= 0) && (x < this.width) && (y >= 0) && (y < this.width))
			this.matrix[x][y] = value;
	}
	
	public double get(int x, int y) {
		if (x > y) {
			int aux = x;
			x = y;
			y = aux;
		}
		
		if ((x >= 0) && (x < this.width) && (y >= 0) && (y < this.width))
			return this.matrix[x][y];
		
		return 0;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
    	for (int i = 0; i < this.width; i++) {
    		for (int j = 0; j < this.width; j++) {
    			if (this.matrix[i][j] >= 0.01) {
        			str.append(String.format("%.2f ", this.matrix[i][j]));
    			} else {
    				str.append(";;;; ");
    			}
    		}
    		str.append("\n");
    	}
    	String s = str.toString();
    	s.replace(",", ".");
    	return s;
    }
	
	public void loadMatrix(String filename, int numberOfFiles) {
		String line = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int i = 0, j = 0;
		try {
			while (null != (line = reader.readLine())) {
				line = line.replace(",", ".");
				String[] tokens = line.split(" ");
				for (; i < numberOfFiles; i++) {
					if (tokens[i].equals(";;;;"))
						if (i == j)
							this.set(i, j, 1.0);
						else
							continue;
					else {
						this.set(i, j, Double.parseDouble(tokens[i]));
						this.set(j, i, Double.parseDouble(tokens[i]));
					}
				}
				i = 0;
				j++;
			}
		} catch (NumberFormatException e) {
			System.out.println("NumberFormatException in function loadMatrix of class Matrix2D.java");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException in function loadMatrix of class Matrix2D.java");
			e.printStackTrace();
		}
	}
}
