package Word2Vec;

import java.io.File;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class getModel {
//	File word_vectors_path = null;
	public getModel() {
//		this.word_vectors_path = word_vectors_path;
		System.out.println("加载模型");
	}
	
	public static Word2Vec restore(File word_vectors_path) {
		Word2Vec vec = (Word2Vec) WordVectorSerializer.loadStaticModel(word_vectors_path);
		System.out.println("加载完成");
		return vec;
	}
	


}
