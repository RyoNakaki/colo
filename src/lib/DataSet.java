package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DataSet implements ConstValue{
	private List<PeakSet> peakSet;
	
	public List<PeakSet> getPeakSet(){return this.peakSet;}
	public PeakSet getPeakSet(int i){return this.peakSet.get(i);}
	
	public DataSet(){
		this.peakSet=new ArrayList<PeakSet>();
	}
	
	public DataSet(String path){
		try{
			int i;
			BufferedReader br=new BufferedReader(new FileReader(path));
			MemoryMXBean mx=ManagementFactory.getMemoryMXBean();
			String buff;
			
			i=0;
			this.peakSet=new ArrayList<PeakSet>();
			while((buff=br.readLine())!=null){
				String[] s=buff.split("\\t");
				if(Pattern.compile("//").matcher(s[0]).find()){}
				else{
					//PeakSet peakSet=new PeakSet(s[0],s[1]+" "+s[2]+" "+s[3]+" "+s[4],s[1],s[2],s[3],s[4],s[5]);
					PeakSet peakSet=new PeakSet(s[0],s[1]+"\t"+s[2]+"\t"+s[3],s[1],s[2],s[3],s[4],s[5]);
					
					System.out.println("["+(i+1)+"]\t"+peakSet.getName()+"\tSites: "+peakSet.getPeak().size()+"\t(Free memory: "+((mx.getHeapMemoryUsage().getMax()-mx.getHeapMemoryUsage().getUsed())/1000000)+")");
					this.peakSet.add(peakSet);
					i++;
				}
			}
			br.close();
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public DataSet divide(){
		int i;
		DataSet dataSet=new DataSet();
		
		for(i=0;i<this.getPeakSet().size();i++){
			PeakSet[] data=this.getPeakSet(i).divide(STDEV);
			
			dataSet.getPeakSet().add(data[LOW]);
			dataSet.getPeakSet().add(data[MIDDLE]);
			dataSet.getPeakSet().add(data[HIGH]);
			
			this.peakSet.set(i,null);
			System.gc();
		}
		
		return dataSet;
	}
}
