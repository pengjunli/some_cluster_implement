package Main;
import java.io.FileNotFoundException;
import Chart.BarChart;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.ChartPanel;

import File_Operation.Bagofwords;
import File_Operation.labels_filepaths;
import File_Operation.words_Segment;
import Weight_Operation.TFIDF_calculate;

public class test {
	private static String train ="D:\\学习实例\\分类测试数据集\\minidata1";
	private static String txt="D:\\学习实例\\分类测试数据集\\minidata\\财经\\10.txt";
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		
//		TFIDF_calculate oop=new TFIDF_calculate();
//		oop.GetBagOfWord(train);
//		
//		labels_filepaths lf=new labels_filepaths();
//		Map<String,Set<String>>callback_l_p=lf.Get(train);
//		for(Iterator<String> itr=callback_l_p.keySet().iterator();itr.hasNext(); ) {
//			String label=itr.next();
//			for(String path:callback_l_p.get(label)) {
//				//获取单个文本的tfidf集合
//				Map<String, Double>res=oop.getDocumenttfidf(path);
//				System.out.println(label+res);
//			}
			
		}
		
		
		
		
		
//		labels_filepaths lf=new labels_filepaths();
//		
//		lf.setmaindir(train);//设置遍历主路径
//		lf.setflag(1);//设置返回类型为类别与对应文档数量
//		System.out.println(lf.Get());
//		
//		
//		System.out.println("\r\n");
//		
//		lf.setflag(0);//设置返回类型为类别与对应文档数量
//		System.out.println(lf.Get());
//		
//		System.out.println("\r\n");
//		words_Segment ws=new words_Segment();
//		ws.setflag(0);
//		ws.setobjdir(txt);
//		System.out.println(ws.wordsSegment());
//		ws.setflag(1);
//		ws.setNum2word(5);
//		System.out.println("\r\n");
//		System.out.println(ws.wordsSegment());
//		
//		
//		//训练并返回一个词袋模型
//		Bagofwords bw=new Bagofwords();
//		bw.setmaindir(train);
//		bw.Initial();
//		Map<String,Map<String,Integer>>res=bw.Get();
//		
//		System.out.println(res);
		
		
	} 


