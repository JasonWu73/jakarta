package net.wuxianjie.springbootweb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;

	@GetMapping("/employees/list")
	public String listEmployees(final Model theModel) {
		final List<Employee> employees =  employeeService.getEmployees();

		// add to the spring model
		theModel.addAttribute("employees", employees);

		return "employees/list-employees";
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(final Model model) {
		final Employee employee = new Employee();

		model.addAttribute("employee", employee);

		return "employees/employee-form";
	}

	@PostMapping("/employees/save")
	public String saveEmployee(@ModelAttribute("employee") final Employee employee) {
		employeeService.save(employee);

		return "redirect:/employees/list";
	}

	@GetMapping
	public String redirectToEmployeeListPage() {
		return "redirect:/employees/list";
	}
}









