package Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import Action.wordcut;
import Helper.TfIdfHelper;
import Word2Vec.LoadModel;
import Word2Vec.sumWordVector;
import kmeansCluster.cluster;

public class W2vClusterMain {
	public static void main(String[] args) throws Exception {
		File clusterfile = new File("E:/Java  files/搜狗聚类样本-无处理-六类");
		String modelfile = "E:\\自然语言处理\\word2vec资料\\大型词向量模型\\复旦、清华、搜狗预料训练的模型/word2vecWordEmbedding.model";
		
		wordcut cut = new wordcut();
		cut.cutWord(clusterfile);
		HashMap<File,HashMap<String,Integer>> articlewordsMap = cut.articleWordsMap;
		
		TfIdfHelper tfidfmodel = new TfIdfHelper(articlewordsMap);
		HashMap<String,Double> DF = tfidfmodel.DFMap();
		HashMap<File,HashMap<String,Double>> tfIdfMap = tfidfmodel.calculate();
		HashMap<String,Double> TC = tfidfmodel.getTC(tfIdfMap,DF);
		
		//加载模型
		 Word2Vec vec = WordVectorSerializer.readWord2VecModel(modelfile);
//		LoadModel model = new LoadModel();
//		model.loadmodel(modelfile);
//		Map<String,float[]> wordMap = model.getWordMap();
		
		sumWordVector vector = new sumWordVector();
		double[][] filesVector = vector.getFileVector(articlewordsMap, TC, vec, 128);
		File[] filelist = vector.getFileList();
		
		cluster doit = new cluster(filesVector,filelist,"E:\\Java  files\\聚类文件",6, 1);
		doit.Cluster();
		
		file_num count = new file_num();
		count.filenum("E:\\\\Java  files\\\\聚类文件");
		
	}

}
