package test;

import java.util.Arrays;
import java.util.List;

import utils.GitUtils4G1;

public class Test_Git_Remote_URI_Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final List<String> repos = Arrays.asList(
	            //"https://github.com/MuchContact/java.git",
	            "git@github.com:MuchContact/java.git"//,
	            //"https://github.com/dnsjava/dnsjava.git",
	            //"https://stackoverflow.com/questions/17586339/caused-by-java-lang-classnotfoundexception-com-jcraft-jsch-jschexception"
	            );
		int index = 0;
		for (String gitRepo: repos)
		{
			index++;
			System.out.println("["+index+"]VALID?->"+GitUtils4G1.isValidRepoUri(gitRepo));
		}
	}

}
