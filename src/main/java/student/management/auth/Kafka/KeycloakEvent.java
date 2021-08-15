package student.management.auth.Kafka;

public enum KeycloakEvent {
    REGISTER("userRegister"),
    LOGIN("login"),
    DELETE_ACCOUNT("user_deleted"),
    UPDATE_EMAIL("update_email"),
    UPDATE_PROFILE("update_profile");

    private final String topicName;

    KeycloakEvent(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
