package com.example.jangkau.security;

import com.example.jangkau.models.User;
import com.example.jangkau.services.oauth.Oauth2UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class Oauth2AccessTokenConverter extends DefaultAccessTokenConverter {

    @Autowired
    private Oauth2UserDetailService userDetailsService;

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // Add user ID to additional information
            additionalInfo.put("user_id", ((User) userDetails).getId());
        }
        Map<String, ?> map = super.convertAccessToken(token, authentication);
        Map<String, Object> response = new HashMap<>(map);
        response.putAll(additionalInfo);
        return response;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        final OAuth2Authentication auth = super.extractAuthentication(map);
        final UserDetails user = userDetailsService.loadUserByUsername((String) auth.getPrincipal());
        return new OAuth2Authentication(auth.getOAuth2Request(), auth.getUserAuthentication()) {
            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                if (user != null) {
                    return (Collection<GrantedAuthority>)
                            user.getAuthorities();
                }
                return auth.getAuthorities();
            }
        };
    }
}
