package kmeansCluster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class copyfileAnddeletedir {
	/**
	 * 清除文件夹中的所有文件
	 * @param file
	 */
	public static void deleteDir(File file) {
//		File file = new File(path);
		String[] content = file.list();
		for(String name: content) {
			File temp = new File(file,name);
			if(temp.isDirectory()) {
				deleteDir(temp);
				temp.delete();
			}else {
				temp.delete();
			}
		}
	}
	/**
	 * 通过文件通道的方式复制文件
	 * @param strdirname
	 * @param desdirname
	 */
	public static void copyFile(File strdirname,String desdirname)  {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		
		try {
			fi = new FileInputStream(strdirname);
			fo = new FileOutputStream(new File(desdirname));
			in = fi.getChannel();  //得到对应的文件通道
			out = fo.getChannel();  //得到对应的文件通道
			in.transferTo(0,in.size(),out); //链接两个通道，并且从in通道读取，从out通道写入
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

}