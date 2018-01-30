package com.wootric.androidsdk.utils;

import com.wootric.androidsdk.objects.EndUser;
import com.wootric.androidsdk.objects.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.wootric.androidsdk.TestHelper.testEndUser;
import static com.wootric.androidsdk.TestHelper.testUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class SHAUtilTest {

    @Mock
    EndUser endUser;

    @Mock
    User user;

    @Before
    public void setUp() {
        user = testUser();
        endUser = testEndUser();
    }

    @Test
    public void whenBuildUniqueLinkHasAllParameters() throws Exception {
        endUser.setEmail("test@example.com");
        String randomString = "16charrandstring";

        String uniqueLink = SHAUtil.buildUniqueLink(user.getAccountToken(), endUser.getEmail(), 1234567890, randomString);

        assertThat(uniqueLink).isNotNull();
        assertThat(uniqueLink.equals("1ed9f1c96018e2d577b3f864dc59dffe2baccc7103f6dcdadc40c3b6ec98cb0b")).isTrue();
    }

    @Test
    public void whenBuildUniqueLinkIsMissingEndUserEmail() throws Exception {
        String randomString = "16charrandstring";

        String uniqueLink = SHAUtil.buildUniqueLink(user.getAccountToken(), endUser.getEmail(), 1234567890, randomString);

        assertThat(uniqueLink).isNotNull();
        assertThat(uniqueLink.equals("1ed9f1c96018e2d577b3f864dc59dffe2baccc7103f6dcdadc40c3b6ec98cb0b")).isFalse();
    }

    @Test
    public void whenBuildUniqueLinkIsMissingRandomString() throws Exception {
        endUser.setEmail("test@example.com");
        String randomString = "";

        String uniqueLink = SHAUtil.buildUniqueLink(user.getAccountToken(), endUser.getEmail(), 1234567890, randomString);

        assertThat(uniqueLink).isNotNull();
        assertThat(uniqueLink.equals("1ed9f1c96018e2d577b3f864dc59dffe2baccc7103f6dcdadc40c3b6ec98cb0b")).isFalse();
    }

    @Test
    public void checkRandomString() throws Exception  {
        String randomString = SHAUtil.randomString();

        assertThat(randomString).isNotNull();
        assertThat(randomString.length() == 16).isTrue();

        assertThat(randomString).matches("^[a-z0-9]{16}");
    }
}
