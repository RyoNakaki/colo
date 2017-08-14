package lib;

import net.sf.javaml.clustering.mcl.SparseMatrix;

public class SquareMatrix extends Matrix{
	private int size;
	
	public int getSize(){return this.size;}
	public void setSize(int size){this.size=size;}
	
	public SquareMatrix(){
		super();
		this.size=0;
	}
	
	public SquareMatrix(int size){
		super(size,size);
		this.size=size;
	}
	
	public SquareMatrix(SquareMatrix m){
		super(m);
		this.size=this.getRowSize();
	}
	
	public SquareMatrix(SparseMatrix m){
		super(m);
		this.size=this.getRowSize();
	}
	
	public SquareMatrix(String path){
		super(path);
		this.size=this.getRowSize();
	}
	
	public void diagonalize(){
		int i,j;
		
		for(i=0;i<this.size;i++){
			for(j=0;j<this.size;j++){
				if(i!=j) this.setElement(i,j,0.0);
			}
		}
	}
}
