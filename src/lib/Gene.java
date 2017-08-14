package lib;

public class Gene extends Peak{
	private char strand;
	private String refseq;
	private String name;
	private PeakSet exon;
	
	public char getStrand(){return this.strand;}
	public String getRefseq(){return this.refseq;}
	public String getName(){return this.name;}
	public PeakSet getExon(){return this.exon;}
	
	public void setStrand(char strand){this.strand=strand;}
	public void setRefseq(String refseq){this.refseq=new String(refseq);}
	public void setName(String name){this.name=new String(name);}
	public void setExon(PeakSet exon){this.exon=new PeakSet(exon);} //Or making new object?
	
	public Gene(){
		super();
		this.strand=0;
		this.refseq=null;
		this.name=null;
		this.exon=new PeakSet();
	}
	
	public Gene(String original){
		int i;
		String[] exonStart,exonEnd;
		String[] splits=original.split("\\t");
		
		this.refseq=new String(splits[1]);
		this.setChrom(splits[2].split("\\.fa")[0]);
		this.strand=splits[3].charAt(0);
		this.setStart(Integer.parseInt(splits[4]));
		this.setEnd(Integer.parseInt(splits[5]));
		this.name=new String(splits[12]);
		
		exonStart=splits[9].split(",");
		exonEnd=splits[10].split(",");
		
		if(exonStart.length!=exonEnd.length) System.out.println("Reading error of exons:\t"+this.name);
		
		this.exon=new PeakSet();
		for(i=0;i<exonStart.length;i++){
			Peak exon=new Peak();
			exon.setChrom(this.getChrom());
			exon.setStart(Integer.parseInt(exonStart[i]));
			exon.setEnd(Integer.parseInt(exonEnd[i]));
			
			this.exon.getPeak().add(exon);
		}
		
		//System.out.println(this.name+"\t"+exonStart.length+"\t"+exonEnd.length);
		//System.exit(1);
	}
	
	public Gene(Gene gene){
		super(gene);
		this.strand=gene.getStrand();
		this.refseq=new String(gene.getRefseq());
		this.name=new String(gene.getName());
		this.exon=new PeakSet(gene.getExon());
	}
}
