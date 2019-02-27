package pe.gob.reniec.pki.idaas.integration.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;
import pe.gob.reniec.pki.idaas.sdk.ReniecIdaasClient;
import pe.gob.reniec.pki.idaas.sdk.dto.TokenResponse;
import pe.gob.reniec.pki.idaas.sdk.dto.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
@Controller
public class IndexController extends ParentController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public ModelAndView getIndex(HttpSession session) throws IOException {
        ModelAndView response = new ModelAndView("index");
        ReniecIdaasClient oClient = getIdaasClient();

        session.setAttribute("state", oClient.getState());
        response.addObject("url", oClient.getLoginUrl());

        return response;
    }

    @GetMapping("/auth-endpoint")
    public String getAuthEndpoint(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @SessionAttribute(value = "state", required = false) String sessionState,
            HttpSession session
    ) {
        try {
            if (error == null && code != null) {
                if (state.equals(sessionState)) {
                    ReniecIdaasClient oClient = getIdaasClient();
                    TokenResponse oTokenResponse = oClient.getTokens(code);

                    if (oTokenResponse != null) {
                        User oUser = oClient.getUserInfo(oTokenResponse.getAccessToken());

                        session.setAttribute("oUser", oUser);

                        return "redirect:/home";
                    }
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            logger.error(sw.toString());
        }

        return "redirect:".concat(baseUrl);
    }

}
