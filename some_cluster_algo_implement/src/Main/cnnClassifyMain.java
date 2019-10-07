package Main;

import java.io.File;

import Classify.CnnClassify;
import Classify.DataPreprocessing;

public class cnnClassifyMain {
	public static void main(String[] args) throws Exception {
		//需要分类的目录文件（包括train和test文件夹的）
		File file = new File("D:\\自然语言处理\\11\\12");
		
		//对文本进行预处理
		DataPreprocessing doit = new DataPreprocessing(file);
		doit.datapreprocessing();
		
		//开始分类(调节网络超参数去CnnClassify类里去调节)
		CnnClassify cnnclassify = new CnnClassify();
		cnnclassify.classify();  
	}

}
