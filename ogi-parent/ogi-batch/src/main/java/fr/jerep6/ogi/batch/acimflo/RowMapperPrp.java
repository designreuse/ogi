package fr.jerep6.ogi.batch.acimflo;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.service.ServiceRealProperty;

public class RowMapperPrp implements RowMapper<RealProperty> {

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public RealProperty mapRow(ResultSet rs, int rowNum) throws SQLException {
		String ref = rs.getString("reference");
		return serviceRealProperty.readByReference(ref).orElseGet(null);
	}

}
