package pe.gob.reniec.pki.idaas.integration.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.pki.idaas.sdk.dto.User;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/home")
    public ModelAndView getIndex(
            @SessionAttribute("oUser") User oUser
    ) {
        ModelAndView response = new ModelAndView("home");

        response.addObject("oUser", oUser);

        return response;
    }
}
