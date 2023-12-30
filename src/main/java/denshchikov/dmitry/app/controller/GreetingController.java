package denshchikov.dmitry.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_GUEST')")
    public String greet(Authentication authentication) {
        return "Hello, %s. You have next permissions: %s"
                .formatted(authentication.getName(), authentication.getAuthorities());
    }

}
