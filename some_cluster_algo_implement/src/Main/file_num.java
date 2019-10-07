package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class file_num {
//	public static void main(String[] args) {
	public void filenum(String filelocation) {
		HashMap<String,HashMap<String,Integer>> result = new HashMap<String,HashMap<String,Integer>>();
		File file = new File(filelocation);
		File[] filelist = file.listFiles();
		for(int i=0;i<filelist.length;i++) {
			HashMap<String,Integer> map = new HashMap<String,Integer>();
			File onefile = filelist[i];
			File[] onefilelist = onefile.listFiles();
			for(int j=0;j<onefilelist.length;j++) {
				String str = onefilelist[j].getName().split("-")[0];
				if(map.containsKey(str)) {
					map.put(str, map.get(str)+1);
				}else {
					map.put(str, 1);
				}
			}
			result.put(onefile.getName(), map);
		}
		Iterator<String> striterator = result.keySet().iterator();
		while(striterator.hasNext()) {
			String asa = striterator.next();
			System.out.println("类别"+asa+":");
			HashMap<String,Integer> strmap = result.get(asa);
			Iterator<String> strasa = strmap.keySet().iterator();
			while(strasa.hasNext()) {
				String str1 = strasa.next();
				System.out.print(str1+": "+strmap.get(str1)+"  ");
			}
			System.out.println();
		}
	}
	
	/**
     * 为文本重新命名
     * @param file
     */
	public void rename_file(File file) {
		if(file.isDirectory()) {
			File[] refile = file.listFiles();
			for(File repetition:refile) {
				rename_file(repetition);
			}
		}else if(file.isFile()) {
			String parentfile = file.getParentFile().getName();
			String filename = file.getName();
			String newname = parentfile+"-"+filename;
			file.renameTo(new File(file.getParent(),newname));
		}
		
	}
	
	/**
	 * 测试：将文件中的词语提出
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public HashMap<File,HashMap<String,Integer>> getwords(File file) throws IOException{
		File[] files = file.listFiles();
		HashMap<File,HashMap<String,Integer>> articlewordsmap = new HashMap<File,HashMap<String,Integer>>();
		for(int i=0;i<files.length;i++) {
			HashMap<String,Integer> wordsmap = new HashMap<String,Integer>();
			BufferedReader reader = new BufferedReader(new FileReader(files[i]));
			String str = null;
			while((str=reader.readLine())!=null) {
				String[] strwords = str.split(" ");
				for(String asa:strwords) {
					wordsmap.put(asa.trim(), 1);
				}
			}
			articlewordsmap.put(files[i], wordsmap);
		}
		return articlewordsmap;
	}
	
	public static void main(String[] args) {
		file_num doit = new file_num();
//		File file = new File("D:\\测试\\train");
//		doit.rename_file(file);
		doit.filenum("D:\\pjl\\dataset\\训练集结果");
	}

}
