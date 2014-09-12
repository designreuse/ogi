package fr.jerep6.ogi.batch.diaporama;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperPrpDiaporama implements RowMapper<RealPropertyDiaporama> {

	@Override
	public RealPropertyDiaporama mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new RealPropertyDiaporama(rs.getInt("techid"), rs.getString("reference"));
	}

}
