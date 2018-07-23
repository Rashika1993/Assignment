package winetasting.ServiceImpl;
import java.util.List;
import java.util.Set;
import winetasting.DaoImpl.WineTasteDaoImpl;
import winetasting.Service.WineTasteService;

public class WineTasteServiceImpl implements WineTasteService {
	WineTasteDaoImpl wineTasteDaoImpl;
	public WineTasteDaoImpl getWineTasteDaoImpl() {
		return wineTasteDaoImpl;
	}
	public void setWineTasteDaoImpl(WineTasteDaoImpl wineTasteDaoImpl) {
		this.wineTasteDaoImpl = wineTasteDaoImpl;
	}

	@Override
	public List<String> selectPersonDetails()
	{
		return wineTasteDaoImpl.selectPersonDetails();
	}
	@Override
	public void addWishlistData(Set<String> wishListData)
	{
		wineTasteDaoImpl.addWishlistData(wishListData);	
	}
	@Override
	public void insertIntoPersonsRank()
	{
		wineTasteDaoImpl.insertIntoPersonsRank();	
	}
	@Override
	public void insertWinesRank()
	{
		wineTasteDaoImpl.insertIntoWinesRank();	
	}
}
