package hierarchicalCluster;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import Word2Vec.sumWordVector;
import WordCut.dowordcut;
import database_connection.Conn;



public class HCTest {

	public static final String clusterFilePath1 = "file/result_HCC1.2_1";
	static PreparedStatement sql;
	static ResultSet res;
	
	public static void main(String[] args) throws Exception {
		String modelfile = "E:\\自然语言处理\\word2vec资料\\大型词向量模型\\复旦、清华、搜狗预料训练的模型\\word2vecWordEmbedding.model";
		double disThreshold = 0.3;  //0.66  3ture  0.5 2ture  0.45 3  0.49为3个月预测最佳
		String file_name = "event_ch_data";
		
		ArrayList<String> sentenceList = new ArrayList<>();  //存放原始句子
		List<List<String>> articleWordsList = new ArrayList<>();  //存放文章分词结果
		ArrayList<Integer> senindex = new ArrayList<>();  //存放句子编号
		
		//连接数据库
		Conn c = new Conn();
		Connection con = (Connection) c.getConnection();
		
		try  {
			//查询数据库
			sql = (PreparedStatement) con.prepareStatement(String.format("SELECT * FROM %s",file_name));
			res = sql.executeQuery();
			int num = res.getMetaData().getColumnCount();
			List<String> temporaryWorker = null;
			
			dowordcut cuter = new dowordcut();
			int index = 1;
			while(res.next()) {
//				if(temporaryWorker!=null) {
//					temporaryWorker.clear();
//				}
				
				String SQLDATE = res.getString("SQLDATE");  //or res.getString("id")
				String EVENTCODE = res.getString("EVENTCODE");
				String GOLDSTEINSCALE = res.getString("GOLDSTEINSCALE");
				String EVENT_SENTENCE = res.getString("EVENT_SENTENCE");
				
				//以“事件日期_类型编码_得分_事件句”作为整个句子输入sentenceList
				String articleSentence = SQLDATE + "_" + EVENTCODE + "_" + GOLDSTEINSCALE + "_" + EVENT_SENTENCE;
				sentenceList.add(articleSentence);
				
				if(index%1000 == 0) {
					System.out.println(articleSentence);
				}
				index++;
				
				//将每条新闻事件句的分词结果存入articleWordsList中，用于后续聚类
				temporaryWorker = cuter.docutwordone(EVENT_SENTENCE);
				articleWordsList.add(temporaryWorker);
//				temporaryWorker.clear();
			}
			
			//加载Word2Vec模型
			 Word2Vec vec = WordVectorSerializer.readWord2VecModel(modelfile);
			 
			//根据分词结果、词典和模型得到权重矩阵和矩阵对应的文件路径
			sumWordVector vector = new sumWordVector();
			List<double[]> filesVector = vector.getFileVector(articleWordsList, 128 , vec);
  
			
			for(int i=0;i<articleWordsList.size();i++) {
				senindex.add(i);
			}
		
			double[][] simmatrix1 = TextSimilarity.CalSimMatrix(filesVector);
			//层次聚类
			HierarchicalClustering hc = new HierarchicalClustering();
			List<Cluster> cluste_res = hc.starAnalysis(senindex, simmatrix1, disThreshold);
			HierarchicalClustering.writeClusterToFile(clusterFilePath1, cluste_res, sentenceList);
		}catch(OutOfMemoryError e) {
			e.printStackTrace();
		}
	}
	
	
}
