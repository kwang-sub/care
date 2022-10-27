package com.example.care.profile;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfileControllerTest {

    @Test
    void realProfileTest() {
        String expectedProfile = "real1";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("oauth");
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);
        String profile = controller.profile();
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    void firstProfileTest() {
        String expectedProfile = "oauth";
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("real-db");

        ProfileController controller = new ProfileController(env);
        String profile = controller.profile();
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @Test
    void defaultProfileTest() {
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();
        ProfileController controller = new ProfileController(env);
        String profile = controller.profile();
        assertThat(profile).isEqualTo(expectedProfile);
    }


}