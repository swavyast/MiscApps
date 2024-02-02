package com.ml.app.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class DatabaseUtilityTest {

	public static void main(String[] args) {
		Connection con = DatabaseUtility.getConnection();
		String sql = "insert into student (id, name, phone, email)values(2, 'Hemant Kumar Rai', '+91-7318305031',  'hemantje1990@gmail.com')";
		if (con != null) {
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				int result = ps.executeUpdate();
				if (result == 1) {
					System.out.println("Record Inserted Successfully");
				} else {
					System.out.println(ps.getWarnings().getMessage());
				}
				DatabaseUtility.closeConnection();
			} catch (SQLIntegrityConstraintViolationException icve) {
				System.out.print("Error Code : "+icve.getErrorCode()+"\tSQL State : "+icve.getSQLState());
				System.out.println("\t"+icve.getMessage()+"\n");
				if(icve.getCause()!=null)
				System.out.println(icve.getCause().getMessage());
			} catch (SQLException s) {
				System.out.println(s.getErrorCode());
				System.out.println(s.getMessage());
				if(s.getCause()!=null)
				System.out.println(s.getCause().getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
