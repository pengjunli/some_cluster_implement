package kmeansCluster;

import java.io.File;

public class cluster {
	/**
	 * 选择聚类方法
	 */
	private static int flag = 1;
	/**
	 * 权重矩阵
	 */
	double[][] tfidflist = null;
	
	File[] filesd = null;
	/**
	 * 聚类结果地址
	 */
	String StoreAddress = null;
	/**
	 * 聚类的类别数目，默认为4
	 */
	int clusternum = 4;
	
	public cluster(double[][] tfidflist,File[] filesd,String StoreAddress,int clusternum,int flag) {
		this.tfidflist = tfidflist;
		this.filesd = filesd;
		this.flag = flag;
		this.clusternum = clusternum;
		this.StoreAddress = StoreAddress;
	}
	
//	public void SetWay(int flag) {
//		this.flag = flag;
//	}
	
	public void Cluster() throws Exception {
		switch(flag) {
		case 1:
			Kmeans();
			break;
		default:
			System.out.println("The "+flag+" is invalid. please set 1 .");
		}
	}
	/**
	 * 调用kmeans算法
	 * @throws Exception
	 */
	private void Kmeans() throws Exception {
		KmeansCluster kmeans = new KmeansCluster(StoreAddress,clusternum);
		kmeans.Kmeanscluster(tfidflist, filesd);
	}

}