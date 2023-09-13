package io;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.TrieNode;
public class textFileFactory {
	public static TrieNode ReadTrie(String path) {
		TrieNode root = new TrieNode();
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (line!=null) {
				root.InsertTrie(line);
				line=br.readLine();				
			}
			br.close();
			isr.close();
			fis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}
	public static ArrayList<String> ReadHashtags(String path) {
		ArrayList<String> hashtags = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (line!=null) {
				hashtags.add(line);
				line=br.readLine();				
			}
			br.close();
			isr.close();
			fis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return hashtags;
	}
	public static String Readtext(String path) {
		String s = "";
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			s=line;
			while (line!=null) {
				line=br.readLine();	
				s=s+" \n"+line;
			}
			br.close();
			isr.close();
			fis.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
}
