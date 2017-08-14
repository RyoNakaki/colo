package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GeneSet{
	private String name;
	private String path;
	private List<Gene> gene;
	
	public String getName(){return this.name;}
	public String getPath(){return this.path;}
	public List<Gene> getGene(){return this.gene;}
	public Gene getGene(int i){return this.gene.get(i);}
	
	public void setName(String name){this.name=new String(name);}
	public void setPath(String path){this.path=new String(path);}
	public void setGene(String path){
		this.gene=new ArrayList<Gene>();
		try{
			BufferedReader br=new BufferedReader(new FileReader(path));
			String s;
			
			while((s=br.readLine())!=null){
				String[] v=s.split("\\t");
				
				if(Pattern.compile("^#").matcher(v[0]).find()||v.length<=1) continue;
				
				Gene gene=new Gene(s);
				this.gene.add(gene);
			}
			
			br.close();
		}
		catch(FileNotFoundException e){System.err.println("ERROR: FileNotFoundException");}
		catch(IOException e){System.err.println("ERROR: IOException");}
	}
	
	public GeneSet(){
		this.name=null;
		this.path=null;
		this.gene=new ArrayList<Gene>();
	}
	
	public GeneSet(String name,String path){
		if(name!=null) this.name=new String(name);
		else this.name=null;
		
		if(path!=null){
			this.path=new String(path);
			this.setGene(path);
		}
		else{
			this.path=null;
			this.gene=new ArrayList<Gene>();
		}
	}
	
	public GeneSet(GeneSet geneSet){
		if(geneSet.name!=null) this.name=new String(geneSet.name);
		else this.name=null;
		
		if(geneSet.path!=null){
			this.path=new String(geneSet.path);
			this.setGene(geneSet.path);
		}
		else{
			this.path=null;
			this.gene=new ArrayList<Gene>();
		}
	}
	
	public GeneSet getOverlappingGenes(PeakSet peakSet){
		int i,j;
		GeneSet proximalGeneSet=new GeneSet();
		
		for(i=0;i<this.gene.size();i++){
			Peak peak=new Peak();
			
			peak.setChrom(this.gene.get(i).getChrom());
			if(this.gene.get(i).getStrand()=='+'){
				peak.setStart(this.gene.get(i).getStart());
				peak.setEnd(this.gene.get(i).getStart());
			}
			else{
				peak.setStart(this.gene.get(i).getEnd());
				peak.setEnd(this.gene.get(i).getEnd());
			}
			
			for(j=0;j<peakSet.getPeak().size();j++){
				if(peak.overlap(peakSet.getPeak(j))){
					proximalGeneSet.getGene().add(this.gene.get(i));
					break;
				}
			}
		}
		
		return proximalGeneSet;
	}
	
	public void disp(String path){
		int i;
		
		if(path!=null){
			try{
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
				
				for(i=0;i<this.getGene().size();i++){
					pw.print(this.getGene(i).getChrom()+"\t");
					pw.print(this.getGene(i).getStart()+"\t");
					pw.print(this.getGene(i).getEnd()+"\t");
					pw.print(this.getGene(i).getStrand()+"\t");
					pw.print(this.getGene(i).getName()+"\t");
					pw.println(this.getGene(i).getRefseq());
				}
				
				pw.close();
			}
			catch(IOException e){e.printStackTrace();}
		}
		else{
			for(i=0;i<this.getGene().size();i++){
				System.out.print(this.getGene(i).getChrom()+"\t");
				System.out.print(this.getGene(i).getStart()+"\t");
				System.out.print(this.getGene(i).getEnd()+"\t");
				System.out.print(this.getGene(i).getStrand()+"\t");
				System.out.print(this.getGene(i).getName()+"\t");
				System.out.println(this.getGene(i).getRefseq());
			}
		}
	}
}
