package ca.sheridancollege.pate2406.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String firstName;
	private String email;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate appointmentDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime appointmentTime;
}
