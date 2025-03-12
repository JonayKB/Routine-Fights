package es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities;

import org.junit.jupiter.api.Test;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReportEntityTest {

    private static final String REPORT_ID_1 = "R1";
    private static final String REPORT_ID_2 = "R2";
    private static final String MESSAGE = "Test report message";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 5, 1, 12, 0);

    private UserEntity createDummyUser() {
        UserEntity user = new UserEntity();
        user.setId("U1");
        return user;
    }

    private PostEntity createDummyPost() {
        PostEntity post = new PostEntity();
        post.setId("P1");
        return post;
    }

    @Test
    void testSettersAndGetters() {
        ReportEntity report = new ReportEntity();
        report.setId(REPORT_ID_1);
        report.setMessage(MESSAGE);
        report.setCreatedAt(CREATED_AT);
        UserEntity user = createDummyUser();
        report.setUser(user);
        PostEntity post = createDummyPost();
        report.setPost(post);

        assertEquals(REPORT_ID_1, report.getId());
        assertEquals(MESSAGE, report.getMessage());
        assertEquals(CREATED_AT, report.getCreatedAt());
        assertEquals(user.toString(), report.getUser().toString());
        assertEquals(post.toString(), report.getPost().toString());
    }

    @Test
    void testConstructorAndGetters() {
        UserEntity user = createDummyUser();
        PostEntity post = createDummyPost();
        ReportEntity report = new ReportEntity(REPORT_ID_1, MESSAGE, CREATED_AT, user, post);
        assertEquals(REPORT_ID_1, report.getId());
        assertEquals(MESSAGE, report.getMessage());
        assertEquals(CREATED_AT, report.getCreatedAt());
        assertEquals(user.toString(), report.getUser().toString());
        assertEquals(post.toString(), report.getPost().toString());
    }

    @Test
    void testEqualsAndHashCode() {
        ReportEntity report1 = new ReportEntity();
        ReportEntity report2 = new ReportEntity();
        assertEquals(report1, report2);
        report1.setId(REPORT_ID_1);
        report2.setId(REPORT_ID_1);
        assertEquals(report1, report2);
        assertEquals(report1.hashCode(), report2.hashCode());
        report2.setId(REPORT_ID_2);
        assertNotEquals(report1, report2);
        assertNotEquals(report1.hashCode(), report2.hashCode());
        assertEquals(report1, report1);
        assertNotEquals(report1, new Object());
    }

    @Test
    void testToString() {
        UserEntity user = createDummyUser();
        PostEntity post = createDummyPost();
        ReportEntity report = new ReportEntity(REPORT_ID_1, MESSAGE, CREATED_AT, user, post);
        String expected = "{" +
                " id='" + REPORT_ID_1 + "'" +
                ", message='" + MESSAGE + "'" +
                ", createdAt='" + CREATED_AT + "'" +
                ", user='" + user.toString() + "'" +
                ", post='" + post.toString() + "'" +
                "}";
        assertEquals(expected, report.toString());
    }
}
