package ca.sheridancollege.pate2406.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.pate2406.beans.Appointment;
import ca.sheridancollege.pate2406.beans.UserRegistration;
import ca.sheridancollege.pate2406.database.DatabaseAccess;

@Controller
public class AppointmentController {
	@Autowired
	JdbcUserDetailsManager jdbcUserDetailsManager;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	DatabaseAccess da;
	
	@GetMapping("/")
	public String getIndex(Appointment appointment) {
		return "index";
	}
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}
	@PostMapping("/")
	public String postIndex(@ModelAttribute Appointment appointment) {
		da.insertAppointment(appointment);
		return "index";
	}
	@GetMapping("/view")
	public String view(Model model) {
		ArrayList<Appointment> list = da.getAllAppointment();
		model.addAttribute("appointment", list);
		return "view";

	}
	@GetMapping("/delete/{id}")
	public String del(Model model, @PathVariable long id) {
	  da.removeAppointment(id);
	  ArrayList<Appointment> list = da.getAllAppointment();
	  model.addAttribute("appointment", list);
	  return "view";
	}
	
	@GetMapping("/edit/{id}")
	public String editLink(Model model, @PathVariable int id){
	Appointment a = da.getAppointmentById(id);
	model.addAttribute("appointment", a);
	return "edit";
	}
 
	@GetMapping("/modify")
	public String modifyAppointment(Model model, @ModelAttribute Appointment appointment) {
	da.modifyAppointment(appointment);
	model.addAttribute("appointment", da.getAllAppointment());
	return "view";
	}
  
	@GetMapping("/register")
	public String register(Model model, UserRegistration user) {
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register")
	public String processRegister(@ModelAttribute UserRegistration user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		User newuser = new User(user.getUsername(), encodedPassword, authorities);
		jdbcUserDetailsManager.createUser(newuser);
		return "redirect:/";
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "/error/access-denied.html";
	}
}
