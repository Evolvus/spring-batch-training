package com.evolvus.spring.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.stereotype.Component;

public final class FileDataStatementSetter implements ItemPreparedStatementSetter<FileData> {

	public void setValues(FileData item, PreparedStatement ps) throws SQLException {
		ps.setString(1, item.getOrgnlInstrId());
		ps.setString(2, item.getStsRsnInf().getRsn().getCd());
		ps.setString(3, item.getStsRsnInf().getAddtlInf());
	}

}
