package com.mile.strategy;


import com.mile.external.client.SocialType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LoginStrategyManager {


    private final Map<SocialType, LoginStrategy> loginStrategies;

    public LoginStrategyManager(final List<LoginStrategy> strategies) {
        this.loginStrategies = strategies.stream().collect(Collectors.toMap(LoginStrategy::getSocialType, Function.identity()));
    }

    public LoginStrategy getLoginStrategy(final SocialType socialType) {
        return loginStrategies.get(socialType);
    }
}
