package test;

import java.io.File;

import utils.FileUtils4G1;

public class Test_Filepath {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String filepath = "C:/cygwin64/home/DarkGattoGamer/NiCad-5.0/";
		//filepath = filepath.replace('/', File.separatorChar);
		System.out.println(FileUtils4G1.filePathNormalize(filepath));
		//filepath = FileUtils4G1.filePathNormalize(filepath) + File.separator;
		if(filepath.endsWith(File.separator))
		{
			System.out.println("ENDSTRUE");
		}
		filepath = filepath + File.separator+"systems";
		System.out.println(filepath);
		File file = new File(filepath);
		System.out.println(file.exists());
		filepath = FileUtils4G1.filePathNormalize(filepath);
		System.out.println(filepath);
	}

}
