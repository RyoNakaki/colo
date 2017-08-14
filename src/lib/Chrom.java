package lib;

public class Chrom{
	public static int fromHgChrom(String chrom){
		if(chrom.equals("chr1")) return 1;
		if(chrom.equals("chr2")) return 2;
		if(chrom.equals("chr3")) return 3;
		if(chrom.equals("chr4")) return 4;
		if(chrom.equals("chr5")) return 5;
		if(chrom.equals("chr6")) return 6;
		if(chrom.equals("chr7")) return 7;
		if(chrom.equals("chr8")) return 8;
		if(chrom.equals("chr9")) return 9;
		if(chrom.equals("chr10")) return 10;
		if(chrom.equals("chr11")) return 11;
		if(chrom.equals("chr12")) return 12;
		if(chrom.equals("chr13")) return 13;
		if(chrom.equals("chr14")) return 14;
		if(chrom.equals("chr15")) return 15;
		if(chrom.equals("chr16")) return 16;
		if(chrom.equals("chr17")) return 17;
		if(chrom.equals("chr18")) return 18;
		if(chrom.equals("chr19")) return 19;
		if(chrom.equals("chr20")) return 20;
		if(chrom.equals("chr21")) return 21;
		if(chrom.equals("chr22")) return 22;
		if(chrom.equals("chrX")) return 23;
		if(chrom.equals("chrY")) return 24;
		if(chrom.equals("chrM")) return 25;
		else return 26;
	}
	
	public static String toHgChrom(int chrom){
		if(chrom==1) return "chr1";
		if(chrom==2) return "chr2";
		if(chrom==3) return "chr3";
		if(chrom==4) return "chr4";
		if(chrom==5) return "chr5";
		if(chrom==6) return "chr6";
		if(chrom==7) return "chr7";
		if(chrom==8) return "chr8";
		if(chrom==9) return "chr9";
		if(chrom==10) return "chr10";
		if(chrom==11) return "chr11";
		if(chrom==12) return "chr12";
		if(chrom==13) return "chr13";
		if(chrom==14) return "chr14";
		if(chrom==15) return "chr15";
		if(chrom==16) return "chr16";
		if(chrom==17) return "chr17";
		if(chrom==18) return "chr18";
		if(chrom==19) return "chr19";
		if(chrom==20) return "chr20";
		if(chrom==21) return "chr21";
		if(chrom==22) return "chr22";
		if(chrom==23) return "chrX";
		if(chrom==24) return "chrY";
		if(chrom==25) return "chrM";
		else return null;
	}
}
