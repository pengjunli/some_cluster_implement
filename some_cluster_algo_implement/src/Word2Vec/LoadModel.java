package Word2Vec;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LoadModel {
	private Map<String,float[]> wordMap = new HashMap<String,float[]>();
	private int words;
	private int size;
	private int topnsize = 10;
	
	/**
	 * 加载模型
	 * @param path
	 * @throws Exception 
	 */
	public void loadmodel(String path) throws Exception {
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		double len =0;
		float vector = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			dis = new DataInputStream(bis);
			//读取词数
			words = Integer.parseInt(readString(dis));
			//读取大小
			size = Integer.parseInt(readString(dis));
			String word;
			float[] vectors = null;
			for(int i=0;i<words;i++) {
				word = readString(dis);
				vectors = new float[size];
				len = 0;
				for(int j=0;j<size;j++) {
					vector = readFloat(dis);
					len += vector*vector;
					vectors[j] = (float)vector;
				}
				len = Math.sqrt(len);
				for(int j=0;j<size;j++) {
					vectors[j] /= len;
				}
				wordMap.put(word, vectors);
				dis.read();
			}
		}finally {
			bis.close();
			dis.close();
		}
	}
	
	/**
	 * 获得词向量
	 * @param word
	 * @return
	 */
	public float[] getWordVector(String word) {
		return wordMap.get(word);
	}
	
	/**
	 * 得到词向量的关联map
	 * @return
	 */
	public Map<String,float[]> getWordMap(){
		return wordMap;
	}
	
	public static float readFloat(InputStream is) throws Exception {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return getFloat(bytes);
	}
	
	public static float getFloat(byte[] b) {
		int accum = 0;
		accum = accum | (b[0] & 0xff) << 0;
		accum = accum | (b[1] & 0xff) << 8;
		accum = accum | (b[2] & 0xff) << 16;
		accum = accum | (b[3] & 0xff) << 24;
		return Float.intBitsToFloat(accum);
	}
	
	private static final int MAX_SIZE = 128;
	
	/**
	 * 读取一个字符串
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	private static String readString(DataInputStream dis) throws IOException {
		// TODO Auto-generated method stub
		byte[] bytes = new byte[MAX_SIZE];
		byte b = dis.readByte();
		int i = -1;
		StringBuilder sb = new StringBuilder();
		while (b != 32 && b != 10) {
			i++;
			bytes[i] = b;
			b = dis.readByte();
			if (i == 49) {
				sb.append(new String(bytes));
				i = -1;
				bytes = new byte[MAX_SIZE];
			}
		}
		sb.append(new String(bytes, 0, i + 1));
		return sb.toString();
	}

	public int getTopNSize() {
		return topnsize;
	}

	public void setTopNSize(int topNSize) {
		this.topnsize = topNSize;
	}

	public int getWords() {
		return words;
	}

	public int getSize() {
		return size;
	}



}
