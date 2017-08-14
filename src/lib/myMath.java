package lib;

import umontreal.iro.lecuyer.probdist.BinomialDist;

//import jsc.contingencytables.ContingencyTable2x2;
//import jsc.contingencytables.FishersExactTest;

public class myMath{
	public static void main(String[] args){
		System.out.println(-logFETest(1000,60,50,5));
		System.out.println(-logFETest(10000000,6000,30000,1));
		System.out.println(-logFETest(10000000,6000,30000,10));
		System.out.println(-logFETest(10000000,6000,30000,100));
		System.out.println(-logFETest(10000000,6000,30000,1000));
		System.out.println();
		
		/*
		ContingencyTable2x2 test=new ContingencyTable2x2(500,600,30000,10000000);
		FishersExactTest fet=new FishersExactTest(test);
		System.out.println(-Math.log(fet.getApproxSP()));
		System.out.println(-Math.log(fet.getChiSquared()));
		System.out.println(-Math.log(fet.getOneTailedMidP()));
		System.out.println(-Math.log(fet.getOneTailedSP()));
		System.out.println(-Math.log(fet.getOppositeTailProb()));
		System.out.println(-Math.log(fet.getSP()));
		System.out.println(-Math.log(fet.getTestStatistic()));
		*/
		
		double[] a1={1.0,2.0,3.0,4.0};
		
		System.out.println(mean(a1));
		System.out.println(sd(a1));
	}
	
	public static double logFETest(int np,int kp,int n,int k){
		return logCombination(kp,k)+logCombination(np-kp,n-k)-logCombination(np,n);
	}
	
	public static double logCombination(int n,int m){
		double v=0.0;
		
		if(n<m){
			//System.out.println("Combination error");
			//System.exit(1);
			m=n;
		}
		
		v+=logFactorial(n);
		v-=logFactorial(m);
		v-=logFactorial(n-m);
		
		return v;
	}
	
	public static double logFactorial(int n){
		if(n<=10){
			/*
			int i,j;
			double v;
			
			for(i=1;i<=15;i++){
				v=1.0;
				for(j=1;j<=i;j++){
					v*=(double)j;
				}
				System.out.println(i+"\t"+Math.log(v));
			}
			
			System.out.println("11\t"+(11.0*(Math.log(11.0)-1.0)));
			System.out.println("12\t"+(12.0*(Math.log(12.0)-1.0)));
			System.out.println("13\t"+(13.0*(Math.log(13.0)-1.0)));
			System.out.println("14\t"+(14.0*(Math.log(14.0)-1.0)));
			System.out.println("15\t"+(15.0*(Math.log(15.0)-1.0)));
			System.out.println("15\t"+(10000000.0*(Math.log(10000000.0)-1.0)));
			System.exit(1);
			*/
			
			if(n==1) return 0.0;
			if(n==2) return 0.693147180559945;
			if(n==3) return 1.791759469228055;
			if(n==4) return 3.178053830347945;
			if(n==5) return 4.787491742782046;
			if(n==6) return 6.579251212010101;
			if(n==7) return 8.525161361065415;
			if(n==8) return 10.60460290274525;
			if(n==9) return 12.80182748008146;
			else return 15.10441257307551;
		}
		else{
			return n*(java.lang.Math.log(n)-1.0);
		}
	}
	
	public static double mean(double[] v){
		int i;
		double mean=0.0;
		
		for(i=0;i<v.length;i++) mean+=v[i];
		mean/=(double)v.length;
		
		return mean;
	}
	
	public static double sd(double[] v){
		int i;
		double sd=0.0;
		double mean=mean(v);
		
		for(i=0;i<v.length;i++) sd+=java.lang.Math.pow(v[i]-mean,2.0);
		sd/=(double)v.length;
		sd=java.lang.Math.pow(sd,0.5);
		
		return sd;
	}
	
	public static double max(double[] v){
		int i;
		double max=0.0;
		
		for(i=0;i<v.length;i++){
			if(max<v[i]) max=v[i];
		}
		
		return max;
	}
}
