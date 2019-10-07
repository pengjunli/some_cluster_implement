package WordCut;

import java.io.UnsupportedEncodingException;
import java.util.List;
import ICTCLAS.I3S.AC.ICTCLAS50;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
/**
 * 此类为分词系统选择类，首先调用Builder（）函数
 * SegmentSentence.Builder(content,1)  content是需要分词的内容，1表示选择hanlp分词，2表示中科院分词；
 * 后续可加入更多分词系统
 * 接着：String[] result = Segment();
 * 得到分词结果result
 * @author pjl
 *
 */
public class SegmentSentence {
//	public enum ChooseTool{
//		one,two
//	}
	public SegmentSentence() {
		
	}
	
	public static ICTCLAS50 ictclas50;
//	private ChooseTool choosetool;
	private static String content;
	private static int flag = 1;
	
	private SegmentSentence(Builder builder) {
//		this.choosetool = choosetool;
		this.flag = builder.flag;
		this.content = builder.content;
	}
	
	public static String[] segment() throws Exception {
		String[] result = null;
		switch(flag) {
		case 1:
			result = Hanlp(content);
			break;
		case 2:
			result = IctClas(content);
			break;
		default:
		    System.out.println("The "+flag+" is invalid. please set 1 or 2.");
		    break;
		}
		return result;
	}
	/**
	 * 用hanlp分词系统
	 * @param content
	 * @return
	 */
	private static String[] Hanlp(String content) {
		Segment segment = HanLP.newSegment().enableNameRecognize(false);
		List<Term> termList = segment.seg(content);
		String termList1=termList.toString();
		String[] arr = termList1.split(" ");
		return arr;
	}
	
	private static String[] IctClas(String content) throws Exception {
        init();
      //分词，返回结果为字符串   需要分词的文本内容为content.getBytes("gbk")，0：字符编码类型，1：是否词性标注,1:标注,0:不标注.标注的词集根据ICTCLAS_SetPOSmap的设置值来定
        byte[] nativeBytes1 = ictclas50.ICTCLAS_ParagraphProcess(content.getBytes("gbk"), 0, 1);
		String nativeStr1 = new String(nativeBytes1);
		String nativeStr2 = getUTF8StringFromGBKString(nativeStr1);
		String[] arr = nativeStr2.split(" ");
		return arr;
	}
	
	/**
	 * 初始化分词
	 */
	private static void init() {
		try {
			ictclas50 = new ICTCLAS50();
			String argu = ".";
			if(ictclas50.ICTCLAS_Init(argu.getBytes("gbk"))==false) {
				//以“gbk”的方式转换为字节数组    ICTCLAS_Init（）为读取配置文件，加载词典等(初始化)；
				System.out.println("init false");
			}
			//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
			ictclas50.ICTCLAS_SetPOSmap(2);
			String userdict = "userdict.txt";
			int nCount = ictclas50.ICTCLAS_ImportUserDictFile(userdict.getBytes(), 0);  //导入用户词典文件
		}catch(Exception e) {
			
		}
	}
	
	/**
	 * 释放分词
	 */
	public void releaseword() {
		//保存用户字典
		ictclas50.ICTCLAS_SaveTheUsrDic();
		//释放分词组件资源
		ictclas50.ICTCLAS_Exit();
	}
	
	/**
	 * gbk转utf-8
	 * @param gbkStr
	 * @return
	 */
	public static String getUTF8StringFromGBKString(String gbkStr) {  
         try {  
             return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");  
         } catch (UnsupportedEncodingException e) {  
             throw new InternalError();  
         }  
     }  
       
     public static byte[] getUTF8BytesFromGBKString(String gbkStr) {  
         int n = gbkStr.length();  
         byte[] utfBytes = new byte[3 * n];  
         int k = 0;  
         for (int i = 0; i < n; i++) {  
             int m = gbkStr.charAt(i);  
             if (m < 128 && m >= 0) {  
                 utfBytes[k++] = (byte) m;  
                 continue;  
             }  
             utfBytes[k++] = (byte) (0xe0 | (m >> 12));  
             utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));  
             utfBytes[k++] = (byte) (0x80 | (m & 0x3f));  
         }  
         if (k < utfBytes.length) {  
             byte[] tmp = new byte[k];  
             System.arraycopy(utfBytes, 0, tmp, 0, k);  
             return tmp;  
         }  
         return utfBytes;  
     }
     
     public static class Builder{
    	 private String content;
    	 private int flag;
    	 
    	 public Builder SetContent(String content) {
    		 this.content = content;
    		 return this;
    	 }
    	 
    	 public Builder SetFlag(int flag) {
    		 this.flag = flag;
    		 return this;
    	 }
    	 
    	 public SegmentSentence builder() {
    		 return new SegmentSentence(this);
    	 }
     }

	public static void Builder(String content1,int flag1) {
		 content = content1;
		 flag = flag1;

	}

}