package datastructures.core;

import java.util.Comparator;

public class Pair {  
    private Double similarity;  
    private Integer index;  
  
    public Pair(Double similarity, Integer index) {  
        this.similarity = similarity;  
        this.index = index;  
    }  
  
    public Double getSimilarity() {  
        return this.similarity;  
    }  
  
    public Integer getIndex() {  
        return this.index;  
    }  
    
    @Override  
    public boolean equals(Object o) {  
        if (o == null)  
        {  
            return false;  
        }  
        if (!(o instanceof Pair))  
        {  
            return false;  
        }  
        Pair pairo = (Pair) o;  
        return this.similarity.equals(pairo.getSimilarity()) &&  
               this.index.equals(pairo.getIndex());  
    }  
  
    @Override  
    public String toString() {  
        return "Pair{" +  
                "m_left=" + this.similarity +  
                ", m_right=" + this.index +  
                '}';  
    }  
    
    public static class PairSimilarityComparator implements Comparator<Pair> {
    	@Override
        public int compare(Pair a, Pair b) {
            return (a.getSimilarity()).compareTo(b.getSimilarity());
        }
    }
    
    public static class PairIndexComparator implements Comparator<Pair> {
    	@Override
        public int compare(Pair a, Pair b) {
            return (a.getIndex()).compareTo(b.getIndex());
        }
    }
}  