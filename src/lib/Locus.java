package lib;

import java.util.regex.Pattern;

public class Locus{
	private String chrom;
	private int start;
	private int end;
	
	public String getChrom(){return this.chrom;}
	public int getStart(){return this.start;}
	public int getEnd(){return this.end;}
	
	public void setChrom(String chrom){this.chrom=new String(chrom);}
	public void setStart(int start){this.start=start;}
	public void setEnd(int end){this.end=end;}
	
	public Locus(){
		this.chrom=null;
		this.start=0;
		this.end=0;
	}
	
	public Locus(String chrom,int start,int end){
		this.chrom=new String(chrom);
		this.start=start;
		this.end=end;
	}
	
	public Locus(String str){
		String[] splits=str.split("\\s+");
		
		if(Pattern.compile("chr").matcher(splits[0]).find()){
			this.chrom=new String(splits[0]);
			this.chrom=this.chrom.split("\\.fa")[0];
		}
		else this.chrom=new String("chr"+splits[0]);
		
		splits[1]=splits[1].replaceAll(",","");
		splits[2]=splits[2].replaceAll(",","");
		this.start=Integer.parseInt(splits[1]);
		this.end=Integer.parseInt(splits[2]);
	}
	
	public Locus(Locus peak){
		this.chrom=new String(peak.getChrom());
		this.start=peak.getStart();
		this.end=peak.getEnd();
	}
	
	public void adjustWidth(int width){
		int center=(this.start+this.end)/2;
		
		if(width%2==0){
			this.start=center-width/2;
			this.end=center+width/2-1;
		}
		else{
			this.start=center-width/2;
			this.end=center+width/2;
		}
		
		if(this.start<1){
			this.start=1;
			this.end=width;
		}
	}
	
	public boolean overlap(Locus peak){
		if(this.chrom.equals(peak.getChrom())){
			if(this.end<peak.getStart()||peak.getEnd()<this.start) return false;
			else return true;
			
			/*
			if(this.start<=peak.getStart()&&peak.getStart()<=this.end) return true;
			else if(this.start<=peak.getEnd()&&peak.getEnd()<=this.end) return true;
			else if(peak.getStart()<=this.start&&this.start<=peak.getEnd()) return true;
			else if(peak.getStart()<=this.end&&this.end<=peak.getEnd()) return true;
			else return false;
			*/
		}
		else return false;
	}
	
	public int interval(Locus peak){
		int interval=0;
		
		if(this.overlap(peak)) interval=0;
		else if(this.chrom.equals(peak.getChrom())){
			if(this.start<peak.getStart()) interval=peak.getStart()-this.end-1;
			else interval=this.start-peak.getEnd()-1;
		}
		else interval=-1;
		
		return interval;
	}
	
	public boolean overlap(Gene gene){
		if(this.chrom.equals(gene.getChrom())){
			if(this.end<gene.getStart()||gene.getEnd()<this.start) return false;
			else return true;
			
			/*
			if(this.start<=gene.getStart()&&gene.getStart()<=this.end) return true;
			else if(this.start<=gene.getEnd()&&gene.getEnd()<=this.end) return true;
			else if(gene.getStart()<=this.start&&this.start<=gene.getEnd()) return true;
			else if(gene.getStart()<=this.end&&this.end<=gene.getEnd()) return true;
			else return false;
			*/
		}
		else return false;
	}
	
	public boolean equals(Object obj){ //override equals
		if(obj instanceof Locus){
			return this.equals((Locus)obj) ;
		}
		return false;
	}
	public boolean equals(Locus peak){
		if(this.chrom.equals(peak.getChrom())&&this.start==peak.getStart()&&this.end==peak.getEnd()) return true;
		else return false;
	}
	public static void main(String[] args){
		Locus peak1=new Locus("chr1",1,5),peak2=new Locus("chr1",10,15),peak3=new Locus("chr1",11,12),peak4=new Locus("chr2",11,12);
		
		System.out.println(peak1.interval(peak2));
		System.out.println(peak1.interval(peak3));
		System.out.println(peak2.interval(peak3));
		System.out.println(peak1.interval(peak4));
		System.out.println(peak2.interval(peak4));
	}
}
