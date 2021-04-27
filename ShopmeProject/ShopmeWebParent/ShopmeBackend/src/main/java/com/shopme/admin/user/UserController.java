package com.shopme.admin.user;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;


@Controller
public class UserController {
	@Autowired
	private UserRepository repo;
	@Autowired
	private UserService service;
	@GetMapping("/users")
	public String listAll(Model model) {
		List<User> listUsers = service.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Create New User");
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes ) {
		System.out.println(user);
		service.save(user);
		redirectAttributes.addFlashAttribute("message", "The user has been successfully added.");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
				
			User user = service.get(id);
			List<Role> listRoles = service.listRoles();
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Edit User (ID: " + id + ")" );
	
			redirectAttributes.addFlashAttribute("message", "The changes has been successfully saved.");
			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
		
	}
	
//	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id,
		Model model,
		RedirectAttributes redirectAttributes) throws UserNotFoundException {
		Long countById = repo.countById(id);
		if(countById == null || countById == null) {
				throw new UserNotFoundException("Could not find any user with id: " + id);
			} 
			repo.deleteById(id);
			redirectAttributes.addFlashAttribute("message", "The user ID " + id + "has been deleted successfully");
			return "redirect:/users";
		}
	
//	@GetMapping("/users/delete/{id}")
//	public String deleteUser(@PathVariable(name = "id") Integer id,
//			Model model,
//			RedirectAttributes redirectAttributes) {
//		try {
//			service.delete(id);;
//			redirectAttributes.addFlashAttribute("message", "The user ID " + id + "has been deleted successfully");
//			return "redirect:/users";
//		} catch (UserNotFoundException ex) {
//			redirectAttributes.addFlashAttribute("message", ex.getMessage());
//			return "redirect:/users";
//		}
//		
//	}
	
	@GetMapping("/login")
	public String viewLoginPage() {
		return "login";
	}
	
}
