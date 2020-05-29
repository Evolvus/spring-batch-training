package com.evolvus.spring.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public final class TableDataStatementSetter implements ItemPreparedStatementSetter<TableData> {

	public void setValues(TableData item, PreparedStatement ps) throws SQLException {
		ps.setString(1, item.getName());
		ps.setString(2, item.getInfo());
		ps.setDate(3, new java.sql.Date(item.getLoadDate().getTime()));
		ps.setString(4, item.getStatus());
	}

}
