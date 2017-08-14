package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.sf.javaml.clustering.mcl.SparseMatrix;

public class Matrix{
	private double[][] element;
	private int rowSize,columnSize;
	
	public enum VectorOption{ROW,COLUMN}
	public enum MultiplyOption{LEFT,RIGHT}
	
	public double[][] getElement(){return this.element;}
	public double getElement(int i,int j){return this.element[i][j];}
	public int getRowSize(){return this.rowSize;}
	public int getColumnSize(){return this.columnSize;}
	
	public void setElement(int i,int j,double v){this.element[i][j]=v;}
	public void setElement(double[][] element){this.element=(double[][])CopyUtil.deepCopyObject(element);}
	public void setRowSize(int rowSize){this.rowSize=rowSize;}
	public void setColumnSize(int columnSize){this.columnSize=columnSize;}
	
	public Matrix(){
		this.element=null;
		this.rowSize=0;
		this.columnSize=0;
	}
	
	public Matrix(int rowSize,int columnSize){
		this.element=new double[rowSize][columnSize];
		this.rowSize=rowSize;
		this.columnSize=columnSize;
	}
	
	public Matrix(double[][] element){
		this.element=(double[][])CopyUtil.deepCopyObject(element);
		this.rowSize=element.length;
		this.columnSize=element[0].length;
	}
	
	public Matrix(Matrix m){
		this.element=(double[][])CopyUtil.deepCopyObject(m.element);
		this.rowSize=m.rowSize;
		this.columnSize=m.columnSize;
	}
	
	public Matrix(SparseMatrix m){
		int i,j;
		this.rowSize=m.getSize()[0];
		this.columnSize=m.getSize()[1];
		this.element=new double[this.rowSize][this.columnSize];
		
		for(i=0;i<this.rowSize;i++){
			for(j=0;j<this.columnSize;j++){
				this.element[i][j]=m.get(i,j);
			}
		}
	}
	
	public Matrix(String path){
		try{
			int i,j;
			BufferedReader br=new BufferedReader(new FileReader(path));
			String buff;
			
			i=0;
			while((buff=br.readLine())!=null){
				String[] v=buff.split("\\t");
				
				if(i==0) this.columnSize=v.length;
				i++;
			}
			br.close();
			this.rowSize=i;
			this.element=new double[this.rowSize][this.columnSize];
			
			br=new BufferedReader(new FileReader(path));
			i=0;
			while((buff=br.readLine())!=null){
				String[] v=buff.split("\\t");
				
				for(j=0;j<this.getColumnSize();j++) this.element[i][j]=Double.parseDouble(v[j]);
				i++;
			}
			br.close();
		}catch(IOException e){e.printStackTrace();}
	}
	
	public void disp(String path){
		int i,j;
		
		if(path!=null){
			try{
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
				pw.print("\t");
				for(i=0;i<this.columnSize;i++) pw.print("["+(i+1)+"]\t");	
				pw.println();
				
				for(i=0;i<this.rowSize;i++){
					pw.print("["+(i+1)+"]\t");
					for(j=0;j<this.columnSize;j++) pw.printf("%.4f\t",this.element[i][j]);
					pw.println();
				}
				pw.close();
			}catch(IOException e){e.printStackTrace();}
		}
		else{
			System.out.print("\t");
			for(i=0;i<this.columnSize;i++) System.out.print("["+(i+1)+"]\t");
			System.out.println();
			
			for(i=0;i<this.rowSize;i++){
				System.out.print("["+(i+1)+"]\t");
				for(j=0;j<this.columnSize;j++) System.out.printf("%.4f\t",this.element[i][j]);
				System.out.println();
			}
		}
	}
	
	public void multiply(Matrix m,MultiplyOption opt){
		int i,j,k;
		double[][] element=new double[this.rowSize][m.columnSize];
		
		if(opt==MultiplyOption.RIGHT){
			for(i=0;i<this.rowSize;i++){
				for(j=0;j<m.columnSize;j++){
					for(k=0;k<this.columnSize;k++) element[i][j]+=this.element[i][k]*m.element[k][j];
				}
			}
			
			this.element=element;
			this.columnSize=m.columnSize;
		}
		else{
			for(i=0;i<m.rowSize;i++){
				for(j=0;j<this.columnSize;j++){
					for(k=0;k<m.columnSize;k++) element[i][j]+=m.element[i][k]*this.element[k][j];
				}
			}
			
			this.element=element;
			this.rowSize=m.rowSize;
		}
	}
	
	public void transpose(){
		int i,j;
		double[][] element=new double[this.columnSize][this.rowSize];
		
		for(i=0;i<this.rowSize;i++){
			for(j=0;j<this.columnSize;j++) element[j][i]=this.element[i][j];
		}
		
		this.element=element;
		this.rowSize=element.length;
		this.columnSize=element[0].length;
	}
	
	public double getMass(int i,VectorOption opt){
		int j;
		double mass=0.0;
		
		if(opt==VectorOption.ROW){
			for(j=0;j<this.columnSize;j++) mass+=this.element[i][j];
		}
		else{
			for(j=0;j<this.rowSize;j++) mass+=this.element[j][i];
		}
		
		return mass;
	}
	
	public double getMean(int i,VectorOption opt){
		if(opt==VectorOption.ROW) return this.getMass(i,VectorOption.ROW)/(double)this.columnSize;
		else return this.getMass(i,VectorOption.COLUMN)/(double)this.rowSize;
	}
	
	public double getVariance(int i,VectorOption opt){
		int j;
		double v=0.0;
		
		if(opt==VectorOption.ROW){
			for(j=0;j<this.columnSize;j++) v+=java.lang.Math.pow(this.element[i][j]-this.getMean(i,VectorOption.ROW),2.0);
			v/=(double)this.columnSize;
		}
		else{
			for(j=0;j<this.rowSize;j++) v+=java.lang.Math.pow(this.element[j][i]-this.getMean(i,VectorOption.COLUMN),2.0);
			v/=(double)this.rowSize;
		}
		
		return v;
	}
	
	public double getStandardDeviation(int i,VectorOption opt){
		double v=0.0;
		
		if(opt==VectorOption.ROW) v=java.lang.Math.sqrt(this.getVariance(i,VectorOption.ROW));
		else v=java.lang.Math.sqrt(this.getVariance(i,VectorOption.COLUMN));
		
		return v;
	}
	
	public double getMaxValue(int i,VectorOption opt){
		int j;
		double max;
		
		if(opt==VectorOption.ROW){
			max=this.element[i][0];
			for(j=1;j<this.columnSize;j++){
				if(max<this.element[i][j]) max=this.element[i][j];
			}
		}
		else{
			max=this.element[0][i];
			for(j=1;j<this.rowSize;j++){
				if(max<this.element[j][i]) max=this.element[j][i];
			}
		}
		
		return max;
	}
	
	public double getEntropy(int i,VectorOption opt){
		int j;
		double entropy=0.0;
		
		if(opt==VectorOption.ROW){
			for(j=0;j<this.columnSize;j++){
				if(this.element[i][j]!=0.0) entropy+=this.element[i][j]*java.lang.Math.log((double)this.columnSize*this.element[i][j]);
			}
		}
		else{
			for(j=0;j<this.rowSize;j++){
				if(this.element[j][i]!=0.0) entropy+=this.element[j][i]*java.lang.Math.log((double)this.rowSize*this.element[j][i]);
			}
		}
		
		return entropy;
	}
	
	public void prune(int i,double min,VectorOption opt){
		int j;
		
		if(opt==VectorOption.ROW){
			for(j=0;j<this.columnSize;j++){
				if(this.element[i][j]<min) this.element[i][j]=0.0;
			}
		}
		else{
			for(j=0;j<this.rowSize;j++){
				if(this.element[j][i]<min) this.element[j][i]=0.0;
			}
		}
	}
	
	public void normalize(int i,VectorOption opt){
		int j;
		double sum=0.0;
		
		if(opt==VectorOption.ROW){
			if(this.getMass(i,VectorOption.ROW)==0.0) return;
			
			for(j=0;j<this.columnSize;j++) sum+=this.element[i][j];
			for(j=0;j<this.columnSize;j++) element[i][j]=element[i][j]/sum;
		}
		else{
			if(this.getMass(i,VectorOption.COLUMN)==0.0) return;
			
			for(j=0;j<this.rowSize;j++) sum+=this.element[j][i];
			for(j=0;j<this.rowSize;j++) element[j][i]=element[j][i]/sum;
		}
	}
	
	public void inflate(int i,double inflate,VectorOption opt){
		int j;
		
		if(opt==VectorOption.ROW){
			for(j=0;j<this.columnSize;j++) this.element[i][j]=java.lang.Math.pow(this.element[i][j],inflate);
		}
		else{
			for(j=0;j<this.rowSize;j++) this.element[j][i]=java.lang.Math.pow(this.element[j][i],inflate);
		}
		
	}
	
	public void standardize(int i,VectorOption opt){
		double sd,mean;
		int j;
		
		if(opt==VectorOption.ROW){
			sd=java.lang.Math.pow(this.getVariance(i,VectorOption.ROW),0.5);
			mean=this.getMean(i,VectorOption.ROW);
			for(j=0;j<this.columnSize;j++) this.setElement(i,j,(this.element[i][j]-mean)/sd);
		}
		else{
			sd=java.lang.Math.pow(this.getVariance(i,VectorOption.COLUMN),0.5);
			mean=this.getMean(i,VectorOption.COLUMN);
			for(j=0;j<this.rowSize;j++) this.setElement(j,i,(this.element[j][i]-mean)/sd);
		}
	}
}
