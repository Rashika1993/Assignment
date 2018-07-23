package winetasting.main;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import winetasting.ServiceImpl.WineTasteServiceImpl;
public class AddToWishListThread implements Runnable{
	private int fromLine;
	private String line;
	private WineTaste wineTaste;
	private int lineNo;
	private WineTasteServiceImpl wineTasteServiceImpl;
	@Override
	public void run() {
		Set<String> set=new HashSet<String>();
		try{
			wineTaste.getLineNumberReader().setLineNumber(lineNo);
			while (((line = wineTaste.getLineNumberReader().readLine()) != null) &&  fromLine<= lineNo+10000) {

				int pid=Integer.parseInt( StringUtils.substringBetween(line, "person", "\twine"));
				int wid=Integer.parseInt( StringUtils.substringAfter(line, "wine"));
				if(pid!=0 && wid!=0)
					set.add(pid+":"+wid);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		set.remove(null);
		wineTasteServiceImpl.addWishlistData(set);
	}
	AddToWishListThread(int lineNo,WineTaste wineTaste,WineTasteServiceImpl wineTasteServiceImpl)
	{
		this.lineNo=lineNo;
		this.wineTaste=wineTaste;
		this.fromLine=lineNo;
		this.wineTaste.getLineNumberReader().setLineNumber(0);
		this.wineTasteServiceImpl=wineTasteServiceImpl;
	}
}
