package jslog.unit.post.domain.url;

import jslog.post.domain.url.CustomUrl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomUrlTest {

    @Test
    @DisplayName("customUrl 생성 성공")
    void create() {
        String[] split = "".split(",");
        System.out.println(split.length);
        //given
        String str = "!@#$%^&*()+|/?'\":;★安t%$&#e@@#s!!!&&t 성++공_2ㅏ";

        //when
        CustomUrl customUrl = CustomUrl.create(str);

        //then
        Assertions.assertThat(customUrl.getUrl()).isEqualTo("test_성공_2ㅏ");
    }
}