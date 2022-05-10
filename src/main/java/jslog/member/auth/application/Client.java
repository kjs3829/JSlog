package jslog.member.auth.application;

public interface Client {

    MemberDetails getDetails(String socialCode);

    void unLink(String accessToken);

}
