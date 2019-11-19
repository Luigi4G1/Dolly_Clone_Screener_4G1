package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils4G1 {
	
	public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    
    public final static String html = "html";
    public final static String xml = "xml";
    public final static String txt = "txt";

    /*
     * Get the extension of a file.
     * */
    public static String getExtension(File f)
    {
    	String ext = null;
    	String s = f.getName();
    	int i = s.lastIndexOf('.');

    	if (i > 0 &&  i < s.length() - 1){
    		ext = s.substring(i+1).toLowerCase();
    	}
    	return ext;
    }

	public static String filePathNormalize(String filepath)
	{
		if(filepath == null)
			return null;
		
		String toReturn = filepath.trim();
		if(File.separatorChar == '/')
			toReturn = toReturn.replace('\\', File.separatorChar);
		else
			toReturn = toReturn.replace('/', File.separatorChar);
		
		return toReturn;
	}
	
	public static String filePathNormalizeURI(String filepath)
	{
		if(filepath == null)
			return null;
		
		String toReturn = filepath.trim();
		toReturn = toReturn.replace('\\', '/');

		return toReturn;
	}
	
	public static String removeStartingSeparator(String filepath)
	{
		if(filepath == null)
			return null;
		
		char[] char_array = filepath.toCharArray();
		
		char starting_char = 0;
		if(char_array.length>0)
			starting_char = char_array[0];
		
		while(starting_char == File.separatorChar)
		{
			int new_length = char_array.length-1;
			if(new_length > 0)
			{
				char[] reduced_char_array = new char[new_length];
				for(int index = 0; index<new_length;index++)
				{
					reduced_char_array[index] = char_array[index+1];
				}
				char_array = reduced_char_array;
				
				if(char_array.length>0)
					starting_char = char_array[0];
				else
					starting_char = 0;
			}
			else
			{
				char_array = new char[0];
				starting_char = 0;
			}
		}
		
		return new String(char_array);
	}
	
	public static String removeTrailingSeparator(String filepath)
	{
		if(filepath == null)
			return null;
		
		char[] char_array = filepath.toCharArray();
		
		char last_char = 0;
		if(char_array.length>0)
			last_char = char_array[char_array.length - 1];
		
		while(last_char == File.separatorChar)
		{
			int new_length = char_array.length-1;
			if(new_length > 0)
			{
				char[] reduced_char_array = new char[new_length];
				for(int index = 0; index<new_length;index++)
				{
					reduced_char_array[index] = char_array[index];
				}
				char_array = reduced_char_array;
				
				if(char_array.length>0)
					last_char = char_array[char_array.length - 1];
				else
					last_char = 0;
			}
			else
			{
				char_array = new char[0];
				last_char = 0;
			}
		}

		return new String(char_array);
	}
	
	public static void deleteDirectoryRecursionJava6(File file)
	{
		if(file.isDirectory())
		{
			File[] entries = file.listFiles();
			if (entries != null)
			{
				for (File entry : entries)
				{
					deleteDirectoryRecursionJava6(entry);
				}
			}
		}
		
		if(!file.delete())
		{
			try {
				throw new IOException("Failed to delete " + file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean copyRenameFile(File origin, File destination)
	{
		//INPUT ERROR
		if(origin == null)
		{
			System.out.println("File ORIGIN NULL!");
			return false;
		}
		if(destination == null)
		{
			System.out.println("File DESTINATION NULL!");
			return false;
		}
		
		boolean result = false;
		InputStream ins = null;
		OutputStream outs = null;
		try
		{
			//throws FileNotFoundException if NOT EXISTS
			ins = new FileInputStream(origin);
			outs = new FileOutputStream(destination);
			byte[] buffer = new byte[1024];
			int file_length;
			while((file_length = ins.read(buffer))>0)
			{
				outs.write(buffer, 0, file_length);
			}
			result = true;
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e)//All the remaining Exceptions, like SecurityException, for example.
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if(ins != null)
					ins.close();
				if(outs != null)
					outs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
