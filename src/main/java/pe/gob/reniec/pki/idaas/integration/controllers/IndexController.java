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
import pe.gob.reniec.pki.idaas.sdk.enums.Acr;
import pe.gob.reniec.pki.idaas.sdk.enums.Scope;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    private String baseUrl = "http://localhost:8080/example";

    @GetMapping("/")
    public ModelAndView getIndex(HttpSession session) throws IOException {
        ModelAndView response = new ModelAndView("index");
        ReniecIdaasClient oClient = getIdaasClient();

        session.setAttribute("state", oClient.getState());
        response.addObject("url", oClient.getLoginUrl());

        return response;
    }

    @GetMapping("/auth-endpoint")
    public ModelAndView getAuthEndpoint(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "state", required = false) String state,
            @SessionAttribute(value = "state", required = false) String sessionState
    ) {
        try {
            if (error == null && code != null) {
                if (state.equals(sessionState)) {
                    ReniecIdaasClient oClient = getIdaasClient();
                    TokenResponse oTokenResponse = oClient.getTokens(code);

                    if (oTokenResponse != null) {
                        User oUser = oClient.getUserInfo(oTokenResponse.getAccessToken());
                        ModelAndView response = new ModelAndView("home");

                        response.addObject("oUser", oUser);

                        return response;
                    }
                }
            }
        } catch (Exception ex) {
        }

        return new ModelAndView("redirect:".concat(baseUrl));
    }

    private ReniecIdaasClient getIdaasClient() throws IOException {
        ReniecIdaasClient oClient = new ReniecIdaasClient(getClass().getClassLoader().getResource("reniec_idaas.json").getFile());
        String state = new String(Base64.getEncoder().encode(String.valueOf(System.currentTimeMillis()).getBytes()));

        oClient.setRedirectUri(baseUrl.concat("/auth-endpoint"));
        oClient.setAcr(Acr.ONE_FACTOR);
        oClient.addScope(Scope.PROFILE);
        oClient.addScope(Scope.EMAIL);
        oClient.addScope(Scope.PHONE);
        oClient.setState(state);

        return oClient;
    }

}
