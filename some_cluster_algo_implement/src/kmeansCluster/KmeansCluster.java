package kmeansCluster;

import java.io.File;

import Helper.getTfIdfMatrixAndDir;
//import wekaKmeans.GetArff;
//import wekaKmeans.KMeans;
/**
 * 调用了weka中的Kmeans方法进行的文本聚类
 * 只需要输入你要进行聚类的文件夹地址和聚类后的文件夹地址即完成聚类
 * @author pjl
 *
 */
public class KmeansCluster {
	/**
	 * 需要聚类的文件夹
	 */
	File ClusterFile = null;
	/**
	 * 聚类后的结果存放地址
	 */
	static String StoreAddress = null;
	/**
	 * 聚类的类别数目，默认为4
	 */
	int clusternum1 = 4;
	
	public KmeansCluster(String StoreAddress,int clusternum) {
		this.StoreAddress = StoreAddress;
		this.clusternum1 = clusternum;
	}
	
	
	public void Kmeanscluster(double[][] tfidflist,File[] filesd) throws Exception {
		//weka方法聚类
				GetArff getarff = new GetArff(tfidflist);
				getarff.getArff();
				KMeans kmeans = new KMeans(clusternum1);
				kmeans.Kmeans();
				double[] clusternum = KMeans.clusternum;
				int cluster = KMeans.cluster;
				mkdirfile_cluster(cluster,clusternum,filesd);
	}
	
	public void Kmeanscluster(File ClusterFile) throws Exception {
		//对文本进行预处理：分词，提取特征，计算权重，得到权重矩阵
		getTfIdfMatrixAndDir TfIdfmatrix = new getTfIdfMatrixAndDir(ClusterFile);
		double[][] tfidflist = TfIdfmatrix.getTfIdfMatrix_Cluster();
		File[] filesd = TfIdfmatrix.filesd;
				
		//weka方法聚类
		GetArff getarff = new GetArff(tfidflist);
		getarff.getArff();
		KMeans kmeans = new KMeans(clusternum1);
		kmeans.Kmeans();
		double[] clusternum = KMeans.clusternum;
		int cluster = KMeans.cluster;
		mkdirfile_cluster(cluster,clusternum,filesd);
	}
	
	/**
	 * 创建聚类文件夹，并将文本分进对应文件夹中
	 * @param cluster
	 * @param clusternum
	 * @param filesd
	 * @throws Exception
	 */
	public static void mkdirfile_cluster(int cluster,double[] clusternum,File[] filesd) throws Exception  {
		for(int i=0;i<cluster;i++) {
			File f = new File(StoreAddress+"\\"+i);
        	if(f.exists()) {
        		copyfileAnddeletedir.deleteDir(f);  //如果文件夹存在，就清楚文件夹中的所有文件
        	}else {
        		f.mkdir();    //否则创建文件夹
        	}
		}
		for(int i=0;i<clusternum.length;i++) {
			String dest = StoreAddress+"\\"+(int)clusternum[i];
        	File inputname = filesd[i];
        	String outputname = dest+"\\"+filesd[i].getName();
        	File files1=new File(outputname);   // 关键词存放处    D:/研究生/信息系统局项目/聚类分析  重点/tf_idf/a.txt
   		
   		    if (files1.exists()) {
   			    files1.delete();
   		    }else {
   		    	files1.createNewFile();
   		    }
   		    copyfileAnddeletedir.copyFile(inputname,outputname);  //将聚类的文本分入相应文件夹
		}
		
	}
	
	public static void main(String[] args) throws Exception {
//		File clusterfile = new File("D:\\Java  files\\训练集");
//		KmeansCluster kmeanscluster = new KmeansCluster("D:\\Java  files\\聚类文件");
//		kmeanscluster.Kmeanscluster(clusterfile);
	}

}