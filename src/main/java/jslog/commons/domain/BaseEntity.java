package jslog.commons.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

// JPA Entity들이 BaseTimeEntity을 상속할 경우
// 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 한다.
@MappedSuperclass
// BaseTime Entity클래스에 Auditing 기능을 포함시킨다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    private static final DateTimeFormatter customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public String getCreatedDateYYYYMMDD() {
        return Objects.requireNonNullElseGet(createdDate, LocalDateTime::now).format(customDateTimeFormatter);
    }

    public String getModifiedDateYYYYMMDD() {
        return this.modifiedDate.format(customDateTimeFormatter);
    }
}
