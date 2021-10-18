package jslog.web.login.oauth.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class KakaoProfile {
    private Long id;
    private Boolean has_signed_up;
    private String connected_at;
    private String synched_at;
    private Properties properties = new Properties();
    private KakaoAccount kakao_account;

    @Data
    public static class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
        public Boolean signed_up;
        public String email;

        public Properties() {
            this.nickname="";
            this.profile_image="";
            this.thumbnail_image="";
            this.signed_up=false;
            this.email="";
        }
    }

    @Data
    public class KakaoAccount {
        public Boolean profile_needs_agreement;
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public Boolean has_age_range;
        public Boolean age_range_needs_agreement;
        public Boolean has_birthday;
        public Boolean birthday_needs_agreement;
        public Boolean has_gender;
        public Boolean gender_needs_agreement;

        @Data
        public class Profile {

            public String nickname;
//          public String thumbnail_image_url;
//          public String profile_image_url;

        }
    }
}
