package Main;
import java.awt.GridLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import Chart._28suo_TimeSeriesChart;
import DataBase_Operation.DB_Insert;
import DataBase_Operation.DBconnection;

public class DBMain {
	public static void main(String[] args) throws Exception {
//		DBconnection db = new DBconnection();
//		db.SetConnection();
		
//		JDBCutils jdb = new JDBCutils();
//		jdb.getConnection();
//		DB_Insert ins = new DB_Insert();
//		ins.InsertOperation(false);
		
		//对查询数据建立可视化图表
		JFrame frame=new JFrame("南海相关事件曲线分析图"); 
		frame.setLayout(new GridLayout(1,1,0,0)); 
		 //添加折线图   
		frame.add(new _28suo_TimeSeriesChart().getChartPanel());   
		frame.setBounds(50, 50, 800, 600);  
	    frame.setVisible(true);  
		
		
//		//插入操作
//		//通过配置文件进行连接
//		DB_Insert.InsertOperation(false);
//		
//		//通过手动配置进行连接
//		DB_Insert.InsertOperation(true);
		
		//查询操作，返回查询结果
//		List<Vector>QuerRes=DataBase_Operation.DBconnection.SetConnection(false);
		
	}

}
