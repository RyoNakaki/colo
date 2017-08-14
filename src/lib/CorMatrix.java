package lib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.tc33.jheatchart.HeatChart;

import com.mxgraph.io.mxGraphMlCodec;
import com.mxgraph.io.graphml.mxGraphMlGraph;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

public class CorMatrix extends lib.SquareMatrix implements ConstValue{
	public CorMatrix(){super();}
	public CorMatrix(int size){super(size);}
	public CorMatrix(CorMatrix m){super(m);}
	public CorMatrix(String path){super(path);}
	
	public enum MethodOption{BOOTSTRAP,RANDOM}
	
	public void initRCM(DataSet dataSet){
		int i,j;
		PeakSet peakSet=new PeakSet();
		Peak e1,e2;
		int p1,p2;
		double max;
		
		for(i=0;i<dataSet.getPeakSet().size();i++){
			for(j=0;j<dataSet.getPeakSet(i).getPeak().size();j++){
				Peak peak=new Peak(dataSet.getPeakSet(i).getPeak(j));
				peak.setScore((double)i);
				peak.adjustWidth(PEAKWIDTH);
				peakSet.getPeak().add(peak);
			}
		}
		peakSet.sortByPosition(PeakSet.ORDER.ASCENDING);
		
		for(i=0;i<peakSet.getPeak().size();i++){
			e1=peakSet.getPeak(i);
			for(j=i+1;j<peakSet.getPeak().size();j++){
				e2=peakSet.getPeak(j);
				
				if(e1.getChrom().equals(e2.getChrom())){
					if(e2.getStart()<=e1.getEnd()){
						p1=(int)(e1.getScore());
						p2=(int)(e2.getScore());
						this.setElement(p1,p2,this.getElement(p1,p2)+1.0);
						this.setElement(p2,p1,this.getElement(p2,p1)+1.0);
					}
					else break;
				}
				else break;
			}
		}
		
		for(i=0;i<this.getSize();i++){
			max=0.0;
			for(j=0;j<this.getSize();j++){
				if(max<=this.getElement(j,i)) max=this.getElement(j,i);
			}
			this.setElement(i,i,max);
		}
		
		for(i=0;i<this.getSize();i++){
			if(dataSet.getPeakSet(i).getPeak().size()!=0){
				for(j=0;j<this.getSize();j++) this.setElement(j,i,(this.getElement(j,i)+MINSIZE1)/((double)dataSet.getPeakSet(i).getPeak().size()+MINSIZE2));
				//for(j=0;j<this.getSize();j++) this.setElement(j,i,this.getElement(j,i)/((double)dataSet.getPeakSet(i).getPeak().size()+10.0));
			}
		}
		
		//for(i=0;i<this.getSize();i++) System.out.println((i+1)+"\t"+dataSet.getPeakSet(i).getPeak().size()+"\t"+dataSet.getPeakSet(i).getName());
		
		/*
		double mean,sd;
		int tmp;
		int[] mass=new int[101];
		
		for(i=0;i<this.getSize();i++){
			mean=this.getMean(i,VectorOption.COLUMN);
			sd=this.getStandardDeviation(i,VectorOption.COLUMN);
			
			for(j=0;j<this.getSize();j++) this.setElement(j,i,(this.getElement(j,i)-mean-MINRCV)/sd);
		}
		
		for(i=0;i<this.getSize();i++){
			tmp=0;
			for(j=0;j<this.getSize();j++){
				if(MINZSCORE<this.getElement(i,j)) tmp++;
			}
			
			for(j=0;j<=100;j++){
				if(tmp<j*20){
					mass[j]++;
					break;
				}
			}
			
			if(380<tmp) System.out.println(dataSet.getPeakSet(i).getName()+"\t"+tmp);
		}
		
		for(i=0;i<=100;i++) System.out.println((i*20)+"\t"+mass[i]);
		System.exit(1);
		*/
	}
	
	public void runCoLo(double r){
		int i,j;
		boolean flg;
		CorMatrix rcm=null,scm=new CorMatrix(this.getSize());
		double mass,v;
		double mean,sd;
		
		while(true){
			rcm=new CorMatrix(this.getSize());
			for(i=0;i<this.getSize();i++){
				mean=this.getMean(i,VectorOption.COLUMN);
				sd=this.getStandardDeviation(i,VectorOption.COLUMN);
				
				for(j=0;j<this.getSize();j++) rcm.setElement(j,i,(this.getElement(j,i)-mean-MINRCV)/sd);
				//for(j=0;j<this.getSize();j++) rcm.setElement(j,i,(this.getElement(j,i)-mean-r)/sd);
				//for(j=0;j<this.getSize();j++) rcm.setElement(j,i,this.getElement(j,i));
			}
			
			flg=true;
			for(i=0;i<this.getSize();i++){
				mass=0.0;
				for(j=0;j<this.getSize();j++){
					//if(MINZSCORE<rcm.getElement(i,j)) mass+=1.0;
					if(r<rcm.getElement(i,j)) mass+=1.0;
				}
				
				//if(mass/(double)this.getSize()<=r){
				if(mass/(double)this.getSize()<=0.05){
					for(j=0;j<this.getSize();j++){
						//if(MINZSCORE<rcm.getElement(i,j)) scm.setElement(i,j,MAXCOR);
						if(r<rcm.getElement(i,j)) scm.setElement(i,j,MAXCOR);
					}
				}
				else{
					v=java.lang.Math.exp(-ATTENUATE*mass/(double)this.getSize());
					for(j=0;j<this.getSize();j++) this.setElement(i,j,v*this.getElement(i,j));
					flg=false;
				}
			}
			if(flg==true) break;
		}
		
		this.setElement(scm.getElement());
	}
	
	public void makeHeatMap(DataSet mps,DataSet mpsY,String path){
		int i;
		HeatChart map;
		String[] labelX,labelY;
		
		if(this.getSize()*this.getSize()>MAXMAPSIZE){
			System.out.println("ERROR: Over map-size limitation");
			return;
		}
		
		map=new HeatChart(this.getElement());
		map.setTitle("Relative Co-localization Matrix");
		map.setAxisColour(Color.DARK_GRAY);
		map.setAxisThickness(1);
		map.setLowValueColour(Color.WHITE);
		map.setHighValueColour(new Color(128,0,0));
		
		//map.setXAxisLabel("X Axis");
		//map.setYAxisLabel("Y Axis");
		//map.setChartMargin(40);
		
		labelX=new String[this.getSize()];
		labelY=new String[this.getSize()];
		for(i=0;i<this.getSize();i++){
			if(mpsY!=null) labelY[i]=new String("["+i+"] "+mpsY.getPeakSet(i).getName()+" ");
			else labelY[i]=new String("["+(i+1)+"] ");
			
			if(mps!=null) labelX[i]=new String("["+i+"] "+mps.getPeakSet(i).getName()+" ");
			else labelX[i]=new String("["+(i+1)+"] ");
		}
		map.setYValues(labelY);
		map.setXValues(labelX);
		
		try{map.saveToFile(new File(path));}
		catch(IOException e){e.printStackTrace();}
	}
	
	public void makeGMLdata(DataSet dataSet,String path){
		int i,j;
		
		try{
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
			
			pw.println("graph [");
			pw.println("directed 1");
			pw.println("label \"No name\"");
			
			for(i=0;i<this.getSize();i++){
				pw.println("node [");
				pw.println("id "+i);
				pw.println("label \"No."+(i+1)+" "+dataSet.getPeakSet(i).getName()+"\"");
				//pw.println("label \""+(i+1)+"\"");
				//pw.println("path \""+dataSet.getPeakSet(i).getPath()+"\"");
				pw.println("path \"cp "+dataSet.getPeakSet(i).getPath()+" .\"");
				pw.println("cellType \""+dataSet.getPeakSet(i).getCell()+"\"");
				
				if(500<dataSet.getPeakSet(i).getPeak().size()&&dataSet.getPeakSet(i).getPeak().size()<5000) pw.println("size \""+(dataSet.getPeakSet(i).getPeak().size()/100)+"\"");
				else if(dataSet.getPeakSet(i).getPeak().size()<500) pw.println("size \"5\"");
				else pw.println("size \"50\"");
				
				/*
				if(dataSet.getPeakSet(i).getPeak().size()>100) pw.println("size \""+(Math.log(dataSet.getPeakSet(i).getPeak().size())*10.0)+"\"");
				else pw.println("size \""+(Math.log(100.0)*10.0)+"\"");
				*/
				pw.println("]");
			}
			
			for(i=0;i<this.getSize();i++){
				//for(j=0;j<this.getSize();j++){
				for(j=i+1;j<this.getSize();j++){
					if(this.getElement(i,j)!=NOCOR&&i!=j){
						pw.println("edge [");
						pw.println("source "+j);
						pw.println("target "+i);
						pw.println("]");
					}
				}
			}
			
			pw.println("]");
			
			pw.close();
		}
		catch(IOException e){e.printStackTrace();}
	}
	
	public void writeNetwork(DataSet dataSet,String path){
		int i,j;
		int size;
		JFrame f=new JFrame();
		f.setSize(1000,1000);
		f.setLocation(300,200);
		Object[] obj=new Object[this.getSize()];
		Hashtable<String,Object> style=null;
		mxStylesheet stylesheet=null;
		
		final mxGraph graph=new mxGraph();
		mxGraphComponent graphComponent=new mxGraphComponent(graph);
		f.getContentPane().add(BorderLayout.CENTER,graphComponent);
		f.setVisible(true);
		
		Object parent=graph.getDefaultParent();
		graph.getModel().beginUpdate();
		try{
			for(i=0;i<this.getSize();i++){
				style=new Hashtable<String,Object>();
				
				if(Pattern.compile("K562").matcher(dataSet.getPeakSet(i).getCell()).find()) style.put(mxConstants.STYLE_FILLCOLOR,mxUtils.getHexColorString(Color.BLUE));
				else if(Pattern.compile("HUVEC").matcher(dataSet.getPeakSet(i).getCell()).find()) style.put(mxConstants.STYLE_FILLCOLOR,mxUtils.getHexColorString(Color.ORANGE));
				else if(Pattern.compile("GM12878").matcher(dataSet.getPeakSet(i).getCell()).find()) style.put(mxConstants.STYLE_FILLCOLOR,mxUtils.getHexColorString(Color.MAGENTA));
				else style.put(mxConstants.STYLE_FILLCOLOR,mxUtils.getHexColorString(Color.GRAY));
				
				style.put(mxConstants.STYLE_STROKEWIDTH,0.0);
				//style.put(mxConstants.STYLE_STROKECOLOR,mxUtils.getHexColorString(new Color(0,0,0)));
				style.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_ELLIPSE);
				style.put(mxConstants.STYLE_PERIMETER,mxConstants.PERIMETER_ELLIPSE);
				
				stylesheet=graph.getStylesheet();
				stylesheet.putCellStyle("NodeStyle"+i,style);
				
				size=dataSet.getPeakSet(i).getPeak().size()/1000+5;
				obj[i]=graph.insertVertex(parent,null,"No."+(i+1)+" "+dataSet.getPeakSet(i).getName(),100,100,size,size,"NodeStyle"+i);
			}
			
			/*
			style=new Hashtable<String,Object>();
			style.put(mxConstants.STYLE_FILLCOLOR,mxUtils.getHexColorString(Color.BLACK));
			
			stylesheet=graph.getStylesheet();
			stylesheet.putCellStyle("EdgeStyle",style);
			*/
			
			for(i=0;i<this.getSize();i++){
				for(j=0;j<this.getSize();j++){
					//if(this.getElement(i,j)!=NOCOR&&i!=j) graph.insertEdge(parent,null,"Edge",obj[i],obj[j],"EdgeStyle");
					if(this.getElement(i,j)!=NOCOR&&i!=j) graph.insertEdge(parent,null,"Edge",obj[i],obj[j]);
				}
			}
		}
		finally{graph.getModel().endUpdate();}
		
		// define layout
		mxIGraphLayout layout=new mxFastOrganicLayout(graph);
		
		// layout using morphing
		graph.getModel().beginUpdate();
		try{
			layout.execute(graph.getDefaultParent());
			
			mxGraphMlGraph gmlGraph=new mxGraphMlGraph();
			mxGraphMlCodec.decodeGraph(graph,parent,gmlGraph);
			try {
				Disp.writeDocument(mxGraphMlCodec.encodeXML(gmlGraph),path);
			}
			catch(Exception e){e.printStackTrace();}
		}
		finally{
			mxMorphing morph=new mxMorphing(graphComponent,20,1.2,20);
			
			morph.addListener(mxEvent.DONE,new mxIEventListener(){
				@Override
				public void invoke(Object arg0,mxEventObject arg1){
					graph.getModel().endUpdate();
					// fitViewport();
				}
			});
			morph.startAnimation();
		}
	}
	
	public CorMatrix evaluateSCM(DataSet dataSet,CorMatrix ref,String path){
		int i,j,all;
		int cis; //cell-specificity index score
		int[] n=new int[2];
		List<PeakSet[]> ppi=new ArrayList<PeakSet[]>();
		CorMatrix m=new CorMatrix(this.getSize()),pim;
		Interaction interaction=new Interaction();
		//pim=interaction.getPlausiblePPIs(dataSet);
		pim=new CorMatrix(this.getSize());
		double v;
		
		all=0;
		cis=0;
		for(i=0;i<this.getSize();i++){
			for(j=0;j<this.getSize();j++){
				if(this.getElement(i,j)!=NOCOR){
					all++;
					
					if(dataSet.getPeakSet(i).getCell().equals(dataSet.getPeakSet(j).getCell())){
						//System.out.println(dataSet.getPeakSet(i).getCell());
						//System.exit(1);
						cis++;
					}
				}
			}
			for(j=i+1;j<this.getSize();j++){
				if(this.getElement(i,j)!=NOCOR&&this.getElement(j,i)!=NOCOR){
					m.setElement(i,j,MAXCOR);
					m.setElement(j,i,MAXCOR);
					PeakSet[] pair=new PeakSet[2];
					pair[0]=new PeakSet();
					pair[1]=new PeakSet();
					pair[0].setUniprot(dataSet.getPeakSet(i).getUniprot());
					pair[1].setUniprot(dataSet.getPeakSet(j).getUniprot());
					//System.out.println(dataSet.getPeakSet(i).getName()+"\t"+dataSet.getPeakSet(j).getName());
					ppi.add(pair);
				}
			}
		}
		
		if(path!=null){
			try{
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
				
				for(i=0;i<this.getSize();i++){
					pw.println(dataSet.getPeakSet(i).getName()+"\t"+dataSet.getPeakSet(i).getPeak().size());
					for(j=0;j<this.getSize();j++){
						if(m.getElement(i,j)==MAXCOR&&j!=i){
							v=ref.getElement(j,i)*(double)dataSet.getPeakSet(i).getPeak().size();
							pw.print("<->\t"+dataSet.getPeakSet(j).getName()+"\t"+dataSet.getPeakSet(j).getPeak().size());
							pw.printf("\t%.4f",ref.getElement(j,i));
							pw.printf("\t%.4f",-myMath.logFETest(10000000,dataSet.getPeakSet(i).getPeak().size(),dataSet.getPeakSet(j).getPeak().size(),(int)v));
							
							if(pim.getElement(i,j)==MAXCOR) pw.println("\t*");
							else pw.println();
						}
					}
					
					pw.println();
				}
				
				pw.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
		n=interaction.countPlausiblePPIs(ppi);
		System.out.println((this.getSize()*this.getSize())+"\t"+all+"\t"+(ppi.size()*2)+"\t"+(n[0]*2)+"\t"+n[1]+"\t"+((double)cis/(double)all));
		
		return m;
	}
	
	public void screen(double r,MethodOption opt){
		int i,j;
		
		if(opt==MethodOption.BOOTSTRAP){
			double min;
			double[] v=new double[this.getSize()*this.getSize()];
			
			for(i=0;i<this.getSize();i++){
				for(j=0;j<this.getSize();j++) v[i*this.getSize()+j]=this.getElement(i,j);
			}
			
			java.util.Arrays.sort(v);
			min=v[(int)((double)(this.getSize()*this.getSize())*(1.0-r))];
			
			for(i=0;i<this.getSize();i++){
				for(j=0;j<this.getSize();j++){
					if(this.getElement(i,j)<min) this.setElement(i,j,NOCOR);
					else this.setElement(i,j,MAXCOR);
				}
			}
		}
		else{
			int n,m;
			Random rand=new Random();
			int rn1,rn2;
			
			for(i=0;i<this.getSize();i++){
				for(j=0;j<this.getSize();j++) this.setElement(i,j,NOCOR);
			}
			
			n=(int)((double)(this.getSize()*this.getSize())*r);
			
			m=0;
			while(true){
				rn1=rand.nextInt(this.getSize());
				rn2=rand.nextInt(this.getSize());
				
				if(this.getElement(rn1,rn2)==NOCOR){
					this.setElement(rn1,rn2,MAXCOR);
					m++;
					
					if(m==n) break;
				}
			}
		}
	}
}
