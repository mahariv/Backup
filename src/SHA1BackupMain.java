
import java.io.FileWriter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SHA1BackupMain {
	private static String inputPath1;
	private static String inputPath2;
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		inputPath1 = "test/dir1";
		inputPath2 = "test/dir2";

		initFile("BashAdd.sh");
		initFile("BashSup.sh");

		File dir1 = new File(inputPath1);
		Map<String, List<String>> shaPath1 = new HashMap<String,List<String>>();
		recursiveScanDir(dir1, "", shaPath1);
		
		File dir2 = new File(inputPath2);
		Map<String, List<String>> shaPath2 = new HashMap<String,List<String>>();
		recursiveScanDir(dir2, "", shaPath2);
		
		compareSha1(shaPath1, shaPath2);

		supFileComp(shaPath2, shaPath1);
		
	}


	private static void compareSha1(Map<String, List<String>> shaPath1, Map<String, List<String>> shaPath2)
	{
		for (Map.Entry<String, List<String>> m : shaPath1.entrySet()) {
			// if both map contain the same key we have to compare the List of String of each map
			if (shaPath2.containsKey(m.getKey())) {
				//compareListOcc(m.getValue(),shaPath2.get(m.getKey()));
			}
			else
			{
				List<String> ls = m.getValue();
				missingFile(ls.get(0));
				shaPath2.put(m.getKey(),m.getValue());
			}

		}
	}

	private static void supFileComp(Map<String, List<String>> shaPath2, Map<String, List<String>> shaPath1) {
		for (Map.Entry<String, List<String>> m : shaPath2.entrySet()) {
			// if both map contain the same key we have to compare the List of String of each map
			if (shaPath1.containsKey(m.getKey())) {
				//compareListOcc(m.getValue(),shaPath1.get(m.getKey()));
			}
			else
			{
				List<String> ls = m.getValue();
				supFile(ls.get(0));
				shaPath2.put(m.getKey(),m.getValue());
			}

		}
	}

	private static void supFile(String s) {
		try{
			File fichierBash = new File("BashSup.sh");
			FileWriter bashWriter = new FileWriter(fichierBash,true); // allow to write at the end of the File
			bashWriter.write("\nrm " + inputPath2 +  s );
			bashWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*
useless ???
	private static void compareListOcc(List<String> l1,List<String> l2)
	{
		for (String s : l1)
		{
			if (l2.contains(s))
			{
				System.out.println("the file " + s + " is commun to the both map");
			}
			else
			{
				System.out.println("the file " + s + " is commun to the both map");
			}
		}
	}
*/

	public static void initFile(String nameBash)
	{
		try {
			File fichierBash = new File(nameBash);
			fichierBash.createNewFile();
			FileWriter bashWriter = null;
			bashWriter = new FileWriter(fichierBash);
			bashWriter.write("#!/bin/sh");
			bashWriter.close();

		}catch (Exception e)
		{System.out.println(e);}

	}

	public static void missingFile(String nameMissingFile)
	{
		try{
			File fichierBash = new File("BashAdd.sh");
			FileWriter bashWriter = new FileWriter(fichierBash,true);
			bashWriter.write("\ncp " + inputPath1 +  nameMissingFile + " " + inputPath2 + "/");
			bashWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public static void recursiveScanDir(File dir,String path, Map<String,List<String>> sha1ToPath) throws NoSuchAlgorithmException{

		//System.out.println(dir);
		for (File f: dir.listFiles()) {
			
			String childPath = path + "/" + f.getName();
			
			if(f.isFile())
			{
				//System.out.println(childPath);
				String sha1 = sha1OfShine(f);
				List<String> l = sha1ToPath.get(sha1);
				if(l == null)
				{
					l = new ArrayList<String>();
					sha1ToPath.put(sha1, l);
				
				}
				l.add(childPath);
			}
			else
			{
				recursiveScanDir(f, childPath, sha1ToPath);
				
			}
			
		}
	}
	private static String sha1OfShine(File f){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			for(;;){
				int data = 0;
				try {
					data = in.read();
				} catch (Exception e) {
					System.out.println(e);
				}
				if(data == -1)
				{
					break;
				}
				md.update((byte) data);
			}
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException();
		}finally {
			try{
				in.close();
			}
			catch (Exception e){
				throw new RuntimeException();
			}
		}
		byte[] digest = md.digest();
		//DigestInputStream di = DigestInputStream(new FileInputStream(f),md);
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}

}
