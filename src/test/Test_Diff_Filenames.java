package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Test_Diff_Filenames {

	public static void main(String[] args) {
		
		//String file_name_orig = "./files/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/commit_list.txt";
		//979 -17
		String file_name_orig = "./repo_4G1/copylist.txt";
		String file_name_test = "./repo_4G1/commit_list.txt";
		
		File file_orig = new File(file_name_orig);
		File file_test = new File(file_name_test);
		Scanner sc_orig = null;
		Scanner sc_test = null;
		try {
			sc_orig = new Scanner(file_orig);
			sc_test = new Scanner(file_test);
			
			int index = 0;
			
			boolean continue_equals = (sc_orig.hasNextLine()&&sc_test.hasNextLine());
			boolean string_equals = false;
			while(continue_equals)
			{
				index++;
				String line_orig = sc_orig.nextLine();
				String line_test = sc_test.nextLine();
				
				string_equals = line_orig.startsWith(line_test);
				
				if(string_equals)
				{
					continue_equals = (sc_orig.hasNextLine()&&sc_test.hasNextLine());
				}
				else
					continue_equals = false;
			}
			System.out.println("STOPPED AT LINE->"+index);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(sc_orig != null)
				sc_orig.close();
			if(sc_test != null)
				sc_test.close();
		}

	}

}
