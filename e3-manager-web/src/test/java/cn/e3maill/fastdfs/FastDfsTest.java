package cn.e3maill.fastdfs;

import static org.junit.Assert.*;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDfsTest {

	@Test
	public void upload() throws Exception {
		//读取配置文件
		ClientGlobal.init("D:/git/e3-manager-web/src/main/resources/conf/client.conf");
		//创建一个trackerClient获得TrackerServer对象
		TrackerClient trackerClient = new TrackerClient();
		//获得trackerServer
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个storageClient对象,需要两个参数
		StorageServer storageServer = null;
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//上床文件
		String[] strings = storageClient.upload_file("C:/Users/lokyw/Pictures/Feedback/{AE60D0FE-AD1F-41FE-9A76-C9AD00D1B1FD}/Capture001.png", "png", null);
		for (String string : strings) {
			System.out.println(string);
			
		}
	}
}
