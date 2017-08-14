package lib;

public interface ConstValue{
	int VARIETY=4;
	
	int LOW=0;
	int MIDDLE=1;
	int HIGH=2;
	
	int MAXMAPSIZE=1000*1000;
	int PEAKWIDTH=300;
	double STDEV=0.5;
	double MINSIZE1=1.0; //fixed
	double MINSIZE2=1000.0; //fixed
	//double MINMASS=1.0;
	
	double MAXCOR=1.0;
	double NOCOR=0.0;
	
	double MINRCV=0.05; //fixed
	double MINZSCORE=0.5; //fixed
	double ATTENUATE=0.1; //fixed
}
