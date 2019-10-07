package Main;

import java.io.File;
import java.util.HashMap;

import Action.wordcut;
import DataBase_Operation.DB_Insert;
import DataBase_Operation.DBconnection;
import DataBase_Operation.JDBCutils;
import Helper.TfIdfHelper;
import Helper.getTfIdfMatrixAndDir;
import kmeansCluster.KmeansCluster;
import kmeansCluster.Word2vecRemove;
import kmeansCluster.cluster;

public class clusterMain {
	
	public static void main(String[] args) throws Exception {
		//需要分类的文件目录
//		File clusterfile = new File("D:\\20180309南京调研\\南海问题\\南海问题2");  //D:\\20180309南京调研\\南海问题\\南海问题2 D:/Java  files/训练集
		File clusterfile = new File("E:\\Java  files\\搜狗聚类样本-无处理-六类");  //D:\\20180309南京调研\\南海问题\\南海问题2 D:/Java  files/训练集
//		File clusterfile = new File("E:\\Java  files\\训练集");  //D:\\20180309南京调研\\南海问题\\南海问题2 D:/Java  files/训练集
		
		//得到分词结果
		wordcut cut = new wordcut();  
		cut.cutWord(clusterfile);
		HashMap<File,HashMap<String,Integer>> articleWordsMap = cut.articleWordsMap;
		
		//使用word2vec合并相似词语
//		Word2vecRemove remove = new Word2vecRemove(articleWordsMap);
//		articleWordsMap = remove.SameMerge();
		
		//得到每个文本中词语的tfidf权重值
		TfIdfHelper modeltfidf = new TfIdfHelper(articleWordsMap);
		HashMap<File,HashMap<String,Double>> tfIdfMap = modeltfidf.calculate();
		HashMap<String,Double> DF = modeltfidf.DF;
		
		//获得权重矩阵和每一行对应的文本路劲向量
//		getTfIdfMatrixAndDir tfidfmatrix = new getTfIdfMatrixAndDir(clusterfile);
//		double[][] tfidflist = tfidfmatrix.getTfIdfMatrix_Cluster();
		getTfIdfMatrixAndDir tfidfmatrix = new getTfIdfMatrixAndDir(tfIdfMap,DF);
		double[][] tfidflist = tfidfmatrix.getTfIdfMatrix();
		File[] filesd = tfidfmatrix.filesd;
		
		//调用聚类算法进行聚类
		cluster doit = new cluster(tfidflist,filesd,"E:\\Java  files\\搜狗聚类样本-无处理-六类-TC-word2vec-result",6, 1);  //输入的分别为：权重矩阵，文件路径向量，聚类结果存放路径，聚类的类别数目，选择聚类算法
		doit.Cluster();
	}

}
