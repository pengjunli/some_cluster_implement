package WordCut;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����һ��String���ͣ��Դ˽��зִʣ�ͨ��ÿ���ʵĴ���ɸѡ����Ҫ�Ĵ����ȥ��ͣ�ôʣ�
 * �õ�һ��hashmap��hashmap<String,Integer>. �� ���� ��Ӧ ��Ƶ ��
 * ��������ǶԵ��仰�����е���ִʵ������ֱ�ӵ���dowordcut��
 * @author pjl
 *
 */
public class dowordcut {
	public dowordcut() {
//		init();
}

/**
 * ��ȡ�������ʴʵ�
 */
HashMap<String, Integer> wordsDict = new HashMap<String,Integer>();
/**
 * �����Ĵ���Ȩֵ���Ӻʹʳ�Ȩֵ����
 */
public static HashMap<String,Double> a_b = new HashMap<String,Double>();


/**
 * ����ִʣ�����item��value��hashmap�����Ѵ�Ƶͳ�Ƴ���,���γ������ʴʵ�
 * @param content
 * @return
 * @throws Exception 
 */
public HashMap<String,Integer> docutword(String content) throws Exception{
//	List<String> resultMap = new ArrayList<>();
	HashMap<String,Integer> result = new HashMap<String,Integer>();
	Set StopWordSet = new HashSet<String>();

	try {
		content = content.replaceAll("\"", "");
		content = content.replaceAll("\\([^\\(^\\)]*\\)", "");  //正则去()内容
		content = content.replaceAll("\\（[^\\(^\\)]*\\）", "");  //正则去（）内容
		content = content.replaceAll("\\【[^\\(^\\)]*\\】", "");  //正则去【】内容
		content = content.replaceAll("[\\pP\\pS\\pZ]", "");   //去除符号
		SegmentSentence.Builder(content,1);   // �ִ�ѡ������content��ʾҪ�ִʵ����ݣ�1��ʾhanlp�ִʣ�2��ʾ�п�Ժ�ִ�
		String[] arr = SegmentSentence.segment();
		
		StopWordSet = Stopword();
		for(String temp:arr) {
			String[] wt = temp.split("/");
			if(wt.length!=2) {
				continue;
			}
			String item = wt[0];
			String ext = wt[1];
			
			if(!(ext.startsWith("w")||ext.startsWith("w,")||ext.startsWith("ude1,")||ext.startsWith("ude2,")||
					ext.startsWith("ude3,")||ext.startsWith("pbei,")||ext.startsWith("vyou,")||ext.startsWith("udeng,")||
					ext.startsWith("ule,"))) {
//				if(ext.startsWith("n")||ext.startsWith("un")||ext.startsWith("v")||ext.startsWith("a")
//						||ext.startsWith("n,")||ext.startsWith("vn,")||ext.startsWith("v,")
//						||ext.startsWith("nis,")||ext.startsWith("ns,")||ext.startsWith("nr,")
//						||ext.startsWith("nnt,")||ext.startsWith("nsf,")||ext.startsWith("nt,")
//						||ext.startsWith("nis,")||ext.startsWith("vi,")||ext.startsWith("a,")) {   //ext�ַ�����ͷ�Ƿ�Ϊn un v   ||ext.startsWith("un")   ||ext.startsWith("nr")||ext.startsWith("ns")
				if(item.trim().startsWith("[")) {
					item = item.replace("[", " ");
				}
				//去除包含数字和字母的字符串
				boolean isDigit = false;
				boolean isLetter = false;
				for(int z=0;z<item.trim().length();z++) {
					if(Character.isDigit(item.trim().charAt(z)))
						isDigit = true;
//						if(Character.isLetter(item.trim().charAt(z)))
//							isLetter = true;
				}
				
				if(StopWordSet.contains(item.trim())||(item.trim().length())==1||item.matches("[a-zA-Z]+")
						||item.matches("[0-9]*")||isDigit||judgeContainsStr(item.trim())||item.trim()=="编辑"
						||item.trim()=="监制"||item.trim()=="责任编辑"||item.trim()=="总编辑"||item.matches("[\\pP\\pS\\pZ]")) {  //||(item.trim().length())==1
					continue;
				}else {
					item = item.replaceAll("[\\pP\\pS\\pZ]", ""); 
					if(item.trim().length()>1) {
						result = addWord(result, item.trim());
//						resultMap.add(item.trim());  //trim()���������ַ���������������ǰ���ո��β���ո�
						System.out.println(item.trim());
					}
//					resultMap.add(item.trim());  //trim()���������ַ���������������ǰ���ո��β���ո�
//					System.out.println(item.trim());
				}
			}
		}
//		if(resultMap.size()<1) {
//			System.out.print(1);
//		}
//		List<String> add = resultMap;
//		while(resultMap.size()>=1&&resultMap.get(0).length()>1&&resultMap.size()<50) {
//			resultMap.addAll(add);
//		}
//	
//		for(int i=0;i<resultMap.size();i++) {
//			addWord(result,resultMap.get(i));
//		}
	}catch(Exception e) {
		e.printStackTrace();
		System.exit(0);
	}
	return result;
}

/**
 * ����ִʣ����ɵ���list�����Ѵ�Ƶͳ�Ƴ���,���γ������ʴʵ�
 * @param content
 * @return
 * @throws Exception 
 */
public List<String> docutwordone(String content) throws Exception{
	List<String> resultMap = new ArrayList<>();
	Set StopWordSet = new HashSet<String>();

	try {			
		SegmentSentence.Builder(content,1);   // �ִ�ѡ������content��ʾҪ�ִʵ����ݣ�1��ʾhanlp�ִʣ�2��ʾ�п�Ժ�ִ�
		String[] arr = SegmentSentence.segment();
		
		StopWordSet = Stopword();
		for(String temp:arr) {
			String[] wt = temp.split("/");
			if(wt.length!=2) {
				continue;
			}
			String item = wt[0];
			String ext = wt[1];
			
			if(!(ext.startsWith("w")||ext.startsWith("w,")||ext.startsWith("ude1,")||ext.startsWith("ude2,")||
					ext.startsWith("ude3,")||ext.startsWith("pbei,")||ext.startsWith("vyou,")||ext.startsWith("udeng,")||
					ext.startsWith("ule,"))) {
//				if(ext.startsWith("n")||ext.startsWith("un")||ext.startsWith("v")||ext.startsWith("a")
//						||ext.startsWith("n,")||ext.startsWith("vn,")||ext.startsWith("v,")
//						||ext.startsWith("nis,")||ext.startsWith("ns,")||ext.startsWith("nr,")
//						||ext.startsWith("nnt,")||ext.startsWith("nsf,")||ext.startsWith("nt,")
//						||ext.startsWith("nis,")||ext.startsWith("vi,")||ext.startsWith("a,")) {   //ext�ַ�����ͷ�Ƿ�Ϊn un v   ||ext.startsWith("un")   ||ext.startsWith("nr")||ext.startsWith("ns")
				if(item.trim().startsWith("[")) {
					item = item.replace("[", " ");
				}
				//去除包含数字和字母的字符串
				boolean isDigit = false;
				boolean isLetter = false;
				for(int z=0;z<item.trim().length();z++) {
					if(Character.isDigit(item.trim().charAt(z)))
						isDigit = true;
//						if(Character.isLetter(item.trim().charAt(z)))
//							isLetter = true;
				}
				
				if(StopWordSet.contains(item.trim())||(item.trim().length())==1||item.matches("[a-zA-Z]+")
						||item.matches("[0-9]*")||isDigit||judgeContainsStr(item.trim())||item.trim()=="编辑"
						||item.trim()=="监制"||item.trim()=="责任编辑"||item.trim()=="总编辑"||item.matches("[\\pP\\pS\\pZ]")) {  //||(item.trim().length())==1
					continue;
				}else {
//					item = item.replaceAll("[\\pP\\pS\\pZ]", "");
					if(item.trim().length()>1) {
						resultMap.add(item.trim());  //trim()���������ַ���������������ǰ���ո��β���ո�
						System.out.println(item.trim());
					}
//					resultMap.add(item.trim());  //trim()���������ַ���������������ǰ���ո��β���ո�
//					System.out.println(item.trim());
				}
			}
		}
		if(resultMap.size()<1) {
			System.out.print(1);
		}
		List<String> add = resultMap;
		while(resultMap.size()>=1&&resultMap.get(0).length()>1&&resultMap.size()<50) {
			resultMap.addAll(add);
		}
	}catch(Exception e) {
		e.printStackTrace();
		System.exit(0);
	}
	return resultMap;
}

/**  
 * 使用正则表达式来判断字符串中是否包含字母  
 * @param str 待检验的字符串 
 * @return 返回是否包含  
 * true: 包含字母 ;false 不包含字母
 */  
public boolean judgeContainsStr(String str) {  
    String regex=".*[a-zA-Z]+.*";  
    Matcher m=Pattern.compile(regex).matcher(str);  
    return m.matches();  
}  

/**
 * ��resultMap�����word
 * @param resultMap
 * @param word
 */

 public HashMap<String,Integer> addWord(HashMap<String,Integer> resultMap,String word) {  //private
	if(resultMap.containsKey(word)) {//����Ѿ����ڸ����Ƶ+1
		resultMap.put(word, resultMap.get(word)+1);
	}else { //��������ڣ�����Ӹ�����Ѵ�Ƶ����Ϊ1
		resultMap.put(word, 1);
	}
	return resultMap;
}

 /**
  * ����ͣ�ôʱ�
  * @return
 * @throws Exception 
  */
 public Set Stopword() throws Exception {
	 File stopwordlist = new File("中文停用词表.txt");
//		 String s = "D:\\\\��Ȼ���Դ���\\\\��Ȼ���Դ�������\\\\������ȡ\\\\�ٶ�ͣ�ô��б�.txt";  ����ͣ�ôʱ�.txt
	 FileReader filereader = new FileReader(stopwordlist);
	 Set StopWordSet = new HashSet<String>();
	 BufferedReader reader = new BufferedReader(filereader);
	 String stopWord = null;
	 while((stopWord = reader.readLine())!=null) {
		 StopWordSet.add(stopWord.trim());
	 }
	 return StopWordSet;
 }
 
/**
 * ���ļ��м��ط���������Ϣ�����ظ�ʽ�� �����=������
 * @param file
 * @return 
 * @throws IOException
 */
public HashMap<String,Integer> loadClassFromFile(File file) throws IOException{
	HashMap<String,Integer> result = new HashMap<String,Integer>();
	BufferedReader reader = new BufferedReader(new FileReader(file));
	String temp = null;
	while((temp = reader.readLine())!=null) {
		String[] str = temp.split(" ");
		result.put(str[1], Integer.parseInt(str[0]));  //Integer.parseInt() ��string����ת����int����
	}
	return result;
}

/**
 * gbkתutf-8
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

}
