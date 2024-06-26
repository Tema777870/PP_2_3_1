package app.controller;

import app.model.User;
import app.service.UserService;
import app.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("users", userService.listAll());
        return "users/index";
    }

    @GetMapping("/new")
    public String newCustomerForm(Model model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors())
            return "users/new";
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/user")
    public String showUser(@RequestParam(name = "userId") int userId, Model model) {
        User user = userService.get(userId);
        model.addAttribute("user", user);
        return "users/show";
    }

    @GetMapping("/edit")
    public String showEditUser(@RequestParam(name = "userId") int userId, Model model) {
        User user = userService.get(userId);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PatchMapping("/edit")
    public String editUser(@RequestParam(name = "userId") int userId,
                           @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        // userValidator.validate(user, bindingResult); эта строка кода не дает менять поля без изменения email, пока не придумал как пофиксить
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete")
    public String deleteUserPage(@RequestParam(name = "userId") int userId,
                                 Model model) {
        User user = userService.get(userId);
        model.addAttribute("user", user);
        return "users/delete";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam(name = "userId") int userId, @ModelAttribute("user") User user) {
        userService.delete(userId);
        return "redirect:/users";
    }

}
