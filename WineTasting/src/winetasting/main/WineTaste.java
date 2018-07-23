package winetasting.main;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import winetasting.ServiceImpl.WineTasteServiceImpl;
public class WineTaste {
	public static String srcFile="";
	public static String destFile="";
	private LineNumberReader lineNumberReader;
	private WineTasteServiceImpl wineTasteServiceImpl;
	public WineTasteServiceImpl getWineTasteServiceImpl() {
		return wineTasteServiceImpl;
	}
	public void setWineTasteServiceImpl(WineTasteServiceImpl wineTasteServiceImpl) {
		this.wineTasteServiceImpl = wineTasteServiceImpl;
	}
	public LineNumberReader getLineNumberReader() {
		return lineNumberReader;
	}
	public void setLineNumberReader(LineNumberReader lineNumberReader) {
		this.lineNumberReader = lineNumberReader;
	}
	static final String problemFilename = "person_wine_4.txt"; /* Path to problem file */
	static final String resultFilename = "result4.txt"; /* Path to result file */
	void process(WineTaste wineTaste)
	{
		try {
			String line;
			int lastLineNumber=0;
			LineNumberReader reader = 
					new LineNumberReader
					(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"));

			while (reader.readLine() != null){
				lastLineNumber++;
			}
			reader.setLineNumber(0);
			wineTaste.setLineNumberReader(new LineNumberReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8")));
			wineTaste.getLineNumberReader().setLineNumber(0);
			//wineTaste.getLineNumberReader().readLine();
			ExecutorService executor = Executors.newFixedThreadPool(lastLineNumber/10000);
			for (int i = 0; i < lastLineNumber; i=i+10000) {
				reader.setLineNumber(0);
				Runnable worker = new AddToWishListThread( i,wineTaste,wineTasteServiceImpl);
				executor.execute(worker);
			}
			executor.shutdown();
			while (!executor.isTerminated()) 
			{
			}
			System.out.println("Finished all threads");

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("C:\\Users\\rashika.chopra\\workspace14\\WineTasting\\src\\application.properties"));
		for (Enumeration<?> e = props.propertyNames(); e.hasMoreElements(); ) {
			String name = (String)e.nextElement();
			String value = props.getProperty(name);
			// now you have name and value
			if (name.equals("srcFile")) {
				srcFile=value;
			}
			else if (name.equals("destFile")) {
				destFile=value;
			}
		}
		AbstractApplicationContext applicationContext = null;
		try {
			applicationContext =new ClassPathXmlApplicationContext( "applicationContext.xml" );
			applicationContext.registerShutdownHook();
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
		WineTaste wineTaste =  (WineTaste) applicationContext.getBean("WineTaste");
		wineTaste.process(wineTaste);
		wineTaste.getWineTasteServiceImpl().insertWinesRank();
		wineTaste.getWineTasteServiceImpl().insertIntoPersonsRank();
		Writer writer = new BufferedWriter(new FileWriter(new File(destFile)));
		List<String> selected=new ArrayList<>();
		selected=wineTaste.getWineTasteServiceImpl().selectPersonDetails();
		Iterator iterator=selected.iterator();
		while(iterator.hasNext())
		{
			writer.append((CharSequence) iterator.next());
			writer.append("\n");
		}
		writer.flush();
		writer.close();
	}
}