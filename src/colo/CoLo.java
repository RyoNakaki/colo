package colo;

import lib.CorMatrix;
import lib.DataSet;

public class CoLo{
	public static void main(String[] args){
		//DataSet rawDataSet=new DataSet(args[0]),dataSet;
		DataSet rawDataSet,dataSet;
		CorMatrix rcm,rcm1;
		CorMatrix dcm1;
		
		//rawDataSet=new DataSet("/Users/nakaki/Data/CoLo/DatasetA.txt");
		//rawDataSet=new DataSet("/Users/nakaki/Data/CoLo/DatasetB.txt");
		rawDataSet=new DataSet("/Users/nakaki/Data/oki/nakaki/DatasetES.txt");
		
		dataSet=rawDataSet.divide();
		rcm=new CorMatrix(dataSet.getPeakSet().size());
		rcm.initRCM(dataSet);
		//rcm.makeHeatMap(null,dataSet,"/Users/nakaki/Data/CoLo/out/rcm.png");
		
		rcm1=new CorMatrix(rcm);
		rcm1.runCoLo(0.05);
		//rcm1.makeHeatMap(null,dataSet,"/Users/nakaki/Data/CoLo/out/rcm1.png");
		dcm1=rcm1.evaluateSCM(dataSet,rcm,"/Users/nakaki/Data/CoLo/out/rcm1.txt");
		dcm1.makeGMLdata(dataSet,"/Users/nakaki/Data/CoLo/out/dcm1.gml");
		//dcm1.writeNetwork(dataSet,"/Users/nakaki/Data/CoLo/out/dcm1.gexf");
	}
	
	private double param1;
	private double param2;
	private int windowSize;
	private DataSet dataSet;
	
	public double getParam1(){return this.param1;}
	public double getParam2(){return this.param2;}
	public int getWindowSize(){return this.windowSize;}
	public DataSet getDataSet(){return this.dataSet;}
	
	public CoLo(double param1,double param2,int windowSize,DataSet dataSet){
		this.param1=param1;
		this.param2=param2;
		this.windowSize=windowSize;
		this.dataSet=dataSet;
	}
	
	public String run(){
		String result=null;
		DataSet subDataSet=this.dataSet.divide();
		CorMatrix rcm,rcm1,dcm1;
		
		rcm=new CorMatrix(subDataSet.getPeakSet().size());
		rcm.initRCM(subDataSet);
		rcm.makeHeatMap(null,subDataSet,"/Users/ryo/Documents/workspace/ngs/img/rcm.png");
		
		rcm1=new CorMatrix(rcm);
		
		rcm1.runCoLo(this.param1);
		rcm1.makeHeatMap(null,subDataSet,"/Users/ryo/Documents/workspace/ngs/img/rcm1.png");
		dcm1=rcm1.evaluateSCM(subDataSet,rcm,"/Users/ryo/Documents/workspace/ngs/work/rcm1.txt");
		dcm1.makeGMLdata(subDataSet,"/Users/ryo/Documents/workspace/ngs/work/dcm1.gml");
		
		return result;
	}
}
