package de.telran.onlineshopforhomeandgarden1;

import de.telran.onlineshopforhomeandgarden1.security.JwtAuthentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Collections;

public class WithMockJwtAuthentication implements TestExecutionListener {

        @Override
        public void beforeTestMethod(TestContext testContext) throws Exception {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            String login = "test@qmail.com";
            String role = "CUSTOMER";

            JwtAuthentication jwtAuth = new JwtAuthentication( login, Collections.singletonList(role));
            jwtAuth.setAuthenticated(true);

            context.setAuthentication(jwtAuth);
            SecurityContextHolder.setContext(context);
        }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        SecurityContextHolder.clearContext();
    }
}


