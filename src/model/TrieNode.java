package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import io.textFileFactory;

class hashtag implements Comparable<hashtag>{
	public String hash;
	public ArrayList<Integer> indexs=new ArrayList<Integer>();
	public hashtag(String hash) {
		this.hash=hash;
	}
	public void add_index(int index) {
		indexs.add(index);
	}
	@Override
	public String toString() {
		return "hashtag [hash=" + hash + ", indexs=" + indexs + "]";
	}
	@Override
	public int compareTo(hashtag o) {
		if (hash == null) return -1;
        if (o == null || o.hash == null) return 1;
        if (indexs.size()>o.indexs.size()) return 1;
        if (indexs.size()<o.indexs.size()) return -1;

		return 0;
	}	
}
interface text_analist{
	void analist(String s);
}
class alignment_analist implements text_analist{
	int maxRow;

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public alignment_analist(int maxRow) {
		this.maxRow = maxRow;
	}

	@Override
	public void analist(String input) {
	    String s="";
	    int start = 0;
	    input = input.replace("\n", "");
		int length = input.length();
	    while (start + maxRow < length) {
			 int end = start + maxRow;
			 if (end > length) end = length;
			 else if (end < length) {
			     while (end > start && input.charAt(end - 1) != ' ') end--;
			          if (end == start) end = start + maxRow;
			  }
			  s=s+input.substring(start, end)+'\n';
			  start = end;
	    }
	    s=s+input.substring(start, length);
	    System.out.println(s);
	}
	
}
class hashtag_analist implements text_analist{
	int n;
	public void setN(int n) {
		this.n = n;
	}
	public hashtag_analist(int n){
		this.n=n;
	}
	public hashtag[] clean_array(hashtag[] h) {
		int i=0;
		while((i<n)&&(h[i]!=null)) {
			i++;
		}
		hashtag hashtags[]= new hashtag[i];
		for(int j=0; j<i;j++) {
			hashtags[j]=h[j];
		}
		return hashtags;
	}
	public void analist(String s) {
		ArrayList<String> hash = textFileFactory.ReadHashtags("C:\\Users\\Admin\\eclipse-workspace\\spelling\\hashtag");
		String arr[] = s.split(" ");
		hashtag hashtags[]= new hashtag[n];
		int length=arr.length;
		int k=0;
		int t=length/n+1;
		for(String h: hash) {
			int i=0;
			while((i<length)&&(!arr[i].equals(h))) {				
				i++;
			}
			if(i<length) {
				hashtags[k]=new hashtag(h);
				for(;i<length;i++) {
					if(arr[i].equals(h)) {hashtags[k].add_index(i);i=(i/t+1)*t;}
				}
				k++;
			}
			if (k>=n) break;
		}
		for(int i=0; i<n;i++) {
			System.out.println(hashtags[i]);
		}
		hashtags=clean_array(hashtags);
		Arrays.sort(hashtags);
		
		int count[]= new int[n];
		for(int i=0; i<n;i++) count[i]=0;
		for(hashtag h: hashtags) {
			int min=h.indexs.get(0);
			for(int i=1;i<h.indexs.size();i++) {
				if(count[h.indexs.get(i)/t]<count[min/t]) min=h.indexs.get(i);
			}
			count[min/t]++;
			h.indexs=new ArrayList<Integer>();
			h.indexs.add(min);
			arr[min]='#'+h.hash;
		}
		String s2="";
		for(String a: arr) {
			s2=s2+" "+a;
		}
		System.out.println(s2);

	}
}

class spell_text implements text_analist{
	TrieNode root = textFileFactory.ReadTrie("D:\\words_alpha.txt");
	public void analist(String s) {
		
		String arr[]=s.split(" ");
		for(int i=0; i<arr.length;i++) {
			if(root.check(arr[i].toLowerCase())==-1) System.out.println(arr[i]+": incorect");
		}
	}
}

public class TrieNode {
	TrieNode Trie[];
	boolean isEnd;
	static int m=0;
	public TrieNode() {
		Trie=new TrieNode[256];
		for(int i=0;i<256;i++) {
			Trie[i]=null;
		}
		isEnd=false;
	}
	public void InsertTrie(String s) {
		TrieNode temp = this;
		for(int i=0;i<s.length();i++) { 
			 if (temp.Trie[s.charAt(i)] == null) {
				temp.Trie[s.charAt(i)] = new TrieNode();
		     }
			 temp=temp.Trie[s.charAt(i)];
		}
		temp.isEnd=true;
		temp.Trie[',']= new TrieNode(); temp.Trie[','].isEnd=true;
		temp.Trie['.']= new TrieNode(); temp.Trie['.'].isEnd=true;

	}
	static void printSuggestions(TrieNode root, String res){
	    
		if (root.isEnd == true){
	        System.out.print(res + ", "); m++;
	    }
	    for(int i = 'a'; i <='z'; i++){
	        if (root.Trie[i] != null){
	            res += (char)i;
	            printSuggestions(root.Trie[i], res);
	            res=res.substring(0,res.length()-1);
	        }
            if (m>2) break; 
	    }
	}
	public int check(String s) {
		s=s.replace("\n", "");
		TrieNode temp=this;
		if(s.length()==0) return 1;
		if(('a'>s.charAt(0)||s.charAt(0)>'z')) {
			try {
		        int intValue = Integer.parseInt(s);
		        return 1;
		    } catch (NumberFormatException e) {
		        return -1;
		    }
		}
		for(int i=0;i<s.length();i++) {
			if(temp.Trie[s.charAt(i)]==null) {
				if(temp.isEnd==true) { System.out.println(s+": maybe you mean "+s.substring(0,i));return 1;}
				return -1;
			}
			temp=temp.Trie[s.charAt(i)];
		}
		if (temp.isEnd==true) {
			return 1;
		}
		else {System.out.print(s+": [");printSuggestions(temp, s);System.out.println("]");m=0;}
		return 0;
	}
	
	public static void main(String[] args) {
		spell_text sp = new spell_text();
		hashtag_analist h = new hashtag_analist(3);
		alignment_analist a =new alignment_analist(50);
		Scanner sc = new Scanner(System.in);
		
		//String s = textFileFactory.Readtext("C:\\Users\\Admin\\eclipse-workspace\\spelling\\test");
		String s= "comput";
		while(true) {
			System.out.println("Hello, Please choose the function you want:\n 1. Spell your text\n 2. Auto add hashtag\n 3. Agliment\n 4. Exit");
			int c = sc.nextInt();
			switch (c) {
				case 1: sp.analist(s); break;
				case 2: {
					System.out.println("How many hashtags do you want?");
					h.setN(sc.nextInt());
					h.analist(s);
					break;
				}
				case 3: {System.out.println("length of each line?"); a.setMaxRow(sc.nextInt()); a.analist(s); break;
				}
				case 4: System.exit(1);
				default: System.out.println("retype");
				
			}
		}
	}
	
}

