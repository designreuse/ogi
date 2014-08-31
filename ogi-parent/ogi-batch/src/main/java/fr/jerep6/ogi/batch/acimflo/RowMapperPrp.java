package fr.jerep6.ogi.batch.acimflo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperPrp implements RowMapper<String> {

	@Override
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		String ref = rs.getString("reference");
		return ref;
	}

}
