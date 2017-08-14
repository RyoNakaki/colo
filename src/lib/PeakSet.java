package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import jsc.distributions.Poisson;
import lib.Chrom;
import lib.GeneSet;

public class PeakSet implements ConstValue{
	private String name;
	private String path;
	private List<Peak> peak;
	private String cell;
	private String tf;
	private String treatment;
	private String accession;
	private String uniprot;
	
	public enum ORDER{ASCENDING,DESCENDING}
	
	public String getName(){return this.name;}
	public String getPath(){return this.path;}
	public List<Peak> getPeak(){return this.peak;}
	public Peak getPeak(int i){return this.peak.get(i);}
	public String getCell(){return this.cell;}
	public String getTf(){return this.tf;}
	public String getTreatment(){return this.treatment;}
	public String getAccession(){return this.accession;}
	public String getUniprot(){return this.uniprot;}
	
	public void setName(String name){this.name=new String(name);}
	public void setPath(String path){
		if(path!=null) this.path=new String(path);
		else this.path=null;
	}
	public void setPeak(String path){
		try{
			BufferedReader br=new BufferedReader(new FileReader(path));
			String s;
			
			this.peak=new ArrayList<Peak>();
			while((s=br.readLine())!=null){
				//if(Pattern.compile("chr.+\\t+\\d+\\t+\\d+").matcher(s).find()){
				if(Pattern.compile("chr.+\\s+[\\d,]+\\s+[\\d,]+").matcher(s).find()||Pattern.compile("Chr.+\\s+[\\d,]+\\s+[\\d,]+").matcher(s).find()||Pattern.compile("^\\d+\\s+[\\d,]+\\s+[\\d,]+").matcher(s).find()){
					Peak peak=new Peak(s);
					this.peak.add(peak);
				}
			}
			
			br.close();
		}
		catch(FileNotFoundException e){System.err.println("It can\'t be opened");}
		catch(IOException e){System.err.println("It can\'t be read");}
	}
	public void setPeak(String peakSetFile,String separator){
		String[] splits=peakSetFile.split(separator);
		int i;
		
		for(i=0;i<splits.length;i++){
			//if(Pattern.compile("chr.+\\t+\\d+\\t+\\d+").matcher(splits[i]).find()){
			if(Pattern.compile("chr.+\\s+[\\d,]+\\s+[\\d,]+").matcher(splits[i]).find()||Pattern.compile("Chr.+\\s+[\\d,]+\\s+[\\d,]+").matcher(splits[i]).find()||Pattern.compile("^\\d+\\s+[\\d,]+\\s+[\\d,]+").matcher(splits[i]).find()){
				Peak peak=new Peak(splits[i]);
				this.peak.add(peak);
			}
		}
	}
	public void setCell(String cell){this.cell=new String(cell);}
	public void setTf(String tf){this.tf=new String(tf);}
	public void setTreatment(String treatment){this.treatment=new String(treatment);}
	public void setAccession(String accession){this.accession=new String(accession);}
	public void setUniprot(String uniprot){this.uniprot=new String(uniprot);}
	
	public PeakSet(){
		this.name=null;
		this.path=null;
		this.peak=new ArrayList<Peak>();
		this.cell=null;
		this.tf=null;
		this.treatment=null;
		this.accession=null;
		this.uniprot=null;
	}
	
	public PeakSet(String path,String name,String cell,String treatment,String tf,String accession,String uniprot){
		if(name!=null) this.name=new String(name);
		else this.name=null;
		
		if(path!=null){
			this.path=new String(path);
			this.setPeak(path);
		}
		else{
			this.path=null;
			this.peak=new ArrayList<Peak>();
		}
		
		this.cell=new String(cell);
		this.tf=new String(tf);
		this.treatment=new String(treatment);
		this.accession=new String(accession);
		this.uniprot=new String(uniprot);
	}
	
	public PeakSet(String name,String path){
		if(name!=null) this.name=new String(name);
		else this.name=null;
		
		if(path!=null){
			this.path=new String(path);
			this.setPeak(path);
		}
		else{
			this.path=null;
			this.peak=new ArrayList<Peak>();
		}
		
		this.cell=new String("None");
		this.tf=new String("None");
		this.treatment=new String("None");
		this.accession=new String("None");
		this.uniprot=new String("None");
	}
	
	public PeakSet(PeakSet peakSet){
		if(peakSet.name!=null) this.name=new String(peakSet.name);
		else this.name=null;
		
		if(peakSet.path!=null){
			this.path=new String(peakSet.path);
			this.setPeak(peakSet.path);
		}
		else{
			this.path=null;
			this.peak=new ArrayList<Peak>();
		}
		
		this.cell=new String(peakSet.cell);
		this.tf=new String(peakSet.tf);
		this.treatment=new String(peakSet.treatment);
		this.accession=new String(peakSet.accession);
		this.uniprot=new String(peakSet.uniprot);
	}
	
	public PeakSet(GeneSet geneSet){
		int i;
		
		if(geneSet.getName()!=null) this.name=new String(geneSet.getName());
		else this.name=null;
		
		if(geneSet.getPath()!=null) this.path=new String(geneSet.getPath());
		else this.path=null;
		
		this.peak=new ArrayList<Peak>();
		this.cell=null;
		this.tf=null;
		this.treatment=null;
		this.accession=null;
		this.uniprot=null;
		
		for(i=0;i<geneSet.getGene().size();i++){
			Peak peak=new Peak();
			peak.setChrom(geneSet.getGene(i).getChrom());
			peak.setStart(geneSet.getGene(i).getStart());
			peak.setEnd(geneSet.getGene(i).getEnd());
			peak.setName(geneSet.getGene(i).getName());
			this.peak.add(peak);
		}
	}
	
	public void sortByPosition(final ORDER order){
		if(order==ORDER.DESCENDING){
			Comparator<Peak> startComparator=new Comparator<Peak>(){
				public int compare(Peak e1,Peak e2){
					return ((Peak)e2).getStart()-((Peak)e1).getStart();
				}
			};
			Collections.sort(this.peak,startComparator);
			
			Comparator<Peak> chromComparator=new Comparator<Peak>(){
				public int compare(Peak e1,Peak e2){
					return Chrom.fromHgChrom(((Peak)e2).getChrom())-Chrom.fromHgChrom(((Peak)e1).getChrom());
				}
			};
			Collections.sort(this.peak,chromComparator);
		}
		else{
			Comparator<Peak> startComparator=new Comparator<Peak>(){
				public int compare(Peak e1,Peak e2){
					return ((Peak)e1).getStart()-((Peak)e2).getStart();
				}
			};
			Collections.sort(this.peak,startComparator);
			
			Comparator<Peak> chromComparator=new Comparator<Peak>(){
				public int compare(Peak e1,Peak e2){
					return Chrom.fromHgChrom(((Peak)e1).getChrom())-Chrom.fromHgChrom(((Peak)e2).getChrom());
				}
			};
			Collections.sort(this.peak,chromComparator);
		}
	}
	
	public void sortByScore(final ORDER order){
		Comparator<Peak> scoreComparator=new Comparator<Peak>(){
			public int compare(Peak e1,Peak e2){
				if(order==ORDER.DESCENDING) return (int)(((Peak)e2).getScore()*10000.0)-(int)(((Peak)e1).getScore()*10000.0);
				else return (int)(((Peak)e1).getScore()*10000.0)-(int)(((Peak)e2).getScore()*10000.0);
			}
		};
		Collections.sort(this.peak,scoreComparator);
	}
	
	public void sortBySignal(final ORDER order){
		Comparator<Peak> signalComparator=new Comparator<Peak>(){
			public int compare(Peak e1,Peak e2){
				if(order==ORDER.DESCENDING) return (int)(((Peak)e2).getSignal()*10000.0)-(int)(((Peak)e1).getSignal()*10000.0);
				else return (int)(((Peak)e1).getSignal()*10000.0)-(int)(((Peak)e2).getSignal()*10000.0);
			}
		};
		Collections.sort(this.peak,signalComparator);
	}
	
	public PeakSet[] divide(double sd){
		int i;
		PeakSet[] peakSet=new PeakSet[3];
		int level;
		double mean=0.0,dev=0.0;
		double[] v=new double[2];
		
		/*
		peakSet[LOW]=new PeakSet(null,this.name+" (low)",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		peakSet[MIDDLE]=new PeakSet(null,this.name+" (middle)",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		peakSet[HIGH]=new PeakSet(null,this.name+" (high)",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		*/
		
		peakSet[LOW]=new PeakSet(null,this.name+"\tLow",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		peakSet[MIDDLE]=new PeakSet(null,this.name+"\tMiddle",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		peakSet[HIGH]=new PeakSet(null,this.name+"\tHigh",this.cell,this.tf,this.treatment,this.accession,this.uniprot);
		//peakSet[LOW].setPath(this.path);
		//peakSet[MIDDLE].setPath(this.path);
		//peakSet[HIGH].setPath(this.path);
		
		for(i=0;i<this.peak.size();i++) mean+=this.peak.get(i).getSignal();
		mean=mean/(double)this.peak.size();
		
		for(i=0;i<this.peak.size();i++) dev+=Math.pow(this.peak.get(i).getSignal()-mean,2.0);
		dev=dev/(double)this.peak.size();
		dev=Math.sqrt(dev);
		
		v[0]=mean-dev*sd;
		v[1]=mean+dev*sd;
		
		for(i=0;i<this.peak.size();i++){
			Peak peak=new Peak(this.peak.get(i));
			
			if(mean!=0.0){
				if(this.peak.get(i).getSignal()<=v[0]) level=LOW;
				else if(this.peak.get(i).getSignal()<=v[1]) level=MIDDLE;
				else level=HIGH;
			}
			else level=MIDDLE;
			peakSet[level].getPeak().add(peak);
		}
		
		peakSet[LOW].disp("/Users/nakaki/Data/tmp/"+this.getAccession()+"_low.narrowPeak");
		peakSet[LOW].setPath("/Users/nakaki/Data/tmp/"+this.getAccession()+"_low.narrowPeak");
		peakSet[MIDDLE].disp("/Users/nakaki/Data/tmp/"+this.getAccession()+"_middle.narrowPeak");
		peakSet[MIDDLE].setPath("/Users/nakaki/Data/tmp/"+this.getAccession()+"_middle.narrowPeak");
		peakSet[HIGH].disp("/Users/nakaki/Data/tmp/"+this.getAccession()+"_high.narrowPeak");
		peakSet[HIGH].setPath("/Users/nakaki/Data/tmp/"+this.getAccession()+"_high.narrowPeak");
		
		return peakSet;
	}
	
	public void adjustWidth(int width){
		int i;
		
		for(i=0;i<this.getPeak().size();i++){
			this.getPeak(i).adjustWidth(width);
		}
	}
	
	public PeakSet getCommonPeaks(PeakSet peakSet){
		int i,j;
		PeakSet commonPeakSet=new PeakSet(),totalPeakSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			Peak peak=new Peak(this.peak.get(i));
			peak.setScore(1.0);
			peak.setPeak(1.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		for(i=0;i<peakSet.getPeak().size();i++){
			Peak peak=new Peak(peakSet.getPeak(i));
			peak.setScore(0.0);
			peak.setPeak(1.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		totalPeakSet.sortByPosition(ORDER.ASCENDING);
		
		for(i=0;i<totalPeakSet.getPeak().size();i++){
			for(j=i+1;j<totalPeakSet.getPeak().size();j++){
				if(totalPeakSet.getPeak(i).overlap(totalPeakSet.getPeak(j))){
					if(totalPeakSet.getPeak(i).getScore()==0.0||totalPeakSet.getPeak(j).getScore()==0.0){
						totalPeakSet.getPeak(i).setPeak(0.0);
						totalPeakSet.getPeak(j).setPeak(0.0);
					}
				}
				else break;
			}
		}
		
		for(i=0;i<totalPeakSet.getPeak().size();i++){
			if(totalPeakSet.getPeak(i).getScore()==1.0&&totalPeakSet.getPeak(i).getPeak()==0.0) commonPeakSet.getPeak().add(new Peak(totalPeakSet.getPeak(i)));
		}
		
		/*
		System.out.println(commonPeakSet.getPeak().size());
		commonPeakSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			for(j=0;j<peakSet.getPeak().size();j++){
				if(this.peak.get(i).overlap(peakSet.getPeak(j))){
					commonPeakSet.getPeak().add(this.peak.get(i));
					break;
				}
			}
		}
		
		System.out.println(commonPeakSet.getPeak().size());
		*/
		
		return commonPeakSet;
	}
	
	public void computeAveSignals(String wigPath,double max,double min, int margin,int skipW){
		DataSet dataSet=new DataSet();
		int topN=2*margin/skipW;
		double sig;
		int n;
		int i,j;
		
		for(i=0;i<this.getPeak().size();i++){
			this.getPeak(i).setStart(this.getPeak(i).getStart()-margin);
			this.getPeak(i).setEnd(this.getPeak(i).getEnd()+margin);
			this.getPeak(i).setSignal(0.0);
			
			dataSet.getPeakSet().add(new PeakSet());
		}
		
		try{
			BufferedReader br=new BufferedReader(new FileReader(wigPath));
			String s;
			String chrom=null;
			
			while((s=br.readLine())!=null){
				if(Pattern.compile("\\d+\\t\\d+").matcher(s).find()){
					String[] splits=s.split("\\t");
					Peak peak=new Peak();
					peak.setChrom(chrom);
					peak.setStart(Integer.parseInt(splits[0]));
					peak.setEnd(Integer.parseInt(splits[0]));
					peak.setSignal(Double.parseDouble(splits[1]));
					
					if(min<peak.getSignal()){
						for(i=0;i<this.getPeak().size();i++){
							if(peak.overlap(this.getPeak(i))){
								dataSet.getPeakSet(i).getPeak().add(peak);
								break;
							}
						}
					}
				}
				else if(Pattern.compile("variableStep chrom").matcher(s).find()) chrom=s.split("\\W")[2];
			}
			br.close();
		}
		catch(FileNotFoundException e){System.err.println("It can\'t be opened");}
		catch(IOException e){System.err.println("It can\'t be read");}
		
		//System.out.println("CheckPoint04");
		
		for(i=0;i<dataSet.getPeakSet().size();i++){
			dataSet.getPeakSet(i).sortBySignal(ORDER.DESCENDING);
			
			if(topN<dataSet.getPeakSet(i).getPeak().size()) n=topN;
			else n=dataSet.getPeakSet(i).getPeak().size();
			
			if(0<n){
				sig=0.0;
				for(j=0;j<n;j++){
					//System.out.println(dataSet.getPeakSet(i).getPeak(j).getSignal());
					sig+=dataSet.getPeakSet(i).getPeak(j).getSignal();
				}
				sig/=(double)n;
				
				this.getPeak(i).setSignal(sig);
			}
			else this.getPeak(i).setSignal(0.0);
		}
	}
	
	public void computePvaluePoisson(String path, double max, double min){
		DataSet dataSet=new DataSet();
		double mean=0.0,sig,pv;
		Poisson pd=null;
		int size;
		int i,j;
		
		//String outPath=new String(this.getPath());
		
		//outPath=outPath.replaceAll("\\.bed","_mod\\.bed");
		//outPath=outPath.replaceAll("\\.narrowPeak","_mod\\.narrowPeak");
		//outPath=outPath.replaceAll("\\.broadPeak","_mod\\.broadPeak");
		
		for(i=0;i<this.getPeak().size();i++){
			PeakSet peakSet=new PeakSet();
			dataSet.getPeakSet().add(peakSet);
			this.getPeak(i).setSignal(0.0);
		}
		
		//System.out.println("CheckPoint01");
		try{
			BufferedReader br=new BufferedReader(new FileReader(path));
			String s;
			
			i=0;
			while((s=br.readLine())!=null){
				if(Pattern.compile("\\d+\\t\\d+").matcher(s).find()){
					String[] splits=s.split("\\t");
					
					if(Double.parseDouble(splits[1])<max){
						mean+=Double.parseDouble(splits[1]);
						i++;
					}
				}
			}
			br.close();
			
			mean=mean/(double)i;
		}
		catch(FileNotFoundException e){System.err.println("It can\'t be opened");}
		catch(IOException e){System.err.println("It can\'t be read");}
		
		pd=new Poisson(mean);
		
		//System.out.println("CheckPoint02");
		
		try{
			BufferedReader br=new BufferedReader(new FileReader(path));
			String s;
			String chrom=null;
			
			while((s=br.readLine())!=null){
				if(Pattern.compile("\\d+\\t\\d+").matcher(s).find()){
					String[] splits=s.split("\\t");
					Peak peak=new Peak();
					peak.setChrom(chrom);
					peak.setStart(Integer.parseInt(splits[0]));
					peak.setEnd(Integer.parseInt(splits[0]));
					peak.setSignal(Double.parseDouble(splits[1]));
					
					if(min<peak.getSignal()){
						for(i=0;i<this.getPeak().size();i++){
							if(peak.overlap(this.getPeak(i))){
								dataSet.getPeakSet(i).getPeak().add(peak);
								break;
							}
						}
					}
				}
				else if(Pattern.compile("variableStep chrom").matcher(s).find()) chrom=s.split("\\W")[2];
			}
			br.close();
		}
		catch(FileNotFoundException e){System.err.println("It can\'t be opened");}
		catch(IOException e){System.err.println("It can\'t be read");}
		
		//System.out.println("CheckPoint03");
		sig=0.0;
		for(i=0;i<dataSet.getPeakSet().size();i++){
			size=dataSet.getPeakSet(i).getPeak().size();
			
			
			if(0<size){
				pv=0.0;
				
				for(j=0;j<size;j++){
					double tmp=this.getPeak(i).getSignal();
					this.getPeak(i).setSignal(tmp+dataSet.getPeakSet(i).getPeak(j).getSignal());
				}
				this.getPeak(i).setSignal(this.getPeak(i).getSignal()/(double)size);
				
				for(j=(int)this.getPeak(i).getSignal();j<(int)max;j++){
					pv+=pd.pdf(j);
					
					if(pd.pdf(j)==0.0){
						pv+=Math.pow(10.0,-max);
						break;
					}
					//System.out.println(j+"\t"+pd.pdf(j));
				}
				
				this.getPeak(i).setSignal(-Math.log(pv));
				//this.getPeak(i).setSignal(pv);
				
				sig+=this.getPeak(i).getSignal();
			}
		}
		
		System.out.println("Average -log(P-value): "+(sig/(double)this.getPeak().size()));
		//this.disp(null);
		//this.disp(outPath);
	}
	
	public PeakSet getInsidePeaks(PeakSet peakSet){
		int i,j;
		PeakSet insidePeakSet=new PeakSet(),totalPeakSet=new PeakSet();
		
		for(i=0;i<peakSet.getPeak().size();i++){
			Peak peak=new Peak(peakSet.getPeak(i));
			peak.setScore(1.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		for(i=0;i<this.peak.size();i++){
			Peak peak=new Peak(this.peak.get(i));
			peak.setScore(0.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		totalPeakSet.sortByPosition(ORDER.ASCENDING);
		
		for(i=0;i<totalPeakSet.getPeak().size();i++){
			if(totalPeakSet.getPeak(i).getScore()==1.0){
				for(j=i+1;j<totalPeakSet.getPeak().size();j++){
					if(totalPeakSet.getPeak(i).getChrom().equals(totalPeakSet.getPeak(j).getChrom())
					&&totalPeakSet.getPeak(j).getEnd()<=totalPeakSet.getPeak(i).getEnd()) insidePeakSet.getPeak().add(new Peak(totalPeakSet.getPeak(j)));
					else break;
					//System.out.println("OK2");
				}
			}
		}
		
		/*
		System.out.println(insidePeakSet.getPeak().size());
		insidePeakSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			for(j=0;j<peakSet.getPeak().size();j++){
				if(this.peak.get(i).getChrom().equals(peakSet.getPeak(j).getChrom())
				&&peakSet.getPeak(j).getStart()<=this.peak.get(i).getStart()&&this.peak.get(i).getEnd()<=peakSet.getPeak(j).getEnd()){
					insidePeakSet.getPeak().add(new Peak(this.peak.get(i)));
					break;
				}
			}
		}
		
		
		System.out.println(insidePeakSet.getPeak().size());
		System.exit(1);
		*/
		
		return insidePeakSet;
	}
	
	public PeakSet getOverlappingRegions(PeakSet peakSet){
		int i,j;
		PeakSet overlappingRegionSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			for(j=0;j<peakSet.getPeak().size();j++){
				if(this.peak.get(i).overlap(peakSet.getPeak(j))){
					Peak peak=new Peak(this.peak.get(i));
					int start,end;
					
					if(this.peak.get(i).getStart()<peakSet.getPeak(j).getStart()) start=peakSet.getPeak(j).getStart();
					else start=this.peak.get(i).getStart();
					
					if(this.peak.get(i).getEnd()<peakSet.getPeak(j).getEnd()) end=this.peak.get(i).getEnd();
					else end=peakSet.getPeak(j).getEnd();
					
					peak.setStart(start);
					peak.setEnd(end);
					overlappingRegionSet.getPeak().add(peak);
					break;
				}
			}
		}
		
		return overlappingRegionSet;
	}
	
	public PeakSet getSpecificPeaks(PeakSet peakSet){
		int i,j;
		PeakSet specificPeakSet=new PeakSet(),totalPeakSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			Peak peak=new Peak(this.peak.get(i));
			peak.setScore(1.0);
			peak.setPeak(1.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		for(i=0;i<peakSet.getPeak().size();i++){
			Peak peak=new Peak(peakSet.getPeak(i));
			peak.setScore(0.0);
			peak.setPeak(1.0);
			
			totalPeakSet.getPeak().add(peak);
		}
		
		totalPeakSet.sortByPosition(ORDER.ASCENDING);
		
		for(i=0;i<totalPeakSet.getPeak().size();i++){
			for(j=i+1;j<totalPeakSet.getPeak().size();j++){
				if(totalPeakSet.getPeak(i).overlap(totalPeakSet.getPeak(j))){
					if(totalPeakSet.getPeak(i).getScore()==0.0||totalPeakSet.getPeak(j).getScore()==0.0){
						totalPeakSet.getPeak(i).setPeak(0.0);
						totalPeakSet.getPeak(j).setPeak(0.0);
					}
				}
				else break;
			}
		}
		
		for(i=0;i<totalPeakSet.getPeak().size();i++){
			if(totalPeakSet.getPeak(i).getScore()==1.0&&totalPeakSet.getPeak(i).getPeak()==1.0) specificPeakSet.getPeak().add(new Peak(totalPeakSet.getPeak(i)));
		}
		
		/*
		System.out.println("  "+specificPeakSet.getPeak().size());
		specificPeakSet=new PeakSet();
		
		for(i=0;i<this.peak.size();i++){
			flg=false;
			for(j=0;j<peakSet.getPeak().size();j++){
				if(this.peak.get(i).overlap(peakSet.getPeak(j))){
					flg=true;
					break;
				}
			}
			
			if(flg!=true){
				Peak peak=new Peak();
				peak.setChrom(this.peak.get(i).getChrom());
				peak.setStart(this.peak.get(i).getStart());
				peak.setEnd(this.peak.get(i).getEnd());
				
				specificPeakSet.getPeak().add(peak);
			}
		}
		
		System.out.println("  "+specificPeakSet.getPeak().size());
		*/
		
		return specificPeakSet;
	}
	
	public PeakSet getIntervals(){
		int i;
		PeakSet peakSet=new PeakSet();
		
		for(i=1;i<this.peak.size();i++){
			Peak peak=new Peak(this.peak.get(i));
			peak.setStart(this.peak.get(i-1).getEnd()+1);
			peak.setEnd(this.peak.get(i).getStart()-1);
			peak.setName(this.peak.get(i-1).getName()+"-"+this.peak.get(i).getName());
			
			peakSet.getPeak().add(peak);
		}
		
		return peakSet;
	}
	
	public void add(PeakSet peakSet){
		int i;
		
		for(i=0;i<peakSet.getPeak().size();i++){
			this.getPeak().add(peakSet.getPeak(i));
		}
	}
	
	public void computeFOS(double[] data,int size,double min){
		int i,j,k;
		double l,c,r;
		
		for(i=0;i<this.peak.size();i++){
			l=min;
			c=min;
			r=min;
			for(j=this.peak.get(i).getStart()-size,k=0;0<j&&k<size;j++,k++) l+=data[j];
			for(j=this.peak.get(i).getStart();j<=this.peak.get(i).getEnd();j++) c+=data[j];
			for(j=this.peak.get(i).getEnd()+1,k=0;k<size&&j<data.length;j++,k++) r+=data[j];
			
			l/=(double)size;
			c/=(double)(this.peak.get(i).getEnd()-this.peak.get(i).getStart()+1);
			r/=(double)size;
			
			if(min<l&&min<r) this.peak.get(i).setSignal(c/l+c/r);
			else this.peak.get(i).setSignal(2.0);

		}
	}
	
	public PeakSet screen(int n){
		int i;
		PeakSet peakSet=new PeakSet();
		double[] v=new double[this.getPeak().size()];
		double tv=0.0;
		
		for(i=0;i<this.getPeak().size();i++) v[i]=this.getPeak(i).getSignal();
		Arrays.sort(v);
		
		tv=v[this.getPeak().size()-n-1];
		
		for(i=0;i<this.getPeak().size();i++){
			if(tv<=this.getPeak(i).getSignal()) peakSet.getPeak().add(this.getPeak(i));
		}
		
		//System.out.println(tv);
		//System.out.println(peakSet.getPeak().size());
		
		//System.out.println(Arrays.toString(v));
		//System.exit(1);
		
		return peakSet;
	}
	
	public PeakSet pruneMin(double min){
		int i;
		PeakSet peakSet=new PeakSet();
		
		for(i=0;i<this.getPeak().size();i++){
			if(min<=this.getPeak().get(i).getSignal()){
				Peak peak=new Peak(this.getPeak().get(i));
				
				peakSet.getPeak().add(peak);
			}
		}
		
		return peakSet;
	}
	
	public PeakSet pruneMax(double max){
		int i;
		PeakSet peakSet=new PeakSet();
		
		for(i=0;i<this.getPeak().size();i++){
			if(this.getPeak().get(i).getSignal()<=max){
				Peak peak=new Peak(this.getPeak().get(i));
				
				peakSet.getPeak().add(peak);
			}
		}
		
		return peakSet;
	}
	
	public PeakSet pruneMinSize(int min){
		int i;
		PeakSet peakSet=new PeakSet();
		
		for(i=0;i<this.getPeak().size();i++){
			if(min<=(this.getPeak().get(i).getEnd()-this.getPeak().get(i).getStart()+1)){
				peakSet.getPeak().add(this.getPeak().get(i));
			}
		}
		
		return peakSet;
	}
	
	public PeakSet pruneMaxSize(int max){
		int i;
		PeakSet peakSet=new PeakSet();
		
		for(i=0;i<this.getPeak().size();i++){
			if((this.getPeak().get(i).getEnd()-this.getPeak().get(i).getStart()+1)<=max){
				peakSet.getPeak().add(this.getPeak().get(i));
			}
		}
		
		return peakSet;
	}
	
	public PeakSet merge(PeakSet peakSet){
		PeakSet totalPeakSet=new PeakSet();
		
		totalPeakSet.add(this.getSpecificPeaks(peakSet));
		totalPeakSet.add(this.getCommonPeaks(peakSet));
		totalPeakSet.add(peakSet.getSpecificPeaks(this));
		
		return totalPeakSet;
	}
	
	public void disp(String path){
		int i;
		
		if(path!=null){
			try{
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
				
				for(i=0;i<this.getPeak().size();i++){
					pw.print(this.getPeak(i).getChrom()+"\t");
					pw.print(this.getPeak(i).getStart()+"\t");
					pw.print(this.getPeak(i).getEnd()+"\t");
					pw.print(this.getPeak(i).getName()+"\t");
					pw.print(this.getPeak(i).getScore()+"\t");
					pw.print(this.getPeak(i).getStrand()+"\t");
					pw.print(this.getPeak(i).getSignal()+"\t");
					pw.print(this.getPeak(i).getPvalue()+"\t");
					pw.print(this.getPeak(i).getQvalue()+"\t");
					pw.println(this.getPeak(i).getPeak());
				}
				
				pw.close();
			}
			catch(IOException e){e.printStackTrace();}
		}
		else{
			for(i=0;i<this.getPeak().size();i++){
				System.out.print(this.getPeak(i).getChrom()+"\t");
				System.out.print(this.getPeak(i).getStart()+"\t");
				System.out.print(this.getPeak(i).getEnd()+"\t");
				System.out.print(this.getPeak(i).getName()+"\t");
				System.out.print(this.getPeak(i).getScore()+"\t");
				System.out.print(this.getPeak(i).getStrand()+"\t");
				System.out.print(this.getPeak(i).getSignal()+"\t");
				System.out.print(this.getPeak(i).getPvalue()+"\t");
				System.out.print(this.getPeak(i).getQvalue()+"\t");
				System.out.println(this.getPeak(i).getPeak());
			}
		}
	}
	
	public void dispUcscBed(String path){
		int i;
		
		if(path!=null){
			try{
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
				
				for(i=0;i<this.getPeak().size();i++){
					pw.print(this.getPeak(i).getChrom()+"\t");
					if(this.getPeak(i).getStrand()=='+'){
						pw.print((this.getPeak(i).getStart()-1)+"\t");
						pw.print((this.getPeak(i).getEnd()-1)+"\t");
					}
					else{
						pw.print(this.getPeak(i).getStart()+"\t");
						pw.print(this.getPeak(i).getEnd()+"\t");
					}
					pw.print(this.getPeak(i).getName()+"\t");
					pw.print("0\t");
					pw.println(this.getPeak(i).getStrand());
				}
				
				pw.close();
			}
			catch(IOException e){e.printStackTrace();}
		}
		else{}
	}
	
	public static void main(String[] args){
		PeakSet peakSet1=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/CEBPB_K562_peaks.bed"),
				peakSet2=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/GATA1_K562_peaks.bed"),
				//peakSet3=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/HDAC2_K562_peaks.bed"),
				peakSet4=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/P300_K562_peaks.bed"),
				peakSet5=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/STAT5A_K562_peaks.bed"),
				//peakSet6=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/TEAD4_K562_peaks.bed"),
				peakSet7=new PeakSet("None","/Users/nakaki/Data/CoLo/bed/all/TRIM28_K562_peaks.bed");
		
		System.out.println(peakSet1.getPeak().size());
		
		peakSet1=peakSet1.getCommonPeaks(peakSet2);
		System.out.println(peakSet1.getPeak().size());
		
		//peakSet1=peakSet1.getCommonPeaks(peakSet3);
		System.out.println(peakSet1.getPeak().size());
		
		peakSet1=peakSet1.getCommonPeaks(peakSet4);
		System.out.println(peakSet1.getPeak().size());
		
		peakSet1=peakSet1.getCommonPeaks(peakSet5);
		System.out.println(peakSet1.getPeak().size());
		
		//peakSet1=peakSet1.getCommonPeaks(peakSet6);
		System.out.println(peakSet1.getPeak().size());
		
		peakSet1=peakSet1.getCommonPeaks(peakSet7);
		System.out.println(peakSet1.getPeak().size());
	}
}
