import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.Assert;

public class SHA1BackupMainTest {
	
	private SHA1BackupMain sut = new SHA1BackupMain();
	
	@Test
	public void test() throws NoSuchAlgorithmException 
	{
		//Prepare
		File dir1 = new File("test/dir1");
		Map<String, List<String>> shaPath1 = new HashMap<String,List<String>>();
		//Perfom
		sut.recursiveScanDir(dir1,"",shaPath1);
		//Post-Check
		Assert.assertEquals(2, shaPath1.size());
	}

}
