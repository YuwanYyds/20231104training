package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("inquiry")
public class InquiryController {

	
	
	@Autowired
	private InquiryService inquiryService;
	
	
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();
		
		Inquiry inquiry = new Inquiry();
		inquiry.setId(4);
		inquiry.setName("Jamie");
		inquiry.setEmail("sample4@example.com");
		inquiry.setPassword("123456");
		inquiry.setContents("Hello.");
		
		inquiryService.update(inquiry);
		
//		try {
//			inquiryService.update(inquiry);
//		} catch (InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		return "inquiry/index";
	}
	
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm,
			Model model,
			@ModelAttribute("login") String login) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form";
	}
	
   
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm,Model model) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form";
	}
	

	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model) {
		if(result.hasErrors()) {
			model.addAttribute("title","Inquiry Form");
			return "inquiry/form";
		}
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setPassword(inquiryForm.getPassword());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		inquiryService.save(inquiry);
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm";
	}
	
	 @GetMapping("/success")
	    public String success() {
	        return "success";
	    }
	
	@GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, InquiryForm inquiryForm,Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password");
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password
    		,@Validated InquiryForm inquiryForm,
			BindingResult result,Model model) {
    	if(result.hasErrors()) {
			model.addAttribute("title","Login");
			return "inquiry/login";
		}
		
        if (isValidUser(email, password)) {
            return "inquiry/success";
        } else {
            return "inquiry/login?error";
        }
    }

    private boolean isValidUser(String email, String password) {
        return "email".equals(email) && "password".equals(password);
    }
	
	
	@ExceptionHandler(InquiryNotFoundException.class)
	public String handleException(InquiryNotFoundException e, Model model) {
		model.addAttribute("message", e);
		return "error/CoustomPage";
	}
	
	
	
}
