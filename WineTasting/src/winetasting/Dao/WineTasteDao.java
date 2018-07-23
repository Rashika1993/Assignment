package winetasting.Dao;

import java.util.List;
import java.util.Set;
public interface WineTasteDao {
	public List<String> selectPersonDetails();
	public void insertIntoWinesRank();
	public void insertIntoPersonsRank();
	public void addWishlistData(Set<String> wishListData);
	public int insertWinesRank(int wid,int rank);
}
