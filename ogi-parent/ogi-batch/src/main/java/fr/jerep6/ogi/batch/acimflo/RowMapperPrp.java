package fr.jerep6.ogi.batch.acimflo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperPrp implements RowMapper<RealPropertyAcimflo> {

	@Override
	public RealPropertyAcimflo mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new RealPropertyAcimflo(rs.getInt("techid"), rs.getString("reference"));
	}

}
