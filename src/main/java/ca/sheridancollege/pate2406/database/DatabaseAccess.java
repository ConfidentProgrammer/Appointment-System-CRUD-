package ca.sheridancollege.pate2406.database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.pate2406.beans.Appointment;

@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	
	//this is to add the app
	public void insertAppointment(Appointment app) {
		String query="INSERT INTO appointment (firstName, email, appointmentDate, appointmentTime) VALUES (:myname, :email, :appDate, :appTime)";

		MapSqlParameterSource namedParameters = 
						new MapSqlParameterSource();
		namedParameters.addValue("myname", app.getFirstName());
		namedParameters.addValue("email", app.getEmail());
		namedParameters.addValue("appDate", app.getAppointmentDate());
		namedParameters.addValue("appTime", app.getAppointmentTime());

		jdbc.update(query, namedParameters);
	}
	
	
	//this method is to fetch all the appointments
	
	public ArrayList<Appointment> getAllAppointment() {
		
		String query = "SELECT id, firstName, email, appointmentDate, appointmentTime  FROM appointment";
		ArrayList<Appointment> list = new ArrayList<Appointment>();
		List<Map<String, Object>> rows = jdbc.queryForList(query, new HashMap<String,Object>());
		//System.out.println(rows);
		
		for (Map<String, Object> row : rows) {
			Date date = (Date)row.get("appointmentDate");
			Time time = (Time)row.get("appointmentTime");
			list.add(new Appointment((Long) row.get("id"),
					(String) row.get("firstName"),
					((String) row.get("email")),
					date.toLocalDate(),time.toLocalTime()));
		}
		return list;
		}
	
	//this method is to delete the appointment by ID
	
	public void removeAppointment(long id) {
		String query="delete from appointment where id=:id";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);
		jdbc.update(query, namedParameters);
		}

	//this is to getAppointment By ID
	public Appointment getAppointmentById(int id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM appointment WHERE id=:id";
		parameters.addValue("id", id);
		ArrayList<Appointment> appointments = (ArrayList<Appointment>)jdbc.query(query, parameters,
		new BeanPropertyRowMapper<Appointment>(Appointment.class));
		if (appointments.size()>0)
		return appointments.get(0);
		return null;
		}
	public void modifyAppointment(Appointment a) {
		String query="UPDATE appointment SET firstName=:name, email=:em, appointmentDate=:appD, appointmentTime=:appT where id=:i";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", a.getFirstName());
		params.put("em", a.getEmail());
		params.put("i", a.getId());
		params.put("appD", a.getAppointmentDate());
		params.put("appT", a.getAppointmentTime());
		
		jdbc.update(query, params);
	}


}
