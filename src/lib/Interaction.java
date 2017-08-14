package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Interaction implements ConstValue{
	private List<String[]> ppi=new ArrayList<String[]>();
	
	public List<String[]> getPPI(){return this.ppi;}
	public String[] getPPI(int i){return this.ppi.get(i);}
	
	public Interaction(){
		try{
			//BufferedReader br=new BufferedReader(new FileReader("/Users/nakaki/Documents/workspace/ngs/lib/TcoF/tcof_ppi_20100927.txt"));
			BufferedReader br=new BufferedReader(new FileReader("/Users/nakaki/Data/CoLo/STRING/out.txt"));
			String buff;
			
			while((buff=br.readLine())!=null){
				String[] splits=buff.split("\\t");
				
				//if(splits.length==11){
				if(splits.length==10&&500<Integer.parseInt(splits[9])){
					String[] pair=new String[2];
					pair[0]=new String(splits[0]);
					pair[1]=new String(splits[1]);
					this.ppi.add(pair);
				}
			}
			br.close();
			
			//System.out.println("PPI size\t"+this.ppi.size());
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public int[] countPlausiblePPIs(List<PeakSet[]> ppi){
		int i,j;
		int[] n=new int[2];
		boolean[] flg;
		
		flg=new boolean[this.ppi.size()];
		for(i=0;i<ppi.size();i++){
			if(ppi.get(i)[0].getUniprot().equals("-")||ppi.get(i)[1].getUniprot().equals("-")) continue;
			//if(ppi.get(i)[0].getUniprot().equals("-")&&ppi.get(i)[1].getUniprot().equals("-")) continue;
			//else if(ppi.get(i)[0].getUniprot().equals("-")||ppi.get(i)[1].getUniprot().equals("-")) continue;
			
			for(j=0;j<this.ppi.size();j++){
				if(this.ppi.get(j)[0].equals(this.ppi.get(j)[1])) continue;
				
				if((ppi.get(i)[0].getUniprot().equals(this.ppi.get(j)[0])&&ppi.get(i)[1].getUniprot().equals(this.ppi.get(j)[1]))||
					(ppi.get(i)[0].getUniprot().equals(this.ppi.get(j)[1])&&ppi.get(i)[1].getUniprot().equals(this.ppi.get(j)[0]))){
					
					flg[j]=true;
					n[0]++;
					break;
				}
			}
		}
		
		for(i=0;i<this.ppi.size();i++){
			if(flg[i]==true) n[1]++;
		}
		
		return n;
	}
	
	public CorMatrix getPlausiblePPIs(DataSet dataSet){
		int i,j,k;
		CorMatrix m=new CorMatrix(dataSet.getPeakSet().size());
		
		for(i=0;i<m.getSize();i++){
			for(j=0;j<m.getSize();j++){
				if(dataSet.getPeakSet(i).getUniprot().equals(dataSet.getPeakSet(j).getUniprot())) continue;
				
				for(k=0;k<this.ppi.size();k++){
					if((dataSet.getPeakSet(i).getUniprot().equals(this.ppi.get(k)[0])&&dataSet.getPeakSet(j).getUniprot().equals(this.ppi.get(k)[1]))||
						(dataSet.getPeakSet(i).getUniprot().equals(this.ppi.get(k)[1])&&dataSet.getPeakSet(j).getUniprot().equals(this.ppi.get(k)[0]))){
						m.setElement(i,j,MAXCOR);
						m.setElement(j,i,MAXCOR);
						break;
					}
				}
			}
		}
		
		return m;
	}
}
