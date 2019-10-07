package kmeansCluster;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

public class GetArff {
	/**
	 * tfidf权重矩阵
	 */
	static double tfidflist[][] = null;
	/**
	 * 初始化
	 * @param tfidflist
	 */
	public GetArff(double[][] tfidflist) {
		this.tfidflist = tfidflist;
	}
	/**
	 * 建立arff文件
	 */
	public static void getArff() {
		File file = new File("arff\\data.arff");  // data.txt
		try {
			FileOutputStream out = new FileOutputStream(file,false);
			FileWriter writer = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write("@RELATION"+" "+"data"+"\r\n");
			
			for(int i=0;i<tfidflist[0].length;i++) {
				bw.write("@attribute"+" "+"attribute"+i+" "+"numeric"+"\r\n");    //numeric
			}
			bw.write("@data"+"\r\n");
			for(int i=0;i<tfidflist.length;i++) {
				bw.write("{");
				int c = 0;
				for(int j=0;j<tfidflist[1].length;j++) {
					if(tfidflist[i][j]!=0) {
						if(c==0) {
						    bw.write(j+" "+tfidflist[i][j]);
						}else {
							bw.write(","+j+" "+tfidflist[i][j]);
						}
						c++;
					}
				}
				bw.write("}");
				bw.newLine();
			}
			bw.close();  
	        writer.close(); 
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}