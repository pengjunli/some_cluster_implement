package File_Operation;

import java.io.File;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * 遍历目标路径，返回对应类别与类别下的文本路径
 * 
 * @author pby
 * */

public class labels_filepaths {
	
	/**遍历主路径*/
	private static String maindir;
	private static Integer flag=0;//默认为0，返回类别-对应文档路径
	
	/**
	 * 设置标志位  flag=0 返回类别与对应路径 flag=1返回类别与对应文档数量
	 * @param flag
	 * */
	public static void setflag(Integer flags) {
		flag=flags;
	}
	
	public static void setmaindir(String path) {
		maindir=path;
	}
	
	/**
	 * @param flag flag=0(默认) 时返回类别-对应文档路径 flag=1返回类别-对应文档数  
	 * @return 返回一个Map集合
	 * */
	public static Map Get(String path){
		
		/**设置主路径*/
		setmaindir(path);
		
		/**返回的映射 类别-文本路径*/
		Map<String,Set<String>>callback_l_p=new HashMap<String,Set<String>>();
		
		/**返回的映射 类别-文本数量*/
		Map<String,Integer>callback_l_n=new HashMap<String,Integer>();
		
		
		File Pdir= new File(maindir);
		if(Pdir.exists()) {
			File Labels[]=Pdir.listFiles();
			for(File label:Labels) {
				String l=label.getName();
				
				/**初始化对应类别下的Map K-V*/
				callback_l_n.put(l, 0);
				callback_l_p.put(l, new HashSet<String>());
					
				/**将对应标签下的文件路径放入map映射*/
				File txts[]=label.listFiles();
				callback_l_n.put(l, txts.length);
				
				for(File txt:txts) {
					callback_l_p.get(l).add(txt.getPath());
					}
					
				}
			}
		if(flag==0) {
			return callback_l_p;
		}
		return callback_l_n;
		}
	
	
	/**
	 * @param flag flag=0(默认) 时返回类别-对应文档路径 flag=1返回类别-对应文档数  
	 * @return 返回一个Map集合
	 * */
	public static Map Get(String path,Integer flag){
		setflag(flag);
		return Get(path);
		
	}
	}
