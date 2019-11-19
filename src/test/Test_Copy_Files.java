package test;

import java.io.File;
import utils.FileUtils4G1;

public class Test_Copy_Files {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File origin = new File("./files/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/commit_list.txt");
		File dest_fold = new File("./files/Dnsjava/test/");
		File dest = new File("./files/Dnsjava/test/prova_copy.txt");
		if(!dest_fold.exists())
			try {
				dest_fold.mkdirs();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println(dest_fold.mkdirs());
		System.out.println("COPIED->"+FileUtils4G1.copyRenameFile(origin, dest));
		

	}

}
