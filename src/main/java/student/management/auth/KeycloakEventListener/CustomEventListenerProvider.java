package student.management.auth.KeycloakEventListener;

import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import student.management.auth.Kafka.KafkaProducer;
import student.management.auth.Kafka.KeycloakEvent;

import java.util.List;
import java.util.stream.Collectors;

public class CustomEventListenerProvider implements EventListenerProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomEventListenerProvider.class);

    private final KeycloakSession session;
    private final RealmProvider model;

    public CustomEventListenerProvider(KeycloakSession session) {
        this.session = session;
        this.model = session.realms();
    }

    @Override
    public void onEvent(Event event) {
        RealmModel realm = this.model.getRealm(event.getRealmId());
        UserModel newRegisteredUser = this.session.users().getUserById(event.getUserId(), realm);
        log.info("##################################");
        log.info("USERNAME ---> {}",newRegisteredUser.getUsername());
        log.info("USER ID ---> {}",newRegisteredUser.getId());
        log.info("REALM --> {}",realm);
        log.info("EVENT TYPE {}",event.getType().name());
        List<RoleModel> userRoleList = newRegisteredUser.getRoleMappingsStream().collect(Collectors.toList());
        log.info("USER ROLE {}",userRoleList.toString());
        switch (event.getType()) {
            case REGISTER:
                KafkaProducer.publish(KeycloakEvent.REGISTER.getTopicName(),newRegisteredUser.getUsername());
                log.info("REGISTER EVENT PUBLISHED TO KAFKA");
                break;
            case LOGIN:
                KafkaProducer.publish(KeycloakEvent.LOGIN.getTopicName(),newRegisteredUser.getUsername());
                log.info("REGISTER EVENT PUBLISHED TO KAFKA");
                break;
            case DELETE_ACCOUNT:
//                KafkaProducer.publish(MyEventType.DELETE_ACCOUNT.getTopicName(), newRegisteredUser.getUsername());
                log.info("DELETE ACCOUNT EVENT PUBLISHED TO KAFKA");
                log.info("User deleted with username --> {}",newRegisteredUser.getUsername());
                break;
            case UPDATE_PROFILE:
//                KafkaProducer.publish(MyEventType.UPDATE_PROFILE.getTopicName(), newRegisteredUser.getUsername());
                log.info("UPDATE PROFILE EVENT PUBLISHED TO KAFKA");
                break;
        }
        log.info("#########################################");
    }

    @Override
    public void onEvent(AdminEvent adminEvent, boolean b) {

    }

    @Override
    public void close() {

    }
}
