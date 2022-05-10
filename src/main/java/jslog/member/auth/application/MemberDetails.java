package jslog.member.auth.application;

import jslog.member.auth.domain.ProviderName;

public interface MemberDetails {

    String accountId();

    ProviderName providerCode();
}
