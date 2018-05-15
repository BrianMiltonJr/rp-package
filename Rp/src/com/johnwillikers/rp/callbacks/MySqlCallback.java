package com.johnwillikers.rp.callbacks;

import java.sql.ResultSet;

public interface MySqlCallback {

	public void onQueryDone(ResultSet rs);
}
