package jslog.member.auth.application;

import jslog.member.auth.domain.ProviderName;

public interface LoginStrategyFactory {
    Client selectClient(ProviderName providerName);
}
