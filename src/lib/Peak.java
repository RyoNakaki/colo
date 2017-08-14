package lib;

import java.util.regex.Pattern;

public class Peak extends Locus{
	private String name;
	private double score;
	private char strand;
	private double signal;
	private double pvalue;
	private double qvalue;
	private double peak;
	private String str;
	private String sequence;
	
	public String getName(){return this.name;}
	public double getScore(){return this.score;}
	public char getStrand(){return this.strand;}
	public double getSignal(){return this.signal;}
	public double getPvalue(){return this.pvalue;}
	public double getQvalue(){return this.qvalue;}
	public double getPeak(){return this.peak;}
	public String getStr(){return this.str;}
	public String getSequence(){return this.sequence;}
	
	public void setName(String name){this.name=new String(name);}
	public void setScore(double score){this.score=score;}
	public void setStrand(char strand){this.strand=strand;}
	public void setSignal(double signal){this.signal=signal;}
	public void setPvalue(double pvalue){this.pvalue=pvalue;}
	public void setQvalue(double qvalue){this.qvalue=qvalue;}
	public void setPeak(double peak){this.peak=peak;}
	public void setStr(String str){this.str=new String(str);}
	public void setSequence(String sequence){this.sequence=new String(sequence);}
	
	public Peak(){
		super();
		this.name=null;
		this.score=0.0;
		this.strand=0;
		this.signal=0.0;
		this.pvalue=0.0;
		this.qvalue=0.0;
		this.peak=0.0;
		this.str=null;
		this.sequence=null;
	}
	
	public Peak(Peak peak){
		super(peak);
		if(peak.getName()!=null) this.name=new String(peak.getName());
		else this.name=null;
		
		this.score=peak.getScore();
		this.strand=peak.getStrand();
		this.signal=peak.getSignal();
		this.pvalue=peak.getPvalue();
		this.qvalue=peak.getQvalue();
		this.peak=peak.getPeak();
		
		if(peak.getStr()!=null) this.str=new String(peak.getStr());
		else this.str=null;
		
		if(peak.getSequence()!=null) this.sequence=new String(peak.getSequence());
		else this.sequence=null;
	}
	
	public Peak(String str){
		super(str);
		String[] splits=str.split("\\s+");
		
		this.name=null;
		this.score=0.0;
		//this.strand=0;
		this.strand='0';
		this.signal=0.0;
		this.pvalue=0.0;
		this.qvalue=0.0;
		this.peak=0.0;
		this.str=new String(str);
		this.sequence=null;
		
		if(4<=splits.length){
			if(splits.length==4){ //Extended bed format or SICER format
				if(checkValue(splits[3])){
					this.signal=Double.parseDouble(splits[3]);
					this.name=new String("None");
				}
				else this.name=new String(splits[3]);
			}
			else if(splits.length==5){ //MACS format
				if(Pattern.compile("^[0-9\\.]+$").matcher(splits[3]).find()&&Pattern.compile("^[0-9\\.]+$").matcher(splits[4]).find()){
					//this.name=new String(splits[3]+":"+splits[4]);
					this.name=new String(splits[3]);
				}
				else{
					this.name=new String(splits[3]);
					this.signal=Double.parseDouble(splits[4]);
				}
			}
			else if(splits.length==6){ //UCSC bed format
				this.name=new String(splits[3]);
				this.score=Double.parseDouble(splits[4]);
				this.signal=Double.parseDouble(splits[4]);
				this.strand=splits[5].charAt(0);
			}
			else if(splits.length==9||splits.length==10){ //narrowPeak/broadPeak format
				this.name=new String(splits[3]);
				this.score=Double.parseDouble(splits[4]);
				this.strand=splits[5].charAt(0);
				this.signal=Double.parseDouble(splits[6]);
				this.pvalue=Double.parseDouble(splits[7]);
				this.qvalue=Double.parseDouble(splits[8]);
				if(splits.length==10) this.peak=Double.parseDouble(splits[9]); //narrowPeak format
				else this.peak=0.0;
			}
		}
	}
	
	public boolean checkValue(String str){
		try{
			Double.parseDouble(str);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	public void disp(){
		System.out.print(this.getChrom()+"\t");
		System.out.print(this.getStart()+"\t");
		System.out.print(this.getEnd()+"\t");
		System.out.print(this.name+"\t");
		System.out.print(this.score+"\t");
		System.out.print(this.strand+"\t");
		System.out.print(this.signal+"\t");
		System.out.print(this.pvalue+"\t");
		System.out.print(this.qvalue+"\t");
		System.out.println(this.peak+"\t");
	}
}
