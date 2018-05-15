package com.johnwillikers.rp;

import java.sql.ResultSet;

public interface MySqlCallback {

	public void onQueryDone(ResultSet rs);
}
