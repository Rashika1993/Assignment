package winetasting.Service;

import java.util.List;
import java.util.Set;

public interface WineTasteService {
	public List<String> selectPersonDetails();
	public void addWishlistData(Set<String> wishListData);
	public void insertIntoPersonsRank();
	public void insertWinesRank();
}
