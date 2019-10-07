package kmeansCluster;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.deeplearning4j.models.word2vec.Word2Vec;

import Word2Vec.getModel;

public class Word2vecRemove {
	HashMap<File,HashMap<String,Integer>> articleWordsMap = new HashMap<File,HashMap<String,Integer>>();
	
	public Word2vecRemove(HashMap<File,HashMap<String,Integer>> articleWordsMap) {
		this.articleWordsMap = articleWordsMap;
	}
	/**
	 * 去除每篇文章中意思相同的词语
	 * @return
	 */
	public HashMap<File,HashMap<String,Integer>> SameMerge(){
		getModel model = new getModel();
		Word2Vec vec = model.restore(new File("D:/自然语言处理/word2vec资料/大型词向量模型/news12g_bdbk20g_nov90g_dim128/news12g_bdbk20g_nov90g_dim128.bin"));
//		Collection<String> similar = vec.wordsNearest(word, n)
		HashMap<File,HashMap<String,Integer>> result = new HashMap<File,HashMap<String,Integer>>();
		Iterator<File> iteratorfile = articleWordsMap.keySet().iterator();
		while(iteratorfile.hasNext()) {
			File file = iteratorfile.next();
			HashMap<String,Integer> wordsMap = articleWordsMap.get(file);
			HashMap<String,Integer> resultvel = new HashMap<String,Integer>();
			Iterator<String> iteratorstr = wordsMap.keySet().iterator();
			while(iteratorstr.hasNext()) {
				String str = iteratorstr.next();
				Collection<String> similar = vec.wordsNearest(str, 5);
				Iterator<String> iteratorsim = similar.iterator();
				if(!resultvel.containsKey(str)) {
					while(iteratorsim.hasNext()) {
						String sim = iteratorsim.next();
						if(wordsMap.containsKey(sim)) {
							if(!resultvel.containsKey(str))
								resultvel.put(str, wordsMap.get(str)+wordsMap.get(sim));
							resultvel.put(str, resultvel.get(str)+wordsMap.get(sim));
							wordsMap.remove(sim);
						}
					}
				}
			}
			result.put(file, resultvel);
		}
		return result;
		
	}
	


}