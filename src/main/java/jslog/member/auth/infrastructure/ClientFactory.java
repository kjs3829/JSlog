package jslog.member.auth.infrastructure;

import jslog.member.auth.application.Client;
import jslog.member.auth.application.LoginStrategyFactory;
import jslog.member.auth.infrastructure.kakao.KakaoClient;
import jslog.member.auth.domain.ProviderName;
import jslog.member.auth.infrastructure.kakao.KakaoOauthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ClientFactory implements LoginStrategyFactory {

    private final RestTemplate restTemplate;
    private final ClientResponseConverter converter;
    private final KakaoOauthInfo kakaoOauthInfo;

    private Map<ProviderName, Client> clients;

    @PostConstruct
    private void initialize() {
        clients = new EnumMap<>(ProviderName.class);
        clients.put(ProviderName.KAKAO, new KakaoClient(restTemplate, converter, kakaoOauthInfo));
    }
    @Override
    public Client selectClient(ProviderName providerName) {
        return clients.get(providerName);
    }
}
