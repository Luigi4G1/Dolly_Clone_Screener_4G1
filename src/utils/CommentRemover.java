package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CommentRemover {
	
	public static final String REMOVED_COMMENTS_FILE_RESULT = "./comment_REMOVED.txt";

	//String line -> single line where to remove all spaces characters [also NEWLINE]
	public static String removeSpaces(String line)
	{
		String norm_line_string = "";
		if(line != null)
		{
			char[] to_char_array = line.toCharArray();
			for(int index_char = 0;index_char<to_char_array.length;index_char++)
			{
				if(!Character.isWhitespace(to_char_array[index_char]))
					norm_line_string = norm_line_string + to_char_array[index_char];
				/*
				if((to_char_array[index_char] != ' ')&&(to_char_array[index_char] != '\t')&&(to_char_array[index_char] != '\r')&&(to_char_array[index_char] != '\b')&&(to_char_array[index_char] != '\n')&&(to_char_array[index_char] != '\f'))
					norm_line_string = norm_line_string + to_char_array[index_char];
				*/
			}
			//line = line.replaceAll(" ", "");
		}
		return norm_line_string;
	}
	
	public static String removeComments(String filepath)
	{
		//INPUT ERROR CONTROL
		if((filepath == null)||(filepath.trim().isEmpty()))
		{
			String response = "FILEPATH NOT SET->";
			if(filepath == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		String string_to_return = null;
		File file_to_normalize = new File(filepath);
		try
		{
			Scanner scanner_file_to_normalize = new Scanner(file_to_normalize);
			
			StringBuilder file_as_string_builder = new StringBuilder();
			StringBuilder file_normalized = new StringBuilder();
			//READ the file AS IS and save into a String
			while(scanner_file_to_normalize.hasNextLine())
				file_as_string_builder.append(scanner_file_to_normalize.nextLine()+System.lineSeparator());
			scanner_file_to_normalize.close();
			
			String file_as_string = file_as_string_builder.toString();
			int line_number = 0;
			int escape_num = 0;
			boolean inString = false;
			boolean inChar = false;
			boolean multiline_deletion = false;
			boolean singleline_deletion = false;
			int file_index = 0;
			char currentChar = '\u0000';//NULL CHAR
			
			while(file_index<file_as_string.length())
			{
				char previousChar = currentChar;
				currentChar = file_as_string.charAt(file_index);
				if((file_index == 0)||(previousChar == '\n'))
					line_number++;
				
				//START MODs FROM HERE
				if(inChar)
				{
					if(currentChar == '\\')
					{
						escape_num++;
						if(escape_num>2)
						{
							//ERROR! INVALID ESCAPE sequence
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[#"+escape_num+" BACKSLASH+"+currentChar+"]");
						}
					}	
					else if(currentChar == '\'')
					{
						if((previousChar == '\'')&&(escape_num != 1))
						{
							//ERROR! INVALID CHAR
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID CHAR sequence[NOTHING BETWEEN APOSTROPHES -EVEN BLANK SPACE-]");
						}
						else
						{
							inChar = false;
							escape_num = 0;
						}
					}
					else if(currentChar == '\n')
					{
						inChar = false;
						escape_num = 0;
						//ERROR! A String MUST be CLOSED before the NEWLINE
						//throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NEWLINE FOUND before CHAR closure[PREV:"+previousChar+"]");
					}
					//ADD CHAR to NEW FILE
					file_normalized.append(currentChar);
				}//END MODs HERE
				else if(inString)//CHARACTERS in a STRING -> no remove COMMENTS
				{
					if(currentChar == '\\')
						escape_num++;
					else
					{
						if(currentChar == '\n')
						{
							//ERROR! A String MUST be CLOSED before the NEWLINE
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NEWLINE FOUND before STRING closure");
						}
						
						if(escape_num == 0)
						{
							if(currentChar == '\"')
							{
								inString = false;
								//END STRING
								//CONTROL CLOSURE of STRING from this file_index -> file_as_string
								//-> NOT CHECKED -> ASSUMING NO SYNTAX ERROR
								//-> MUST CHECK: +concat | ;EOS | .stringclassvalidmethodinvocation()
								//NOTE: SPACES \n \r \t are VALID in between
							}
						}
						else
						{
							//VALID ESCAPE SEQUENCE
							//->\b
							//->\t
							//->\n
							//->\f
							//->\r
							//->\'
							//->\"
							//->\\
							//->\+u ->UNICODE SEQUENCE
							if((currentChar == 'u')||(currentChar == 'b')||(currentChar == 't')||(currentChar == 'n')||(currentChar == 'f')||(currentChar == 'r')||(currentChar == '\''))
							{
								//OK
								escape_num = 0;
							}
							else
							{
								if(currentChar == '\"')
								{
									if((escape_num % 2) == 0)
									{
										//END STRING
										inString = false;
										//CONTROL CLOSURE of STRING from this file_index -> file_as_string
										//-> NOT CHECKED -> ASSUMING NO SYNTAX ERROR
										//-> MUST CHECK: +concat | ;EOS | .stringclassvalidmethodinvocation()
										//NOTE: SPACES \n \r \t are VALID in between
									}
									else
									{
										//VALID ESCAPE sequence
										escape_num = 0;
										inString = true;
									}
								}
								else
								{
									if((escape_num % 2) == 0)
									{
										//OK
										escape_num = 0;
									}
									else
									{
										//ERROR! INVALID ESCAPE sequence
										throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[#"+escape_num+" BACKSLASH+"+currentChar+"]");
									}
								}
							}
						}
						escape_num = 0;
					}
					//ADD CHAR to NEW FILE
					file_normalized.append(currentChar);
				}
				else
				{
					//->NOT IN A STRING/CHAR SECTION
					if(multiline_deletion)
					{
						if((currentChar == '\r')||(currentChar == '\n'))
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							file_normalized.append(currentChar);
						}
						else if(currentChar == '*')
						{
							singleline_deletion = true;
							//USING the PRECEDENCE of the multiline_deletion BOOLEAN VAR
							//AVOIDING bad syntax case -> /*/
						}
						else if((currentChar == '/')&&(previousChar == '*'))
						{
							if(singleline_deletion)
							{
								singleline_deletion = false;
								multiline_deletion = false;
							}
							else
							{
								//bad syntax case -> /*/
								throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[/*/]");
							}
						}
						else
						{
							//REMOVE CHAR
						}
					}
					else if(singleline_deletion)
					{
						if(currentChar == '\r')
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							file_normalized.append(currentChar);
							//TEST PRINT
							//System.err.println("CARR RET->"+line_number);
						}
						else if(currentChar == '\n')
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							//END DELETION -> NEWLINE
							file_normalized.append(currentChar);
							singleline_deletion = false;
							multiline_deletion = false;
							//TEST PRINT
							//System.err.println("NEW LINE->"+line_number);
						}
						else
						{
							//REMOVE CHAR
						}
					}
					else
					{
						//-> find //
						//-> find /*
						//->boolean deletion = false;
						//->while(deletion){}
						if(currentChar == '\'')
						{
							inString = false;
							inChar = true;
							singleline_deletion = false;
							multiline_deletion = false;
							file_normalized.append(currentChar);
						}
						else if(currentChar == '\"')
						{
							inString = true;
							inChar = false;
							singleline_deletion = false;
							multiline_deletion = false;
							file_normalized.append(currentChar);
						}
						else if((currentChar == '*')&&(previousChar == '/'))
						{
							inString = false;
							inChar = false;
							singleline_deletion = false;
							multiline_deletion = true;
							file_normalized.deleteCharAt(file_normalized.length()-1);
						}
						else if((currentChar == '/')&&(previousChar == '/'))
						{
							inString = false;
							inChar = false;
							singleline_deletion = true;
							multiline_deletion = false;
							file_normalized.deleteCharAt(file_normalized.length()-1);
						}
						else
						{
							file_normalized.append(currentChar);
						}
					}
				}
				//NEXT CHAR
				file_index++;
			}
			//TEST-> RESULT
			//System.out.println(file_normalized.toString());
			//TEST-> WRITE TO FILE
			//createFileWithoutCommentsFromString(file_normalized.toString());
			//NEXT -> HASHMAP
			string_to_return = file_normalized.toString();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(RuntimeException re)
		{
			re.printStackTrace();
			return null;
		}
		return string_to_return;
	}
	
	public static String removeCommentsFromFileAsString(String file_as_string)
	{
		//INPUT ERROR CONTROL
		if((file_as_string == null)||(file_as_string.trim().isEmpty()))
		{
			String response = "FILE AS STRING NOT SET->";
			if(file_as_string == null)
				response = response + "NULL";
			else
				response = response + "EMPTY";
			System.out.println(response);
			return null;
		}
		String string_to_return = null;

		try
		{
			StringBuilder file_normalized = new StringBuilder();

			int line_number = 0;
			int escape_num = 0;
			boolean inString = false;
			boolean inChar = false;
			boolean multiline_deletion = false;
			boolean singleline_deletion = false;
			int file_index = 0;
			char currentChar = '\u0000';//NULL CHAR
			
			while(file_index<file_as_string.length())
			{
				char previousChar = currentChar;
				currentChar = file_as_string.charAt(file_index);
				if((file_index == 0)||(previousChar == '\n'))
					line_number++;
				
				//START MODs FROM HERE
				if(inChar)
				{
					if(currentChar == '\\')
					{
						escape_num++;
						if(escape_num>2)
						{
							//ERROR! INVALID ESCAPE sequence
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[#"+escape_num+" BACKSLASH+"+currentChar+"]");
						}
					}	
					else if(currentChar == '\'')
					{
						if((previousChar == '\'')&&(escape_num != 1))
						{
							//ERROR! INVALID CHAR
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID CHAR sequence[NOTHING BETWEEN APOSTROPHES -EVEN BLANK SPACE-]");
						}
						else
						{
							inChar = false;
							escape_num = 0;
						}
					}
					else if(currentChar == '\n')
					{
						//ERROR! A String MUST be CLOSED before the NEWLINE
						throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NEWLINE FOUND before CHAR closure");
					}
					//ADD CHAR to NEW FILE
					file_normalized.append(currentChar);
				}//END MODs HERE
				else if(inString)//CHARACTERS in a STRING -> no remove COMMENTS
				{
					if(currentChar == '\\')
						escape_num++;
					else
					{
						if(currentChar == '\n')
						{
							//ERROR! A String MUST be CLOSED before the NEWLINE
							throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NEWLINE FOUND before STRING closure");
						}
						
						if(escape_num == 0)
						{
							if(currentChar == '\"')
							{
								inString = false;
								//END STRING
								//CONTROL CLOSURE of STRING from this file_index -> file_as_string
								//-> NOT CHECKED -> ASSUMING NO SYNTAX ERROR
								//-> MUST CHECK: +concat | ;EOS | .stringclassvalidmethodinvocation()
								//NOTE: SPACES \n \r \t are VALID in between
							}
						}
						else
						{
							//VALID ESCAPE SEQUENCE
							//->\b
							//->\t
							//->\n
							//->\f
							//->\r
							//->\'
							//->\"
							//->\\
							//->\+u ->UNICODE SEQUENCE
							if((currentChar == 'u')||(currentChar == 'b')||(currentChar == 't')||(currentChar == 'n')||(currentChar == 'f')||(currentChar == 'r')||(currentChar == '\''))
							{
								//OK
								escape_num = 0;
							}
							else
							{
								if(currentChar == '\"')
								{
									if((escape_num % 2) == 0)
									{
										//END STRING
										inString = false;
										//CONTROL CLOSURE of STRING from this file_index -> file_as_string
										//-> NOT CHECKED -> ASSUMING NO SYNTAX ERROR
										//-> MUST CHECK: +concat | ;EOS | .stringclassvalidmethodinvocation()
										//NOTE: SPACES \n \r \t are VALID in between
									}
									else
									{
										//VALID ESCAPE sequence
										escape_num = 0;
										inString = true;
									}
								}
								else
								{
									if((escape_num % 2) == 0)
									{
										//OK
										escape_num = 0;
									}
									else
									{
										//ERROR! INVALID ESCAPE sequence
										throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[#"+escape_num+" BACKSLASH+"+currentChar+"]");
									}
								}
							}
						}
						escape_num = 0;
					}
					//ADD CHAR to NEW FILE
					file_normalized.append(currentChar);
				}
				else
				{
					//->NOT IN A STRING/CHAR SECTION
					if(multiline_deletion)
					{
						if((currentChar == '\r')||(currentChar == '\n'))
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							file_normalized.append(currentChar);
						}
						else if(currentChar == '*')
						{
							singleline_deletion = true;
							//USING the PRECEDENCE of the multiline_deletion BOOLEAN VAR
							//AVOIDING bad syntax case -> /*/
						}
						else if((currentChar == '/')&&(previousChar == '*'))
						{
							if(singleline_deletion)
							{
								singleline_deletion = false;
								multiline_deletion = false;
							}
							else
							{
								//bad syntax case -> /*/
								throw new RuntimeException("[LINE "+line_number+"]-bad syntax -> NOT a VALID ESCAPE sequence[/*/]");
							}
						}
						else
						{
							//REMOVE CHAR
						}
					}
					else if(singleline_deletion)
					{
						if(currentChar == '\r')
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							file_normalized.append(currentChar);
							//TEST PRINT
							//System.err.println("CARR RET->"+line_number);
						}
						else if(currentChar == '\n')
						{
							//PRESERVE LINE NUMBER -> NEWLINE system dependence
							//END DELETION -> NEWLINE
							file_normalized.append(currentChar);
							singleline_deletion = false;
							multiline_deletion = false;
							//TEST PRINT
							//System.err.println("NEW LINE->"+line_number);
						}
						else
						{
							//REMOVE CHAR
						}
					}
					else
					{
						//-> find //
						//-> find /*
						//->boolean deletion = false;
						//->while(deletion){}
						if(currentChar == '\'')
						{
							inString = false;
							inChar = true;
							singleline_deletion = false;
							multiline_deletion = false;
							file_normalized.append(currentChar);
						}
						else if(currentChar == '\"')
						{
							inString = true;
							inChar = false;
							singleline_deletion = false;
							multiline_deletion = false;
							file_normalized.append(currentChar);
						}
						else if((currentChar == '*')&&(previousChar == '/'))
						{
							inString = false;
							inChar = false;
							singleline_deletion = false;
							multiline_deletion = true;
							file_normalized.deleteCharAt(file_normalized.length()-1);
						}
						else if((currentChar == '/')&&(previousChar == '/'))
						{
							inString = false;
							inChar = false;
							singleline_deletion = true;
							multiline_deletion = false;
							file_normalized.deleteCharAt(file_normalized.length()-1);
						}
						else
						{
							file_normalized.append(currentChar);
						}
					}
				}
				//NEXT CHAR
				file_index++;
			}
			//TEST-> RESULT
			//System.out.println(file_normalized.toString());
			//TEST-> WRITE TO FILE
			//createFileWithoutCommentsFromString(file_normalized.toString());
			//NEXT -> HASHMAP
			string_to_return = file_normalized.toString();
		}
		catch(RuntimeException re)
		{
			re.printStackTrace();
			return null;
		}
		return string_to_return;
	}
	
	public static boolean createFileWithoutCommentsFromString(String file_normalized_string)
	{
		File file_normalized = new File(REMOVED_COMMENTS_FILE_RESULT);
		try
		{
			if(file_normalized.exists())
				file_normalized.delete();
			
			FileWriter writer = new FileWriter(file_normalized);
			BufferedWriter buffered_writer = new BufferedWriter(writer);
			buffered_writer.write(file_normalized_string);
			buffered_writer.close();
			return true;
		}
		catch(SecurityException | IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public static HashMap<Integer, String> getLinesWithRemovedComments(String filepath)
	{
		String file_normalized_string = removeComments(filepath);
		if(file_normalized_string == null)
			return null;
		else
		{
			File file_normalized = new File(REMOVED_COMMENTS_FILE_RESULT);
			if(createFileWithoutCommentsFromString(file_normalized_string))
			{
				Scanner scanner_file_normalized;
				try {
					scanner_file_normalized = new Scanner(file_normalized);
					int num_lines = 0;
					HashMap<Integer, String> lines_to_return = new HashMap<Integer, String>();
					while(scanner_file_normalized.hasNextLine())
					{
						num_lines++;
						lines_to_return.put(new Integer(num_lines), scanner_file_normalized.nextLine());
					}
					scanner_file_normalized.close();
					if(file_normalized.exists())
						file_normalized.delete();
					System.out.println("->["+lines_to_return.size()+"] - LINES READ for the file:\n"+filepath);
					return lines_to_return;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					if(file_normalized.exists())
						file_normalized.delete();
					return null;
				}
			}
			else
			{
				if(file_normalized.exists())
					file_normalized.delete();
				return null;
			}
		}
	}
}
