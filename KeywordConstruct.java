import java.io.*;

import java.util.*;
public class KeywordConstruct {
	
	// This class is used to perform File Parsing, and Keyword Storage and to display top n keywords
	public void parse(String filepath)
	{
		// This mehod parses the content of the input-file one line at a time
		try {
			File f = new File(filepath);
			Scanner input = new Scanner(f);
			String inputText;
			do
			{
				inputText=input.nextLine();
				if (inputText.equalsIgnoreCase("stop")==false)
				{
					processKeyword(inputText);
				}
			}while (inputText.equalsIgnoreCase("stop")==false);
			input.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	// Object creation for HashMap and Fibonacci Heap used for keyword processing
	HashMap<String,Object> h = new HashMap<String,Object>();
	FibonacciHeap heap = new FibonacciHeap();
	public void processKeyword(String inputLine)
	{	
		File f = new File("output_file.txt"); // Output File
		BufferedWriter writer = null;
		if(inputLine.startsWith("$"))
		{
			// Valid Keyword and hit line
			int separator = inputLine.indexOf(' ');
			String keyword = inputLine.substring(1,separator);
			int count = Integer.parseInt(inputLine.substring(separator+1));
			if(h.containsKey(keyword))
			{
				// If keyword is already present in the HashMap, increase its key value
				FibNode oldValue = (FibNode) h.remove(keyword);
				int oldFrequency=oldValue.key;
				h.put(keyword,heap.increaseKey(oldValue, count+oldFrequency));
			}
			else
			{
				// Insert a new keyword with its key value
				h.put(keyword,heap.insertNode(count));
			}
		}
		else if(inputLine.equalsIgnoreCase("stop")==false)
		{
			// Query Line
			int query = Integer.parseInt(inputLine);
			FibNode topResults[] = new FibNode[query];
			for (int i=0;i<query;i++)
			{
				// Find the top n keywords (Nodes)
				topResults[i]=heap.extractMax();
			}
			for (int i=0;i<topResults.length;i++)
			{
				//re-insert into the heap
				heap.insertNode(topResults[i].key);
			}
			String topKeywords[] = new String[query];
			String KeywordString="";
			try
			{
				writer = new BufferedWriter(new FileWriter(f));
				for (int i=0;i<query;i++)
				{
					// Find Keyword string values for the top Node
					for(Map.Entry<String,Object> entry: h.entrySet())
					{
						if(topResults[i].equals(entry.getValue()))
						{
							topKeywords[i]=entry.getKey();
							break;
						}
					}
					
					// Construct output String
					if(i==0)
					{
						KeywordString=KeywordString+topKeywords[i];
					}
					else
					{
						KeywordString=KeywordString+","+topKeywords[i];
					}
				}
				// write to output file
				writer.write(KeywordString);
				writer.newLine();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(inputLine.equalsIgnoreCase("stop"))
		{
			// stop the program
			return;
		}
		else
		{
			// erroneous input combination
			System.out.println("The Input file has an erroneous line -"+inputLine);
			return;
		}
	}
}
