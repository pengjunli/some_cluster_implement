package Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Chi_Operation.Feature_Combination;
import Chi_Operation.Feature_Extract;
import Weight_Operation.TFIDF_calculate2CHI;
import Weight_Operation.Weight_Format2SVM;
import svm.svm_predict;
import svm.svm_train;

public class ChiClassifyMain {
	
	private static String trainP ="1\\train";
	private static String testP ="1\\test";
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		TFIDF_calculate2CHI tfc=new TFIDF_calculate2CHI();
		Map<String,List<List<Double>>>TrainSet=tfc.docsTFIDFWeight(trainP,true);
		Map<String,Integer> Label2Num=Weight_Format2SVM.Label2Num(TrainSet.keySet());
		Weight_Format2SVM.Format2SVM("svm", TrainSet,Label2Num, true);
		Map<String,List<List<Double>>>TestSet=tfc.docsTFIDFWeight(testP, false);
		Weight_Format2SVM.Format2SVM("svm", TestSet,Label2Num, false);
		
		// 根据训练结果构造SVM分类器
		String[] arg = { "svm/train.txt", // 训练集
		"svm/model.txt" }; // 存放SVM训练模型

		String[] parg = { "svm/test.txt", // 测试数据
				"svm/model.txt", // 调用训练模型
				"svm/predict.txt" }; // 预测结果
		System.out.println("........SVM运行开始..........");
		long start = System.currentTimeMillis();
		
		svm_train.main(arg); // 训练
		System.out.println("用时:" + (System.currentTimeMillis() - start));
		// 预测
		svm_predict.main(parg);
		
	
		
		
	}

}
