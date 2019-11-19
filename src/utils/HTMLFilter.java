package utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class HTMLFilter extends FileFilter{

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())
			return true;
		
		String extension = FileUtils4G1.getExtension(f);
		if(extension != null)
		{
			if(extension.equals(FileUtils4G1.html))
				return true;
			else
				return false;
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "HTML files";
	}

}
