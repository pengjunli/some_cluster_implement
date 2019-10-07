package hierarchicalCluster;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 层次聚类
 * @author PJL
 *
 */
public class HierarchicalClustering {
	
	//初始化  得到一个Cluster实列，将每一项变为一类
	public  List<Cluster> initialCluster(ArrayList<Integer> CatalogList){
		List<Cluster> originalClusters = new ArrayList<Cluster>();
		for(int i = 0; i < CatalogList.size(); i ++){
			int index = CatalogList.get(i);
			List<Integer> tempDataPoints = new ArrayList<Integer>();
			tempDataPoints.add(index);  
            Cluster tempCluster = new Cluster();    //存放
            tempCluster.setClusterName("Cluster " + String.valueOf(i));  
            tempCluster.setDataPoints(tempDataPoints);  
            originalClusters.add(tempCluster);  
		}
		
		return originalClusters;
	}
	
	
	 /** 
     * 合并两个类
     * @param clusters 
     * @param mergeIndexA 
     * @param mergeIndexB 
     * @return 
     */  
	 private List<Cluster> mergeCluster(List<Cluster> clusters, int mergeIndexA,  int mergeIndexB) {  
	        if (mergeIndexA != mergeIndexB) {  
	            // 将cluster[mergeIndexB]与cluster[mergeIndexA]合并 
	            Cluster clusterA = clusters.get(mergeIndexA);  
	            Cluster clusterB = clusters.get(mergeIndexB);  
	            List<Integer> dpA = clusterA.getDataPoints();  
	            List<Integer> dpB = clusterB.getDataPoints(); 
	            for (int dp : dpB) {
	                dpA.add(dp); 
	            }	            
	            clusterA.setDataPoints(dpA); 
	            clusters.remove(mergeIndexB);  
	        }  
	        return clusters;  
	    }  
	
	/**
	 * 对输入的句子进行聚类
	 * @param CatalogList
	 * @param sim
	 * @param threshold
	 * @return
	 * @throws IOException
	 */
	 public List<Cluster> starAnalysis(ArrayList<Integer> CatalogList, double[][] sim,double threshold) throws IOException{
			List<Cluster> finalClusters = new ArrayList<Cluster>();
			List<Cluster> originalClusters = initialCluster(CatalogList);
			finalClusters = originalClusters;
			
			int index=0;
			while(finalClusters.size() >  1){
				index++;
				double max = Double.MIN_VALUE; 
				int mergeIndexA = 0;  
	            int mergeIndexB = 0;
	            for (int i = 0; i < finalClusters.size(); i++) {  
	                for (int j = i + 1; j < finalClusters.size(); j++) {   
	                    Cluster clusterA = finalClusters.get(i);//获取聚类1  
	                    Cluster clusterB = finalClusters.get(j);//获取聚类2  
	                    List<Integer> dataPointsA = clusterA.getDataPoints();  
	                    List<Integer> dataPointsB = clusterB.getDataPoints();  
	                          
	                    //以下两个for循环来确定应聚那两个类 
	                    double minTempWeight = Double.MAX_VALUE;
	                    boolean flag = false;
	                    for (int m = 0; m < dataPointsA.size(); m++) {  
	                    	for (int n = 0; n < dataPointsB.size(); n++) {//划分类的标准: 两类中相似度最低的两个focus作为类的距离。    
	                    		//计算两个聚类focus之间的相似度，选择相似度最低的focus之间的相似度作为类的相似度
	                            double tempWeight = sim[dataPointsA.get(m)][dataPointsB.get(n)];
//	                            if (tempWeight > 0.8) 
//	                                System.out.println(tempWeight);
	                                
	                            if(tempWeight < minTempWeight){
	                                minTempWeight = tempWeight;
	                                flag = true;
	                            }
	                                                               
	                        }//end_for  
	                    }//end_for
	                                              
	                        
	                    if (minTempWeight > max && flag == true) {  
	                        max = minTempWeight;  
	                        mergeIndexA = i;// --保存相似度最高的focus所在的簇
	                        mergeIndexB = j;// --保存相似度最高的focus所在的簇
	                    }//end_if  
//	                    if(max==1.0) {
//	                    	System.out.println(j);
//	                    }
//	                    System.out.println(max);
	                } // end for j  
	            }// end for i  
	            //将cluster[mergeIndexA]与cluster[mergeIndexB]合并
	            if(max < threshold) {  
	                System.out.println("相似度小于指定的阈值，聚类结束！");  
	                break;  
	            } 
	            finalClusters = mergeCluster(finalClusters, mergeIndexA, mergeIndexB);  
	            System.out.println("当前循环次数： "+index+"      当前最短距离："+max+"      类别数目："+finalClusters.size());
	        }// end while  
	        return finalClusters;  
	           		
		}
	
	
/** 
 * 将聚类结果写入文件
 * @param clusterFilePath 
 * @param clusters 
 * @throws FileNotFoundException 
 * @throws UnsupportedEncodingException 
 */
	public static void writeClusterToFile(String clusterFilePath,List<Cluster>clusters, ArrayList<String> sentencelist){
		File f = new File(clusterFilePath);
		
		BufferedWriter bw;
		int count = 0;
		int count0 = 0;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
			for (Cluster cl : clusters){
				count0++;
				try {
					bw.write("聚类" + count0);
					bw.newLine();
					bw.flush();	
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
				List<Integer> tempDps = cl.getDataPoints();
				for(int tempdp:tempDps){				
					try { 				
						bw.write(tempdp + ":" + sentencelist.get(tempdp));
						bw.newLine();
						bw.flush();
					} catch (IOException e) {
					// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				}
				if (tempDps.size() >= 3) count++;        
			}
		} catch (UnsupportedEncodingException e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("目录大于等于3的聚类数为:" + count + "/" + clusters.size());

	}
	
	/**
	 * 将层次聚类后的每一类单独存放成LDA的输入格式文件
	 * @param num
	 * @param clusterFilePath
	 * @param clusters
	 * @param csvresult
	 */
	public static void writeInputFile(int num,String clusterFilePath,List<Cluster>clusters, List<HashMap<String,Integer>> csvresult) {
		File f = new File(clusterFilePath);
		
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
			Cluster cl = clusters.get(num);
			
			List<Integer> tempDps = cl.getDataPoints();
			try {
				bw.write(tempDps.size());
				bw.newLine();
				bw.flush();	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			HashMap<String,Integer> wordsMap = new HashMap<>();
			for(int tempdp:tempDps){				
				try {
					wordsMap.clear();
					wordsMap = csvresult.get(tempdp);
					Iterator<String> itera = wordsMap.keySet().iterator();
					while(itera.hasNext()) {
						String str = itera.next();
						for(int z=0;z<wordsMap.get(str);z++) {
							bw.write(str+" ");
						}
					}
					bw.newLine();
					bw.flush();
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
			
		}catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
