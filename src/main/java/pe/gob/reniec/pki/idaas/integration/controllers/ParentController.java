package pe.gob.reniec.pki.idaas.integration.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pe.gob.reniec.pki.idaas.sdk.ReniecIdaasClient;
import pe.gob.reniec.pki.idaas.sdk.enums.Acr;
import pe.gob.reniec.pki.idaas.sdk.enums.Scope;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
public class ParentController {

    private static final Logger logger = LoggerFactory.getLogger(ParentController.class);

    protected String baseUrl = "http://localhost:8080/example";

    protected ReniecIdaasClient getIdaasClient() {
        try {
            ReniecIdaasClient oClient = new ReniecIdaasClient(getClass().getClassLoader().getResource("reniec_idaas.json").getFile());
            String state = new String(Base64.getEncoder().encode(String.valueOf(System.currentTimeMillis()).getBytes()));

            oClient.setRedirectUri(baseUrl.concat("/auth-endpoint"));
            oClient.setAcr(Acr.ONE_FACTOR);
            oClient.addScope(Scope.PROFILE);
            oClient.addScope(Scope.EMAIL);
            oClient.addScope(Scope.PHONE);
            oClient.setState(state);

            return oClient;
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));

            logger.error(sw.toString());
        }

        return null;
    }

}
