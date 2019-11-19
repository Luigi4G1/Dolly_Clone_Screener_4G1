package test;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;

@SuppressWarnings("unused")
public class Test_Git_Remote_URI {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final LsRemoteCommand lsCmd = new LsRemoteCommand(null);
	    final List<String> repos = Arrays.asList(
	            "https://github.com/MuchContact/java.git",
	            "git@github.com:MuchContact/java.git",
	            "https://github.com/dnsjava/dnsjava.git",
	            "https://stackoverflow.com/questions/17586339/caused-by-java-lang-classnotfoundexception-com-jcraft-jsch-jschexception"
	            );
	    int index = 0;
	    for (String gitRepo: repos){
	        lsCmd.setRemote(gitRepo);
	        try {
	        	index++;
	        	System.out.println("["+index+"->"+gitRepo);
				System.out.println(lsCmd.call().toString());
				System.out.println("==========================");
			} catch (InvalidRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("["+index+"->INVALID");
			}
	        catch (TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("["+index+"->TRANSPORT");
				e.getCause();
			} catch (GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("["+index+"->GitAPI");
			}
	        catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("["+index+"->NORMALEXCPT");
			}
	    }

	}

}
