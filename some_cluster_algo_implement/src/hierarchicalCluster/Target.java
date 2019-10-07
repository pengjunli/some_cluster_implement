package hierarchicalCluster;

import java.util.HashSet;

/**
 * 存放聚类后的指标和其特征集
 * @author pjl
 *
 */
public class Target {
	private HashSet<String> FeatureSet= new HashSet<>();
	private String TargetName;
	
	public String getTargetName() {
		return TargetName;
	}
	
	public void setTargetName(String TargetName) {
		this.TargetName = TargetName;
	}
	
	public HashSet<String> getFeatureSet(){
		return FeatureSet;
	}
	
	public void setFeatureSet(HashSet<String> FeatureSet) {
		this.FeatureSet = FeatureSet;
	}

}
