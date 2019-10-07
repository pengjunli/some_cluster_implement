package File_Operation;

import java.io.File;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * ����Ŀ��·�������ض�Ӧ���������µ��ı�·��
 * 
 * @author pby
 * */

public class labels_filepaths {
	
	/**������·��*/
	private static String maindir;
	private static Integer flag=0;//Ĭ��Ϊ0���������-��Ӧ�ĵ�·��
	
	/**
	 * ���ñ�־λ  flag=0 ����������Ӧ·�� flag=1����������Ӧ�ĵ�����
	 * @param flag
	 * */
	public static void setflag(Integer flags) {
		flag=flags;
	}
	
	public static void setmaindir(String path) {
		maindir=path;
	}
	
	/**
	 * @param flag flag=0(Ĭ��) ʱ�������-��Ӧ�ĵ�·�� flag=1�������-��Ӧ�ĵ���  
	 * @return ����һ��Map����
	 * */
	public static Map Get(String path){
		
		/**������·��*/
		setmaindir(path);
		
		/**���ص�ӳ�� ���-�ı�·��*/
		Map<String,Set<String>>callback_l_p=new HashMap<String,Set<String>>();
		
		/**���ص�ӳ�� ���-�ı�����*/
		Map<String,Integer>callback_l_n=new HashMap<String,Integer>();
		
		
		File Pdir= new File(maindir);
		if(Pdir.exists()) {
			File Labels[]=Pdir.listFiles();
			for(File label:Labels) {
				String l=label.getName();
				
				/**��ʼ����Ӧ����µ�Map K-V*/
				callback_l_n.put(l, 0);
				callback_l_p.put(l, new HashSet<String>());
					
				/**����Ӧ��ǩ�µ��ļ�·������mapӳ��*/
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
	 * @param flag flag=0(Ĭ��) ʱ�������-��Ӧ�ĵ�·�� flag=1�������-��Ӧ�ĵ���  
	 * @return ����һ��Map����
	 * */
	public static Map Get(String path,Integer flag){
		setflag(flag);
		return Get(path);
		
	}
	}
