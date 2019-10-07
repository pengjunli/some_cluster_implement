package kmeansCluster;

import java.io.File;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
/**
 * 为Kmeans的聚类方法调用类，可设置cluster=聚类类别数目；
 * 可设置KM.setSeed(77)中的数值，改变聚类的初始类别中心，
 * 初始类别中心不同最后的聚类效果不同。
 * @author pjl
 *
 */
public class KMeans {
	/**
	 * 维数是文本的数目，每个元素的值对应被分入哪一簇
	 */
	public static double[] clusternum ;
	/**
	 * 聚类的数目，在这里设置
	 */
	public static int cluster = 4;
	
	public KMeans(int cluster) {
		this.cluster = cluster;
	}
	
	public void Kmeans() {
		Instances ins = null;  
        
        SimpleKMeans KM = null;  
        DistanceFunction disFun = null;  
          
        try {  
            // 读入样本数据  
            File file = new File("arff/data.arff");  
            ArffLoader loader = new ArffLoader();  
            loader.setFile(file);  
            ins = loader.getDataSet();  
              
            // 初始化聚类器 （加载算法）  
            KM = new SimpleKMeans();  
            KM.setNumClusters(cluster);       //设置聚类要得到的类别数量    83 56%
            KM.setSeed(77); //设置seed的值  200：719  21：715  25：717  28：713 33：706 39：707 43：711 53：712 69：707 71：708 95：710   86 59% 97 54.5%
            KM.buildClusterer(ins);     //开始进行聚类  
//            System.out.println(KM.preserveInstancesOrderTipText());  
//            // 打印聚类结果  
//            System.out.println(KM.toString()); 
            
//            Instances tempIns = KM.getClusterCentroids(); 
//            System.out.println("CentroIds: " + tempIns);   // 输出arff文件中的

            ClusterEvaluation eval = new ClusterEvaluation();
            eval.setClusterer(KM); // the cluster to evaluate
            // data to evaluate the clusterer on
            eval.evaluateClusterer(ins);    //得到kmeans的聚类数据
            // output # of clusters
            System.out.println("# of clusters: " + eval.clusterResultsToString()); 
            
            //**这句获得了每条记录所属的clusterer
          //得到的数组是每个文本属于哪一个簇，数组长度为文本数量，每个数组元素对应一篇文本，每个元素的数值是这篇文本属于的簇的数值。如：聚成3个簇，则元素可能=0.0 1.0 2.0 这三种情况
			clusternum = eval.getClusterAssignments();   
        } catch(Exception e) {  
            e.printStackTrace();  
        } 
	}

}