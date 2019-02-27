package pe.gob.reniec.pki.idaas.integration.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.pki.idaas.sdk.ReniecIdaasClient;
import pe.gob.reniec.pki.idaas.sdk.dto.User;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
@Controller
public class HomeController extends ParentController {

    @GetMapping("/home")
    public ModelAndView getIndex(
            @SessionAttribute("oUser") User oUser
    ) {
        ModelAndView response = new ModelAndView("home");

        response.addObject("oUser", oUser);
        response.addObject("baseUrl", baseUrl);

        return response;
    }

    @GetMapping("/logout")
    public String logout() {
        ReniecIdaasClient oClient = getIdaasClient();
        String logoutUri = oClient.getLogoutUri(baseUrl);

        return "redirect:".concat(logoutUri);
    }
}
