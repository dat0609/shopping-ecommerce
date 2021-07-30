package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.entity.User;

public class UserCsvExporter extends AbstractExporter{
	
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response,"text/csv", ".csv","users_" );
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(),CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"UserId", "Email", "First Name",  "Last Name", "Roles", "Enable"};
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		
		csvBeanWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvBeanWriter.write(user, fieldMapping);
		}
		
		csvBeanWriter.close();
	}
}
