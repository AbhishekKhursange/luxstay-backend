package com.hotel.auth.oauth;

import com.hotel.auth.dto.RegisterRequest;
import com.hotel.auth.feign.UserClient;
import com.hotel.auth.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email    = oauthUser.getAttribute("email");
        String name     = oauthUser.getAttribute("name");
        String baseName = name.toLowerCase()
        	    .replaceAll("\\s+", "")
        	    .replaceAll("[^a-z0-9]", "");
        String username = baseName.isEmpty() ? email.split("@")[0] : baseName;

        // Register or find existing user
        try {
            RegisterRequest req = new RegisterRequest();
            req.setUsername(username);
            req.setPassword("OAUTH_USER"); // placeholder — OAuth users don't use password
            req.setFullName(name);
            req.setEmail(email);
            req.setRole("USER");

            userClient.registerOAuthUser(req);
        } catch (Exception e) {
            // Already exists — fine
        }

        // Generate JWT
        String token = jwtUtil.generateToken(username, "USER");

        // Redirect to frontend
        String redirectUrl = "http://localhost:3000/oauth/callback?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
