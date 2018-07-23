package winetasting.DaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import winetasting.Dao.WineTasteDao;
public class WineTasteDaoImpl implements WineTasteDao{
	private JdbcTemplate fetchJdbcTemplate;
	private JdbcTemplate updateJdbcTemplate;

	public JdbcTemplate getFetchJdbcTemplate() {
		return fetchJdbcTemplate;
	}
	public void setFetchJdbcTemplate(JdbcTemplate fetchJdbcTemplate) {
		this.fetchJdbcTemplate = fetchJdbcTemplate;
	}
	public JdbcTemplate getUpdateJdbcTemplate() {
		return updateJdbcTemplate;
	}
	public void setUpdateJdbcTemplate(JdbcTemplate updateJdbcTemplate) {
		this.updateJdbcTemplate = updateJdbcTemplate;
	}
	@Override
	public List<String> selectPersonDetails()
	{
		List<String> persondetails=new ArrayList<String>();

		fetchJdbcTemplate.query("SELECT @row_number:=if(@customer_no = pid and @row_number<4, @row_number + 1, 1) as rownumber,"+
				"@customer_no:=temp.pid as pid ,temp.wid from"+
				"(select PersonsRank.pid as pid"+
				", WinesRank.wid"+ 
				" FROM PersonsRank, WishList, WinesRank ,(SELECT @customer_no:=0,@row_number:=0) as t "+
				" WHERE PersonsRank.pid = WishList.pid AND WishList.wid = WinesRank.wid"+ 
				" ORDER BY PersonsRank.rank DESC, PersonsRank.pid ASC, WinesRank.rank DESC)as temp "+
				" order by pid ", new ResultSetExtractor() {

			@Override
			public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				while (resultSet.next())
					persondetails.add("Person"+resultSet.getString("pid")+" "+"WinesRank"+resultSet.getString("wid"));
				return resultSet;

			}
		});
		return persondetails;
	}
	@Override
	public void insertIntoWinesRank()
	{
		updateJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO WinesRank(wid, rank) SELECT wid AS 'wid', 10.0/COUNT(pid) as 'rank' FROM WishList GROUP BY wid");
				pstmt.executeBatch();
				return pstmt;
			}
		});		

	}

	@Override
	public void insertIntoPersonsRank()
	{
		updateJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO PersonsRank(pid, rank) SELECT WishList.pid as pid, SUM(WinesRank.rank) AS rank FROM WishList, WinesRank  WHERE (WishList.wid = WinesRank.wid) GROUP BY WishList.pid");
				pstmt.executeBatch();
				return pstmt;
			}
		});		

	}

	@Override
	public void addWishlistData(Set<String> wishListData)
	{

		updateJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(
						"INSERT INTO wishlist (pid, wid)" +
						" VALUES(?,?)");
				// Add rows to a batch in a loop. Each iteration adds a
				// new row.

				Iterator<String> iterator=wishListData.iterator();
				while(iterator.hasNext() ) 
				{
					String[] entry=iterator.next().split(":");
					pstmt.setInt(1, Integer.parseInt(entry[0]) );
					pstmt.setInt(2, Integer.parseInt(entry[1]));
					pstmt.addBatch();
				}
				int[] result = pstmt.executeBatch();
				return pstmt;
			}
		});		
	}

	@Override
	public int insertWinesRank(int wid,int rank)
	{

		String query="INSERT INTO WinesRank(wid, rank) SELECT wid AS wid, 10.0/COUNT(pid) as rank FROM WishList GROUP BY wid";
		return updateJdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(query);

				return ps;
			}
		});
	}
}
